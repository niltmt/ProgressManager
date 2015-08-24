package self.ebolo.progressmanager.appcentral.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.paperdb.Paper;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.recyclerview.internal.BaseRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardViewNative;
import self.ebolo.progressmanager.appcentral.R;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.Inflater;

/**
 * Created by YOLO on 8/24/2015.
 */
public class ProjectCardRecyclerAdapter extends RecyclerView.Adapter<ProjectCardRecyclerAdapter.ViewHolder>
    implements ProjectCardTouchHelperAdapter {

    private final Context usingAct;
    private ArrayList<ProjectItem> projectItemList;

    public ProjectCardRecyclerAdapter(Context context
        , ArrayList<ProjectItem> data) {
        projectItemList = data;
        usingAct = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int i = position;
        final ProjectItem mProject = projectItemList.get(i);

        holder.projectCardNative.setForceReplaceInnerLayout(true);

        Card card = new Card(usingAct);

        ProjectCardHeader cardHeader = new ProjectCardHeader(usingAct, mProject.getSubjectName());
        cardHeader.setButtonExpandVisible(true);

        ProjectCardExpand cardExpand = new ProjectCardExpand(usingAct, mProject);

        card.addCardHeader(cardHeader);
        card.addCardExpand(cardExpand);

        holder.projectCardNative.setCard(card);
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
    public void onItemDismiss(int position) {
        projectItemList.remove(position);
        Paper.put("projects", projectItemList);
        notifyItemRemoved(position);
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
        public ViewHolder (View view) {
            super(view);
            projectCardNative = (CardViewNative)view.findViewById(R.id.list_cardId);
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
        private String stringTitle;

        public ProjectCardHeader(Context context, String titleInput) {
            super(context, R.layout.project_card_header_inner);
            stringTitle = titleInput;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            if (view != null) {
                TextView projectCardTitle = (TextView)view.findViewById(R.id.projet_card_title);
                if (projectCardTitle != null) {
                    projectCardTitle.setText(stringTitle);
                }
            }
        }
    }
}
