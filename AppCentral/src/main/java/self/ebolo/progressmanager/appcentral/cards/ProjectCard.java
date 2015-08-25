package self.ebolo.progressmanager.appcentral.cards;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.rey.material.widget.RippleManager;
import it.gmariotti.cardslib.library.internal.Card;
import self.ebolo.progressmanager.appcentral.R;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;

/**
 * Created by YOLO on 8/25/2015.
 */
public class ProjectCard extends Card {
    protected ProgressBar testProgressBar;
    protected ProjectItem projectItem;

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

        //No Header

        //Set a OnClickListener listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "Click Listener card=", Toast.LENGTH_LONG).show();
            }
        });
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
