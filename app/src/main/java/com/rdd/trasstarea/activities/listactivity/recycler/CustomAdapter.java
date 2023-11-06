package com.rdd.trasstarea.activities.listactivity.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.listactivity.recycler.viewholder.ViewHolder;
import com.rdd.trasstarea.model.Task;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<Task> tasksDataSet;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet ArrayList<Task> containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(List<Task> dataSet) {
        tasksDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list, viewGroup, false);

        return new ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bindTask(tasksDataSet.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tasksDataSet.size();
    }
}
