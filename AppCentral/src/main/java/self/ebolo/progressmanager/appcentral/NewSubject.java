package self.ebolo.progressmanager.appcentral;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.rey.material.widget.Button;
import io.codetail.animation.SupportAnimator;
import self.ebolo.progressmanager.appcentral.data.DeviceScreenInfo;
import self.ebolo.progressmanager.appcentral.data.SubjectItem;


public class NewSubject extends AppCompatActivity {
    final private static AccelerateDecelerateInterpolator ACCELERATE_DECELERATE = new AccelerateDecelerateInterpolator();
    private DeviceScreenInfo ScreenInfo;
    private FrameLayout screenCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_subject_layout);
        ScreenInfo = new DeviceScreenInfo(this);
        if (Build.VERSION.SDK_INT < 21) {
            RelativeLayout dummy = (RelativeLayout) findViewById(R.id.new_subject_dummy);
            dummy.setVisibility(View.VISIBLE);
            setTheme(R.style.Theme_NewSubject_Old);
            screenCover = (FrameLayout) findViewById(R.id.screen_cover);
            screenCover.setVisibility(View.VISIBLE);
            raise();
        }

        final EditText subjectNameInput = (EditText) findViewById(R.id.subject_name_input);
        final EditText subjectDescInput = (EditText) findViewById(R.id.subject_desc_input);
        final Button buttonDone = (Button) findViewById(R.id.button_done);
        buttonDone.setEnabled(false);

        Toolbar newProjectToolbar = (Toolbar) findViewById(R.id.new_project_toolbar);
        setSupportActionBar(newProjectToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subjectNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                setButtonDonestate();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonDonestate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                setButtonDonestate();
            }

            private void setButtonDonestate() {
                if (isEmpty(subjectNameInput))
                    buttonDone.setEnabled(false);
                else
                    buttonDone.setEnabled(true);
            }
        });
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonDone.isEnabled()) {
                    SubjectItem returnSubject = new SubjectItem();
                    returnSubject.setSubjectName(subjectNameInput.getText().toString());
                    if (!isEmpty(subjectDescInput))
                        returnSubject.setSubjectDesc(subjectDescInput.getText().toString());
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("subject", returnSubject);
                    setResult(RESULT_OK, returnIntent);
                    if (Build.VERSION.SDK_INT < 21)
                        down();
                    finish();
                    if (Build.VERSION.SDK_INT < 21)
                        overridePendingTransition(0, 0);
                }
            }
        });
    }

    private void raise() {
        ObjectAnimator screenAnim = ObjectAnimator.ofInt(screenCover, "bottom", (int) ScreenInfo.HeightPx, 0);
        screenAnim.addListener(new SimpleListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                screenCover.setVisibility(View.INVISIBLE);
            }
        });
        screenAnim.setInterpolator(ACCELERATE_DECELERATE);
        screenAnim.start();
    }

    private void down() {
        screenCover.setVisibility(View.VISIBLE);
        ObjectAnimator screenAnim = ObjectAnimator.ofInt(screenCover, "bottom", 0, (int) ScreenInfo.HeightPx);
        screenAnim.addListener(new SimpleListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //do nothing
            }
        });
        screenAnim.setInterpolator(ACCELERATE_DECELERATE);
        screenAnim.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_subject, menu);
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

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT < 21)
            down();
        super.onBackPressed();
        if (Build.VERSION.SDK_INT < 21)
            overridePendingTransition(0, 0);
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
