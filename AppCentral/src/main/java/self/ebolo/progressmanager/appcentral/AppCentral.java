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
import com.software.shell.fab.ActionButton;
import self.ebolo.progressmanager.appcentral.data.SubjectItem;
import self.ebolo.progressmanager.appcentral.data.SubjectRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AppCentral extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ActionButton appFAB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_central_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.app_central_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SubjectRecyclerViewAdapter(datacCreate());
        mRecyclerView.setAdapter(mAdapter);

        appFAB = (ActionButton) findViewById(R.id.app_fab);
        appFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //appFAB.hide();
                Intent newSubject = new Intent(getApplicationContext(), NewSubject.class);
                startActivity(newSubject);
            }
        });
    }

    private List<SubjectItem> datacCreate() {
        List<SubjectItem> testItems = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            SubjectItem object = new SubjectItem();
            object.setSubjectName("Project " + i);
            object.setCompletePerc(i + 50);
            testItems.add(object);
        }
        return testItems;
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
