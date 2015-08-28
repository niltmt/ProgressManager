package self.ebolo.progressmanager.appcentral.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.rey.material.widget.FloatingActionButton;

import java.util.ArrayList;

import io.paperdb.Paper;
import self.ebolo.progressmanager.appcentral.R;
import self.ebolo.progressmanager.appcentral.activities.MainActivity;
import self.ebolo.progressmanager.appcentral.adapters.ProjectCentralRecyclerAdapter;
import self.ebolo.progressmanager.appcentral.cards.CardTouchHelperCallback;
import self.ebolo.progressmanager.appcentral.data.ProjectItem;
import self.ebolo.progressmanager.appcentral.utils.DeviceScreenInfo;

public class ProjectCentralFragment extends Fragment {
    private static final String ARG_PARAM1 = "subjList";
    FloatingActionButton appFAB;
    private ObservableRecyclerView mRecyclerView;
    private ProjectCentralRecyclerAdapter mAdapter;
    private ArrayList<ProjectItem> mData;
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
        deviceScreenInfo = new DeviceScreenInfo((AppCompatActivity) getActivity());
        appFAB = ((MainActivity) getActivity()).getFAB();
        appFAB.setOnClickListener(
            new MainActivity.FABListener((AppCompatActivity) getActivity(), this));
        mRecyclerView = (ObservableRecyclerView) view.findViewById(R.id.project_card_recyclerview);
        mRecyclerView.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new ProjectCentralRecyclerAdapter(getContext(), mData);
        mAdapter.setCardRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new CardTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int i, boolean b, boolean b1) {

            }

            @Override
            public void onDownMotionEvent() {
            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
                if (scrollState == ScrollState.UP) {
                    if (toolbarIsShown()) { // TODO Not implemented
                        hideToolbar(); // TODO Not implemented
                    }
                } else if (scrollState == ScrollState.DOWN) {
                    if (toolbarIsHidden()) { // TODO Not implemented
                        showToolbar(); // TODO Not implemented
                    }
                }
            }
        });

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

    public void addNewProject(ProjectItem projectItem) {
        mData.add(0, projectItem);
        mAdapter.notifyItemInserted(0);
        Paper.put("projects", mData);
        mRecyclerView.smoothScrollToPosition(0);
    }

    private boolean toolbarIsShown() {
        // Toolbar is 0 in Y-axis, so we can say it's shown.
        return ViewHelper.getTranslationY(((MainActivity) getActivity()).getAB()) == 0;
    }

    private boolean toolbarIsHidden() {
        // Toolbar is outside of the screen and absolute Y matches the height of it.
        // So we can say it's hidden.
        return ViewHelper.getTranslationY(((MainActivity) getActivity()).getAB())
            == -(((MainActivity) getActivity()).getAB()).getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-(((MainActivity) getActivity()).getAB()).getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        final float toTransY = toTranslationY;
        final Toolbar mToolbar = ((MainActivity) getActivity()).getAB();
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mToolbar), toTranslationY).setDuration(200);
        final ObservableRecyclerView mScrollable = mRecyclerView;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (ViewHelper.getTranslationY(mToolbar) == toTransY) {
                    return;
                }
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(mToolbar, translationY);
                FrameLayout container = (FrameLayout) getActivity().findViewById(R.id.fragments_container);
                ViewHelper.setTranslationY(container, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) container.getLayoutParams();
                lp.height = (int) -translationY + (int) deviceScreenInfo.HeightPx - (int) (1.47f * (float) mToolbar.getHeight());
                container.requestLayout();
            }
        });
        animator.start();
    }
}
