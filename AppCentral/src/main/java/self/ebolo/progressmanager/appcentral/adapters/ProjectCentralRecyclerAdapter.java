package self.ebolo.progressmanager.appcentral.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rey.material.widget.SnackBar;

import java.util.ArrayList;
import java.util.Collections;

import io.paperdb.Paper;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
import it.gmariotti.cardslib.library.view.CardViewNative;
import self.ebolo.progressmanager.appcentral.R;
import self.ebolo.progressmanager.appcentral.activities.MainActivity;
import self.ebolo.progressmanager.appcentral.activities.ProjectViewActivity;
import self.ebolo.progressmanager.appcentral.cards.ProjectCard;
import self.ebolo.progressmanager.appcentral.cards.ProjectCardExpand;
import self.ebolo.progressmanager.appcentral.cards.ProjectCardHeader;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;

/**
 * Created by YOLO on 8/24/2015.
 */
public class ProjectCentralRecyclerAdapter extends CardRecyclerView.Adapter<ProjectCentralRecyclerAdapter.ViewHolder>
    implements ProjectCardTouchHelperAdapter {
    private final MainActivity mActivity;
    private final SnackBar mSnackbar;
    protected CardRecyclerView mCardRecyclerView;
    private ArrayList<ProjectItem> mData;
    private ProjectItem backup;

    public ProjectCentralRecyclerAdapter(Context context
        , ArrayList<ProjectItem> data) {
        mData = data;
        mActivity = (MainActivity) context;
        mSnackbar = ((MainActivity) context).getSnackBar();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int i = position;
        final ProjectItem mProject = mData.get(i);

        holder.mCardView.setForceReplaceInnerLayout(true);
        holder.mCardView.setRecycle(false);

        final ProjectCardHeader cardHeader = new ProjectCardHeader(
            mActivity, 22, mProject.getSubjectName());
        cardHeader.setButtonExpandVisible(true);

        ProjectCard card = new ProjectCard(mActivity, mProject);
        card.setClickable(true);
        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                final Intent projectViewIntent = new Intent(mActivity, ProjectViewActivity.class);
                projectViewIntent.putExtra("subjList", mData);
                projectViewIntent.putExtra("selectedSubjNum", i);
                if (Build.VERSION.SDK_INT > 20) {
                    holder.mCardView.setTransitionName("info_card");
                    cardHeader.getTitleView().setTransitionName("subj_title");
                    Pair<View, String> p1 = Pair.create((View) holder.mCardView, "info_card");
                    Pair<View, String> p2 = Pair.create((View) cardHeader.getTitleView(), "subj_title");
                    //((AppCentralActivity) mActivity).hideFAB();
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, p1, p2);
                    ActivityCompat.startActivityForResult(mActivity, projectViewIntent, 2, optionsCompat.toBundle());
                } else {
                    mActivity.startActivity(projectViewIntent);
                }
            }
        });
        //card.setBackgroundResource(new ColorDrawable(Color.parseColor(mProject.getColor())));

        ProjectCardExpand cardExpand = new ProjectCardExpand(mActivity, mProject);

        card.addCardHeader(cardHeader);
        card.addCardExpand(cardExpand);

        holder.mCardView.setCard(card);
        holder.mCardHeader.setBackgroundColor(Color.parseColor(mProject.getColor()));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_card, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onItemDismiss(final int position) {
        backup = mData.get(position);
        mData.remove(position);
        Paper.put("projects", mData);
        notifyItemRemoved(position);
        mSnackbar.applyStyle(R.style.SnackBarUndoRemove).actionClickListener(new SnackBar.OnActionClickListener() {
            @Override
            public void onActionClick(SnackBar snackBar, int i) {
                mData.add(position, backup);
                Paper.put("projects", mData);
                notifyItemInserted(position);
                mCardRecyclerView.smoothScrollToPosition(position);
            }
        }).show();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        Paper.put("projects", mData);
        notifyItemMoved(fromPosition, toPosition);
    }

    public CardRecyclerView getCardRecyclerView() {
        return mCardRecyclerView;
    }

    /**
     * Sets the RecyclerView
     *
     * @param cardRecyclerView
     */
    public void setCardRecyclerView(CardRecyclerView cardRecyclerView) {
        mCardRecyclerView = cardRecyclerView;
    }

    public class ViewHolder extends CardRecyclerView.ViewHolder {
        public CardViewNative mCardView;
        public RelativeLayout mCardHeader;

        public ViewHolder(View view) {
            super(view);
            mCardView = (CardViewNative) view.findViewById(R.id.project_card_view);
            mCardHeader = (RelativeLayout) view.findViewById(R.id.project_card_header);
        }
    }

}

