package cadesus.co.cadesus.MeusPostos;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class MeusPostosViewHolder extends RecyclerView.ViewHolder {

    TextView mName;
    TextView mLocation;
    CheckBox mCheckBox;


    public MeusPostosViewHolder(View itemView) {
        super(itemView);
        prepareViews(itemView);
    }

    private void prepareViews(View v)
    {
        mName = (TextView)v.findViewById(R.id.cell_post_nome);
        mLocation = (TextView)v.findViewById(R.id.cell_posto_local);
        mCheckBox = (CheckBox)v.findViewById(R.id.cell_posto_checkbox);
    }

    public void setView(PostoDeSaude posto, boolean state)
    {
        mName.setText(posto.nome);
        mLocation.setText(posto.endereco);
        mCheckBox.setChecked(state);
    }

    public void setOnClickCheckBox(View.OnClickListener onChecked )
    {
        mCheckBox.setOnClickListener(onChecked);
    }
}
