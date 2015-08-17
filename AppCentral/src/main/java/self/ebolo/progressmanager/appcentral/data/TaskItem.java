package self.ebolo.progressmanager.appcentral.data;

import java.io.Serializable;

/**
 * Created by YOLO on 8/17/2015.
 */
public class TaskItem implements Serializable {
    private String taskName;
    private int taskPercent;

    public TaskItem() {
        taskName = "";
        taskPercent = 0;
    }

    public TaskItem(String name, int perc) {
        taskName = name;
        taskPercent = perc;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String input) {
        taskName = input;
    }

    public int getTaskPercent() {
        return taskPercent;
    }

    public void setTaskPercent(int input) {
        taskPercent = input;
    }
}
