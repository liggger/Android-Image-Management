package uk.ac.shef.oak.com4510.viewmodels;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import uk.ac.shef.oak.com4510.model.ImagePathDatabase;
import uk.ac.shef.oak.com4510.model.Path;
import uk.ac.shef.oak.com4510.model.PathDao;

/**
 * @description The PathRepository.
 * @author Zhicheng Zhou
 */

public class PathRepository {
    private PathDao pathDao;

    /**
     * The constructor of the PathRepository.
     * @param context The context.
     */
    public PathRepository(Context context) {
        ImagePathDatabase imageDatabase = ImagePathDatabase.getDatabase(context.getApplicationContext());
        pathDao = imageDatabase.getPathDao();
    }

    /**
     * Insert the path.
     * @param path The path.
     */
    public void insertPath(Path path) {
        new InsertAsyncTask(pathDao).execute(path);
    }

    /**
     * Get all paths.
     * @return The whole paths.
     */
    public LiveData<List<Path>> getPaths() {
        return pathDao.getPaths();
    }

    /**
     * Get the latest path.
     * @return The latest path.
     */
    public LiveData<Path> getOnePath() {
        return pathDao.getOnePath();
    }

    /**
     * Find the path according to the path id.
     * @param path_id The path id.
     * @return The path.
     */
    public LiveData<Path> findPathById(int path_id) {
        return pathDao.findPathById(path_id);
    }

    /**
     * Update the path.
     * @param path The path to be updated.
     */
    public void updatePath(Path path) {
        new UpdateAsyncTask(pathDao).execute(path);
    }

    /**
     * Count the path number.
     * @return The path number.
     */
    public LiveData<Integer> countPathNumber() {
        return pathDao.countPathNumber();
    }

    /**
     * Insert the path using the AsyncTask.
     */
    private static class InsertAsyncTask extends AsyncTask<Path, Void, Void> {
        private PathDao pathDao;

        public InsertAsyncTask(PathDao pathDao) {this.pathDao = pathDao;}

        @Override
        protected Void doInBackground(final Path... paths) {
            pathDao.insertPath(paths[0]);
            return null;
        }
    }

    /**
     * Update the path using the AsyncTask.
     */
    private static class UpdateAsyncTask extends AsyncTask<Path, Void, Void> {
        private PathDao pathDao;

        public UpdateAsyncTask(PathDao pathDao) {
            this.pathDao = pathDao;
        }

        @Override
        protected Void doInBackground(Path... paths) {
            pathDao.updatePath(paths[0]);
            return null;
        }
    }
}
