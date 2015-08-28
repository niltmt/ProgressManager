package self.ebolo.progressmanager.appcentral.cards;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import it.gmariotti.cardslib.library.internal.Card;
import self.ebolo.progressmanager.appcentral.R;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;

/**
 * Created by YOLO on 8/25/2015.
 */
public class ProjectCard extends Card {
    protected ProgressBar testProgressBar;
    protected ProjectItem projectItem;
    protected LinearLayout innerContainer;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public ProjectCard(Context context, ProjectItem proj) {
        this(context, R.layout.project_card_inner);
        projectItem = proj;
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public ProjectCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    /**
     * Init
     */
    private void init(){
        //Nothing
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        //Retrieve elements
        testProgressBar = (ProgressBar) parent.findViewById(R.id.project_progress);

        if (testProgressBar!=null) {
            LayerDrawable bgShape = (LayerDrawable)testProgressBar.getProgressDrawable();
            bgShape.setColorFilter(Color.parseColor(projectItem.getColor()), PorterDuff.Mode.MULTIPLY);
            testProgressBar.setProgress(80);
        }
    }
}
