package uk.ac.shef.oak.com4510.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import uk.ac.shef.oak.com4510.model.Path;

public class PathViewModel extends AndroidViewModel {
    private PathRepository pathRepository;

    private LiveData<List<Path>> paths;

    private LiveData<Path> path;

    public PathViewModel(@NonNull Application application) {
        super(application);
        pathRepository = new PathRepository(application);
        paths = pathRepository.getPaths();
        path = pathRepository.getOnePath();
    }

    public LiveData<List<Path>> getPaths() {
        return paths;
    }

    public LiveData<Path> getOnePath() {
        return path;
    }

    public LiveData<Path> findPathById(int path_id) {
        return pathRepository.findPathById(path_id);
    }

    public void insertPath(Path path) {
        pathRepository.insertPath(path);
    }

    public void updatePath(Path path) {
        pathRepository.updatePath(path);
    }
}