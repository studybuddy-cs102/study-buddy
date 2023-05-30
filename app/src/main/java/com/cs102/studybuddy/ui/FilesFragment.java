package com.cs102.studybuddy.ui;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cs102.studybuddy.R;
import com.cs102.studybuddy.core.StudyBuddy;
import com.cs102.studybuddy.core.User;
import com.cs102.studybuddy.views.FileListView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class FilesFragment extends Fragment {
    FileListView fileListView;
    Button choseFile, uploadFile;
    FirebaseFirestore db;
    User currentUser;
    private FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference  childRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_files, container, false);

        this.currentUser = ((StudyBuddy) requireActivity().getApplication()).currentUser;
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        choseFile = rootView.findViewById(R.id.chooseFile);
        uploadFile = rootView.findViewById(R.id.uploadFile);
        choseFile.setOnClickListener(this::onChooseFile);
        uploadFile.setOnClickListener(this::onUploadFile);

        fileListView = rootView.findViewById(R.id.filesListView);
        fileListView.populateFileList(this.currentUser);
        fileListView.setFileClickListener(this::onFileClick);
        fileListView.setVisibility(View.VISIBLE);

        // Create a storage reference from our app
        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReferenceFromUrl("gs://study-buddy-3f4cb.appspot.com/");

        // Create a reference with an initial file path and name

        return rootView;
    }

    private void onFileClick(com.cs102.studybuddy.core.File file) {

        storageRef = storage.getInstance().getReference().child(file.getName());
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String url = uri.toString();
            downloadFile(FilesFragment.this.getContext(), file.getName(), DIRECTORY_DOWNLOADS, url);
        }).addOnFailureListener(e -> {

        });

        
    }

    private void downloadFile(Context context, String filename, String distinationDir, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, distinationDir,filename);

        downloadManager.enqueue(request);
    }

    private void onUploadFile(View view) {
    }

    private void onChooseFile(View view) {

    }


}