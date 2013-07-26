package eu.livotov.labs.android.fasttrack.base;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import eu.livotov.labs.android.fasttrack.R;

/**
 * Author: alex askerov
 * Date: 7/17/13
 * Time: 11:50 PM
 */
public abstract class AbstractListFragment extends BaseFragment
{

    private AbsListView list;
    private TextView txtNoItems;

    protected abstract String getNoItemsMessage();

    public abstract void setAdapter(ListAdapter adapter);

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(getLayoutResource(), container, false);
        list = (AbsListView) rootView.findViewById(getListId());
        txtNoItems = (TextView) rootView.findViewById(getNoItemsTextId());
        txtNoItems.setText(getNoItemsMessage());
        return rootView;
    }

    protected int getListId()
    {
        return R.id.list;
    }

    protected int getLayoutResource()
    {
        return R.layout.fragment_list;
    }

    protected int getNoItemsTextId()
    {
        return R.id.list_no_items;
    }

    protected void initAdapterObserver(final ListAdapter adapter)
    {
        adapter.registerDataSetObserver(new DataSetObserver()
        {
            public void onChanged()
            {
                checkListForEmpty();
            }
        });
    }

    public void checkListForEmpty()
    {
        if (list.getAdapter() == null || list.getAdapter().isEmpty())
        {
            list.setVisibility(View.GONE);
            setNoItemsTextVisibility(true);
        } else
        {
            list.setVisibility(View.VISIBLE);
            setNoItemsTextVisibility(false);
        }
    }

    public void setNoItemsTextVisibility(boolean visibility)
    {
        txtNoItems.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener)
    {
        list.setOnItemClickListener(onItemClickListener);
    }

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener)
    {
        list.setOnScrollListener(onScrollListener);
    }

    public AbsListView getList()
    {
        return list;
    }
}
