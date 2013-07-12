package eu.livotov.labs.android.fasttrack.base;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import eu.livotov.labs.android.fasttrack.R;

public class BaseActivity extends SherlockFragmentActivity
{

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
