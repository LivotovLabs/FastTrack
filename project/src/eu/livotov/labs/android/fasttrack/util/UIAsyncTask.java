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
    protected boolean useActionBarProgress = true;
    protected boolean showProgress = true;
    protected boolean cancellable;

    public UIAsyncTask(BaseActivity activity)
    {
        this.activity = activity;
    }

    public UIAsyncTask(BaseActivity activity, String customMessage)
    {
        this.activity = activity;
        this.customMessage = customMessage;
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

    public Result performExecutionThread(final Param... parameters) throws Exception
    {
        return onUITaskBody(parameters);
    }

    public void onExecutionStarted()
    {
        showTaskProgressDialog();
    }

    public void onExecutionFinished(final Result result)
    {
        if (isActivityAlive())
        {
            hideTaskPrograssDialog();
            if (!userCancelled)
            {
                onUITaskFinished(result);
            }
        }
    }

    public void onExecutionFailed(final Throwable error)
    {
        Log.e(activity.getClass().getSimpleName(), error.getMessage(), error);

        if (isActivityAlive())
        {
            hideTaskPrograssDialog();

            if (!userCancelled && !onUITaskFailed(error))
            {
                activity.showError(error);
            }
        }
    }

    public void onExecutionAborted()
    {
        userCancelled = true;
        if (isActivityAlive())
        {
            hideTaskPrograssDialog();
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

    protected abstract void onUITaskPreExecute(final Param... params);

    protected abstract void onUITaskFinished(final Result result);

    protected abstract boolean onUITaskFailed(final Throwable error);

    protected abstract void onUITaskCancelled();

    protected abstract Result onUITaskBody(final Param... parameters) throws Exception;


}