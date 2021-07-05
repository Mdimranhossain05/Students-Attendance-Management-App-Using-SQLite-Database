package com.example.attendanceapp;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    private String className, subName;
    private int position;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private com.example.attendanceapp.StudentAdapter adapter;
    private ArrayList<com.example.attendanceapp.StudentItem> studentItems;
    private RecyclerView.LayoutManager layoutManager;
    private DBHelper dbHelper;
    private long cid;
    MyCalendar calendar;
    TextView subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        dbHelper = new DBHelper(this);
        calendar = new MyCalendar();

        try {
            Intent intent = getIntent();
            className = intent.getStringExtra("className");
            subName = intent.getStringExtra("subName");
            position = intent.getIntExtra("position", -1);
            cid = intent.getLongExtra("cid", -1);
            studentItems = new ArrayList<>();
            toolbar = findViewById(R.id.stdApbarID);
            SetupToolbar();
            loadData();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Ex: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        toolbar = findViewById(R.id.stdApbarID);

        recyclerView = findViewById(R.id.stdRecyclerID);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this, studentItems);
        recyclerView.setAdapter(adapter);

        loadStatusData();
        adapter.setOnClickInterface((position -> SetStatus(position)));


    }

    private void loadData() {

        Cursor cursor = dbHelper.getStudentTable(cid);
        studentItems.clear();
        while (cursor.moveToNext()) {
            long sid = cursor.getLong(cursor.getColumnIndex(DBHelper.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndex(DBHelper.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.STUDENT_NAME_KEY));
            studentItems.add(new StudentItem(sid, roll, name));

        }
        cursor.close();

    }


    private void SetStatus(int position) {
        String status = studentItems.get(position).getStatus();
        if (status.equals("P")) {
            status = "A";

            long val = dbHelper.updateStatus(studentItems.get(position).getSid(), calendar.getDate(), status);
            if (val != -1) {
                Toast.makeText(this, "Attendance Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                status = "P";
                long value = dbHelper.addStatus(studentItems.get(position).getSid(), cid, calendar.getDate(), status);
                if (value != -1) {
                    Toast.makeText(this, "Attendance Saved Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    long updateVal = dbHelper.updateStatus(studentItems.get(position).getSid(), calendar.getDate(), status);
                    if (updateVal != -1) {
                        Toast.makeText(this, "Attendance Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (SQLException e){
                Toast.makeText(this, "Ex: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        }
        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);

    }

    public void SetupToolbar() {
        TextView title = toolbar.findViewById(R.id.titleTxtToolbarID);
        subTitle = toolbar.findViewById(R.id.subtitleTxtToolbarID);
        ImageButton backBtn = toolbar.findViewById(R.id.backBtnToolbarID);


        title.setText(className);
        subTitle.setText(calendar.getDate() + " | " + subName);

        backBtn.setOnClickListener(v -> onBackPressed());


        toolbar.inflateMenu(R.menu.manu_layout);
        toolbar.setOnMenuItemClickListener(menuItem -> OnMenuItemClick(menuItem));

    }


    private void loadStatusData() {
        for (StudentItem studentItem : studentItems) {
            String status = dbHelper.getStatus(studentItem.getSid(), calendar.getDate());

            if (status != null) studentItem.setStatus(status);
            else studentItem.setStatus("A");

            adapter.notifyDataSetChanged();
        }
    }

    private boolean OnMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.addStdID) {
            ShowDialog();
        }
        if (menuItem.getItemId() == R.id.calendarStdID) {
            showCalendar();
        }

        return true;
    }


    private void showCalendar() {
        calendar.show(getSupportFragmentManager(), "");
        calendar.setOnCalendarOkClickListener(this::onCalendarClicked);

    }

    private void onCalendarClicked(int year, int month, int dayOfMonth) {
        calendar.setDate(year, month, dayOfMonth);
        subTitle.setText(calendar.getDate() + " | " + subName);
        loadStatusData();
    }

    private void ShowDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), dialog.STUDENT_DIALOG_CODE);
        dialog.setOnCLickinterface((roll, name) -> AddStudent(cid, roll, name));
    }

    private void AddStudent(long cid, String roll, String name) {

        long sid = dbHelper.addStudent(cid, Integer.parseInt(roll), name);
        StudentItem studentItem = new StudentItem(sid, Integer.parseInt(roll), name);
        studentItems.add(studentItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                Toast.makeText(this, "Edit Clicked", Toast.LENGTH_SHORT).show();
                showEditStudentsDialog(item.getGroupId());

                break;
            case 1:
                Toast.makeText(this, "Delete Clicked", Toast.LENGTH_SHORT).show();
                deleteStudent(item.getGroupId()); //groupID returns the position of item from student Item
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void showEditStudentsDialog(int position) {

        try {
            MyDialog dialog = new MyDialog(studentItems.get(position).getName(), studentItems.get(position).getRoll());
            dialog.show(getSupportFragmentManager(), dialog.UPDATE_STUDENT_CODE);

            dialog.setOnCLickinterface((roll, name) -> updateStudent(position, name));
        } catch (Exception e) {
            Toast.makeText(this, "Ex: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void updateStudent(int position, String name) {

        dbHelper.updateStudent(studentItems.get(position).getSid(), name);
        studentItems.get(position).setName(name);
        adapter.notifyDataSetChanged();

    }

    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getSid());
        studentItems.remove(position);
        adapter.notifyDataSetChanged();

    }

}