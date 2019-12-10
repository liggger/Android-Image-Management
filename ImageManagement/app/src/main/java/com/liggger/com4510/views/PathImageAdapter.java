package com.liggger.com4510.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.liggger.com4510.model.ImageElement;
import com.liggger.imagemanagement.R;

import java.util.List;

public class PathImageAdapter extends RecyclerView.Adapter<PathImageAdapter.View_Holder> {
    static private Context context;
    private static List<ImageElement> items;

    public PathImageAdapter(List<ImageElement> items) {
        this.items = items;
    }

    public PathImageAdapter(Context cont, List<ImageElement> items) {
        super();
        this.items = items;
        context = cont;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_path_image,
                parent, false);
        View_Holder holder = new View_Holder(v);
        context= parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (holder!=null && items.get(position)!=null) {
            if (items.get(position).getImage()!=-1) {
                holder.imageView.setImageResource(items.get(position).getImage());
                holder.textView.setText("1");
            }
            else if (items.get(position).getFile()!=null){
                new UploadSingleImageTask().execute(new HolderAndPosition(position, holder));
            }
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ShowImageActivity.class);
//                    intent.putExtra("position", position);
//                    context.startActivity(intent);
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class UploadSingleImageTask extends AsyncTask<HolderAndPosition, Void, Bitmap> {
        HolderAndPosition holdAndPos;

        @Override
        protected Bitmap doInBackground(HolderAndPosition... holderAndPosition) {
            holdAndPos = holderAndPosition[0];
            Bitmap myBitmap = decodeSampledBitmapFromResource(items.get(holdAndPos.position).getFile().getAbsolutePath(), 100, 100);
            return myBitmap;
        }

        @Override
        protected void onPostExecute (Bitmap bitmap) {
            holdAndPos.holder.imageView.setImageBitmap(bitmap);
        }
    }

    private class HolderAndPosition{
        int position;
        View_Holder holder;

        public HolderAndPosition(int position, View_Holder holder) {
            this.position = position;
            this.holder = holder;
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        //options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 3;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    // convenience method for getting data at click position
    ImageElement getItem(int id) {
        return items.get(id);
    }


    public class View_Holder extends RecyclerView.ViewHolder  {
        ImageView imageView;
        TextView textView;

        View_Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }

    }

    public static List<ImageElement> getItems() {
        return items;
    }

    public static void setItems(List<ImageElement> items) {
        PathImageAdapter.items = items;
    }
}
