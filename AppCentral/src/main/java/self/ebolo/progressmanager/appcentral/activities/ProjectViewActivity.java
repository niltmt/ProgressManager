package self.ebolo.progressmanager.appcentral.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.FloatingActionButton;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardViewNative;
import self.ebolo.progressmanager.appcentral.R;
import self.ebolo.progressmanager.appcentral.cards.ProjectCard;
import self.ebolo.progressmanager.appcentral.cards.ProjectCardHeader;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;


public class ProjectViewActivity extends AppCompatActivity {
    private int subjNum;
    private ProjectItem mProjectItem;
    private CardViewNative subjCardView;
    private ProjectCardHeader cardHeader;
    private FloatingActionButton projectViewFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_view);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mProjectItem = (ProjectItem) extras.get("proj");
        } else {
            mProjectItem = new ProjectItem();
        }

        projectViewFAB
            = (FloatingActionButton) findViewById(R.id.project_view_fab);
        projectViewFAB.setBackgroundColor(Color.parseColor(mProjectItem.getColor()));

        subjCardView = (CardViewNative) findViewById(R.id.project_card);
        RelativeLayout cardViewHeader = (RelativeLayout) findViewById(R.id.project_card_header);
        cardViewHeader.setBackgroundColor(Color.parseColor(mProjectItem.getColor()));
        Card subjCard = new ProjectCard(this, mProjectItem);

        cardHeader = new ProjectCardHeader(this, 16, "General Information");
        cardHeader.setButtonExpandVisible(true);

        subjCard.addCardHeader(cardHeader);

        subjCardView.setCard(subjCard);
        //Customize the ActionBar
        final ActionBar abar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.ab_subj_view, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.view_title);
        textviewTitle.setText(mProjectItem.getSubjectName());
        textviewTitle.setTextSize(22);
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeButtonEnabled(true);
        abar.setElevation(0);
        abar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(mProjectItem.getColor())));
        if (Build.VERSION.SDK_INT > 20) {
            float[] hsv = new float[3];
            int color = Color.parseColor(mProjectItem.getColor());
            Color.colorToHSV(color, hsv);
            hsv[2] = 0.2f + 0.5f * hsv[2];
            color = Color.HSVToColor(hsv);
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
        //End of ActionBar customizing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subject_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        projectViewFAB.setVisibility(View.GONE);
        cardHeader.getTitleView().setVisibility(View.GONE);
        ActivityCompat.finishAfterTransition(this);
    }
}
