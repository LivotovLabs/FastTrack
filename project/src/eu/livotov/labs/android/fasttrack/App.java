package eu.livotov.labs.android.fasttrack;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import eu.livotov.labs.android.robotools.async.RTAsyncTask;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 12/07/2013
 */
public class App extends Application
{

    private static App instance;
    private Activity lastRegisteredActivity;
    private AtomicBoolean appInitializationCompleted = new AtomicBoolean(false);
    private AtomicBoolean appInitializationFailed = new AtomicBoolean(false);

    public void onCreate()
    {
        super.onCreate();
        appInitializationCompleted.set(false);
        appInitializationFailed.set(false);
        instance = this;
        runBackgroundInitializationTasks();
    }

    private void runBackgroundInitializationTasks()
    {
        new RTAsyncTask<Void, Void, Void>()
        {

            public Void performExecutionThread(final Void... voids) throws Exception
            {
                onInitializeAppInBackground();
                return null;
            }

            public void onExecutionStarted()
            {
            }

            public void onExecutionFinished(final Void aVoid)
            {
                appInitializationFailed.set(false);
                appInitializationCompleted.set(true);
            }

            public void onExecutionFailed(final Throwable throwable)
            {
                appInitializationFailed.set(true);
                appInitializationCompleted.set(true);
                Log.e(App.class.getCanonicalName(), "Error starting up the application: " + throwable.getMessage(), throwable);
                throw new RuntimeException(throwable);
            }

            public void onExecutionAborted()
            {
            }
        }.executeAsync();
    }

    private void onInitializeAppInBackground() throws Exception
    {
        Thread.sleep(3000L);
        //todo: place your non-instant initialization tasks here.
    }

    public void onTerminate()
    {
        super.onTerminate();
        instance = null;
    }

    public static Context getContext()
    {
        return instance;
    }

    public static App getInstance()
    {
        return instance;
    }

    public static boolean isAppInitializationInProgress()
    {
        return !instance.appInitializationCompleted.get();
    }

    public static boolean isAppInitializationFailed()
    {
        return instance.appInitializationFailed.get();
    }

    public static boolean isAppReady()
    {
        return instance.appInitializationCompleted.get() && !instance.appInitializationFailed.get();
    }

    public static void waitForApplication(final ApplicationStartupListener readyListener)
    {
        if (isAppReady())
        {
            readyListener.onApplicationReady();
        } else
        {
            final Timer timer = new Timer();
            timer.schedule(new TimerTask()
            {
                public void run()
                {
                    if (isAppReady() || isAppInitializationFailed())
                    {
                        timer.cancel();

                        if (isAppReady())
                        {
                            readyListener.onApplicationReady();
                        }
                    }
                }
            }, 500, 500);
        }
    }

    public static Context getDialogContext()
    {
        return instance.lastRegisteredActivity;
    }

    public static synchronized void registerTopActivity(Activity activity)
    {
        instance.lastRegisteredActivity = activity;
    }

    public static synchronized void unregisterTopActivity(Activity activity)
    {
        if (activity != null && activity == instance.lastRegisteredActivity)
        {
            instance.lastRegisteredActivity = null;
        }
    }

    public static void sendPrivateBroadcast(Intent data)
    {
        LocalBroadcastManager.getInstance(instance).sendBroadcast(data);
    }

    public static void sendPrivateBroadcastAndWait(Intent data)
    {
        LocalBroadcastManager.getInstance(instance).sendBroadcastSync(data);
    }

    public static void registerPrivateBroadcastReceiver(final BroadcastReceiver receiver, IntentFilter filter)
    {
        LocalBroadcastManager.getInstance(instance).registerReceiver(receiver, filter);
    }

    public static void unregisterPrivateBroadcastReceiver(final BroadcastReceiver receiver)
    {
        try
        {
            LocalBroadcastManager.getInstance(instance).unregisterReceiver(receiver);
        } catch (Throwable ignored)
        {
        }
    }

    public interface ApplicationStartupListener
    {

        void onApplicationReady();
    }
}
