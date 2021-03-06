package br.estacio.cadastrodeclientes.task;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import br.estacio.cadastrodeclientes.dao.ClienteDAO;
import br.estacio.cadastrodeclientes.model.Cliente;
import br.estacio.cadastrodeclientes.ws.WebRequest;


/**
 * Created by carlos on 05/11/2015.
 */
public class SaveClienteTask extends AsyncTask<String, Object, Long> {
    private final Activity activity;
    private final Cliente cliente;
    private ProgressDialog progress;

    private static final String ID = "id";

    public SaveClienteTask(Activity activity, Cliente cliente) {
        this.activity = activity;
        this.cliente = cliente;
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(activity, "Aguarde...", "Enviando dados!!!", true);
    }

    @Override
    protected Long doInBackground(String... params) {
        try {
            WebRequest request = new WebRequest();
            String jsonResult = request.save(cliente);
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(ID);
        }
        catch (Exception e) {
            return 0L;
        }
    }

    @Override
    protected void onPostExecute(Long id) {
        if (id == 0) {
            Toast.makeText(activity, "Houve um erro ao salvar o cliente", Toast.LENGTH_LONG).show();
        }
        else {
            cliente.setId(id);
            cliente.setNovo(false);
            ClienteDAO dao = new ClienteDAO(activity);
            dao.update(cliente);
            dao.close();
        }
        progress.dismiss();
        activity.finish();
    }
}
