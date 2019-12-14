package uk.ac.shef.oak.com4510.viewmodels;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.model.ImageDao;
import uk.ac.shef.oak.com4510.model.ImagePathDatabase;

public class ImageRepository {
    private ImageDao imageDao;

    public ImageRepository(Context context) {
        ImagePathDatabase imageDatabase = ImagePathDatabase.getDatabase(context.getApplicationContext());
        imageDao = imageDatabase.getImageDao();
    }

    public void insertOneImage(Image image) {
        new InsertAsyncTask(imageDao).execute(image);
    }

    public LiveData<Image> getOneImage() {
        return imageDao.getOneImage();
    }

    public LiveData<List<Image>> getImages() {
        return imageDao.getImages();
    }

    public LiveData<List<Image>> findImagesByPath(int path_id) {
        return imageDao.findImagesByPath(path_id);
    }

//    public List<Image> findImagesByPathId(int path_id) {
//        return imageDao.findImagesByPathId(path_id);
//    }

    public LiveData<List<Image>> findImagesByPathId(int path_id) {
        return imageDao.findImagesByPathId(path_id);
    }

    public LiveData<Image> findImageByImageId(int image_id) {
        return imageDao.findImageByImageId(image_id);
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
