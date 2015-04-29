package de.xikolo.model;

import android.content.Context;

import com.path.android.jobqueue.JobManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.xikolo.data.database.DatabaseHelper;
import de.xikolo.data.database.ItemDataAccess;
import de.xikolo.data.database.VideoDataAccess;
import de.xikolo.data.entities.Course;
import de.xikolo.data.entities.Item;
import de.xikolo.data.entities.Module;
import de.xikolo.model.jobs.RetrieveItemDetailJob;
import de.xikolo.model.jobs.RetrieveItemsJob;
import de.xikolo.model.jobs.UpdateProgressionJob;

public class ItemModel extends BaseModel {

    public static final String TAG = ItemModel.class.getSimpleName();

    private ItemDataAccess itemDataAccess;
    private VideoDataAccess videoDataAccess;

    public ItemModel(Context context, JobManager jobManager, DatabaseHelper databaseHelper) {
        super(context, jobManager);

        this.itemDataAccess = new ItemDataAccess(databaseHelper);
        this.videoDataAccess = new VideoDataAccess(databaseHelper);
    }

    public void getItems(Result<List<Item>> result, Course course, Module module) {
        result.setResultFilter(result.new ResultFilter() {
            @Override
            public List<Item> onFilter(List<Item> result, Result.DataSource dataSource) {
                sortItems(result);
                return result;
            }
        });

        mJobManager.addJobInBackground(new RetrieveItemsJob(result, course, module, itemDataAccess));
    }

    public void getItemDetail(Result<Item> result, Course course, Module module, Item item, String itemType) {
        mJobManager.addJobInBackground(new RetrieveItemDetailJob(result, course, module, item, itemType, videoDataAccess));
    }

    public void updateProgression(Result<Void> result, Module module, Item item) {
        mJobManager.addJobInBackground(new UpdateProgressionJob(result, module, item, itemDataAccess));
    }

    public static void sortItems(List<Item> items) {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item lhs, Item rhs) {
                return lhs.position - rhs.position;
            }
        });
    }

}
