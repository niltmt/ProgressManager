package self.ebolo.progressmanager.appcentral;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.rey.material.widget.Button;
import self.ebolo.progressmanager.appcentral.data.SubjectItem;


public class NewSubject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_subject_layout);
        if (Build.VERSION.SDK_INT > 20)
            lowerABElevation();

        final EditText subjectNameInput = (EditText) findViewById(R.id.subject_name_input);
        final EditText subjectDescInput = (EditText) findViewById(R.id.subject_desc_input);
        final Button buttonDone = (Button) findViewById(R.id.button_done);
        buttonDone.setEnabled(false);

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
                    finish();
                }
            }
        });
    }

    @TargetApi(21)
    private void lowerABElevation() {
        this.getSupportActionBar().setElevation(0);
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
}
