package co.chegoususadmin.Main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Map;

import co.chegoususadmin.DB.DBPostosDeSaude;
import co.chegoususadmin.DB.Entidades.PostoDeSaude;
import co.chegoususadmin.DB.Entidades.Remedio;
import co.chegoususadmin.R;

/**
 * Created by fraps on 7/14/16.
 */
public class MeusRemediosAdapter extends RecyclerView.Adapter {

    static int POSTO_VIEW = 1;
    static int REMEDIO_VIEW = 2;

    private final Activity mActivity;
    private Map<String,Long> mQuantidades;
    private ArrayList<Remedio> mRemedios = new ArrayList<>();

    MeusRemediosAdapter(ArrayList<Remedio> remedios, Map<String,Long> quantidades, Activity activity)
    {
        mRemedios = remedios;
        mQuantidades = quantidades;
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == POSTO_VIEW) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_posto_main, parent, false);
            PostoInfoHolder holder = new PostoInfoHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_remedio, parent, false);
            RemediosHolder holder = new RemediosHolder(view);
            return holder;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return POSTO_VIEW;
        } else {
            return REMEDIO_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == 0) {
            ((PostoInfoHolder)holder).setView(mActivity.getBaseContext(),PostoDeSaude.shared());
        } else {
            final Remedio remedio = mRemedios.get(position-1);
            ((RemediosHolder)holder).setRemedio(remedio,mQuantidades.get(remedio.uid));
            View.OnClickListener onEditClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    criarDialog(remedio,position);
                }
            };
            ((RemediosHolder)holder).setEdit(onEditClick);
        }
    }

    private void criarDialog(final Remedio remedio, int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        View alertView = mActivity.getLayoutInflater().inflate(R.layout.dialog_quantas_caixas,null);
        final EditText quantidadeRemedio = (EditText)alertView.findViewById(R.id.dialog_caixas);
        quantidadeRemedio.setText(String.valueOf(mQuantidades.get(mRemedios.get(position).uid)));

        builder.setView(alertView);
        builder.setTitle("Editar remédio");
        builder.setMessage("Altere a quantidade de caixas de "
                + remedio.principio_ativo + " que você necessita ou remova da sua lista.");
        builder.setCancelable(true);
        builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int quantidade = Integer.parseInt(quantidadeRemedio.getText().toString());
                PostoDeSaude.shared().adicionarRemedio(remedio.uid,Long.valueOf(quantidade));
                DBPostosDeSaude.shared().saveUser();
            }
        });
        builder.setNegativeButton("Remover remédio", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PostoDeSaude.shared().removerRemedio(remedio.uid);
                DBPostosDeSaude.shared().saveUser();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        if (PostoDeSaude.shared().nome != null) {
            return 1 + mRemedios.size();
        } else {
            return 0;
        }
    }
}
