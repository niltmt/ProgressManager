package self.ebolo.progressmanager.appcentral.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubjectItem implements Serializable {
    private String subjectName;
    private int startDate;
    private int dueDate;
    private String subjectDesc;
    private List<ActivityItem> subjectActList;
    private int completePerc;

    public SubjectItem() {
        subjectName = "";
        subjectActList = new ArrayList<>();
        completePerc = 0;
    }

    public void addAct(String input) {
        ActivityItem activityItem = new ActivityItem(input);
        subjectActList.add(activityItem);
    }

    public int getActListCount() {
        return subjectActList.size();
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String input) {
        subjectName = input;
    }

    public String getSubjectDesc() {
        return subjectDesc;
    }

    public void setSubjectDesc(String input) {
        subjectDesc = input;
    }

    public int getCompletePerc() {
        return completePerc;
    }

    public void setCompletePerc(int input) {
        completePerc = input;
    }
}
