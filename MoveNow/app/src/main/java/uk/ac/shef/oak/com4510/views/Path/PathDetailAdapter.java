package uk.ac.shef.oak.com4510.views.Path;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Image;

public class PathDetailAdapter extends RecyclerView.Adapter<PathDetailAdapter.ViewHolder> {
    private List<Image> images;

    public PathDetailAdapter(List<Image> images) {
        this.images = images;
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
        System.out.println("yeeee");
        holder.image.setImageBitmap(image);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.Image);
        }
    }

}
