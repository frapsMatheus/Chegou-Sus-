package cadesus.co.cadesus.PostoInfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Map;

import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.DB.Entidades.Remedio;
import cadesus.co.cadesus.Main.RemediosHolder;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class PostoInfoAdapter extends RecyclerView.Adapter {


    static int POSTO_VIEW = 1;
    static int REMEDIO_VIEW = 2;

    private Context mContext;
    private PostoDeSaude mPostoDeSaude;
    private Map<String,Long> mQuantidades;
    private ArrayList<Remedio> mRemedios = new ArrayList<>();

    PostoInfoAdapter(Context context, PostoDeSaude postoDeSaude, Map<String,Long> quantidades,
                     ArrayList<Remedio> remedios)
    {
        mContext = context;
        mPostoDeSaude = postoDeSaude;
        mQuantidades = quantidades;
        mRemedios = remedios;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((PostoInfoHolder)holder).setView(mContext,mPostoDeSaude);
        } else {
            int currentPosition = position-1;
            Remedio remedio = mRemedios.get(currentPosition);
            ((RemediosHolder)holder).setRemedio(remedio,mQuantidades.get(remedio.uid));
            ((RemediosHolder)holder).hideEdit();
        }
    }

    @Override
    public int getItemCount() {
        return 1 + mRemedios.size();
    }
}
