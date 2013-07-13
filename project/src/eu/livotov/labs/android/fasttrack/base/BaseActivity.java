package eu.livotov.labs.android.fasttrack.base;

import android.app.ProgressDialog;
import android.content.*;
import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import eu.livotov.labs.android.fasttrack.App;
import eu.livotov.labs.android.fasttrack.R;
import eu.livotov.labs.android.robotools.ui.RTDialogs;

public abstract class BaseActivity extends SherlockFragmentActivity
{

    private BroadcastReceiver privateBroadcastsReceiver = new PrivateBroadcastsReceiver();
    private ProgressDialog progressDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.show();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    protected void onDestroy()
    {
        disablePrivateBroadcastsReceiver();
        super.onDestroy();
    }

    protected void onResume()
    {
        super.onResume();
        App.registerTopActivity(this);
    }

    protected void onPause()
    {
        super.onPause();
        App.unregisterTopActivity(this);
    }

    protected void enablePrivateBroadcastsReceiver(IntentFilter filter)
    {
        App.registerPrivateBroadcastReceiver(privateBroadcastsReceiver, filter);
    }

    protected void disablePrivateBroadcastsReceiver()
    {
        App.unregisterPrivateBroadcastReceiver(privateBroadcastsReceiver);
    }

    protected void onPrivateBroadcastReceived(Intent message)
    {
    }

    public synchronized void showProgressDialog(boolean cancelable)
    {
        showProgressDialog(0, cancelable);
    }

    public synchronized void showProgressDialog(final int messageRes, final boolean cancelable)
    {
        if (progressDialog == null)
        {
            progressDialog = ProgressDialog.show(BaseActivity.this, getString(R.string.app_name), getString(messageRes), true, cancelable, new DialogInterface.OnCancelListener()
            {
                public void onCancel(final DialogInterface dialogInterface)
                {
                    progressDialog = null;
                }
            });
        } else
        {
            if (messageRes > 0)
            {
                progressDialog.setMessage(getString(messageRes));
            }
        }
    }

    public synchronized void hideProgressDialog()
    {
        try
        {
            progressDialog.hide();
            progressDialog.dismiss();
        } catch (Throwable ignored)
        {
        } finally
        {
            progressDialog = null;
        }
    }

    public void showError(Throwable error)
    {
        showError(error, null);
    }

    public void showError(Throwable error, RTDialogs.RTModalDialogResultListener listener)
    {
        RTDialogs.showMessageBox(this,R.drawable.ic_launcher,getString(R.string.fs_dialog_error_title),error.getMessage(), listener);
    }

    public void showErrorAndFinish(Throwable error)
    {
        showError(error, new RTDialogs.RTModalDialogResultListener()
        {
            public void onDialogClosed()
            {
                finish();
            }
        });
    }

    public void showMessage(final String message)
    {
        showMessage(message, null);
    }

    public void showMessage(final int messageRes)
    {
        showMessage(getString(messageRes), null);
    }

    public void showMessage(final int messageRes, RTDialogs.RTModalDialogResultListener dismissListener)
    {
        showMessage(getString(messageRes), dismissListener);
    }

    public void showMessage(final String message, RTDialogs.RTModalDialogResultListener dismissListener)
    {
        RTDialogs.showMessageBox(this, R.drawable.ic_launcher, getString(R.string.app_name), message, dismissListener);
    }

    public void showQuestion(final int message, RTDialogs.RTYesNoDialogResultListener yesNoListener)
    {
        showQuestion(R.drawable.ic_launcher, getString(R.string.app_name), getString(message), yesNoListener);
    }

    public void showQuestion(final String message, RTDialogs.RTYesNoDialogResultListener yesNoListener)
    {
        showQuestion(R.drawable.ic_launcher, getString(R.string.app_name), message, yesNoListener);
    }

    public void showQuestion(final int icon, final String title, final String message, RTDialogs.RTYesNoDialogResultListener yesNoListener)
    {
        RTDialogs.showYesNoDialog(this, icon, title, message, getString(R.string.fs_dialog_yesno_yes), getString(R.string.fs_dialog_yesno_no), yesNoListener);
    }

    public void requestUserInput(final int title, final int message, final int hint, final int defaultValue, RTDialogs.RTInputDialogResultListener resultListener)
    {
        requestUserInput(getString(title), getString(message), getString(hint), getString(defaultValue), resultListener);
    }

    public void requestUserInput(final String title, final String message, final String hint, final String defaultValue, RTDialogs.RTInputDialogResultListener resultListener)
    {
        RTDialogs.buildInputDialog(this)
                .icon(R.drawable.ic_launcher)
                .title(title)
                .message(message)
                .hint(hint)
                .value(defaultValue)
                .build(resultListener);
    }

    public void quit(Class quitToActivity, Intent extras)
    {
        Intent quitIntent = new Intent(this, quitToActivity);

        if (extras != null)
        {
            quitIntent.putExtras(extras);
            quitIntent.setAction(extras.getAction());
        }

        finish();
        startActivity(quitIntent);
    }

    public void setFrontMode(boolean front)
    {
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(!front);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (!onActionBarItemSelected(item))
        {
            switch (item.getItemId())
            {
                case android.R.id.home:
                    finish();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        } else
        {
            return true;
        }
    }

    public boolean onCreateOptionsMenu(final Menu menu)
    {
        final int menuResource = getActionBarItemsMenuResource();

        if (menuResource > 0)
        {
            getSupportMenuInflater().inflate(menuResource, menu);
            onActionBarItemsReloaded(menu);
            return true;
        } else
        {
            return super.onCreateOptionsMenu(menu);
        }
    }

    protected abstract int getActionBarItemsMenuResource();

    protected abstract void onActionBarItemsReloaded(final Menu menu);

    protected abstract boolean onActionBarItemSelected(final MenuItem item);

    class PrivateBroadcastsReceiver extends BroadcastReceiver
    {

        public void onReceive(final Context context, final Intent intent)
        {
            onPrivateBroadcastReceived(intent);
        }
    }
}
