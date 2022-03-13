package com.se3.elearning.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.se3.elearning.R;
import com.se3.elearning.student.course.Courses;
import com.se3.elearning.student.other.WsConfig;
import com.se3.elearning.utilities.Constants;
import com.se3.elearning.utilities.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class VerifyPassword extends AppCompatActivity {
    TextView newName, newEmail;
    PreferenceManager preferenceManager;
    EditText pass, opass, cpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_password);

        preferenceManager = new PreferenceManager(getApplicationContext());

        newEmail = findViewById(R.id.emails);
        pass = findViewById(R.id.pass);
        opass = findViewById(R.id.opass);
        cpass = findViewById(R.id.cpass);

        newEmail.setText(preferenceManager.getString(Constants.KEY_NAME) + " / " + preferenceManager.getString(Constants.KEY_EMAIL));
    }

    public void changePassword(View view) {
        if (!opass.getText().toString().isEmpty() && !cpass.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()) {
            if (!opass.getText().toString().equals(preferenceManager.getString(Constants.KEY_PASSWORD))) {
                Toast.makeText(getApplicationContext(), "The old password does not correspond", Toast.LENGTH_SHORT).show();
            } else {
                if (pass.getText().toString().equals(cpass.getText().toString())) {
                    verify(preferenceManager.getString(Constants.KEY_USERID), cpass.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "the 2 password does not correspond", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Enter all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToLogin(View view) {
        Toast.makeText(getApplicationContext(), "Back", Toast.LENGTH_SHORT).show();
    }

    public void verify(String userid, String Password) {


        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("password", Password);
            jsonBody.put("userid", userid);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, WsConfig.Base_URL + "/auth/verify",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                            if (response.equals("true")) {

                                FirebaseFirestore database = FirebaseFirestore.getInstance();
                                DocumentReference documentReference =
                                        database.collection(Constants.KEY_COLLECTION_USERS).document(
                                                preferenceManager.getString(Constants.KEY_USER_ID)
                                        );
                                documentReference.update(Constants.KEY_PASSWORD, Password)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                preferenceManager.putString(Constants.KEY_PASSWORD, Password);
                                                Intent intent = new Intent(getApplicationContext(), Courses.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "unable to send token: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }else{
                                Toast.makeText(getApplicationContext(), "Cant Change the password", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString;
                    try {
                        responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    } catch (UnsupportedEncodingException e) {
                        responseString = new String(response.data);
                    }
                    System.out.println("API RESULT: " + responseString);
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            queue.add(stringRequest);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }

    }
}