package uk.ac.shef.oak.com4510.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import uk.ac.shef.oak.com4510.model.Path;

/**
 * @description The PathViewModel.
 * @author Zhicheng Zhou
 */

public class PathViewModel extends AndroidViewModel {
    // The path repository.
    private PathRepository pathRepository;
    // The whole paths.
    private LiveData<List<Path>> paths;
    // The latest path.
    private LiveData<Path> path;

    /**
     * The constructor of the PathViewModel.
     * @param application The Application.
     */
    public PathViewModel(@NonNull Application application) {
        super(application);
        pathRepository = new PathRepository(application);
        paths = pathRepository.getPaths();
        path = pathRepository.getOnePath();
    }

    /**
     * Get all the paths.
     * @return All the paths.
     */
    public LiveData<List<Path>> getPaths() {
        return paths;
    }

    /**
     * Get the latest path.
     * @return The latest path.
     */
    public LiveData<Path> getOnePath() {
        return path;
    }

    /**
     * Find the path according to the path id.
     * @param path_id The path id.
     * @return The path.
     */
    public LiveData<Path> findPathById(int path_id) {
        return pathRepository.findPathById(path_id);
    }

    /**
     * Insert the path.
     * @param path The path.
     */
    public void insertPath(Path path) {
        pathRepository.insertPath(path);
    }

    /**
     * Update the path.
     * @param path The path.
     */
    public void updatePath(Path path) {
        pathRepository.updatePath(path);
    }

    /**
     * Count the path number.
     * @return The path number.
     */
    public LiveData<Integer> countPathNumber() {
        return pathRepository.countPathNumber();
    }
}