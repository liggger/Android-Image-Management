package uk.ac.shef.oak.com4510.viewmodels;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;

import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.model.ImageDao;
import uk.ac.shef.oak.com4510.model.ImagePathDatabase;

/**
 * @description The ImageRepository.
 * @author Zhicheng Zhou
 */

public class ImageRepository {

    private ImageDao imageDao;

    /**
     * The constructor of the ImageRepository and get the value of the imageDao.
     * @param context The context.
     */
    ImageRepository(Context context) {
        ImagePathDatabase imageDatabase = ImagePathDatabase.getDatabase(context.getApplicationContext());
        imageDao = imageDatabase.getImageDao();
    }

    /**
     * Insert one image using asynchronous task.
     * @param image The image to be inserted.
     */
    void insertOneImage(Image image) {
        new InsertAsyncTask(imageDao).execute(image);
    }

    /**
     * Get the whole images by date in ascending order.
     * For every 20 pages, it will load more pages.
     * @return PagedList images.
     */
    LiveData<PagedList<Image>> getImagesByDate() {
        return new LivePagedListBuilder<>(
                imageDao.getImagesByDate(), 20).build();
    }

    /**
     * Find the target images according to the path id.
     * @param path_id The path id.
     * @return The target images.
     */
    LiveData<List<Image>> findImagesByPathId(int path_id) {
        return imageDao.findImagesByPathId(path_id);
    }

    /**
     * Find the target image according to the image id.
     * @param image_id The image id.
     * @return The target image.
     */
    LiveData<Image> findImageByImageId(int image_id) {
        return imageDao.findImageByImageId(image_id);
    }

    /**
     * Count the image number.
     * @return The image number.
     */
    public LiveData<Integer> countImageNumber() {
        return imageDao.countImageNumber();
    }

    /**
     * Insert the image using AsyncTak.
     */
    private static class InsertAsyncTask extends AsyncTask<Image, Void, Void> {
        private ImageDao imageDao;

        InsertAsyncTask(ImageDao imageDao) {this.imageDao = imageDao;}

        @Override
        protected Void doInBackground(final Image... image) {
            imageDao.insertImage(image[0]);
            return null;
        }
    }

}
