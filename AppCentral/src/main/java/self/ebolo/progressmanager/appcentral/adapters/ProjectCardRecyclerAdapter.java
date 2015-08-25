package self.ebolo.progressmanager.appcentral.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.rey.material.widget.SnackBar;
import io.paperdb.Paper;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
import it.gmariotti.cardslib.library.view.CardViewNative;
import self.ebolo.progressmanager.appcentral.R;
import self.ebolo.progressmanager.appcentral.activities.AppCentralActivity;
import self.ebolo.progressmanager.appcentral.cards.ProjectCard;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by YOLO on 8/24/2015.
 */
public class ProjectCardRecyclerAdapter extends RecyclerView.Adapter<ProjectCardRecyclerAdapter.ViewHolder>
    implements ProjectCardTouchHelperAdapter {

    private final AppCompatActivity usingAct;
    private ArrayList<ProjectItem> projectItemList;
    protected SnackBar mSnackbar;
    private ProjectItem backup;

    /**
     * {@link CardRecyclerView}
     */
    protected CardRecyclerView mCardRecyclerView;

    public ProjectCardRecyclerAdapter(AppCompatActivity context
        , ArrayList<ProjectItem> data) {
        projectItemList = data;
        usingAct = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int i = position;
        final ProjectItem mProject = projectItemList.get(i);

        holder.projectCardNative.setForceReplaceInnerLayout(true);
        holder.projectCardNative.setRecycle(false);

        Card card = new ProjectCard(usingAct, mProject);
        card.setClickable(true);
        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
            }
        });
        //card.setBackgroundResource(new ColorDrawable(Color.parseColor(mProject.getColor())));

        ProjectCardHeader cardHeader = new ProjectCardHeader(usingAct);
        cardHeader.setTitle(mProject.getSubjectName());
        cardHeader.setButtonExpandVisible(true);

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
        mSnackbar = ((AppCentralActivity)usingAct).getSnackBar();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardViewNative projectCardNative;
        public RelativeLayout projectCardHeader;
        public ViewHolder (View view) {
            super(view);
            projectCardNative = (CardViewNative)view.findViewById(R.id.list_cardId);
            projectCardHeader = (RelativeLayout)view.findViewById(R.id.project_card_header);
        }
    }

    private class ProjectCardExpand extends CardExpand {
        private TextView projectCardInfo;
        private final ProjectItem projectItem;

        public ProjectCardExpand(Context context, ProjectItem data) {
            super(context, R.layout.project_card_expand);
            projectItem = data;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            if (view != null) {
                projectCardInfo = (TextView)view.findViewById(R.id.project_card_info);
                if (projectCardInfo != null) {
                    projectCardInfo.setText(projectItem.getSubjectName());
                }
            }
        }
    }

    private class ProjectCardHeader extends CardHeader {
        public ProjectCardHeader(Context context) {
            super(context, R.layout.project_card_header_inner);
        }
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

}
