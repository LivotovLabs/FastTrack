package eu.livotov.labs.android.fasttrack.base;

import android.app.ProgressDialog;
import android.content.*;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.text.TextUtils;
import android.view.*;
import eu.livotov.labs.android.fasttrack.App;
import eu.livotov.labs.android.fasttrack.R;
import eu.livotov.labs.android.fasttrack.async.TaskList;
import eu.livotov.labs.android.fasttrack.async.UIAsyncTask;
import eu.livotov.labs.android.robotools.ui.RTDialogs;

public abstract class BaseActivity extends ActionBarActivity implements ActionMode.Callback
{

    private BroadcastReceiver privateBroadcastsReceiver = new PrivateBroadcastsReceiver();
    private ProgressDialog progressDialog;
    private boolean useActionBarProgress;
    private ActionMode actionModeHandler;
    private TaskList uiTaskList;
    protected DrawerLayout menuDrawer;
    protected ActionBarDrawerToggle menuDrawerToggle;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_PROGRESS);

        uiTaskList = new TaskList();

        ActionBar bar = getSupportActionBar();
        bar.show();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        if (getActivityLayoutResource() != 0)
        {
            setContentView(getActivityLayoutResource());
        }
    }

    protected void onDestroy()
    {
        disablePrivateBroadcastsReceiver();
        uiTaskList.cancelAll();
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

    protected void onStart()
    {
        super.onStart();
    }

    protected void onStop()
    {
        super.onStop();
    }

    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        if (menuDrawerToggle != null)
        {
            menuDrawerToggle.syncState();
        }
    }

    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (menuDrawerToggle != null)
        {
            menuDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    public void setContentView(final int layoutResID)
    {
        super.setContentView(layoutResID);
        initContentRelatedStuff();
    }

    public void setContentView(final View view)
    {
        super.setContentView(view);
        initContentRelatedStuff();
    }

    public void setContentView(final View view, final ViewGroup.LayoutParams params)
    {
        super.setContentView(view, params);
        initContentRelatedStuff();
    }

    public void addUiTask(UIAsyncTask uiAsyncTask)
    {
        uiTaskList.add(uiAsyncTask);
    }

    public void removeUiTask(UIAsyncTask uiAsyncTask)
    {
        uiTaskList.remove(uiAsyncTask);
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

    public void setUseActionBarProgress(final boolean useActionBarProgress)
    {
        this.useActionBarProgress = useActionBarProgress;
    }

    public synchronized void showProgressDialog(boolean cancelable)
    {
        showProgressDialog(0, cancelable, true, null);
    }

    public synchronized void showProgressDialog(final int messageRes, final boolean cancelable, final boolean indeterminate, final DialogInterface.OnCancelListener cancelListener)
    {
        showProgressDialog(messageRes > 0 ? getString(messageRes) : getString(R.string.fs_dialog_progress_pleasewait), cancelable, indeterminate, cancelListener);
    }

    public synchronized void showProgressDialog(final String message, final boolean cancelable, boolean indeterminate, final DialogInterface.OnCancelListener cancelListener)
    {
        if (useActionBarProgress)
        {
            setSupportProgressBarIndeterminate(indeterminate);
            setSupportProgressBarVisibility(true);

            if (!indeterminate)
            {
                setSupportProgress(0);
            } else
            {
                setSupportProgressBarIndeterminateVisibility(true);
            }
        } else
        {
            if (progressDialog == null)
            {
                progressDialog = ProgressDialog.show(BaseActivity.this, getString(R.string.app_name), message, true, cancelable, new DialogInterface.OnCancelListener()
                {
                    public void onCancel(final DialogInterface dialogInterface)
                    {
                        progressDialog = null;
                        if (cancelListener != null)
                        {
                            cancelListener.onCancel(dialogInterface);
                        }
                    }
                });

                progressDialog.setIndeterminate(indeterminate);

                if (!indeterminate)
                {
                    progressDialog.setProgress(0);
                }
            } else
            {
                if (!TextUtils.isEmpty(message))
                {
                    progressDialog.setMessage(message);
                }
            }
        }
    }

    public synchronized void updateProgress(final String message)
    {
        if (progressDialog != null)
        {
            progressDialog.setMessage(message);
        }
    }

    public synchronized void updateProgress(final int percent)
    {
        if (progressDialog != null && !progressDialog.isIndeterminate())
        {
            progressDialog.setProgress(percent);
        }

        setSupportProgress(percent);
    }

    public synchronized void updateProgress(final String message, final int percent)
    {
        updateProgress(message);
        updateProgress(percent);
    }

    public synchronized void hideProgressDialog()
    {
        try
        {
            if (progressDialog != null)
            {
                progressDialog.hide();
                progressDialog.dismiss();
            }
        } catch (Throwable ignored)
        {
        } finally
        {
            progressDialog = null;
        }

        try
        {
            setSupportProgressBarVisibility(false);
            setSupportProgressBarIndeterminateVisibility(false);
        } catch (Throwable ignored)
        {
        }
    }

    public void showError(Throwable error)
    {
        showError(error, null);
    }

    public void showError(Throwable error, RTDialogs.RTModalDialogResultListener listener)
    {
        RTDialogs.showMessageBox(this, R.drawable.ic_launcher, getString(R.string.fs_dialog_error_title), error.getMessage(), listener);
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
        if (menuDrawerToggle == null)
        {
            ActionBar bar = getSupportActionBar();
            bar.setDisplayHomeAsUpEnabled(!front);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (menuDrawerToggle != null && menuDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        if (!onActionBarItemSelected(item))
        {
            switch (item.getItemId())
            {
                case android.R.id.home:
                    if (actionModeHandler != null)
                    {
                        finishActionMode();
                    } else
                    {
                        finish();
                    }
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
            getMenuInflater().inflate(menuResource, menu);
            onActionBarItemsStateUpdate(menu);
            return true;
        } else
        {
            return super.onCreateOptionsMenu(menu);
        }
    }

    public void startActionMode()
    {
        if (getActionBarActionModeMenuResource() != 0)
        {
            finishActionMode();
            startSupportActionMode(this);
        } else
        {
            throw new RuntimeException(String.format("Activity %s does not support action mode. Please return valid action mode menu resource ID from getActionBarActionModeMenuResource() method.", this.getClass().getCanonicalName()));
        }
    }

    public void finishActionMode()
    {
        if (actionModeHandler != null)
        {
            actionModeHandler.finish();
        }
    }

    public void toggleActionMode()
    {
        if (actionModeHandler != null)
        {
            finishActionMode();
        } else
        {
            startActionMode();
        }
    }

    public boolean onCreateActionMode(final ActionMode mode, final Menu menu)
    {
        mode.getMenuInflater().inflate(getActionBarActionModeMenuResource(), menu);
        return true;
    }

    public boolean onPrepareActionMode(final ActionMode mode, final Menu menu)
    {
        onActionBarActionModeStarted(mode, menu);
        return true;
    }

    public boolean onActionItemClicked(final ActionMode mode, final MenuItem item)
    {
        return onActionBarActionModeItemSelected(mode, item);
    }

    public void onDestroyActionMode(final ActionMode mode)
    {
        onActionBarActionModeStopped(mode);
        actionModeHandler = null;
    }

    private void initContentRelatedStuff()
    {
        if (getMenuDrawerLayoutId() > 0)
        {
            initMenuDrawer();
        }
    }

    private void initMenuDrawer()
    {
        menuDrawer = (DrawerLayout) findViewById(getMenuDrawerLayoutId());

        if (getMenuDrawerToggleIconResource() > 0)
        {
            menuDrawerToggle = new ActionBarDrawerToggle(this, menuDrawer, getMenuDrawerToggleIconResource(), R.string.fs_menudrawer_toggle_desc_open, R.string.fs_menudrawer_toggle_desc_close)
            {

                public void onDrawerSlide(final View view, final float v)
                {
                    super.onDrawerSlide(view, v);
                    onMenuDrawerSlide(view, v);
                }

                public void onDrawerOpened(final View view)
                {
                    super.onDrawerOpened(view);
                    onMenuDrawerOpened();
                }

                public void onDrawerClosed(final View view)
                {
                    super.onDrawerClosed(view);
                    onMenuDrawerClosed();
                }

                public void onDrawerStateChanged(final int i)
                {
                    super.onDrawerStateChanged(i);
                    onMenuDrawerStateChanged(i);
                }
            };

            menuDrawer.setDrawerListener(menuDrawerToggle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else
        {
            menuDrawer.setDrawerListener(new DrawerLayout.DrawerListener()
            {
                public void onDrawerSlide(final View view, final float v)
                {
                    onMenuDrawerSlide(view, v);
                }

                public void onDrawerOpened(final View view)
                {
                    onMenuDrawerOpened();
                }

                public void onDrawerClosed(final View view)
                {
                    onMenuDrawerClosed();
                }

                public void onDrawerStateChanged(final int i)
                {
                    onMenuDrawerStateChanged(i);
                }
            });
        }
    }

    private void showDialog(DialogFragment dialog)
    {
        dismissDialog();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        dialog.show(ft, "fstdialog");
    }

    private void dismissDialog()
    {
        Fragment prev = getSupportFragmentManager().findFragmentByTag("fstdialog");

        if (prev != null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(prev);
            ft.commit();
        }
    }

    protected abstract int getActivityLayoutResource();

    protected abstract int getMenuDrawerLayoutId();

    protected abstract int getMenuDrawerToggleIconResource();

    protected abstract int getActionBarActionModeMenuResource();

    protected abstract int getActionBarItemsMenuResource();

    protected abstract void onActionBarItemsStateUpdate(final Menu menu);

    protected abstract void onActionBarActionModeStarted(final ActionMode mode, final Menu menu);

    protected abstract void onActionBarActionModeStopped(final ActionMode mode);

    protected abstract boolean onActionBarItemSelected(final MenuItem item);

    protected abstract boolean onActionBarActionModeItemSelected(final ActionMode mode, final MenuItem item);

    protected abstract void onMenuDrawerOpened();

    protected abstract void onMenuDrawerClosed();

    protected void onMenuDrawerStateChanged(int state)
    {
    }

    protected void onMenuDrawerSlide(final View view, final float v)
    {
    }

    class PrivateBroadcastsReceiver extends BroadcastReceiver
    {

        public void onReceive(final Context context, final Intent intent)
        {
            onPrivateBroadcastReceived(intent);
        }
    }
}
