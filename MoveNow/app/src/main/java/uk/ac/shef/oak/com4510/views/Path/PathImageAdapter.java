package uk.ac.shef.oak.com4510.views.Path;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import uk.ac.shef.oak.com4510.R;

import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.model.Path;
import uk.ac.shef.oak.com4510.viewmodels.ImageViewModel;

/**
 * @description The adapter of the path image fragment.
 * @author Yichen Niu
 */

public class PathImageAdapter extends RecyclerView.Adapter<PathImageAdapter.ViewHolder> {
    // The paths to be displayed.
    private List<Path> paths;
    // The ImageViewModel.
    private ImageViewModel imageViewModel;
    // The PathDetailAdapter.
    private PathDetailAdapter pathDetailAdapter;
    // The context.
    private Context context;
    // The LifecycleOwner.
    private LifecycleOwner lifecycleOwner;

    /**
     * Construct the Adapter.
     *
     * @param context The context
     * @param paths The paths list
     * @param imageViewModel The ImageViewModel
     * @param lifecycleOwner The LifecycleOwner
     */
    public PathImageAdapter(Context context, List<Path> paths, ImageViewModel imageViewModel, LifecycleOwner lifecycleOwner) {
        this.paths = paths;
        this.context = context;
        this.imageViewModel = imageViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * Get the size of the paths.
     *
     * @return the paths's sizes
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
    public PathImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_path_image, parent, false);
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
    public void onBindViewHolder(@NonNull final PathImageAdapter.ViewHolder holder, final int position) {
        // Constructs a SimpleDateFormat using the given pattern.
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        // Update the title TextView at the given position.
        holder.dateTitle.setText(String.format("%s    %s", format.format(paths.get(position).getDate()), paths.get(position).getTitle()));
        /*
          Find the images according to the path id.
         */
        imageViewModel.findImagesByPathId(paths.get(position).getPath_id()).observe(lifecycleOwner, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                // Initialize the PathDetailAdapter.
                pathDetailAdapter = new PathDetailAdapter(images, true);
                // The number of columns in the grid.
                int numberOfColumns = 4;
                // Set the GridLayoutManager that this RecyclerView will use.
                holder.recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
                // Set the gallery adapter to provide child views on demand.
                holder.recyclerView.setAdapter(pathDetailAdapter);
            }
        });

    }

    /**
     * The ViewHolder describes an item view and metadata about its place within the RecyclerView
     */

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView dateTitle;
        RecyclerView recyclerView;

        /**
         * The constructor of the ViewHolder
         * @param itemView The item view of the adapter.
         */
        ViewHolder(View itemView) {
            super(itemView);
            dateTitle = itemView.findViewById(R.id.DateTitle);
            recyclerView = itemView.findViewById(R.id.recycler_view);
        }

    }

}