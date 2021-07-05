package com.example.attendanceapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

public class MyDialog extends DialogFragment {

    String CLASS_SUB_DIALOG_CODE = "classDialog";
    String STUDENT_DIALOG_CODE = "stdDialog";
    String UPDATE_CLASS_CODE = "updateClassDialog";
    String UPDATE_STUDENT_CODE = "updateStudentDialog";

    OnCLickinterface onCLickinterface;

    String name2;
    int roll2;

    public MyDialog(String name, int roll) {
        this.name2 = name;
        this.roll2 = roll;
    }

    public MyDialog() {

    }


    public interface OnCLickinterface {
        void onClick(String tx1, String txt2);
    }

    public void setOnCLickinterface(OnCLickinterface onCLickinterface) {
        this.onCLickinterface = onCLickinterface;
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        Dialog dialog = null;
        if (getTag() == (CLASS_SUB_DIALOG_CODE)) dialog = AddClassDialog();
        if (getTag() == (STUDENT_DIALOG_CODE)) dialog = AddStdDialog();
        if (getTag() == (UPDATE_CLASS_CODE)) dialog = UpdateClassDialog();
        if (getTag() == UPDATE_STUDENT_CODE) dialog = UpdateStudentDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    private Dialog UpdateStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialogTxtID);
        title.setText("Edit Student");
        TextInputLayout roll = view.findViewById(R.id.ed01);
        roll.setHint("Roll");
        roll.getEditText().setText(roll2+" ");
        roll.getEditText().setEnabled(false);
        TextInputLayout studentName = view.findViewById(R.id.ed02);
        studentName.setHint("Student Name");
        studentName.getEditText().setText(name2);

        Button add = view.findViewById(R.id.add_class_btn_ID);
        Button cancel = view.findViewById(R.id.cancel_btn_ID);
        add.setText("Update");

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String rollText = roll.getEditText().getText().toString();
            String stdName = studentName.getEditText().getText().toString();
            onCLickinterface.onClick(rollText, stdName);
            roll.getEditText().setText("");
            studentName.getEditText().setText("");
            dismiss();
        });
        return builder.create();
    }

    private Dialog UpdateClassDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialogTxtID);
        title.setText("Update class and subject");
        TextInputLayout className = view.findViewById(R.id.ed01);
        className.setHint("Class Name");
        TextInputLayout subName = view.findViewById(R.id.ed02);
        subName.setHint("Subject Name");

        Button add = view.findViewById(R.id.add_class_btn_ID);
        Button cancel = view.findViewById(R.id.cancel_btn_ID);
        add.setText("Update");

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subNameT = subName.getEditText().getText().toString();
                String classNameT = className.getEditText().getText().toString();
                onCLickinterface.onClick(classNameT, subNameT);
                dismiss();
            }
        });
        return builder.create();


    }

    private Dialog AddStdDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialogTxtID);
        title.setText("Add new Student");
        TextInputLayout roll = view.findViewById(R.id.ed01);
        roll.setHint("Roll");
        TextInputLayout studentName = view.findViewById(R.id.ed02);
        studentName.setHint("Student Name");

        Button add = view.findViewById(R.id.add_class_btn_ID);
        Button cancel = view.findViewById(R.id.cancel_btn_ID);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String rollText = roll.getEditText().getText().toString();
            String stdName = studentName.getEditText().getText().toString();
            onCLickinterface.onClick(rollText, stdName);
            roll.getEditText().setText(String.valueOf(Integer.parseInt(rollText) + 1));
            studentName.getEditText().setText("");
        });
        return builder.create();

    }

    public Dialog AddClassDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialogTxtID);
        title.setText("Add new class");
        TextInputLayout className = view.findViewById(R.id.ed01);
        className.setHint("Class Name");
        TextInputLayout subName = view.findViewById(R.id.ed02);
        subName.setHint("Subject Name");

        Button add = view.findViewById(R.id.add_class_btn_ID);
        Button cancel = view.findViewById(R.id.cancel_btn_ID);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subNameT = subName.getEditText().getText().toString();
                String classNameT = className.getEditText().getText().toString();
                onCLickinterface.onClick(classNameT, subNameT);
                dismiss();
            }
        });
        return builder.create();
    }


}
