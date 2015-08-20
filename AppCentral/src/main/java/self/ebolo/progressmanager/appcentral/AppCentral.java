/*Main screen*/

package self.ebolo.progressmanager.appcentral;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.rey.material.widget.FloatingActionButton;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.animation.arcanimator.ArcAnimator;
import io.codetail.animation.arcanimator.Side;
import self.ebolo.progressmanager.appcentral.data.DatabaseManagement;
import self.ebolo.progressmanager.appcentral.data.DeviceScreenInfo;
import self.ebolo.progressmanager.appcentral.data.SubjectItem;
import self.ebolo.progressmanager.appcentral.data.SubjectRecyclerViewAdapter;

public class AppCentral extends AppCompatActivity {
    final private static AccelerateInterpolator ACCELERATE = new AccelerateInterpolator();
    final private static DecelerateInterpolator DECELERATE = new DecelerateInterpolator();
    AppCompatActivity thisAct;
    private RecyclerView mRecyclerView;
    private SubjectRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton appFAB;
    private DatabaseManagement databaseManagement;
    private FrameLayout newSubjectScreen;
    private FrameLayout dummy;
    private float startBlueX;
    private float startBlueY;
    private int endBlueX;
    private int endBlueY;
    private int ANIMDUR = 400;
    private DeviceScreenInfo ScreenInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_central_layout);
        ScreenInfo = new DeviceScreenInfo(this);

        databaseManagement = new DatabaseManagement();
        newSubjectScreen = (FrameLayout) findViewById(R.id.new_subject_frame_layout);
        thisAct = this;
        dummy = (FrameLayout) findViewById(R.id.app_central_dummy);

        Toolbar appCentralToolbar = (Toolbar) findViewById(R.id.app_central_toolbar);
        setSupportActionBar(appCentralToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.app_central_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SubjectRecyclerViewAdapter(databaseManagement.getSubjectList(), this);
        mRecyclerView.setAdapter(mAdapter);

        appFAB = (FloatingActionButton) findViewById(R.id.app_fab);
        appFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBlueX = ViewHelper.getX(appFAB) + appFAB.getWidth() / 2;
                startBlueY = ViewHelper.getY(appFAB) + appFAB.getHeight() / 2;

                endBlueX = (int) (ScreenInfo.WidthPx / 2f);
                endBlueY = (int) (ScreenInfo.HeightPx * 0.7f);

                ArcAnimator arcAnimator = ArcAnimator.createArcAnimator(appFAB, endBlueX,
                        endBlueY, 30, Side.LEFT)
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

        //Testing area - Must delete after done
        testOnly();
    }

    private void testOnly() {
        SubjectItem testSubj = new SubjectItem();
        testSubj.setSubjectName("Testing");
        testSubj.setCompletePerc(25);
        databaseManagement.getSubjectList().add(testSubj);
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
                Intent newSubject = new Intent(getApplicationContext(), NewSubject.class);
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

    void disappearBluePair() {
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
                startBlueY, 30, Side.LEFT)
                .setDuration(ANIMDUR);
        arcAnimator.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        disappearBluePair();

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                SubjectItem returnSubject = (SubjectItem) data.getSerializableExtra("subject");
                databaseManagement.getSubjectList().add(returnSubject);
                mAdapter.notifyDataSetChanged();
            }
        }
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
