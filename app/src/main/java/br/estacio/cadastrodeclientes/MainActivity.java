package br.estacio.cadastrodeclientes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import br.estacio.cadastrodeclientes.adapter.ClienteAdapater;
import br.estacio.cadastrodeclientes.dao.ClienteDAO;
import br.estacio.cadastrodeclientes.model.Cliente;
import br.estacio.cadastrodeclientes.task.DeleteClienteTask;
import br.estacio.cadastrodeclientes.task.ListaClienteTask;
import br.estacio.cadastrodeclientes.util.Util;

public class MainActivity extends AppCompatActivity {

    private ListView listaCliente;
    private ClienteAdapater adapter;
    private List<Cliente> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (Util.isConnected(this)) {
                new ListaClienteTask(this).execute();
            }
        }
        catch(Exception e){
        }

        listaCliente = (ListView) findViewById(R.id.listaCliente);
        listaCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cliente clienteSelecionado = adapter.getItem(position);

                Intent cadCliente = new Intent(MainActivity.this, ClienteActivity.class);
                cadCliente.putExtra("clienteSelecionado", clienteSelecionado);
                startActivity(cadCliente);
            }
        });

        Button btnNovoAluno = (Button) findViewById(R.id.btnNovoAluno);
        btnNovoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClienteActivity.class);
                startActivity(intent);
            }
        });

        registerForContextMenu(listaCliente);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }


    public void carregaLista() {
        ClienteDAO dao = new ClienteDAO(this);
        clientes = dao.list();
        dao.close();

        adapter = new ClienteAdapater(this, clientes);
        listaCliente.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Cliente clienteSelecionado = adapter.getItem(info.position);
        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                confirmaRemocao(clienteSelecionado);
                return false;
            }
        });
    }

    private void confirmaRemocao(final Cliente clienteSelecionado) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Remoção");
            dialogBuilder.setMessage(
                    String.format("Confirma a remoção do cliente %s?",
                            clienteSelecionado.getNome()));
            dialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
/*
                    ClienteDAO dao = new ClienteDAO(MainActivity.this);
                    dao.delete(clienteSelecionado.getId());
                    dao.close();
                    carregaLista();
*/
                    dialog.cancel();
                    new DeleteClienteTask(MainActivity.this, clienteSelecionado).execute();
                }
            });
            dialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
            dialogBuilder.create().show();
    }

}
