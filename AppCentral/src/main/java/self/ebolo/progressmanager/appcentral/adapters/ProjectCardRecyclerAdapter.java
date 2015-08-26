package self.ebolo.progressmanager.appcentral.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
import self.ebolo.progressmanager.appcentral.activities.AppCentralActivity;
import self.ebolo.progressmanager.appcentral.activities.ProjectViewActivity;
import self.ebolo.progressmanager.appcentral.cards.ProjectCard;
import self.ebolo.progressmanager.appcentral.cards.ProjectCardExpand;
import self.ebolo.progressmanager.appcentral.cards.ProjectCardHeader;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;

/**
 * Created by YOLO on 8/24/2015.
 */
public class ProjectCardRecyclerAdapter extends RecyclerView.Adapter<ProjectCardRecyclerAdapter.ViewHolder>
    implements ProjectCardTouchHelperAdapter {

    private final AppCompatActivity usingAct;
    protected SnackBar mSnackbar;
    /**
     * {@link CardRecyclerView}
     */
    protected CardRecyclerView mCardRecyclerView;
    private ArrayList<ProjectItem> projectItemList;
    private ProjectItem backup;

    public ProjectCardRecyclerAdapter(AppCompatActivity context
        , ArrayList<ProjectItem> data) {
        projectItemList = data;
        usingAct = context;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int i = position;
        final ProjectItem mProject = projectItemList.get(i);

        holder.projectCardNative.setForceReplaceInnerLayout(true);
        holder.projectCardNative.setRecycle(false);

        final ProjectCardHeader cardHeader = new ProjectCardHeader(
            usingAct, 22, mProject.getSubjectName());
        cardHeader.setButtonExpandVisible(true);

        Card card = new ProjectCard(usingAct, mProject);
        card.setClickable(true);
        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                final Intent projectViewIntent = new Intent(usingAct, ProjectViewActivity.class);
                projectViewIntent.putExtra("subjList", projectItemList);
                projectViewIntent.putExtra("selectedSubjNum", i);
                if (Build.VERSION.SDK_INT > 20) {
                    holder.projectCardNative.setTransitionName("info_card");
                    cardHeader.getTitleView().setTransitionName("subj_title");
                    Pair<View, String> p1 = Pair.create((View) holder.projectCardNative, "info_card");
                    Pair<View, String> p2 = Pair.create((View) cardHeader.getTitleView(), "subj_title");
                    ((AppCentralActivity) usingAct).hideFAB();
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(usingAct, p1, p2);
                    ActivityCompat.startActivityForResult(usingAct, projectViewIntent, 2, optionsCompat.toBundle());
                } else {
                    usingAct.startActivity(projectViewIntent);
                }
            }
        });
        //card.setBackgroundResource(new ColorDrawable(Color.parseColor(mProject.getColor())));

        ProjectCardExpand cardExpand = new ProjectCardExpand(usingAct, mProject);

        card.addCardHeader(cardHeader);
        card.addCardExpand(cardExpand);

        holder.projectCardNative.setCard(card);
        setupExpandCollapseListAnimation(holder.projectCardNative);
        holder.projectCardHeader.setBackgroundColor(Color.parseColor(mProject.getColor()));
    }

    protected void setupExpandCollapseListAnimation(CardViewNative cardView) {
        cardView.setOnExpandListAnimatorListener(mCardRecyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_card, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public int getItemCount() {
        return projectItemList.size();
    }

    @Override
    public void onItemDismiss(final int position) {
        mSnackbar = ((AppCentralActivity) usingAct).getSnackBar();
        backup = projectItemList.get(position);
        projectItemList.remove(position);
        Paper.put("projects", projectItemList);
        notifyItemRemoved(position);
        mSnackbar.applyStyle(R.style.SnackBarUndoRemove).actionClickListener(new SnackBar.OnActionClickListener() {
            @Override
            public void onActionClick(SnackBar snackBar, int i) {
                projectItemList.add(position, backup);
                Paper.put("projects", projectItemList);
                notifyItemInserted(position);
                mCardRecyclerView.smoothScrollToPosition(position);
            }
        }).show();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(projectItemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(projectItemList, i, i - 1);
            }
        }
        Paper.put("projects", projectItemList);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardViewNative projectCardNative;
        public RelativeLayout projectCardHeader;

        public ViewHolder(View view) {
            super(view);
            projectCardNative = (CardViewNative) view.findViewById(R.id.list_cardId);
            projectCardHeader = (RelativeLayout) view.findViewById(R.id.project_card_header);
        }
    }
}
