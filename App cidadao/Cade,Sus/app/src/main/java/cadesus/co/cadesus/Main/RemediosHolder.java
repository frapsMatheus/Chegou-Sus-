package cadesus.co.cadesus.Main;

import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cadesus.co.cadesus.DB.Entidades.Remedio;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class RemediosHolder extends RecyclerView.ViewHolder {

    TextView mNome;
    TextView mQuantidade;
    TextView mPatologia;
    public ImageView mEdit;

    public RemediosHolder(View itemView) {
        super(itemView);
        prepareViews(itemView);
    }

    private void prepareViews(View v)
    {
        mNome = (TextView) v.findViewById(R.id.cell_remedio_nome);
        mQuantidade = (TextView) v.findViewById(R.id.cell_remedio_quantidade);
        mEdit = (ImageView) v.findViewById(R.id.cell_remedio_edit);
        mPatologia = (TextView) v.findViewById(R.id.cell_remedio_patologia);
    }

    public void setRemedio(Remedio remedio, Long quantidade)
    {
        mNome.setText(remedio.principio_ativo);
        mPatologia.setText(remedio.patologia);
        mQuantidade.setText(String.valueOf(quantidade));
    }

    public void setEdit(View.OnClickListener click)
    {
        mEdit.setOnClickListener(click);
    }

    public void hideEdit()
    {
        mEdit.setVisibility(View.GONE);
    }
}
