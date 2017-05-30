package br.estacio.cadastrodeclientes.ws;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import br.estacio.cadastrodeclientes.model.Cliente;

/**
 * Created by carlos on 22/05/17.
 */

public class WebRequest {

    private static final String BASE_URL = "http://gestor.quantati.com.br/cadastrodecliente/services/ws/";

    private static final String SAVE_CLIENTE = "savecliente";

    private static final int TIMEOUT = 3000;

    private String sendRequest(URL url, String method, JSONObject jsonObject) {
        HttpURLConnection conn = null;
        StringBuffer result = new StringBuffer();
        InputStream inputStream = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod(method);
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);
            if (jsonObject != null) {
                Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(String.valueOf(jsonObject));
                writer.close();
            }
            conn.connect();
            inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                result.append(inputLine);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.i(String.format("sendRequest(%s)", conn.getURL().toString()), "Error closing InputStream");
                }
            }
        }
        return null;
    }

    public String saveCliente(Cliente cliente) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", cliente.getId());
            jsonObject.put("nome", cliente.getNome());
            jsonObject.put("email", cliente.getEmail());
            jsonObject.put("dataNasc", cliente.getDataNasc());
            jsonObject.put("fone", cliente.getFone());
            jsonObject.put("CEP", cliente.getCEP());
            jsonObject.put("endereco", cliente.getEndereco());
            jsonObject.put("numero", cliente.getNumero());
            jsonObject.put("sexo", cliente.getSexo());
            jsonObject.put("estadoCivil", cliente.getEstadoCivil().name());
            URL url = new URL(BASE_URL + SAVE_CLIENTE);
            return sendRequest(url, "POST", jsonObject);
        }
        catch (Exception e) {
            return null;
        }
    }


}
