package com.example.android.rxsearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private static final String TAG = "StudentAdapter";

    private ArrayList<String> studentNames = new ArrayList<>();

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.student_list_item, null, false);

        StudentViewHolder viewHolder = new StudentViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        String name = studentNames.get(position);

        holder.bind(name);
    }

    @Override
    public int getItemCount() {
        return studentNames.size();
    }

    public void setStudentNames(ArrayList<String> studentNames) {
        this.studentNames = studentNames;
        notifyDataSetChanged();
    }

    public void clearStudentNames() {
        studentNames.clear();
        notifyDataSetChanged();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView mStudentName;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            mStudentName = itemView.findViewById(R.id.tv_student_name);
        }

        /**
         *
         * @param studentName
         */
        public void bind(String studentName) {
            mStudentName.setText(studentName);
        }
    }
}
