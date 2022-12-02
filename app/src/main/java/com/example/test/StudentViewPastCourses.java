package com.example.test;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

public class StudentViewPastCourses extends AppCompatActivity {
    String coursesTaken;
    ArrayList<String> courses;
    ArrayList<Integer> courseList = new ArrayList<>();
    ArrayList<Integer> finalCourseList = new ArrayList<>();
    private TextView pastCourseButton;
    String [] courseArray;
    boolean [] selectedCourse;
    boolean [] finalSelectedCourse;
    void arrayCopy(boolean[] currentArray, boolean[] wantedArray) {
        for (int i=0; i<wantedArray.length; i++) {
            currentArray[i] = wantedArray[i];
        }
    }
    void arrayListCopy(ArrayList<Integer> currentList, ArrayList<Integer> wantedList) {
        currentList.clear();
        if (wantedList == null) return;
        for (int num: wantedList){
            currentList.add(num);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_courses);
        pastCourseButton = findViewById(R.id.pastCourseButton);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        coursesTaken = sp.getString("courses_taken", null);
        System.out.println(coursesTaken);
        courseArray = coursesTaken.split(",");
        courses = new ArrayList<>(Arrays.asList(courseArray));

        ListView listView = (ListView) findViewById(R.id.PastCoursesView);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(StudentViewPastCourses.this, android.R.layout.simple_list_item_1,courses);
        listView.setAdapter(adapter);
        pastCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        StudentViewPastCourses.this
                );
                builder.setTitle("Add to Past Courses");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(courseArray, selectedCourse, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            courseList.add(i);
                            Collections.sort(courseList);
                        } else {
                            courseList.remove(i);
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j=0; j<courseList.size(); j++) {
                            stringBuilder.append(courseArray[courseList.get(j)]);
                            if (j != courseList.size()-1) {
                                stringBuilder.append(", ");
                            }
                        }
                        pastCourseButton.setText(stringBuilder.toString());
                        arrayCopy(finalSelectedCourse, selectedCourse);
                        arrayListCopy(finalCourseList, courseList);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        arrayCopy(selectedCourse, finalSelectedCourse);
                        arrayListCopy(courseList, finalCourseList);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j=0; j<selectedCourse.length; j++) {
                            selectedCourse[j] = false;
                            courseList.clear();
                            finalCourseList.clear();
                            pastCourseButton.setText("");
                        }
                    }
                });
                builder.show();
            }
            //prerequisites

        });
    }
}