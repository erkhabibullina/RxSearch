package com.example.android.rxsearch;

import android.animation.Animator;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * {@link StudentAdapter} exposes a list of students to a
 * {@link androidx.recyclerview.widget.RecyclerView}
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {


    static String baconTitle = "Bacon";
    static String baconText = "Bacon ipsum dolor amet pork belly meatball kevin spare ribs. Frankfurter swine corned beef meatloaf, strip steak.";
    static String veggieTitle = "Veggie";
    static String veggieText = "Veggies es bonus vobis, proinde vos postulo essum magis kohlrabi welsh onion daikon amaranth tatsoi tomatillo melon azuki bean garlic.";
    private static final String TAG = "StudentAdapter";

    /**
     * ArrayList of Strings that we use to display list of student names
     */
    private ArrayList<String> studentNames = new ArrayList<>();

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill screen and allow for scrolling.
     *
     * @param parent   The ViewGroup that these ViewHolders are contained within.
     * @param viewType If your RecyclerView has more than one type of item (which ours doesn't) you
     *                 can use this viewType integer to provide a different layout. See
     *                 {@link androidx.recyclerview.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                 for more details.
     * @return A New StudentViewHolder that holds the View for each list item
     */

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.student_list_item, parent, false);


        StudentViewHolder viewHolder = new StudentViewHolder(itemView);


        return viewHolder;
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * names in the list for particular student, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item
     *                 at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        String name = studentNames.get(position);
        holder.bind(name);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our students list
     */

    @Override
    public int getItemCount() {
        return studentNames.size();
    }

    /**
     * This method is used to set the student's names on an ArrayList of students names
     *
     * @param studentNames The new student names to be displayed
     */

    public void setStudentNames(ArrayList<String> studentNames) {
        this.studentNames = studentNames;
        notifyDataSetChanged();
    }

    /**
     * This method is used to clear an ArrayList of students names.
     */

    public void clearStudentNames() {
        studentNames.clear();
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a list item.
     */

    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mStudentName;
        static int green;
        static int white;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            mStudentName = itemView.findViewById(R.id.package_name);

            itemView.setOnClickListener(this);

            if (green == 0)
                green = itemView.getContext().getResources().getColor(R.color.colorAccent);
            if (white == 0)
                white = itemView.getContext().getResources().getColor(R.color.background_material_light);
        }

        @Override
        public void onClick(View v) {
            boolean isVeggie = ((ColorDrawable) v.getBackground()) != null && ((ColorDrawable) v.getBackground()).getColor() == green;

            int finalRadius = (int) Math.hypot(v.getWidth() / 2, v.getHeight() / 2);

            if (isVeggie) {
                mStudentName.setText(baconTitle);
                v.setBackgroundColor(white);
            } else {
                Animator anim = ViewAnimationUtils.createCircularReveal(v, (int) v.getWidth() / 2, (int) v.getHeight() / 2, 0, finalRadius);
                mStudentName.setText(veggieTitle);
                v.setBackgroundColor(green);
                anim.start();
            }
        }

        /**
         * Called when we need to update the contents of the ViewHolder to display the correct
         * names of students.
         *
         * @param studentName The name of the student
         */
        public void bind(String studentName) {
            mStudentName.setText(studentName);
        }
    }
}
