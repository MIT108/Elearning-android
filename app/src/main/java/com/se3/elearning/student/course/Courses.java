package com.se3.elearning.student.course;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.se3.elearning.R;
import com.se3.elearning.authentication.LoginActivity;
import com.se3.elearning.firebase.MessagingService;
import com.se3.elearning.student.other.WsConfig;
import com.se3.elearning.utilities.Constants;
import com.se3.elearning.utilities.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class Courses extends AppCompatActivity {


    RecyclerView categoryRecyclerView;
    categoryAdapter adapter;
    Boolean side = false;
    ListView listview;
    TextView textView;


    private JSONArray category;
    private String cat_desc[], cat_name[];
    private Integer [] cat_id;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());

        setContentView(R.layout.activity_course);

        listview = findViewById(R.id.course_recycler);
        textView = findViewById(R.id.textView);

        textView.setText(new StringBuilder().append("Hey !! ").append(preferenceManager.getString(Constants.KEY_NAME)).append(" / ").append(preferenceManager.getString(Constants.KEY_EMAIL)).toString());


        Toast.makeText(this, preferenceManager.getString(Constants.KEY_USER_ID), Toast.LENGTH_SHORT).show();

        getAllCategory(1);

        findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful() && task.getResult() != null){
                    sendFCMTokenToDatabase(task.getResult().getToken());
                }
            }
        });

//
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.d("FCM", "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        sendFCMTokenToDatabase(token);
//
//                    }
//                });
//        FirebaseMessaging.getInstance().setAutoInitEnabled(true);


//        call.enqueue(new Callback<List<Category>>() {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//
//                List<Category> categoryList = response.body();
//
////                getAllCategory(categoryList);
//
//                //lets run it
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "No response from server", Toast.LENGTH_SHORT).show();
//            }
//        });

        //lets run this app and check server is responding or not.
        // we have successfully fetched data from api.
        // now we will setup this json response to recyclerview.


    }


    // welcome to all of you.
    // first of all i am goinf to import some assets

    // now we will setup Retrofit for network call fetching data from server.
    // lets import retrofit dependency

    private void getAllCategory(int cls){
        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println(preferenceManager.getString(Constants.KEY_CLASSID));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, WsConfig.Base_URL+"/course/listCourseClass/"+preferenceManager.getString(Constants.KEY_CLASSID),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            category = new JSONArray(response);

                            cat_desc = new String[category.length()];
                            cat_name = new String[category.length()];
                            cat_id = new Integer[category.length()];

                            for (int i = 0; i < category.length(); i++) {
                                cat_desc[i] = category.getJSONObject(i).getString("description");
                                cat_name[i] = category.getJSONObject(i).getString("name");
                                cat_id[i] = category.getJSONObject(i).getInt("courseid");

                            }
                            adapter = new categoryAdapter(Courses.this,cat_desc,cat_name);
                            listview.setAdapter(adapter);
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    preferenceManager.putString(Constants.KEY_COURSEID, Integer.toString(cat_id[position]));
                                    preferenceManager.putString(Constants.KEY_COURSE_NAME, cat_name[position]);
                                    preferenceManager.putString(Constants.KEY_COURSE_DESCRIPTION, cat_desc[position]);
//                                    Intent intent = new Intent(getApplicationContext(), CoursePage.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(intent);

                                    startActivity(new Intent(getApplicationContext(), CoursePage.class));
                                }
                            });
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });

        queue.add(stringRequest);


    }

    // now again we need to create a model class for data and adapter class for recycler view
    // lest have a look on json data
    // this data comming from server having course content details
    // lets do it fast.

    // tutorial has benn complited see you the next tutorial.


    class categoryAdapter extends ArrayAdapter<String> {
        private Context context;;
        String desc [];
        String name [];


        categoryAdapter(Context c,String desc[],String name []){
            super(c,R.layout.category_row_items,R.id.app_package,desc);
            this.context = c;
            this.desc = desc;
            this.name = name;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;
            if (side) {
                row = layoutInflater.inflate(R.layout.category_row_items, parent, false);
            }else {
                row = layoutInflater.inflate(R.layout.category_row_items2, parent, false);
            }

            side = !side;


            TextView category_desc = row.findViewById(R.id.app_package);
            TextView category_name = row.findViewById(R.id.app_label);

            //setting text to description and status
            category_desc.setText(desc[position]);
            category_name.setText(name[position]);

            return row;
        }
    }


    private void sendFCMTokenToDatabase(String token){
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "token updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "unable to send token: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signOut(){
        Toast.makeText(this, "Signing Out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        preferenceManager.clearPreferences();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Courses.this, "Unable to sign out", Toast.LENGTH_SHORT).show();
            }
        });
    }

}