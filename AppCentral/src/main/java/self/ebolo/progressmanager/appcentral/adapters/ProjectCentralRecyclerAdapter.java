package self.ebolo.progressmanager.appcentral.adapters;

import android.content.Context;
import android.graphics.Color;
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
import self.ebolo.progressmanager.appcentral.data.ProjectItem;

/**
 * Created by YOLO on 8/24/2015.
 */
public class ProjectCentralRecyclerAdapter extends CardRecyclerView.Adapter<ProjectCentralRecyclerAdapter.ViewHolder>
    implements ProjectCardTouchHelperAdapter {
    private final MainActivity mActivity;
    private final SnackBar mSnackbar;
    protected CardRecyclerView mCardRecyclerView;
    private ArrayList<Card> mCards;
    private ArrayList<ProjectItem> mData;
    private Card mBackupCard;
    private ProjectItem mBackupData;

    public ProjectCentralRecyclerAdapter(Context context
        , ArrayList<Card> cards, ArrayList<ProjectItem> data) {
        mCards = cards;
        mData = data;
        mActivity = (MainActivity) context;
        mSnackbar = ((MainActivity) context).getSnackBar();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Card mProjectCard = mCards.get(position);

        holder.mCardView.setForceReplaceInnerLayout(true);
        holder.setIsRecyclable(false);

        holder.mCardView.setCard(mProjectCard);
        holder.mCardHeader.setBackgroundColor(Color.parseColor(mData.get(position).getColor()));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_card, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    @Override
    public void onItemDismiss(final int position) {
        mBackupData = mData.get(position);
        mBackupCard = mCards.get(position);
        mCards.remove(position);
        mData.remove(position);
        Paper.put("projects", mData);
        notifyItemRemoved(position);
        mSnackbar.applyStyle(R.style.SnackBarUndoRemove).actionClickListener(new SnackBar.OnActionClickListener() {
            @Override
            public void onActionClick(SnackBar snackBar, int i) {
                mCards.add(position, mBackupCard);
                mData.add(position, mBackupData);
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
                Collections.swap(mCards, i, i + 1);
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mCards, i, i - 1);
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

