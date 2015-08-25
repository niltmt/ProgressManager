/*Main screen*/

package self.ebolo.progressmanager.appcentral.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.SnackBar;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.animation.arcanimator.ArcAnimator;
import io.codetail.animation.arcanimator.Side;
import io.paperdb.Paper;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
import self.ebolo.progressmanager.appcentral.R;
import self.ebolo.progressmanager.appcentral.adapters.ProjectCardRecyclerAdapter;
import self.ebolo.progressmanager.appcentral.cards.CardTouchHelperCallback;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;
import self.ebolo.progressmanager.appcentral.utils.DatabaseManagement;
import self.ebolo.progressmanager.appcentral.utils.DeviceScreenInfo;

public class AppCentralActivity extends AppCompatActivity {
    final private static AccelerateInterpolator ACCELERATE = new AccelerateInterpolator();
    final private static DecelerateInterpolator DECELERATE = new DecelerateInterpolator();

    private AppCompatActivity thisAct;

    private CardRecyclerView mRecyclerView;
    private ProjectCardRecyclerAdapter mAdapter;
    private DatabaseManagement databaseManagement;

    private FloatingActionButton appFAB;
    private FrameLayout newSubjectScreen;
    private FrameLayout dummy;
    private float startBlueX;
    private float startBlueY;
    private int endBlueX;
    private int endBlueY;
    private int ANIMDUR = 300;
    private DeviceScreenInfo ScreenInfo;

    private SnackBar snackBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_central);
        snackBar = (SnackBar)findViewById(R.id.app_central_snackbar);

        Paper.init(getApplicationContext());
        databaseManagement = new DatabaseManagement();

        if (!Paper.exist("projects"))
            Paper.put("projects", databaseManagement.subjectList);

        databaseManagement.subjectList = Paper.get("projects");

        ScreenInfo = new DeviceScreenInfo(this);
        newSubjectScreen = (FrameLayout) findViewById(R.id.new_subject_frame_layout);
        thisAct = this;
        dummy = (FrameLayout) findViewById(R.id.app_central_dummy);

        Toolbar appCentralToolbar = (Toolbar) findViewById(R.id.app_central_toolbar);
        setSupportActionBar(appCentralToolbar);


        appFAB = (FloatingActionButton) findViewById(R.id.app_fab);
        appFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBlueX = ViewHelper.getX(appFAB) + appFAB.getWidth() / 2;
                startBlueY = ViewHelper.getY(appFAB) + appFAB.getHeight() / 2;

                endBlueX = (int) (ScreenInfo.WidthPx / 2f);
                endBlueY = (int) (ScreenInfo.HeightPx * 0.7f);

                ArcAnimator arcAnimator = ArcAnimator.createArcAnimator(appFAB, endBlueX,
                    endBlueY, 0, Side.LEFT)
                    .setDuration(ANIMDUR);
                arcAnimator.addListener(new SimpleListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        appFAB.setVisibility(View.INVISIBLE);
                        appearNewSubjectScreen();
                    }
                });
                arcAnimator.start();

            }
        });

        mRecyclerView = (CardRecyclerView)findViewById(R.id.app_central_recyclerview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ProjectCardRecyclerAdapter(this, databaseManagement.subjectList);
        mAdapter.setCardRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new CardTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void appearNewSubjectScreen() {
        newSubjectScreen.setVisibility(View.VISIBLE);

        float finalRadius = Math.max(ScreenInfo.WidthPx, ScreenInfo.HeightPx) * 1.5f;

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(newSubjectScreen, endBlueX, endBlueY,
            appFAB.getWidth() / 2f,
            finalRadius);
        animator.setDuration(ANIMDUR);
        animator.setInterpolator(ACCELERATE);
        animator.addListener(new SimpleListener() {
            @Override
            public void onAnimationEnd() {
                dummy.setVisibility(View.VISIBLE);
                Intent newSubject = new Intent(getApplicationContext(), NewProjectActivity.class);
                if (Build.VERSION.SDK_INT > 20) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        thisAct, newSubjectScreen, "new_subject_holder"
                    );
                    ActivityCompat.startActivityForResult(thisAct, newSubject, 1, optionsCompat.toBundle());
                } else {
                    startActivityForResult(newSubject, 1);
                    overridePendingTransition(0, 0);
                }
            }
        });
        animator.start();
    }

    void disappearNewSubjectScreen() {
        float finalRadius = Math.max(ScreenInfo.WidthPx, ScreenInfo.HeightPx) * 1.5f;
        dummy.setVisibility(View.INVISIBLE);
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(newSubjectScreen, endBlueX, endBlueY,
            finalRadius, appFAB.getWidth() / 2f);
        animator.setDuration(ANIMDUR);
        animator.addListener(new SimpleListener() {
            @Override
            public void onAnimationEnd() {
                newSubjectScreen.setVisibility(View.INVISIBLE);
                returnBlue();
            }
        });
        animator.setInterpolator(DECELERATE);
        animator.start();
    }

    void returnBlue() {
        appFAB.setVisibility(View.VISIBLE);
        ArcAnimator arcAnimator = ArcAnimator.createArcAnimator(appFAB, startBlueX,
            startBlueY, 0, Side.LEFT)
            .setDuration(ANIMDUR);
        arcAnimator.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        disappearNewSubjectScreen();

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ProjectItem returnSubject = (ProjectItem) data.getSerializableExtra("subject");
                databaseManagement.subjectList.add(0, returnSubject);
                Paper.put("projects", databaseManagement.subjectList);
                mAdapter.notifyItemInserted(0);
                mRecyclerView.smoothScrollToPosition(0);
            }
        }
    }

    public SnackBar getSnackBar() {
        return snackBar;
    }

    private static class SimpleListener implements SupportAnimator.AnimatorListener, ObjectAnimator.AnimatorListener {
        @Override
        public void onAnimationStart() {
        }

        @Override
        public void onAnimationEnd() {
        }

        @Override
        public void onAnimationCancel() {
        }

        @Override
        public void onAnimationRepeat() {
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
