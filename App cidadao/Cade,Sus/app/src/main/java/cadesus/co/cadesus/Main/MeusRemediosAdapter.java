package cadesus.co.cadesus.Main;

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

import cadesus.co.cadesus.DB.DBUser;
import cadesus.co.cadesus.DB.Entidades.Remedio;
import cadesus.co.cadesus.DB.Entidades.User;
import cadesus.co.cadesus.PostosComRemedio.PostosComRemedioActivity;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class MeusRemediosAdapter extends RecyclerView.Adapter<RemediosHolder> {

    private final Activity mActivity;
    private final Map<String,Boolean> mNotifications;
    private Map<String,Long> mQuantidades;
    private ArrayList<Remedio> mRemedios = new ArrayList<>();

    MeusRemediosAdapter(ArrayList<Remedio> remedios, Map<String,Long> quantidades, Activity activity,
                        Map<String,Boolean> notifications)
    {
        mRemedios = remedios;
        mQuantidades = quantidades;
        mActivity = activity;
        mNotifications = notifications;
    }


    @Override
    public RemediosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_remedio, parent, false);
        RemediosHolder holder = new RemediosHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RemediosHolder holder, final int position) {
        View view = holder.itemView;
        Remedio remedio = mRemedios.get(position);
        if (mNotifications.get(remedio.uid) != null) {
            holder.setRemedio(remedio,mQuantidades.get(remedio.uid),mNotifications.get(remedio.uid));
        } else {
            holder.setRemedio(remedio,mQuantidades.get(remedio.uid),false);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, PostosComRemedioActivity.class);
                intent.putExtra("remedioID",mRemedios.get(position).uid);
                mActivity.startActivity(intent);
            }
        });
        View.OnClickListener onEditClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Remedio remedio = mRemedios.get(position);
                criarDialog(remedio,position);
            }
        };
        holder.setEdit(onEditClick);
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
                User.shared().adicionarRemedio(remedio.uid,Long.valueOf(quantidade));
                DBUser.shared().saveUser();
            }
        });
        builder.setNegativeButton("Remover remédio", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                User.shared().removerRemedio(remedio.uid);
                DBUser.shared().saveUser();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return mRemedios.size();
    }
}
