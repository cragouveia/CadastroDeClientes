package br.estacio.cadastrodeclientes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.estacio.cadastrodeclientes.model.Cliente;
import br.estacio.cadastrodeclientes.model.EstadoCivil;


/**
 * Created by carlos on 03/04/17.
 */

public class ClienteDAO extends SQLiteOpenHelper {

    final static String DATABASE = "CLIENTES";
    final static int VERSION = 1;
    final static String TABLE = "cliente";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private Calendar calendar = Calendar.getInstance();

    public ClienteDAO(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddlCliente = "create table cliente (" +
                "id integer not null primary key autoincrement," +
                "nome text not null, " +
                "endereco text, " +
                "fone text, " +
                "email text, " +
                "cep text, " +
                "numero text, " +
                "sexo integer, " +
                "cidade text," +
                "caminhoFoto text," +
                "dataNasc text," +
                "estadoCivil text," +
                "novo integer);";
        db.execSQL(ddlCliente);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private ContentValues getValues(Cliente cliente) {
        ContentValues values = new ContentValues();

        if (cliente.isImporting()) {
            values.put("id", cliente.getId());
        }
        values.put("nome", cliente.getNome());
        values.put("endereco", cliente.getEndereco());
        values.put("fone", cliente.getFone());
        values.put("email",  cliente.getEmail());
        values.put("cep", cliente.getCep());
        values.put("numero", cliente.getNumero());
        values.put("sexo", cliente.getSexo());
        values.put("cidade", cliente.getCidade());
        values.put("caminhoFoto", cliente.getCaminhoFoto());
        values.put("estadoCivil", cliente.getEstadoCivil().name());
        values.put("dataNasc", dateFormat.format(cliente.getDataNasc().getTime()));
        values.put("novo", cliente.isNovo() ? 1 : 0);
        return values;
    }

    public void insert(Cliente cliente) {
        getWritableDatabase().insert(TABLE, null, getValues(cliente));
    }

    public void update(Cliente cliente) {
        String[] args = { String.valueOf(cliente.getId())};
        getWritableDatabase().update(TABLE, getValues(cliente), "id=?", args);
    }

    public void delete (Long id) {
        String[] args = { id.toString() };
        getWritableDatabase().delete (TABLE, "id=?", args);
    }

    public Cliente findById(Long id) {
        String[] args = { String.valueOf(id) };
        Cursor c = getReadableDatabase()
                .rawQuery("SELECT * FROM " + TABLE +  " where id = ?", args);
        Cliente cliente = null;
        if (c.moveToNext()) {
            cliente = fill(c);
        }
        c.close();
        return cliente;
    }

    public List<Cliente> list() {
        Cursor c = getReadableDatabase()
                .rawQuery("SELECT * FROM " + TABLE +  " order by nome", null);
        List<Cliente> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(fill(c));
        }
        c.close();
        return list;
    }

    private Cliente fill(Cursor c) {
        Cliente cliente = new Cliente();
        cliente.setId(c.getLong(c.getColumnIndex("id")));
        cliente.setNome(c.getString(c.getColumnIndex("nome")));
        cliente.setEndereco(c.getString(c.getColumnIndex("endereco")));
        cliente.setFone(c.getString(c.getColumnIndex("fone")));
        cliente.setEmail(c.getString(c.getColumnIndex("email")));
        cliente.setCep(c.getString(c.getColumnIndex("cep")));
        cliente.setNumero(c.getString(c.getColumnIndex("numero")));
        cliente.setSexo(c.getInt(c.getColumnIndex("sexo")));
        cliente.setCidade(c.getString(c.getColumnIndex("cidade")));
        cliente.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));
        try {
            calendar.setTime(dateFormat.parse(c.getString(c.getColumnIndex("dataNasc"))));
        }
        catch (Exception e) {
            calendar.setTime(new Date());
        }
        cliente.setDataNasc(calendar);
        cliente.setEstadoCivil(EstadoCivil.valueOf(c.getString(c.getColumnIndex("estadoCivil"))));
        cliente.setNovo(c.getInt(c.getColumnIndex("novo")) == 1);
        cliente.setImporting(false);
        return cliente;
    }
}
