package br.estacio.cadastrodeclientes.task;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import br.estacio.cadastrodeclientes.util.ImageUtil;
import br.estacio.cadastrodeclientes.MainActivity;
import br.estacio.cadastrodeclientes.converter.ClienteConverter;
import br.estacio.cadastrodeclientes.dao.ClienteDAO;
import br.estacio.cadastrodeclientes.model.Cliente;
import br.estacio.cadastrodeclientes.ws.WebRequest;


/**
 * Created by carlos on 05/11/2015.
 */
public class ListaClienteTask extends AsyncTask<String, Object, Boolean> {
    private final MainActivity activity;
    private ProgressDialog progress;

    public ListaClienteTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(activity, "Aguarde...", "Obtendo dados!!!", true);
    }

    private Bitmap downloadImageBitmap(String url) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebRequest request = new WebRequest();
            String jsonResult = request.list();
            JSONArray jsonArray = new JSONArray(jsonResult);
            List<Cliente> clientes = new ClienteConverter().fromJson(jsonArray);
            if (clientes != null && !clientes.isEmpty()) {
                ClienteDAO dao = new ClienteDAO(activity);
                FileOutputStream fos;
                Bitmap image;
                for (Cliente cliente: clientes) {
                    if (cliente.getCaminhoFoto() != null) {
                        cliente.setCaminhoFoto(activity.getExternalFilesDir(null) + "/" + cliente.getCaminhoFoto());
                    }
                    if (dao.findById(cliente.getId()) == null) {
                        cliente.setImporting(true);
                        //image = downloadImageBitmap(String.format(WebRequest.BASE_URL + WebRequest.IMAGE, cliente.getChave(), cliente.getId()));
                        //ImageUtil.saveImage(image, cliente.getCaminhoFoto());
                        if (cliente.getImage() != null && !cliente.getImage().equals("")) {
                            ImageUtil.saveImage(cliente.getImage(), cliente.getCaminhoFoto());
                        }
                        dao.insert(cliente);
                    }
                    else {
                        dao.update(cliente);
                    }
                }
                dao.close();
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao obter a lista de clientes", Toast.LENGTH_LONG).show();
        }
        else {
            activity.carregaLista();
        }
        progress.dismiss();
    }
}
