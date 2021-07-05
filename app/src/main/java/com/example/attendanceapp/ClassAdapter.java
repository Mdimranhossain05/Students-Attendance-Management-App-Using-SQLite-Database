package com.example.attendanceapp;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.viewHolder> {

    ArrayList<ClassItem> classItems;
    Context context;

    OnClickInterface onClickInterface;

    public interface OnClickInterface{
        void onClick(int position);
    }

    public void setOnClickInterface(OnClickInterface onClickInterface) {
        this.onClickInterface = onClickInterface;
    }

    public ClassAdapter(Context context, ArrayList<ClassItem> classItems) {
        this.context = context;
        this.classItems = classItems;
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView subName,className;
        public viewHolder( View itemView,OnClickInterface onClickInterface) {
            super(itemView);
            subName = itemView.findViewById(R.id.subNameRecID);
            className = itemView.findViewById(R.id.classNameRecID);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_sub_sample_layout,null);
        return new viewHolder(view,onClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.className.setText(classItems.get(position).className);
        holder.subName.setText(classItems.get(position).subName);
    }

    @Override
    public int getItemCount() {
        return classItems.size();
    }



}
