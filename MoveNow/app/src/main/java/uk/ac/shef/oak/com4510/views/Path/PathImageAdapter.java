package uk.ac.shef.oak.com4510.views.Path;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.shef.oak.com4510.R;

import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.model.Path;
import uk.ac.shef.oak.com4510.viewmodels.ImageViewModel;

public class PathImageAdapter extends RecyclerView.Adapter<PathImageAdapter.ViewHolder> {
    private List<Path> paths;
    private ImageViewModel imageViewModel;
    private PathDetailAdapter pathDetailAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    private LifecycleOwner lifecycleOwner;

    public PathImageAdapter(Context context, List<Path> paths, ImageViewModel imageViewModel, LifecycleOwner lifecycleOwner) {
        this.paths = paths;
        this.context = context;
        this.imageViewModel = imageViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    //创建ViewHolder
    @NonNull
    @Override
    public PathImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_path_image, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    //填充视图
    @Override
    public void onBindViewHolder(@NonNull final PathImageAdapter.ViewHolder holder, final int position) {
        holder.dateTitle.setText(paths.get(position).getDate() + "    "+ paths.get(position).getTitle());
//        List<Image> images = imageViewModel.findImagesByPathId(paths.get(position).getPath_id());
//        pathDetailAdapter = new PathDetailAdapter(images);
//        linearLayoutManager = new LinearLayoutManager(context);
//        holder.recyclerView.setLayoutManager(linearLayoutManager);
//        holder.recyclerView.setAdapter(pathDetailAdapter);
//        holder.recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL));
        imageViewModel.findImagesByPathId(paths.get(position).getPath_id()).observe(lifecycleOwner, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                pathDetailAdapter = new PathDetailAdapter(images);
                int numberOfColumns = 4;
                holder.recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
                holder.recyclerView.setAdapter(pathDetailAdapter);
                holder.recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView dateTitle;
        RecyclerView recyclerView;

        ViewHolder(View itemView) {
            super(itemView);

            dateTitle = (TextView) itemView.findViewById(R.id.DateTitle);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        }

    }

}
