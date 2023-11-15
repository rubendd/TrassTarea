package com.rdd.trasstarea.activities.listactivity.recycler;

import static java.security.AccessController.getContext;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.listactivity.dialogs.AboutDialog;
import com.rdd.trasstarea.activities.listactivity.recycler.viewholder.ViewHolder;
import com.rdd.trasstarea.comunicator.IComunicator;
import com.rdd.trasstarea.model.Task;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private final List<Task> tasksDataSet;
    private final IComunicator comunicator;
    private int posicion;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet ArrayList<Task> containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(List<Task> dataSet, IComunicator comunicator) {
        tasksDataSet = new ArrayList<>(dataSet);
        this.comunicator = comunicator;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list, viewGroup, false);

        return new ViewHolder(view,tasksDataSet,comunicator);
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bindTask(tasksDataSet.get(position));
        posicion = position;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tasksDataSet.size();
    }

    @Override
    public void onClick(View v) {
        // Acciones a realizar cuando se hace clic en el RelativeLayout
        if (posicion != RecyclerView.NO_POSITION) {
            actualTask = taskList.get(position);
            // Si la posición es válida, puedes obtener el objeto Task correspondiente a la posición
            showPopup(v);
        }
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.taskmenu, popup.getMenu());
        popup.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.description) {
            new AboutDialog(getContext(), actualTask.getDescription());
            return true;
        }
        if (item.getItemId() == R.id.delete) {
            deleteTask();
            return true;
        }
        return false;
    }


}
