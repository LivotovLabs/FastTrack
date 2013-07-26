package eu.livotov.labs.android.fasttrack.async;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: alex askerov
 * Date: 7/17/13
 * Time: 8:14 PM
 */
public class TaskList
{
    private List<UIAsyncTask> taskList;

    public TaskList()
    {
        taskList = new LinkedList<UIAsyncTask>();
    }

    public void add(UIAsyncTask task)
    {
        taskList.add(task);
    }

    public void remove(UIAsyncTask task)
    {
        taskList.remove(task);
    }

    public void cancelAll()
    {
        for (UIAsyncTask asyncTask : taskList)
        {
            asyncTask.cancel(false);
        }
        taskList.clear();
    }
}
