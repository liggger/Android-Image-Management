package uk.ac.shef.oak.com4510.views.Home.Service;

import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobService extends android.app.job.JobService {
    private static String TAG= JobService.class.getSimpleName();
    private static RestartServiceBroadcastReceiver restartSensorServiceReceiver;
    private static JobService instance;
    private static JobParameters jobParameters;

    /**
     * Called to indicate that the job has begun executing.
     * @param jobParameters The JobParameters.
     * @return {@code false} means that this job has completed its work.
     */
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        ProcessMainClass bck = new ProcessMainClass();
        bck.launchService(this);
        registerRestarterReceiver();
        instance= this;
        JobService.jobParameters= jobParameters;

        return false;
    }

    private void registerRestarterReceiver() {

        /*
          the context can be null if app just installed and this is called from restartsensorservice
          Final decision: in case it is called from installation of new version (i.e. from manifest, the application is
          null. So we must use context.registerReceiver. Otherwise this will crash and we try with context.getApplicationContext
         */
        if (restartSensorServiceReceiver == null)
            restartSensorServiceReceiver = new RestartServiceBroadcastReceiver();
        else try{
            unregisterReceiver(restartSensorServiceReceiver);
        } catch (Exception e){
            // not registered
        }
        // give the time to run
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // we register the  receiver that will restart the background service if it is killed
                // see onDestroy of Service
                IntentFilter filter = new IntentFilter();
                filter.addAction(Globals.RESTART_INTENT);
                try {
                    registerReceiver(restartSensorServiceReceiver, filter);
                } catch (Exception e) {
                    try {
                        getApplicationContext().registerReceiver(restartSensorServiceReceiver, filter);
                    } catch (Exception ex) {

                    }
                }
            }
        }, 1000);

    }

    /**
     * Called if Android kills the job service.
     * @param jobParameters The JobParameters.
     * @return {@code true} to indicate to the JobManager whether you'd like to reschedule
     * this job based on the retry criteria provided at job creation-time; or {@code false}
     * to end the job entirely.
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(TAG, "Stopping job");
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        // give the time to run
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                unregisterReceiver(restartSensorServiceReceiver);
            }
        }, 1000);

        return false;
    }


    /**
     * called when the tracker is stopped for whatever reason.
     * @param context The context.
     */
    public static void stopJob(Context context) {
        if (instance!=null && jobParameters!=null) {
            try{
                instance.unregisterReceiver(restartSensorServiceReceiver);
            } catch (Exception e){
                // not registered
            }
            Log.i(TAG, "Finishing job");
            instance.jobFinished(jobParameters, true);
        }
    }
}
