package com.se3.elearning.student.course;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.se3.elearning.databinding.ActivityUploadImageBinding;
import com.se3.elearning.utilities.Constants;
import com.se3.elearning.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class UploadImage extends AppCompatActivity {

    StorageReference objectStorageReference;
    FirebaseFirestore objectFirebaseFirestore;
    PreferenceManager preferenceManager;
    ActivityUploadImageBinding binding;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.uploadimagebtn.setVisibility(View.GONE);
        objectFirebaseFirestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding.selectImagebtn.setOnClickListener(v -> selectImage());

        binding.uploadimagebtn.setOnClickListener(v -> {
            if (binding.imagename.getText().toString().isEmpty()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(UploadImage.this);
                builder1.setMessage("Insert a name to the file");
                builder1.setCancelable(true);

                builder1.setNegativeButton(
                        "OK",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert11 = builder1.create();
                alert11.show();

            } else {
                uploadImage();
            }

        });

    }

    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();


        Date now = new Date();
        String strDate = now.toString();
        String ranName = getAlphaNumericString(15);
        storageReference = FirebaseStorage.getInstance().getReference("CourseContent/" + ranName);

        objectStorageReference = FirebaseStorage.getInstance().getReference("Course Content");
        final StorageReference imageRef = objectStorageReference.child(ranName);

        UploadTask objectUploadTask = imageRef.putFile(imageUri);
        objectUploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, String> objectMap = new HashMap<>();
                objectMap.put("url", task.getResult().toString());
                objectMap.put("ranName", ranName);
                objectMap.put("courseId", preferenceManager.getString(Constants.KEY_COURSEID));
                objectMap.put("name", binding.imagename.getText().toString());
                objectMap.put("time", strDate);

                objectFirebaseFirestore.collection("Course Content").document()
                        .set(objectMap)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(UploadImage.this, "File upload", Toast.LENGTH_SHORT).show();
                            binding.imagename.setText("");
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), CoursePage.class));
                            finish();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(UploadImage.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                            binding.imagename.setText("");
                            progressDialog.dismiss();
                        });

            } else if (!task.isSuccessful()) {
                Toast.makeText(UploadImage.this, "Error : " + task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        binding.uploadimagebtn.setVisibility(View.GONE);

    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {

            imageUri = data.getData();
            System.out.println(imageUri);
            Toast.makeText(UploadImage.this, imageUri.toString(), Toast.LENGTH_SHORT).show();
            binding.firebaseimage.setImageURI(imageUri);
            binding.uploadimagebtn.setVisibility(View.VISIBLE);

        }
    }

    // function to generate a random string of length n
    public String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}