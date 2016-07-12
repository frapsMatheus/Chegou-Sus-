package cadesus.co.cadesus.Cells;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/12/16.
 */
public class PostosHolder extends RecyclerView.ViewHolder {

    private TextView mName;
    private TextView mDistance;
    private CheckBox mSelected;

    public PostosHolder(View itemView) {
        super(itemView);
        prepareView(itemView);
    }

    private void prepareView(View v)
    {
        mName = (TextView)v.findViewById(R.id.cell_post_nome);
        mDistance = (TextView)v.findViewById(R.id.cell_posto_distancia);
        mSelected = (CheckBox)v.findViewById(R.id.cell_posto_checkbox);
    }
}
