package uk.ac.shef.oak.com4510.viewmodels;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import uk.ac.shef.oak.com4510.model.Path;
import uk.ac.shef.oak.com4510.model.PathDao;
import uk.ac.shef.oak.com4510.model.PathDatabase;

public class PathRepository {
    private PathDao pathDao;

    public PathRepository(Context context) {
        PathDatabase pathDatabase = PathDatabase.getDatabase(context.getApplicationContext());
        pathDao = pathDatabase.getPathDao();
    }

    public void insertPath(Path path) {
        new InsertAsyncTask(pathDao).execute(path);
    }

    public LiveData<List<Path>> getPaths() {
        return pathDao.getPaths();
    }

    private static class InsertAsyncTask extends AsyncTask<Path, Void, Void> {
        private PathDao pathDao;

        public InsertAsyncTask(PathDao pathDao) {this.pathDao = pathDao;}

        @Override
        protected Void doInBackground(final Path... paths) {
            pathDao.insertPath(paths[0]);
            return null;
        }
    }
}
