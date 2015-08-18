package self.ebolo.progressmanager.appcentral.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.gc.materialdesign.views.ButtonFlat;
import self.ebolo.progressmanager.appcentral.R;

import java.util.List;

public class SubjectRecyclerViewAdapter extends RecyclerView.Adapter<SubjectRecyclerViewAdapter.SubjectViewHolder> {
    private List<SubjectItem> subjectItemList;

    public SubjectRecyclerViewAdapter(List<SubjectItem> data) {
        subjectItemList = data;
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder subjectViewHolder, int i) {
        SubjectItem thisSubjectItem = subjectItemList.get(i);
        subjectViewHolder.subjectName.setText(thisSubjectItem.getSubjectName() + " (" +
                thisSubjectItem.getCompletePerc() + "%)");
        subjectViewHolder.subjectPerc.setProgress(thisSubjectItem.getCompletePerc());
    }

    @Override
    public int getItemCount() {
        return subjectItemList.size();
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);
        return new SubjectViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder {
        public TextView subjectName;
        public ProgressBar subjectPerc;
        public ButtonFlat startDate;
        public ButtonFlat dueDate;

        public SubjectViewHolder(View v) {
            super(v);
            subjectName = (TextView) v.findViewById(R.id.subject_name);
            startDate = (ButtonFlat) v.findViewById(R.id.start_date);
            dueDate = (ButtonFlat) v.findViewById(R.id.due_date);
            subjectPerc = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }
}
