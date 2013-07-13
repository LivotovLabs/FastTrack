package eu.livotov.labs.android.fasttrack.screen;

import android.os.Bundle;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import eu.livotov.labs.android.fasttrack.App;
import eu.livotov.labs.android.fasttrack.R;
import eu.livotov.labs.android.fasttrack.base.BaseActivity;

public class MainActivity extends BaseActivity
{

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFrontMode(true);
    }

    protected int getActionBarActionModeMenuResource()
    {
        return 0;
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
