package self.ebolo.progressmanager.appcentral.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YOLO on 8/17/2015.
 */
public class ActivityItem implements Serializable {
    private String name;
    private List<TaskItem> activityTaskList;
    private int percent;

    public ActivityItem() {
        name = "";
        activityTaskList = new ArrayList<>();
        percent = 0;
    }

    public ActivityItem(String actName) {
        name = actName;
        activityTaskList = new ArrayList<>();
        percent = 0;
    }

    public void addTask(String taskName, int perc) {
        TaskItem taskItem = new TaskItem(taskName, perc);
        activityTaskList.add(taskItem);
    }

    public int percenCalc() {
        percent = 0;
        double generalTaskPerc = 100.0 / (double) activityTaskList.size();

        if (activityTaskList != null) {
            for (int i = 0; i < activityTaskList.size(); i++) {
                percent += (int) Math.ceil((double) (activityTaskList.get(i).getTaskPercent()) * generalTaskPerc);
            }
            if (percent > 100)
                percent = 100;
        }
        return percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String input) {
        name = input;
    }

    public int getTaskListCount() {
        return activityTaskList.size();
    }
}
