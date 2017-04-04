package br.estacio.cadastrodeclientes.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.estacio.cadastrodeclientes.R;
import br.estacio.cadastrodeclientes.model.Cliente;

/**
 * Created by carlos on 03/04/17.
 */

public class ClienteAdapater extends BaseAdapter {

    private Activity activity;
    private List<Cliente> list;

    private TextView txtNomeCliente, txtFoneCliente;

    public ClienteAdapater(Activity activity, List<Cliente> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Cliente getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = activity.getLayoutInflater().inflate(R.layout.cliente, parent, false);

        txtNomeCliente = (TextView) layout.findViewById(R.id.txtNomeCliente);
        txtFoneCliente = (TextView) layout.findViewById(R.id.txtFoneCliente);

        Cliente cliente = getItem(position);
        txtNomeCliente.setText(cliente.getNome());
        txtFoneCliente.setText(cliente.getFone());

        return layout;
    }
}
