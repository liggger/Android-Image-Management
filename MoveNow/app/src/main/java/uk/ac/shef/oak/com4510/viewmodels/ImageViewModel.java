package uk.ac.shef.oak.com4510.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;

import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.model.Path;

/**
 * @description The ImageViewModel.
 * @author Zhicheng Zhou
 */

public class ImageViewModel extends AndroidViewModel {
    private ImageRepository imageRepository;

    /**
     * The constructor of the ImageViewModel.
     * @param application
     */
    public ImageViewModel(@NonNull Application application) {
        super(application);
        imageRepository = new ImageRepository(application);
    }

    /**
     * Get the whole images by date in ascending order.
     * For every 20 pages, it will load more pages.
     * @return PagedList images.
     */
    public final LiveData<PagedList<Image>> getImagesByDate() {
        return imageRepository.getImagesByDate();
    }

    /**
     * Insert the image.
     * @param image The image.
     */
    public void insertOneImage(Image image) {
        imageRepository.insertOneImage(image);
    }

    /**
     * Find images according to the path id.
     * @param path_id The path id.
     * @return The images.
     */
    public LiveData<List<Image>> findImagesByPathId(int path_id) {
        return imageRepository.findImagesByPathId(path_id);
    }

    /**
     * Find the image according to the image id.
     * @param image_id The image id.
     * @return The image.
     */
    public LiveData<Image> findImageByImageId(int image_id) {
        return imageRepository.findImageByImageId(image_id);
    }
}
