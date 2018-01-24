package br.com.fabiano.snapdark.ifkeys.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Fabiano on 14/08/2016.
 */
public class Produto implements Comparable<Produto>,Parcelable {
    public int id;
    public String nome;
    public String categoria;
    public String descricao;
    public int id_loja;
    public double preco;
    public int estado;
    public int estoque;
    public String unidade;

    public Produto(){}


    protected Produto(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        categoria = in.readString();
        descricao = in.readString();
        id_loja = in.readInt();
        preco = in.readDouble();
        estado = in.readInt();
        estoque = in.readInt();
        unidade = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeString(categoria);
        dest.writeString(descricao);
        dest.writeInt(id_loja);
        dest.writeDouble(preco);
        dest.writeInt(estado);
        dest.writeInt(estoque);
        dest.writeString(unidade);
    }

    public static final Creator<Produto> CREATOR = new Creator<Produto>() {
        @Override
        public Produto createFromParcel(Parcel in) {
            return new Produto(in);
        }

        @Override
        public Produto[] newArray(int size) {
            return new Produto[size];
        }
    };

    @Override
    public int compareTo(@NonNull Produto o) {
        return this.nome.compareTo(o.nome);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
