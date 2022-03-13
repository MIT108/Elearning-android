package com.se3.elearning.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.se3.elearning.R;
import com.se3.elearning.student.course.Courses;
import com.se3.elearning.student.other.Utils;
import com.se3.elearning.student.other.WsConfig;
import com.se3.elearning.utilities.Constants;
import com.se3.elearning.utilities.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

//import com.squareup.picasso.Picasso;


public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText pass, email;
    boolean addStatus;
    private PreferenceManager preferenceManager;
    private ProgressBar progressBar;
    JSONObject obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.button);
        pass = findViewById(R.id.pass);
        email = findViewById(R.id.email);
        progressBar = findViewById(R.id.progressBar);

        preferenceManager = new PreferenceManager(getApplicationContext());

        //checking if the user has a login session
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), Courses.class);
            startActivity(intent);
            finish();
        }

    }

    //user login function
    public void loginUser(View view) {
        //checking if the 2 input fields are filled or not
        if (!pass.getText().toString().isEmpty() && !email.getText().toString().isEmpty()){
            progressBar.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);

            //creating a volley que request for the consumption of the api
            RequestQueue queue = Volley.newRequestQueue(this);
            try {
                //creating a json object that contain the login information;
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("name", email.getText().toString());
                jsonBody.put("password", pass.getText().toString());

                //creating the requestBody and passing the jsonBody into the requestbody in the for
                //a sting to pass it into the api
                final String requestBody = jsonBody.toString();

                //creating a request of type post
                StringRequest stringRequest = new StringRequest(Request.Method.POST, WsConfig.Base_URL+"/auth/login",
                        response -> {
                            try {
                                //getting the response and passing it into a json object
                                obj = new JSONObject(response);
                                //verify if the user has firebase account
                                if (obj.getInt("firebase") == 0){
                                    //if the user has no firebase account the system automatically creates one
                                    addFirebaseAccount(obj.getString("name"),obj.getInt("adminid"), obj.getString("email"), obj.getString("password"), obj.getInt("userid"), obj.getInt("role"), obj.getInt("classid"));
                                }else{
                                    //else the login function is called;
                                    Login();
                                }
                            } catch (JSONException e) {
                                //throwing an error message if the json object could not be created
                                progressBar.setVisibility(View.GONE);
                                login.setVisibility(View.VISIBLE);
                                Utils.negativeAlertMessage("Errora : "+e.getMessage(), LoginActivity.this);
                            }
                        }, error -> System.out.println(error.getMessage())){
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() {
                        return requestBody.getBytes(StandardCharsets.UTF_8);
                    }
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString;
                        try {
                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        } catch (UnsupportedEncodingException e) {
                            responseString = new String(response.data);
                        }
                        System.out.println("API RESULT: "+responseString);
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                queue.add(stringRequest);
            }catch (JSONException e){
                //throw an error message in the request json creation
                Utils.negativeAlertMessage("Errord : "+ e.getMessage(), LoginActivity.this);
            }
        }else{
            //error if one of the to input fields are empty
            Utils.negativeAlertMessage("Enter all the fields", LoginActivity.this);
        }

    }

    //this function permit a user that has no user account on firebase to have one
    public void addFirebaseAccount(String name, int adminid, String email, String password, int userid, int role, int classid){
        addStatus = false;
        //creating a fireStore instant
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        //creating a hashMap object ot keep the users information
        HashMap<String, Object> user = new HashMap<>();
        //putting user information in the hashMap
        user.put(Constants.KEY_USERID, userid);
        user.put(Constants.KEY_NAME, name);
        user.put(Constants.KEY_EMAIL, email);
        user.put(Constants.KEY_PASSWORD, password);
        user.put(Constants.KEY_ADMINID, adminid);
        user.put(Constants.KEY_ROLE, role);
        user.put(Constants.KEY_CLASSID, classid);
        //creating the request
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    //message on success
                    try {
                        Utils.positiveAlertMessage("user created on firebase", LoginActivity.this);
                        //calling the method to update the firebase status of the user in the database
                        updateUserFirebaseStatus(obj.getInt("userid"), obj.getInt("firebase"));
                    } catch (JSONException e) {
                        Utils.negativeAlertMessage("Error : "+e.getMessage(), LoginActivity.this);
                    }
                }).addOnFailureListener(e -> {
                    //firebase onfailure Error message
                    Utils.negativeAlertMessage("Error while saving information in firestore : "+e.getMessage(), LoginActivity.this);
                });

    }

    //method to update the firebase status of the user
    public void updateUserFirebaseStatus(int userid, int fire){
        //creating athe volley queue
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WsConfig.Base_URL+"/auth/firebaseUpdate/"+userid+"/"+fire,
                response -> {
                    Utils.positiveAlertMessage("Status updated successfully", LoginActivity.this);
                    //calling the login function
                    Login();
                }, error -> {
                    //volley throw back error message
                    Utils.negativeAlertMessage("Error : "+error.getMessage(), LoginActivity.this);
                });

        queue.add(stringRequest);
    }

    //in the method all the user static variable are update and an fcm_token on is created on firebase
    public void Login(){
        try {
            //getting the firebase instance
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            //creating the firebase request that fits the user email and password
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .whereEqualTo(Constants.KEY_EMAIL, obj.getString("email"))
                    .whereEqualTo(Constants.KEY_PASSWORD, obj.getString("password"))
                    .get()
                    .addOnCompleteListener(task -> {
                        //on Success task all the users information are updated in the application
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
                            try {
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                preferenceManager.putString(Constants.KEY_EMAIL, obj.getString("email"));
                                preferenceManager.putString(Constants.KEY_NAME, obj.getString("name"));
                                preferenceManager.putString(Constants.KEY_USER_NAME, obj.getString("name"));
                                preferenceManager.putString(Constants.KEY_FN, obj.getString("name").substring(0,1));
                                preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                                preferenceManager.putString(Constants.KEY_PASSWORD, obj.getString("password"));
                                preferenceManager.putString(Constants.KEY_ADMINID, Integer.toString(obj.getInt("adminid")));
                                preferenceManager.putString(Constants.KEY_USERID, Integer.toString(obj.getInt("userid")));
                                preferenceManager.putString(Constants.KEY_ROLE, Integer.toString(obj.getInt("role")));
                                preferenceManager.putString(Constants.KEY_CLASSID, Integer.toString(obj.getInt("classid")));
                                if (obj.getInt("verify") == 0){
                                    //if the user has not yet verified his account he is send to a page that will help him verify
                                    startActivity(new Intent(this.getApplicationContext(), VerifyPassword.class));
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    login.setVisibility(View.VISIBLE);
                                    //else he login
                                    Intent intent = new Intent(getApplicationContext(), Courses.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                            }catch (JSONException e){
                                progressBar.setVisibility(View.GONE);
                                login.setVisibility(View.VISIBLE);
                                Utils.negativeAlertMessage("Error : "+e.getMessage(), LoginActivity.this);
                            }

                        }else{
                            progressBar.setVisibility(View.GONE);
                            login.setVisibility(View.VISIBLE);
                            Utils.negativeAlertMessage("unable to sign in", LoginActivity.this);
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            Utils.negativeAlertMessage("Error : "+ e.getMessage(), LoginActivity.this);
        }
    }

}