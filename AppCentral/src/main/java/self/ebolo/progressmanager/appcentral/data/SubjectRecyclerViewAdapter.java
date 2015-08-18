package self.ebolo.progressmanager.appcentral.data;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.gc.materialdesign.views.ButtonFlat;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import self.ebolo.progressmanager.appcentral.R;

import java.util.Calendar;
import java.util.List;

public class SubjectRecyclerViewAdapter extends RecyclerView.Adapter<SubjectRecyclerViewAdapter.SubjectViewHolder> {
    private List<SubjectItem> subjectItemList;
    private AppCompatActivity usingAct;
    private SubjectRecyclerViewAdapter thisAdapter;

    public SubjectRecyclerViewAdapter(List<SubjectItem> data, AppCompatActivity demo) {
        subjectItemList = data;
        usingAct = demo;
        thisAdapter = this;
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder subjectViewHolder, int i) {
        final SubjectItem thisSubjectItem = subjectItemList.get(i);
        final DateButtonsListener dateButtonsListener = new DateButtonsListener(i);
        subjectViewHolder.subjectName.setText(thisSubjectItem.getSubjectName() + " (" +
                thisSubjectItem.getCompletePerc() + "%)");
        subjectViewHolder.subjectPerc.setProgress(thisSubjectItem.getCompletePerc());
        subjectViewHolder.startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                dateButtonsListener.start = true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        dateButtonsListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                if (thisSubjectItem.getDueDate() != 0) {
                    Calendar maxDate = now;
                    maxDate.set(thisSubjectItem.getDueDate() / 10000,
                            ((thisSubjectItem.getDueDate() / 100) % 100) - 1,
                            thisSubjectItem.getDueDate() % 100);
                    dpd.setMaxDate(maxDate);
                    dpd.setMinDate(null);
                }
                dpd.show(usingAct.getFragmentManager(), "startDatePicker");
            }
        });

        subjectViewHolder.dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                dateButtonsListener.start = false;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd;
                if (thisSubjectItem.getStartDate() != 0) {
                    dpd = DatePickerDialog.newInstance(
                            dateButtonsListener,
                            thisSubjectItem.getStartDate() / 10000,
                            ((thisSubjectItem.getStartDate() / 100) % 100) - 1,
                            thisSubjectItem.getStartDate() % 100
                    );
                    Calendar minDate = now;
                    minDate.set(thisSubjectItem.getStartDate() / 10000,
                            ((thisSubjectItem.getStartDate() / 100) % 100) - 1,
                            thisSubjectItem.getStartDate() % 100);
                    dpd.setMinDate(minDate);
                } else {
                    dpd = DatePickerDialog.newInstance(
                            dateButtonsListener,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                }
                dpd.show(usingAct.getFragmentManager(), "datePicker");
            }
        });
        if (thisSubjectItem.getStartDate() != 0)
            subjectViewHolder.startDateButton.setText("From    " + (thisSubjectItem.getStartDate() % 100)
                    + "/" + ((thisSubjectItem.getStartDate() / 100) % 100)
                    + "/" + (thisSubjectItem.getStartDate() / 10000));
        if (thisSubjectItem.getDueDate() != 0)
            subjectViewHolder.dueDateButton.setText("To    " + (thisSubjectItem.getDueDate() % 100)
                    + "/" + ((thisSubjectItem.getDueDate() / 100) % 100)
                    + "/" + (thisSubjectItem.getDueDate() / 10000));
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
        public ButtonFlat startDateButton;
        public ButtonFlat dueDateButton;

        public SubjectViewHolder(View v) {
            super(v);
            subjectName = (TextView) v.findViewById(R.id.subject_name);
            startDateButton = (ButtonFlat) v.findViewById(R.id.start_date);
            dueDateButton = (ButtonFlat) v.findViewById(R.id.due_date);
            subjectPerc = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    private class DateButtonsListener implements DatePickerDialog.OnDateSetListener {
        public boolean start; //this var is used to determine which kind of date will be set
        private int i;

        public DateButtonsListener(int number) {
            i = number;
            start = true;
        }

        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            int date = year * 10000 + (month + 1) * 100 + day;
            if (start)
                subjectItemList.get(i).setStartDate(date);
            else
                subjectItemList.get(i).setDueDate(date);
            thisAdapter.notifyDataSetChanged();
        }
    }
}
