package self.ebolo.progressmanager.appcentral.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import android.widget.FrameLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.rey.material.widget.Button;
import io.codetail.animation.SupportAnimator;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
import it.gmariotti.cardslib.library.view.CardViewNative;
import self.ebolo.progressmanager.appcentral.R;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;
import self.ebolo.progressmanager.appcentral.utils.DeviceScreenInfo;

import java.util.ArrayList;


public class NewProjectActivity extends AppCompatActivity {
    final private static AccelerateDecelerateInterpolator ACCELERATE_DECELERATE = new AccelerateDecelerateInterpolator();
    private DeviceScreenInfo ScreenInfo;
    private FrameLayout screenCover;
    private ObjectAnimator screenAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subject);

        ScreenInfo = new DeviceScreenInfo(this);
        final MaterialEditText subjectNameInput;
        final MaterialEditText subjectDescInput;
        final Button buttonDone;
        final Button buttonCancel;

        if (Build.VERSION.SDK_INT < 21) {
            oldSupportInit();
            subjectNameInput = (MaterialEditText) findViewById(R.id.subject_name_input_old);
            subjectDescInput = (MaterialEditText) findViewById(R.id.subject_desc_input_old);
            buttonDone = (Button) findViewById(R.id.button_done_old);
            buttonCancel = (Button) findViewById(R.id.button_cancel_old);
            raise();
        } else {
            subjectNameInput = (MaterialEditText) findViewById(R.id.subject_name_input);
            subjectDescInput = (MaterialEditText) findViewById(R.id.subject_desc_input);
            buttonDone = (Button) findViewById(R.id.button_done);
            buttonCancel = (Button) findViewById(R.id.button_cancel);
        }

        //Edit Text Decoration
        editTextSetUp(subjectNameInput, Color.WHITE, "Title");
        editTextSetUp(subjectDescInput, Color.WHITE, "Description");

        buttonDone.setEnabled(false);
        //final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

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
                    ProjectItem returnSubject = new ProjectItem();
                    returnSubject.setSubjectName(subjectNameInput.getText().toString());
                    if (!isEmpty(subjectDescInput))
                        returnSubject.setSubjectDesc(subjectDescInput.getText().toString());
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("subject", returnSubject);
                    setResult(RESULT_OK, returnIntent);
                    //inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    if (Build.VERSION.SDK_INT < 21) {
                        down();
                        screenAnim.addListener(new SimpleListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        });
                        screenAnim.setInterpolator(ACCELERATE_DECELERATE);
                        screenAnim.setDuration(300).start();
                    } else
                        finishAfterTransition();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                onBackPressed();
            }
        });
        //inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void raise() {
        screenAnim = ObjectAnimator.ofInt(screenCover, "bottom", (int) ScreenInfo.HeightPx, 0);
        screenAnim.addListener(new SimpleListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                screenCover.setVisibility(View.INVISIBLE);
            }
        });
        screenAnim.setInterpolator(ACCELERATE_DECELERATE);
        screenAnim.setDuration(300).start();
    }

    private void down() {
        screenCover.setVisibility(View.VISIBLE);
        screenAnim = ObjectAnimator.ofInt(screenCover, "bottom", 0, (int) ScreenInfo.HeightPx);
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

    private boolean isEmpty(MaterialEditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT < 21) {
            down();
            screenAnim.addListener(new SimpleListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    finish();
                    overridePendingTransition(0, 0);
                }
            });
            screenAnim.setInterpolator(ACCELERATE_DECELERATE);
            screenAnim.setDuration(300).start();
        } else
            finishAfterTransition();
    }


    private void oldSupportInit() {
        findViewById(R.id.new_subject_title_holder_old).setVisibility(View.VISIBLE);
        findViewById(R.id.new_subject_title_holder).setVisibility(View.INVISIBLE);
        findViewById(R.id.new_subject_text_edits_old).setVisibility(View.VISIBLE);
        setTheme(R.style.Theme_NewSubject_Old);
        screenCover = (FrameLayout) findViewById(R.id.screen_cover);
        screenCover.setVisibility(View.VISIBLE);
        ((Toolbar) findViewById(R.id.new_project_toolbar_old)).setContentInsetsAbsolute(0, 0);
    }

    private void editTextSetUp(MaterialEditText editText, int color, String floatingText) {
        editText.setBaseColor(color);
        editText.setPrimaryColor(color);
        editText.setTextColor(color);
        editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        editText.setFloatingLabelText(floatingText);
        editText.setSingleLineEllipsis(true);
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
