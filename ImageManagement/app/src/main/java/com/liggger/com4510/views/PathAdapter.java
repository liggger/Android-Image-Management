package com.liggger.com4510.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.liggger.com4510.model.Path;
import com.liggger.imagemanagement.R;

import java.util.List;

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.ViewHolder> {
    //数据源
    private List<Path> mList;

    public PathAdapter(List<Path> list) {
        mList = list;
    }

    //返回item个数
    @Override
    public int getItemCount() {
        return mList.size() ;
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
        holder.date.setText(mList.get(position).getTime());
        holder.pathTitle.setText(mList.get(position).getName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView pathTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.Date);
            pathTitle = itemView.findViewById(R.id.pathTitle);
        }
    }
}
