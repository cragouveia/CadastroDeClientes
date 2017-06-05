package br.estacio.cadastrodeclientes.task;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import br.estacio.cadastrodeclientes.MainActivity;
import br.estacio.cadastrodeclientes.dao.ClienteDAO;
import br.estacio.cadastrodeclientes.model.Cliente;
import br.estacio.cadastrodeclientes.ws.WebRequest;


/**
 * Created by carlos on 05/11/2015.
 */
public class DeleteClienteTask extends AsyncTask<String, Object, Boolean> {
    private final MainActivity activity;
    private final Cliente cliente;
    private ProgressDialog progress;

    private static final String QTDE = "qtde";

    public DeleteClienteTask(MainActivity activity, Cliente cliente) {
        this.activity = activity;
        this.cliente = cliente;
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(activity, "Aguarde...", "Enviando dados!!!", true);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebRequest request = new WebRequest();
            String jsonResult = request.delete(cliente.getId());
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(QTDE) > 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao remover o cliente", Toast.LENGTH_LONG).show();
        }
        else {
            ClienteDAO dao = new ClienteDAO(activity);
            dao.delete(cliente.getId());
            dao.close();
        }
        activity.carregaLista();
        progress.dismiss();
    }
}
