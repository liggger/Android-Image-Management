package uk.ac.shef.oak.com4510.views.Path;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Path;

/**
 * @description The adapter of the path fragment.
 * @author Yichen Niu
 */

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.ViewHolder>  {

    // The paths to be displayed.
    private List<Path> paths;

    /**
     * The constructor.
     * @param paths The paths.
     */
    public PathAdapter(List<Path> paths) {
        this.paths = paths;
    }

    /**
     * Get the size of the paths.
     * @return the paths' sizes
     */
    @Override
    public int getItemCount() {
        return paths.size();
    }

    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder}
     * of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return The new holder.
     */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_path, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    /**
     * This method updates the contents of the {@link ViewHolder#itemView}
     * to reflect the item at the given position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(@NonNull final PathAdapter.ViewHolder holder, final int position) {
        // If the position number is even, set the background color white.
        if (position % 2 == 0) {
            holder.constraintLayout.setBackgroundColor(Color.WHITE);
        }

        // Constructs a SimpleDateFormat using the given pattern.
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  Create a mapping from String keys to various {@link Parcelable} values.
                 */
                Bundle bundle = new Bundle();
                // Insert the title into the mapping of this Bundle.
                bundle.putString("title", paths.get(position).getTitle());
                // Insert the path id into the mapping of this Bundle.
                bundle.putString("path_id", String.valueOf(paths.get(position).getPath_id()));
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_navigation_paths_to_pathDetailFragment, bundle);
            }
        });
        // Set the date.
        holder.date.setText(String.format("%s   %s", format.format(paths.get(position).getDate()), paths.get(position).getTitle()));
    }

    /**
     * The ViewHolder describes an item view and metadata about its place within the RecyclerView
     */

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.Layout);
            date = itemView.findViewById(R.id.Date);

        }
    }
}
