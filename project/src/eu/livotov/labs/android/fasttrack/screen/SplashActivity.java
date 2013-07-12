package eu.livotov.labs.android.fasttrack.screen;

import android.os.Bundle;
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
        setContentView(R.layout.activity_splash);
    }
}
