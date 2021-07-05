package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ArrayList<ClassItem> classItems;
    ClassAdapter adapter;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floatingActionBtnID);
        recyclerView = findViewById(R.id.recyclerID);
        toolbar = findViewById(R.id.toolbar_main_page_ID);
        classItems = new ArrayList<>();
        dbHelper = new DBHelper(this);

        loadData();


        recyclerView.setHasFixedSize(true);
        adapter = new ClassAdapter(this, classItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnClickInterface(position -> GoToStudentActivity(position));

        SetupToolbar();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog dialog = new MyDialog();
                dialog.show(getSupportFragmentManager(), dialog.CLASS_SUB_DIALOG_CODE);

                dialog.setOnCLickinterface(((tx1, tx2) -> AddClasSub(tx1, tx2)));

            }
        });


    }

    private void loadData() {

        Cursor cursor = dbHelper.getClassTable();
        classItems.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(dbHelper.C_ID));
            String className = cursor.getString(cursor.getColumnIndex(dbHelper.CLASS_NAME_KEY));
            String subName = cursor.getString(cursor.getColumnIndex(dbHelper.SUBJECT_NAME_KEY));
            classItems.add(new ClassItem(id,className,subName));
        }

    }

    private void GoToStudentActivity(int position) {
        try {
            Intent i = new Intent(MainActivity.this, StudentActivity.class);
            i.putExtra("className", classItems.get(position).className);
            i.putExtra("subName", classItems.get(position).subName);
            i.putExtra("position", position);
            i.putExtra("cid", classItems.get(position).getCid());

            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(this, "Ex: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void AddClasSub(String className, String subName) {

        if (!className.isEmpty() && !subName.isEmpty()) {
            try {
                long cid = dbHelper.addClass(className,subName);
                Toast.makeText(this, "CID: "+cid, Toast.LENGTH_LONG).show();
                ClassItem classItem = new ClassItem(cid,className,subName);
                classItems.add(classItem);
                adapter.notifyDataSetChanged();
            }catch (SQLException e){
                Toast.makeText(this, "Ex: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }



        } else {
            Toast.makeText(MainActivity.this, "Enter all fields, and try again", Toast.LENGTH_LONG).show();
        }

    }

    public void SetupToolbar() {
        TextView title = toolbar.findViewById(R.id.titleTxtToolbarID);
        TextView subTitle = toolbar.findViewById(R.id.subtitleTxtToolbarID);
        ImageButton backBtn = toolbar.findViewById(R.id.backBtnToolbarID);


        title.setText("Attendance App");
        subTitle.setVisibility(View.GONE);
        backBtn.setVisibility(View.INVISIBLE);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {

        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),dialog.UPDATE_CLASS_CODE);
        dialog.setOnCLickinterface((className,subName)->updateClassAndSubject(position,className,subName));

    }

    private void updateClassAndSubject(int position,String className, String subName) {
        dbHelper.updateClass(classItems.get(position).getCid(),className,subName);
        classItems.get(position).setClassName(className);
        classItems.get(position).setSubName(subName);
        adapter.notifyDataSetChanged();

    }

    private void deleteClass(int position) {
        dbHelper.deleteClass(classItems.get(position).getCid());
        classItems.remove(position);
        adapter.notifyItemRemoved(position);
    }
}