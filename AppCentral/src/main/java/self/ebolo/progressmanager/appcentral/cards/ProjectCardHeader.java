package self.ebolo.progressmanager.appcentral.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.CardHeader;
import self.ebolo.progressmanager.appcentral.R;

/**
 * Created by YOLO on 8/26/2015.
 */
public class ProjectCardHeader extends CardHeader {
    private int titSize;
    private String titleTxt;
    private TextView title;

    public ProjectCardHeader(Context context, int titleSize, String titleText) {
        super(context, R.layout.project_card_header_inner);
        titSize = titleSize;
        titleTxt = titleText;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        title = (TextView) view.findViewById(R.id.card_header_inner_simple_title);
        title.setTextSize((float) titSize);
        title.setText(titleTxt);
    }

    public TextView getTitleView() {
        return title;
    }
}
