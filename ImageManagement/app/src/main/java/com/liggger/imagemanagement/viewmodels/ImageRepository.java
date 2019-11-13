package com.liggger.imagemanagement.viewmodels;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.liggger.imagemanagement.model.Image;
import com.liggger.imagemanagement.model.ImageDao;
import com.liggger.imagemanagement.model.ImageDatabase;

import java.util.Random;

public class ImageRepository {
    private ImageDao imageDao;

    public ImageRepository(Context context) {
        ImageDatabase imageDatabase = ImageDatabase.getDatabase(context.getApplicationContext());
        imageDao = imageDatabase.getImageDao();
    }

    public void generateNewImage() {
        Random r = new Random();
        int i1 = r.nextInt(10000) + 1;
        new InsertAsyncTask(imageDao).execute(new Image(i1));
    }

    public void insertOneImage(Image image) {
        new InsertAsyncTask(imageDao).execute(image);
    }

    public LiveData<Image> getOneImage() {
        return imageDao.getOneImage();
    }

    private static class InsertAsyncTask extends AsyncTask<Image, Void, Void> {
        private ImageDao imageDao;

        public InsertAsyncTask(ImageDao imageDao) {this.imageDao = imageDao;}

        @Override
        protected Void doInBackground(final Image... image) {
            imageDao.insertImage(image[0]);
            return null;
        }
    }

}
