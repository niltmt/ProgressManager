package self.ebolo.progressmanager.appcentral.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.konifar.fab_transformation.FabTransformation;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.SnackBar;

import java.util.ArrayList;

import io.paperdb.Paper;
import self.ebolo.progressmanager.appcentral.R;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;
import self.ebolo.progressmanager.appcentral.fragments.ProjectCentralFragment;
import self.ebolo.progressmanager.appcentral.utils.DatabaseManagement;

public class MainActivity extends AppCompatActivity {
    private static FloatingActionButton mFAB;
    private Toolbar mAB;
    private DatabaseManagement mdatabase;
    private SnackBar mSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(getApplicationContext());

        mAB = (Toolbar) findViewById(R.id.app_ab);
        setSupportActionBar(mAB);
        mFAB = (FloatingActionButton) findViewById(R.id.app_fab);
        mSnackBar = (SnackBar) findViewById(R.id.app_snackbar);
        mdatabase = new DatabaseManagement();

        if (Paper.exist("projects")) {
            mdatabase.subjectList = Paper.get("projects");
        }

        if (findViewById(R.id.fragments_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            ProjectCentralFragment projectCentralFragment
                = ProjectCentralFragment.newInstance(mdatabase.subjectList);
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragments_container, projectCentralFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public Toolbar getAB() {
        return mAB;
    }

    public ArrayList<ProjectItem> getDatabase() {
        return mdatabase.subjectList;
    }

    public FloatingActionButton getFAB() {
        return mFAB;
    }

    public SnackBar getSnackBar() {
        return mSnackBar;
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.project_new_card).getVisibility() == View.VISIBLE) {
            FABListener dummy = new FABListener(this);
            dummy.hideProjectNewCard();
        } else {
            super.onBackPressed();
        }
    }

    public static class FABListener implements View.OnClickListener {
        private final String[] COLOR_CODE = {"#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5"
            , "#2196F3", "#03A9F4", "#00BCD4", "#009688", "#4CAF50", "#8BC34A", "#FF9800", "#795548"
            , "#607D8B"};
        private CardView projectNewView;
        private MaterialEditText projectTitleInput;
        private MaterialEditText projectDescInput;
        private View overlay;
        private MainActivity mActivity;
        private ProjectCentralFragment mFragment;

        public FABListener(AppCompatActivity mainActivity, Fragment fragment) {
            mActivity = (MainActivity) mainActivity;
            mFragment = (ProjectCentralFragment) fragment;
            projectNewView = (CardView) mActivity.findViewById(R.id.project_new_card);
            projectTitleInput = (MaterialEditText) mActivity.findViewById(R.id.project_name_input);
            projectDescInput = (MaterialEditText) mActivity.findViewById(R.id.project_desc_input);
            overlay = mActivity.findViewById(R.id.overlay);
            overlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideViewFromFAB(projectNewView);
                }
            });

            editTextSetUp(projectTitleInput
                , Color.parseColor("#795548")
                , "Title");
            editTextSetUp(projectDescInput
                , Color.parseColor("#795548")
                , "Description");

            final Button doneButton = (Button) mainActivity.findViewById(R.id.button_done);
            doneButton.setEnabled(false);
            final Button cancelButton = (Button) mainActivity.findViewById(R.id.button_cancel);

            projectTitleInput.addTextChangedListener(new TextWatcher() {
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
                    if (isEmpty(projectTitleInput))
                        doneButton.setEnabled(false);
                    else
                        doneButton.setEnabled(true);
                }
            });

            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (doneButton.isEnabled()) {
                        int colorID = (int) (Math.random() * COLOR_CODE.length);
                        ProjectItem mProject = new ProjectItem();
                        mProject.setSubjectName(projectTitleInput.getText().toString());
                        if (!isEmpty(projectDescInput))
                            mProject.setSubjectDesc(projectDescInput.getText().toString());
                        mProject.setColor(COLOR_CODE[colorID]);
                        mFragment.addNewProject(mProject);
                        projectTitleInput.setText("");
                        projectDescInput.setText("");
                        projectTitleInput.clearFocus();
                        projectDescInput.clearFocus();

                        //Force hide the keyboard
                        View view = mActivity.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm
                                = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        hideProjectNewCard();
                    }
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideProjectNewCard();
                }
            });

        }

        public FABListener(AppCompatActivity mainActivity) {
            mActivity = (MainActivity) mainActivity;
            projectNewView = (CardView) mActivity.findViewById(R.id.project_new_card);
            overlay = mActivity.findViewById(R.id.overlay);
        }

        private void editTextSetUp(MaterialEditText editText, int color, String floatingText) {
            editText.setBaseColor(color);
            editText.setPrimaryColor(color);
            editText.setTextColor(color);
            editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
            editText.setFloatingLabelText(floatingText);
            editText.setSingleLineEllipsis(true);
        }

        @Override
        public void onClick(View v) {
            FabTransformation.with(mFAB).setOverlay(overlay).transformTo(projectNewView);
        }

        private void hideViewFromFAB(View view) {
            FabTransformation.with(mFAB).setOverlay(overlay).duration(200).transformFrom(view);
        }

        public void hideProjectNewCard() {
            hideViewFromFAB(projectNewView);
        }

        private boolean isEmpty(MaterialEditText etText) {
            return etText.getText().toString().trim().length() == 0;
        }
    }
}
