/*Main screen*/

package self.ebolo.progressmanager.appcentral;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.gc.materialdesign.views.ButtonFloat;
import self.ebolo.progressmanager.appcentral.data.DatabaseManagement;
import self.ebolo.progressmanager.appcentral.data.SubjectItem;
import self.ebolo.progressmanager.appcentral.data.SubjectRecyclerViewAdapter;

public class AppCentral extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SubjectRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ButtonFloat appFAB;
    private DatabaseManagement databaseManagement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_central_layout);
        databaseManagement = new DatabaseManagement();

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

        appFAB = (ButtonFloat) findViewById(R.id.app_fab);
        appFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //appFAB.hide();
                Intent newSubject = new Intent(getApplicationContext(), NewSubject.class);
                startActivityForResult(newSubject, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                SubjectItem returnSubject = (SubjectItem) data.getSerializableExtra("subject");
                databaseManagement.getSubjectList().add(returnSubject);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                //openSearch();
                return true;
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
