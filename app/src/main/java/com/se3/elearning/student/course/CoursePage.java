package com.se3.elearning.student.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.se3.elearning.adapters.CourseContentAdapter;
import com.se3.elearning.model.CourseContent;
import com.se3.elearning.student.ChatActivity;
import com.se3.elearning.student.StudentDashboard;

import com.se3.elearning.R;
import com.se3.elearning.utilities.Constants;
import com.se3.elearning.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CoursePage extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ImageView goToCourse;

    TextView member, rating, name, price;

    //firebase storage
    EditText textView6;
    ImageView uploadFile;
    private TextView nameCourse, descCourse;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private List<CourseContent> courseContents;
    private TextView textErrorMessage;
    private PreferenceManager preferenceManager;
    FirebaseFirestore db;
    ArrayList<CourseContent> courseContentArrayList = new ArrayList<>();
    CourseContentAdapter courseContentAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    Intent intt;
    private FirebaseAuth mAuth;
// ...
// Initialize Firebase Auth


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        nameCourse = new TextView(this);
        descCourse = new TextView(this);

        goToCourse = findViewById(R.id.imageViewseven);
        uploadFile = findViewById(R.id.uploadFile);
        preferenceManager = new PreferenceManager(this);

        nameCourse = findViewById(R.id.nameCourse);
        descCourse = findViewById(R.id.descCourse);

        nameCourse.setText(preferenceManager.getString(Constants.KEY_COURSE_NAME));
        descCourse.setText(preferenceManager.getString(Constants.KEY_COURSE_DESCRIPTION));

        mRecyclerView = new RecyclerView(this);

        mRecyclerView = findViewById(R.id.course_recycler2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        textErrorMessage = findViewById(R.id.textErrorMessage);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::dataFromFirebase);

        uploadFile = findViewById(R.id.uploadFile);

        Toast.makeText(this, preferenceManager.getString(Constants.KEY_ROLE), Toast.LENGTH_SHORT).show();
        if (preferenceManager.getString(Constants.KEY_ROLE).equals("0")){
            uploadFile.setVisibility(View.VISIBLE);
        }else{
            uploadFile.setVisibility(View.GONE);
        }


        setUpFB();
        dataFromFirebase();


    }


    public void goToCourse(View view) {
        startActivity(new Intent(this.getApplicationContext(), Courses.class));
    }

    public void uploadFile(View view) {
        startActivity(new Intent(this.getApplicationContext(), UploadImage.class));
    }

    public void openDash(View view) {
        startActivity(new Intent(this.getApplicationContext(), StudentDashboard.class));
    }

    private void dataFromFirebase() {
        swipeRefreshLayout.setRefreshing(true);
        textErrorMessage.setVisibility(View.GONE);
        if (courseContentArrayList.size() > 0) {
            courseContentArrayList.clear();
        }

        db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_COURSE_CONTENT)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        swipeRefreshLayout.setRefreshing(false);
                        textErrorMessage.setVisibility(View.GONE);


                        if (task.isSuccessful() && task.getResult() != null) {
                            textErrorMessage.setVisibility(View.GONE);

                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                String course = preferenceManager.getString(Constants.KEY_COURSEID);
                                String newCourse = documentSnapshot.getString("courseId");

                                if (course.equals(newCourse)) {
                                    CourseContent courseContent = new CourseContent(documentSnapshot.getString("courseId")
                                            , documentSnapshot.getString("name")
                                            , documentSnapshot.getString("ranName")
                                            , getReadableDateTime(documentSnapshot.getString("time"))
                                            , documentSnapshot.getString("url"));
                                    courseContentArrayList.add(courseContent);
                                }

                            }

                            if (courseContentArrayList.size() <= 0) {
                                textErrorMessage.setText(String.format("%s", "No course available"));
                                textErrorMessage.setVisibility(View.VISIBLE);
                            }
                        }else{
                            textErrorMessage.setText(String.format("%s", "No course available"));
                            textErrorMessage.setVisibility(View.VISIBLE);
                        }
                        courseContentAdapter = new CourseContentAdapter(CoursePage.this, courseContentArrayList);
                        mRecyclerView.setAdapter(courseContentAdapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CoursePage.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void setUpFB() {
        db = FirebaseFirestore.getInstance();
    }
    public String getReadableDateTime(String str){

        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(new Date());
    }


}
