package cadesus.co.cadesus.DB.Entidades;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fraps on 7/13/16.
 */
@IgnoreExtraProperties
public class PostoDeSaude {

    public  String uid;
    public List<Double> location;
    public String nome;
    public String endereco;
    public String cep;
    public String telefone;
    public Map<String, Long> remedios = new HashMap<>();

    public void adicionarRemedio(String id, Long caixas)
    {
        remedios.put(id,caixas);
    }

    public void removerRemedio(String id)
    {
        remedios.remove(id);
    }

}
