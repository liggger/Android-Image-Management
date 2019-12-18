package uk.ac.shef.oak.com4510.views.Gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Image;

/**
 * @description The GalleryAdapter.
 * @author Zhicheng Zhou
 */

public class GalleryAdapter extends PagedListAdapter<Image, GalleryAdapter.ViewHolder> {

    // The images to be displayed;
    private List<Image> images;

    protected GalleryAdapter() {
        super(DIFF_CALLBACK);
    }

    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder}
     * of the given type to represent an item.
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return The new ViewHolder.
     */
    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_path_detail, parent, false);
        return new ViewHolder(v);
    }

    /**
     * This method updates the contents of the {@link ViewHolder#itemView}
     * to reflect the item at the given position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final GalleryAdapter.ViewHolder holder, final int position) {
        // Get the picture.
        byte[] picture = getItem(position).getPicture();

        // Decode the immutable bitmap from the specified byte array.
        Bitmap image = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        // Resize the input image.
        image = scaleBitmap(image, 0.25f);
        // Update the image at the given position.
        holder.image.setImageBitmap(image);

        /*
          Register a callback to be invoked when this itemView is clicked.
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            /**
             * When the itemView is clicked,
             * navigate to the imageFragment from the current navigation graph
             * @param v The itemView at the given position.
             */
            @Override
            public void onClick(View v) {
                /*
                  Create a mapping from String keys to various {@link Parcelable} values.
                 */
                Bundle bundle = new Bundle();
                // Inserts the path id into the mapping of this Bundle.
                bundle.putInt("path_id", getItem(position).getPath_id());
                // Inserts the image id into the mapping of this Bundle.
                bundle.putInt("image_id", getItem(position).getImage_id());

                /*
                  Find a {@link NavController} given a local {@link View}.
                 */
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_navigation_gallery_to_imageFragment, bundle);
            }
        });
    }

    /**
     * The ViewHolder describes an item view and metadata about its place within the RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        /**
         * The constructor of the ViewHolder
         * @param itemView The item view of the adapter.
         */
        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.Image);
        }
    }

    private static DiffUtil.ItemCallback<Image> DIFF_CALLBACK = new DiffUtil.ItemCallback<Image>() {

        /**
         * Called to check whether two objects represent the same item.
         * @param oldItem The item in the old list.
         * @param newItem The item in the new list.
         * @return True if the two items represent the same object or false if they are different.
         */
        @Override
        public boolean areItemsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
            return oldItem.getImage_id() == newItem.getImage_id();
        }

        /**
         * Called to check whether two items have the same data.
         * @param oldItem The item in the old list.
         * @param newItem The item in the new list.
         * @return True if the contents of the items are the same or false if they are different.
         */
        @Override
        public boolean areContentsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
                    return oldItem.equals(newItem);
        }
    };

    /**
     * Resize the input image.
     * @param origin The input image.
     * @param ratio The ratio of the image.
     * @return The new image.
     */
    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        // The width of the input image.
        int width = origin.getWidth();
        // The height of the input image.
        int height = origin.getHeight();

        // Optional matrix to be applied to the pixels.
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);

        // Get the bitmap that represents the specified subset of source
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }
}
