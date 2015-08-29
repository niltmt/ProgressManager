package self.ebolo.progressmanager.appcentral.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.FloatingActionButton;

import java.util.ArrayList;

import io.paperdb.Paper;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
import self.ebolo.progressmanager.appcentral.R;
import self.ebolo.progressmanager.appcentral.activities.MainActivity;
import self.ebolo.progressmanager.appcentral.activities.ProjectViewActivity;
import self.ebolo.progressmanager.appcentral.adapters.ProjectCentralRecyclerAdapter;
import self.ebolo.progressmanager.appcentral.cards.CardTouchHelperCallback;
import self.ebolo.progressmanager.appcentral.cards.ProjectCard;
import self.ebolo.progressmanager.appcentral.cards.ProjectCardExpand;
import self.ebolo.progressmanager.appcentral.cards.ProjectCardHeader;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;
import self.ebolo.progressmanager.appcentral.utils.DeviceScreenInfo;
import self.ebolo.progressmanager.appcentral.utils.PreCachingLayoutManager;

public class ProjectCentralFragment extends Fragment {
    private static final String ARG_PARAM1 = "subjList";
    FloatingActionButton appFAB;
    private CardRecyclerView mRecyclerView;
    private ProjectCentralRecyclerAdapter mAdapter;
    private ArrayList<ProjectItem> mData;
    private ArrayList<Card> mCards;
    private DeviceScreenInfo deviceScreenInfo;
    //private OnFragmentInteractionListener mListener;

    public ProjectCentralFragment() {
        // Required empty public constructor
    }

    public static ProjectCentralFragment newInstance(ArrayList<ProjectItem> data) {
        ProjectCentralFragment fragment = new ProjectCentralFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mData = (ArrayList<ProjectItem>) getArguments().get(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_central, container, false);
        mCards = new ArrayList<>();
        if (mData != null && mData.size() > 0) {
            for (int i = 0; i < mData.size(); i++) {
                addNewCard(mData.get(i));
            }
        }
        deviceScreenInfo = new DeviceScreenInfo((AppCompatActivity) getActivity());
        appFAB = ((MainActivity) getActivity()).getFAB();
        appFAB.setOnClickListener(
            new MainActivity.FABListener((AppCompatActivity) getActivity(), this));
        mRecyclerView = (CardRecyclerView) view.findViewById(R.id.project_card_recyclerview);
        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setExtraLayoutSpace((int) deviceScreenInfo.HeightPx);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ProjectCentralRecyclerAdapter(getContext(), mCards, mData);
        mAdapter.setCardRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new CardTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
        mAdapter.notifyDataSetChanged();

        return view;
    }

    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/

    public void addNewProject(final ProjectItem projectItem) {
        mData.add(0, projectItem);
        addNewCard(projectItem);
        mAdapter.notifyItemInserted(0);
        Paper.put("projects", mData);
        mRecyclerView.smoothScrollToPosition(0);
    }

    private void addNewCard(final ProjectItem projectItem) {
        final ProjectCardHeader cardHeader = new ProjectCardHeader(
            getActivity(), 22, projectItem.getSubjectName());
        cardHeader.setButtonExpandVisible(true);

        ProjectCard card = new ProjectCard(getActivity(), projectItem);
        card.setClickable(true);
        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                final Intent projectViewIntent = new Intent(getActivity(), ProjectViewActivity.class);
                projectViewIntent.putExtra("project", projectItem);
                getActivity().startActivity(projectViewIntent);
            }
        });

        ProjectCardExpand cardExpand = new ProjectCardExpand(getActivity(), projectItem);

        card.addCardHeader(cardHeader);
        card.addCardExpand(cardExpand);

        mCards.add(0, card);
    }
}
