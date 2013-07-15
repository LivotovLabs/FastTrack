package eu.livotov.labs.android.fasttrack.util;

import android.content.DialogInterface;
import android.util.Log;
import eu.livotov.labs.android.fasttrack.base.BaseActivity;
import eu.livotov.labs.android.robotools.async.RTAsyncTask;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 15/07/2013
 */
public abstract class UIAsyncTask<Param, Progress, Result> extends RTAsyncTask<Param, Progress, Result> implements DialogInterface.OnCancelListener
{

    protected BaseActivity activity;
    protected String customMessage;
    protected boolean userCancelled;

    public UIAsyncTask(BaseActivity activity)
    {
        this.activity = activity;
    }

    public UIAsyncTask(BaseActivity activity, String customMessage)
    {
        this.activity = activity;
        this.customMessage = customMessage;
    }

    public Result performExecutionThread(final Param... parameters) throws Exception
    {
        return onUITaskBody(parameters);
    }

    public void onExecutionStarted()
    {
        activity.showProgressDialog(customMessage, true, this);
    }

    public void onExecutionFinished(final Result result)
    {
        if (!userCancelled && isActivityAlive())
        {
            activity.hideProgressDialog();
            onUITaskFinished(result);
        }
    }

    public void onExecutionFailed(final Throwable error)
    {
        Log.e(activity.getClass().getSimpleName(), error.getMessage(), error);

        if (isActivityAlive())
        {
            activity.hideProgressDialog();
            if (!onUITaskFailed(error))
            {
                activity.showError(error);
            }
        }
    }

    public void onExecutionAborted()
    {
        if (isActivityAlive())
        {
            activity.hideProgressDialog();
            onUITaskCancelled();
        }
    }

    private boolean isActivityAlive()
    {
        return !activity.isFinishing() && !activity.isFinishing();
    }

    public void onCancel(final DialogInterface dialogInterface)
    {
        userCancelled = true;
        cancel(true);
    }

    protected abstract void onUITaskPreExecute(final Param... params);

    protected abstract void onUITaskFinished(final Result result);

    protected abstract boolean onUITaskFailed(final Throwable error);

    protected abstract void onUITaskCancelled();

    protected abstract Result onUITaskBody(final Param... parameters) throws Exception;


}