package com.cs102.studybuddy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cs102.studybuddy.core.Course;
import com.cs102.studybuddy.core.File;
import com.cs102.studybuddy.core.Match;
import com.cs102.studybuddy.core.User;
import com.cs102.studybuddy.databinding.CourseCardLayoutBinding;
import com.cs102.studybuddy.databinding.FileCardLayoutBinding;
import com.cs102.studybuddy.listeners.CourseClickListener;
import com.cs102.studybuddy.listeners.FileClickListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class FileListView extends ScrollView {
    private final TableLayout fileTable;
    private final HashMap<String, File> fileList;

    private FileClickListener fileClickListener;

    public FileListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        fileList = new HashMap<>();

        fileTable = new TableLayout(context);
        fileTable.setStretchAllColumns(true);
        fileTable.setLayoutParams(
            new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        );
        addView(fileTable);
    }

    public TableLayout getFileTable() { return fileTable; }

    public void setFileClickListener(FileClickListener fileClickListener) {
        this.fileClickListener = fileClickListener;
    }

    public void removeFile(File f) {
        fileList.remove(f.getName());
        fileTable.removeAllViews();
        for (File file : fileList.values()) {
            createFileView(file);
        }
    }

    // Add a course to the list
    public void addFile(File file) {
        if (fileList.containsKey(file.getName())) return;

        fileList.put(file.getName(), file);
    }

    public void createFileView(File file) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        FileCardLayoutBinding binding = FileCardLayoutBinding.inflate(inflater);
        binding.setFile(file);
        binding.setCallback(fileClickListener);

        View courseView = binding.getRoot();
        fileTable.addView(courseView);
    }

//    // Create course list from all courses
//    public void populateFileList() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("courses")
//            .get().addOnCompleteListener(this::onGetCourses);
//    }

//     Create course list from user's courses
    public void populateFileList(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("files")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        for (Object field : document.getData().keySet()) {
                            if (field.equals("courseId")) {
                                for (String enrolls: user.getEnrollments().keySet()){
                                    if (document.getData().values().toString().contains(enrolls)){
                                        addFile(document.toObject(File.class));
                                        createFileView(document.toObject(File.class));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        getContext(),
                        "Failed to fetch match list",
                        Toast.LENGTH_LONG
                    ).show();
                }
            });
    }

    // Callback when we got a single course data
//    private void onGetCourse(@NonNull Task<DocumentSnapshot> task) {
//        if (!task.isSuccessful()) {
//            Toast.makeText(
//                getContext(),
//                "Failed to fetch course",
//                Toast.LENGTH_LONG
//            ).show();
//
//            return;
//        }
//
//        // When we got the course from Firebase,
//        // create the view with the data bindings.
//        File file = task.getResult().toObject(File.class);
//        addFile(file);
//        createFileView(file);
//    }
//
//    // Callback when we got courses collection
//    private void onGetCourses(@NonNull Task<QuerySnapshot> task) {
//        if (!task.isSuccessful()) {
//            Toast.makeText(
//                getContext(),
//                "Failed to fetch course list",
//                Toast.LENGTH_LONG
//            ).show();
//
//            return;
//        }
//
//        for (DocumentSnapshot fileDoc : task.getResult().getDocuments()) {
//            File file = fileDoc.toObject(File.class);
//            addFile(file);
//        }
//    }
}
