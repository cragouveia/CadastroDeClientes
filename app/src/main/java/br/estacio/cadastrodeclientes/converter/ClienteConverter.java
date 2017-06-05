package br.estacio.cadastrodeclientes.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.estacio.cadastrodeclientes.model.Cliente;


/**
 * Created by carlos on 05/11/2015.
 */
public class ClienteConverter {

    private ObjectMapper mapper = new ObjectMapper();

    public Cliente fromJson(JSONObject jsonObject) {
        Cliente obj;
        try {
            obj = mapper.readValue(jsonObject.toString(), Cliente.class);
            obj.setNovo(false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    public List<Cliente> fromJson(JSONArray jsonArray) {
        List<Cliente> list = new ArrayList<Cliente>();
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject objectJson = null;
            try {
                objectJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Cliente obj = fromJson(objectJson);
            if (obj != null) {
                list.add(obj);
            }
        }
        return list;
    }
}
