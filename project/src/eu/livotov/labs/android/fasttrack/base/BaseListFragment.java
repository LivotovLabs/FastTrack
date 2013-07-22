package eu.livotov.labs.android.fasttrack.base;

import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Author: alex askerov
 * Date: 7/21/13
 * Time: 8:22 PM
 */
public abstract class BaseListFragment extends AbstractListFragment
{
    public void setAdapter(final ListAdapter adapter)
    {
        final ListView list = (ListView) getList();
        list.setAdapter(adapter);
        initAdapterObserver(adapter);
        checkListForEmpty();
    }
}
