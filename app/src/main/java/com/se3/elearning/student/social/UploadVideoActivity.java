package com.se3.elearning.student.social;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.se3.elearning.R;
import com.se3.elearning.student.other.Utils;
import com.se3.elearning.utilities.Constants;
import com.se3.elearning.utilities.PreferenceManager;

import java.util.Date;
import java.util.HashMap;

public class UploadVideoActivity extends AppCompatActivity {
    private EditText titleEt;
    private VideoView videoView;
    private Button uploadVideoBtn;
    private FloatingActionButton pickVideoFab;

    private static final int VIDEO_PICK_GALLERY_CODE = 100;
    private static final int VIDEO_PICK_CAMERA_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;

    private  String[] cameraPermissions;

    private Uri videoUri;
    private ProgressDialog progressDialog;

    PreferenceManager preferenceManager;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        titleEt = findViewById(R.id.titleEt);
        videoView = findViewById(R.id.videoView);
        uploadVideoBtn = findViewById(R.id.uploadViewBtn);
        pickVideoFab = findViewById(R.id.pickVideoFab);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Uploading Video");
        progressDialog.setCanceledOnTouchOutside(false);

        preferenceManager = new PreferenceManager(this);



        //camera Permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};


        uploadVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleEt.getText().toString().trim();
                if (TextUtils.isEmpty(title)){
                    Utils.negativeAlertMessage("Title is required",UploadVideoActivity.this);
                }else if (videoUri == null){
                    Utils.negativeAlertMessage("Pick a video before you can upload",UploadVideoActivity.this);
                }else{
                    uploadVideoFirebase();
                }
            }
        });

        pickVideoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoPicDialog();
            }
        });

    }

    private void uploadVideoFirebase() {
        progressDialog.show();

        String timestamp = (new Date()).toString();

        String filePathName = Constants.KEY_VIDEO_FOLDER+"/video"+timestamp;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathName);

        storageReference.putFile(videoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadUrl = uriTask.getResult();
                        if (uriTask.isSuccessful()){
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", ""+timestamp);
                            hashMap.put("description", ""+title);
                            hashMap.put(Constants.KEY_USER_NAME, ""+preferenceManager.getString(Constants.KEY_USER_NAME));
                            hashMap.put(Constants.KEY_USER_ID, ""+preferenceManager.getString(Constants.KEY_USER_ID));
                            hashMap.put("timestamp", ""+timestamp);
                            hashMap.put("videoUrl", ""+downloadUrl);
                            hashMap.put("classId", ""+preferenceManager.getString(Constants.KEY_CLASSID));

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.KEY_VIDEO_FOLDER);
                            reference.child(timestamp)
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.dismiss();
                                            Utils.positiveAlertMessage("video uploaded...",UploadVideoActivity.this);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Utils.negativeAlertMessage(e.getMessage(), UploadVideoActivity.this);
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Utils.negativeAlertMessage(e.getMessage(), UploadVideoActivity.this);
                    }
                });


    }

    private void requestCameraPermission(){
        //request camera permission
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED;
        return  result1 && result2;
    }

    private void videoPickGallery(){
        //pick video from gallery intent

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Videos"), VIDEO_PICK_GALLERY_CODE);
    }

    private void videopPickCamera(){
        //pick video from camera intent
        Intent intent = new Intent((MediaStore.ACTION_VIDEO_CAPTURE));
        startActivityForResult(intent, VIDEO_PICK_CAMERA_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        videopPickCamera();
                    }
                }else{
                    Toast.makeText(this, "Camera and storage permissions are required", Toast.LENGTH_SHORT).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void setVideoToVideoView(){
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);

        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.pause();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == VIDEO_PICK_GALLERY_CODE){
                videoUri = data.getData();
                setVideoToVideoView();
            }else if (requestCode == VIDEO_PICK_CAMERA_CODE){
                videoUri = data.getData();
                setVideoToVideoView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void videoPicDialog(){
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("pick Video From")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            if (!checkCameraPermission()){
                                requestCameraPermission();
                            }else{
                                videopPickCamera();
                            }
                        }else if (i == 1){
                            videoPickGallery();
                        }
                    }
                }).show();
    }
}