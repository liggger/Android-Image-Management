package uk.ac.shef.oak.storeimage;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

public class ImageRepository {
    private ImageDao imageDao;

    public ImageRepository(Context context) {
        ImageDatabase imageDatabase = ImageDatabase.getDatabase(context.getApplicationContext());
        imageDao = imageDatabase.getImageDao();
    }

    public void insertOneImage(Image image) {
        new InsertAsyncTask(imageDao).execute(image);
    }

    public LiveData<Image> getOneImage() {
        return imageDao.getOneImage();
    }

    private static class InsertAsyncTask extends AsyncTask<Image, Void, Void> {
        private ImageDao imageDao;

        public InsertAsyncTask(ImageDao imageDao) {
            this.imageDao = imageDao;
        }

        @Override
        protected Void doInBackground(final Image... images) {
            imageDao.insertImage(images[0]);
            return null;
        }
    }
}
