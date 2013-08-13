package eu.livotov.labs.android.fasttrack.screen;

import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import eu.livotov.labs.android.fasttrack.App;
import eu.livotov.labs.android.fasttrack.R;
import eu.livotov.labs.android.fasttrack.base.BaseActivity;

public class SplashActivity extends BaseActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        if (App.isAppReady())
        {
            quitSplash();
        }
    }

    protected void onResume()
    {
        super.onResume();
        App.waitForApplication(new App.ApplicationStartupListener()
        {
            public void onApplicationReady()
            {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        quitSplash();
                    }
                });
            }
        });
    }

    protected int getActivityLayoutResource()
    {
        return R.layout.activity_splash;
    }

    protected int getMenuDrawerLayoutId()
    {
        return 0;
    }

    protected int getMenuDrawerToggleIconResource()
    {
        return 0;
    }

    protected int getActionBarActionModeMenuResource()
    {
        return 0;
    }

    private void quitSplash()
    {
        quit(MainActivity.class, getIntent());
    }

    protected int getActionBarItemsMenuResource()
    {
        return 0;
    }

    protected void onActionBarItemsStateUpdate(final Menu menu)
    {
    }

    protected void onActionBarActionModeStarted(final ActionMode mode, final Menu menu)
    {
    }

    protected void onActionBarActionModeStopped(final ActionMode mode)
    {
    }

    protected boolean onActionBarItemSelected(final MenuItem item)
    {
        return false;
    }

    protected boolean onActionBarActionModeItemSelected(final ActionMode mode, final MenuItem item)
    {
        return false;
    }

    protected void onMenuDrawerOpened()
    {

    }

    protected void onMenuDrawerClosed()
    {

    }
}
