package self.ebolo.progressmanager.appcentral.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YOLO on 8/18/2015.
 */
public class DatabaseManagement {
    private List<SubjectItem> subjectList;

    public DatabaseManagement() {
        subjectList = new ArrayList<>();
    }

    public List<SubjectItem> getSubjectList() {
        return subjectList;
    }
}
