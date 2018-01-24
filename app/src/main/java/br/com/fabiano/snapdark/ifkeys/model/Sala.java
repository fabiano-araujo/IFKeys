package br.com.fabiano.snapdark.ifkeys.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Fabiano on 14/08/2016.
 */
public class Sala implements Comparable<Sala>,Parcelable {
    public int id;
    public String nome;
    public String campus;
    public int numero;
    public boolean disponivel;
    public Sala(){}


    protected Sala(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        campus = in.readString();
        numero = in.readInt();
        disponivel = in.readByte() != 0;
    }

    public static final Creator<Sala> CREATOR = new Creator<Sala>() {
        @Override
        public Sala createFromParcel(Parcel in) {
            return new Sala(in);
        }

        @Override
        public Sala[] newArray(int size) {
            return new Sala[size];
        }
    };

    @Override
    public int compareTo(@NonNull Sala o) {
        return this.nome.compareTo(o.nome);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeString(campus);
        dest.writeInt(numero);
        dest.writeByte((byte) (disponivel ? 1 : 0));
    }
}
