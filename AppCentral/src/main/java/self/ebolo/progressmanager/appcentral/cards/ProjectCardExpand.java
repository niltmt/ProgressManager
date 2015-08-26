package self.ebolo.progressmanager.appcentral.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.CardExpand;
import self.ebolo.progressmanager.appcentral.R;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;

/**
 * Created by YOLO on 8/26/2015.
 */
public class ProjectCardExpand extends CardExpand {
    private final ProjectItem projectItem;
    private TextView projectCardInfo;

    public ProjectCardExpand(Context context, ProjectItem data) {
        super(context, R.layout.project_card_expand);
        projectItem = data;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (view != null) {
            projectCardInfo = (TextView) view.findViewById(R.id.project_card_info);
            if (projectCardInfo != null) {
                projectCardInfo.setText(projectItem.getSubjectName());
            }
        }
    }
}
