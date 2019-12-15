package uk.ac.shef.oak.com4510.views.Path;

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
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Image;

public class PathDetailAdapter extends RecyclerView.Adapter<PathDetailAdapter.ViewHolder> {
    private List<Image> images;
    private boolean pathImage;

    public PathDetailAdapter(List<Image> images, boolean pathImage) {
        this.images = images;
        this.pathImage = pathImage;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    @NonNull
    @Override
    public PathDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_path_detail, parent, false);
        PathDetailAdapter.ViewHolder holder = new PathDetailAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PathDetailAdapter.ViewHolder holder, final int position) {
        byte[] picture = images.get(position).getPicture();
        Bitmap image = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        image = scaleBitmap(image, 0.25f);
        holder.image.setImageBitmap(image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("path_id", images.get(position).getPath_id());
                bundle.putInt("image_id", images.get(position).getImage_id());
                NavController controller = Navigation.findNavController(v);
                if (pathImage) {
                    controller.navigate(R.id.action_pathImageFragment_to_imageFragment, bundle);
                } else {
                    controller.navigate(R.id.action_pathDetailFragment_to_imageFragment, bundle);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.Image);
        }
    }

    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

}
