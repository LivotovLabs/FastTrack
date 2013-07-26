package eu.livotov.labs.android.fasttrack.base;

import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Author: alex askerov
 * Date: 7/21/13
 * Time: 8:33 PM
 */
public abstract class BaseGridFragment extends AbstractListFragment
{
    public void setAdapter(final ListAdapter adapter)
    {
        final GridView list = (GridView) getList();
        list.setAdapter(adapter);
        initAdapterObserver(adapter);
        checkListForEmpty();
    }
}
