package br.estacio.cadastrodeclientes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Calendar;

import br.estacio.cadastrodeclientes.util.ImageUtil;

/**
 * Created by carlos on 27/03/17.
 */

public class Cliente implements Serializable {

    private long id;
    private String chave;
    private String nome;
    private String email;
    private Calendar dataNasc;
    private String fone;
    private String cep;
    private String endereco;
    private String numero;
    private String cidade;
    private int sexo;
    private String caminhoFoto;
    private EstadoCivil estadoCivil;
    private String image;
    @JsonIgnore
    private boolean novo;
    @JsonIgnore
    private boolean importing;

    public Cliente() {
        novo = false;
        importing = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public Calendar getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Calendar dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getImage() {
        if (!importing && this.caminhoFoto != null) {
            image = ImageUtil.bitmapToBase64(caminhoFoto);
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isNovo() {
        return novo;
    }

    public void setNovo(boolean novo) {
        this.novo = novo;
    }

    public boolean isImporting() {
        return importing;
    }

    public void setImporting(boolean importing) {
        this.importing = importing;
    }

    @Override
    public String toString() {
        return nome + " " + estadoCivil.toString();
    }

}
