package self.ebolo.progressmanager.appcentral.adapters;

import android.content.Context;
import io.paperdb.Paper;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.internal.Card;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by YOLO on 8/24/2015.
 */
public class ProjectCardRecyclerAdapter extends CardArrayRecyclerViewAdapter implements ProjectCardTouchHelperAdapter {

    private ArrayList<ProjectItem> projectItemList;
    private final Context usingAct;
    private final ProjectCardRecyclerAdapter thisAdapter = this;

    public ProjectCardRecyclerAdapter(Context context, ArrayList<Card> cards
        , ArrayList<ProjectItem> data) {
        super(context, cards);
        projectItemList = data;
        usingAct = context;
    }

    @Override
    public void onItemDismiss(int position) {
        mCards.remove(position);
        projectItemList.remove(position);
        Paper.put("projects", projectItemList);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mCards, i, i + 1);
                Collections.swap(projectItemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mCards, i, i - 1);
                Collections.swap(projectItemList, i, i - 1);
            }
        }
        Paper.put("projects", projectItemList);
        notifyItemMoved(fromPosition, toPosition);
    }
}
