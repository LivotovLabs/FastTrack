package eu.livotov.labs.android.fasttrack.async;

import android.content.DialogInterface;
import android.util.Log;
import eu.livotov.labs.android.fasttrack.base.BaseActivity;
import eu.livotov.labs.android.robotools.async.RTAsyncTask;

import java.util.Timer;
import java.util.TimerTask;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 15/07/2013
 */
public abstract class UIAsyncTask<Param, Progress, Result> extends RTAsyncTask<Param, Progress, Result> implements DialogInterface.OnCancelListener
{

    private final String TAG = UIAsyncTask.class.getName();


    protected BaseActivity activity;
    protected String customMessage;
    protected boolean userCancelled;
    protected boolean useActionBarProgress = true;
    protected boolean showProgress = true;
    protected boolean cancellable;
    private Timer busyTimer;
    private int delayedProgress = 0;

    public UIAsyncTask(BaseActivity activity)
    {
        this.activity = activity;
        this.activity.addUiTask(this);
    }

    public UIAsyncTask(BaseActivity activity, String customMessage)
    {
        this.activity = activity;
        this.customMessage = customMessage;
        this.activity.addUiTask(this);
    }

    public UIAsyncTask message(final String customMessage)
    {
        this.customMessage = customMessage;
        this.showProgress = true;
        return this;
    }

    public UIAsyncTask displayProgress(final boolean showProgress)
    {
        this.showProgress = showProgress;
        return this;
    }

    public UIAsyncTask useActionBarProgress(final boolean useActionBarProgress)
    {
        this.useActionBarProgress = useActionBarProgress;
        return this;
    }

    public UIAsyncTask cancellable(boolean cancellable)
    {
        this.cancellable = cancellable;
        return this;
    }

    public UIAsyncTask delayProgressMessage(int delayMs)
    {
        this.delayedProgress = delayMs;
        return this;
    }

    public Result performExecutionThread(final Param... parameters) throws Exception
    {
        return onUITaskBody(parameters);
    }

    public void onExecutionStarted()
    {
        Log.d(TAG, "start task");

        if (delayedProgress > 0)
        {
            busyTimer = new Timer();
            busyTimer.schedule(new TimerTask()
            {
                public void run()
                {
                    if (isActivityAlive())
                    {
                        activity.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                UIAsyncTask.this.showTaskProgressDialog();
                            }
                        });
                    }
                }
            }, delayedProgress);
        } else
        {
            showTaskProgressDialog();
        }
    }


    public void onExecutionFinished(final Result result)
    {
        Log.d(TAG, "finish task");

        cancelBusyTimer();

        if (isActivityAlive())
        {
            hideTaskPrograssDialog();
            if (!userCancelled)
            {
                onUITaskFinished(result);
            }
        }
        activity.removeUiTask(this);
    }

    public void onExecutionFailed(final Throwable error)
    {
        Log.d(TAG, "failed task");

        cancelBusyTimer();
        Log.e(activity.getClass().getSimpleName(), error.getMessage(), error);

        if (isActivityAlive())
        {
            hideTaskPrograssDialog();

            if (!userCancelled && !onUITaskFailed(error))
            {
                activity.showError(error);
            }
        }
        activity.removeUiTask(this);
    }

    public void onExecutionAborted()
    {
        Log.d(TAG, "abort task");

        cancelBusyTimer();
        userCancelled = true;

        if (isActivityAlive())
        {
            hideTaskPrograssDialog();
            onUITaskCancelled();
        }
        activity.removeUiTask(this);
    }

    public void onCancel(final DialogInterface dialogInterface)
    {
        Log.d(TAG, "cancel task");
        userCancelled = true;
        cancel(true);
    }

    private void showTaskProgressDialog()
    {
        if (showProgress)
        {
            activity.setUseActionBarProgress(useActionBarProgress);
            activity.showProgressDialog(customMessage, cancellable, true, this);
        }
    }

    private void hideTaskPrograssDialog()
    {
        if (showProgress)
        {
            activity.hideProgressDialog();
        }
    }

    private boolean isActivityAlive()
    {
        return !activity.isFinishing() && !activity.isFinishing();
    }

    private void cancelBusyTimer()
    {
        if (busyTimer != null)
        {
            try
            {
                busyTimer.cancel();
                busyTimer = null;
            } catch (Throwable ignored)
            {
            }
        }
    }

    protected abstract void onUITaskPreExecute(final Param... params);

    protected abstract void onUITaskFinished(final Result result);

    protected abstract boolean onUITaskFailed(final Throwable error);

    protected abstract void onUITaskCancelled();

    protected abstract Result onUITaskBody(final Param... parameters) throws Exception;


}