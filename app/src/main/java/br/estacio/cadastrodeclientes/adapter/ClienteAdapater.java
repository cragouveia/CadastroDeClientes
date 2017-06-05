package br.estacio.cadastrodeclientes.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.estacio.cadastrodeclientes.util.ImageUtil;
import br.estacio.cadastrodeclientes.R;
import br.estacio.cadastrodeclientes.model.Cliente;

/**
 * Created by carlos on 03/04/17.
 */

public class ClienteAdapater extends BaseAdapter {

    private Activity activity;
    private List<Cliente> list;

    private TextView txtNomeCliente, txtFoneCliente, txtMailCliente;
    private ImageView foto;

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

        foto = (ImageView) layout.findViewById(R.id.itemFoto);
        txtNomeCliente = (TextView) layout.findViewById(R.id.txtNomeCliente);
        txtMailCliente = (TextView) layout.findViewById(R.id.txtMailCliente);
        txtFoneCliente = (TextView) layout.findViewById(R.id.txtFoneCliente);

        Cliente cliente = getItem(position);
        txtNomeCliente.setText(cliente.getNome());
        txtMailCliente.setText(cliente.getEmail());
        txtFoneCliente.setText(cliente.getFone());
        Bitmap bm;
        if (cliente.getCaminhoFoto() != null) {
            bm = BitmapFactory.decodeFile(cliente.getCaminhoFoto());
        }
        else {
            bm = BitmapFactory.decodeResource(activity.getResources(),
                    R.mipmap.ic_no_image);
        }
        foto.setImageBitmap(ImageUtil.crocpCircle(bm));
        return layout;
    }

}
