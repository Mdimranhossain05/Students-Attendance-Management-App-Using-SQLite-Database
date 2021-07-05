package com.example.attendanceapp;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.viewHolder> {

    ArrayList<StudentItem> studentItems;
    Context context;

    OnClickInterface onClickInterface;



    public interface OnClickInterface{
        void onClick(int position);
    }

    public void setOnClickInterface(OnClickInterface onClickInterface) {
        this.onClickInterface = onClickInterface;
    }

    public StudentAdapter(Context context, ArrayList<StudentItem> studentItems) {
        this.context = context;
        this.studentItems = studentItems;
    }


    public class viewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView roll,studentName,status;
        CardView cardView;
        public viewHolder( View itemView,OnClickInterface onClickInterface) {
            super(itemView);
            roll = itemView.findViewById(R.id.rollStdViewID);
            studentName = itemView.findViewById(R.id.nameStdViewID);
            status = itemView.findViewById(R.id.statusStdViewID);
            cardView = itemView.findViewById(R.id.cardViewID);
            itemView.setOnClickListener(v->onClickInterface.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"Edit");
            menu.add(getAdapterPosition(),1,0,"Delete");
        }
    }


    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_sample_layout,null);
        return new viewHolder(view,onClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.roll.setText(studentItems.get(position).getRoll()+"");
        holder.studentName.setText(studentItems.get(position).getName());
        holder.status.setText(studentItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
    }

    private int getColor(int position) {
        String status  = studentItems.get(position).getStatus();
        if (status.equals("P"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.present)));
        else if(status.equals("A"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));
        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.normal)));
    }

    @Override
    public int getItemCount() {
        return studentItems.size();
    }



}
