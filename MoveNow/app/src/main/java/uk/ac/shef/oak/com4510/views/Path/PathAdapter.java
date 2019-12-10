package uk.ac.shef.oak.com4510.views.Path;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Path;

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.ViewHolder> {
    //数据源
    private List<Path> paths;

    public PathAdapter(List<Path> paths) {
        this.paths = paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }
    //返回item个数
    @Override
    public int getItemCount() {
        return paths.size();
    }

    //创建ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_path, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    //填充视图
    @Override
    public void onBindViewHolder(@NonNull final PathAdapter.ViewHolder holder, final int position) {
        holder.pathTitle.setText(paths.get(position).getTitle());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pathTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            pathTitle = itemView.findViewById(R.id.pathTitle);
        }
    }
}
