package eu.livotov.labs.android.fasttrack.screen;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import eu.livotov.labs.android.fasttrack.App;
import eu.livotov.labs.android.fasttrack.R;
import eu.livotov.labs.android.fasttrack.base.BaseActivity;

public class SplashActivity extends BaseActivity
{

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

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
}
