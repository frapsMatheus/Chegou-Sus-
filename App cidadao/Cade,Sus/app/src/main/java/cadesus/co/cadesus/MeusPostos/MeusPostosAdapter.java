package cadesus.co.cadesus.MeusPostos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cadesus.co.cadesus.AdicionarRemedios.AdicionarRemedioHolder;
import cadesus.co.cadesus.Cells.EmptyView;
import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class MeusPostosAdapter extends RecyclerView.Adapter {

    final static int POSTOS_VIEW = 1;
    final static int EMPTY_VIEW = 2;

    private ArrayList<PostoDeSaude> mPostosDeSaude;
    private HashMap<String, Boolean> mPostosSelected;


    MeusPostosAdapter(ArrayList<PostoDeSaude> postos, HashMap<String, Boolean> selected)
    {
        mPostosDeSaude = postos;
        mPostosSelected = selected;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == POSTOS_VIEW) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_adicionar_posto, parent, false);
            MeusPostosViewHolder holder = new MeusPostosViewHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_empty, parent, false);
            EmptyView holder = new EmptyView(view);
            return holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position<mPostosDeSaude.size()) {
            return POSTOS_VIEW;
        } else {
            return EMPTY_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < mPostosDeSaude.size()) {
            View itemView = holder.itemView;
            final PostoDeSaude posto = mPostosDeSaude.get(position);
            ((MeusPostosViewHolder)holder).setView(posto, mPostosSelected.get(posto.uid));
            CompoundButton.OnClickListener check = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPostosSelected.put(posto.uid, !mPostosSelected.get(posto.uid));
                }
            };
            ((MeusPostosViewHolder)holder).setOnClickCheckBox(check);
        }
    }

    @Override
    public int getItemCount() {
        return mPostosDeSaude.size() + 1;
    }
}
