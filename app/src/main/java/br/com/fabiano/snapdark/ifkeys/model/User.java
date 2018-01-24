package br.com.fabiano.snapdark.ifkeys.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Fabiano on 13/10/2017.
 */
public class User implements Parcelable,Comparable<User> {
    public String nome;
    public String nomeCompleto;
    public String email;
    public String matricula;
    public String tipo_vinculo;
    public String senha;
    public String campus;
    public String foto;
    public String curso;
    public User(){}
    protected User(Parcel in) {
        nome = in.readString();
        nomeCompleto= in.readString();
        email = in.readString();
        matricula = in.readString();
        tipo_vinculo = in.readString();
        senha = in.readString();
        campus = in.readString();
        foto= in.readString();
        curso = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    @Override
    public int compareTo(@NonNull User o) {
        return this.nome.compareTo(o.nome);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nome);
        parcel.writeString(nomeCompleto);
        parcel.writeString(email);
        parcel.writeString(matricula);
        parcel.writeString(tipo_vinculo);
        parcel.writeString(senha);
        parcel.writeString(campus);
        parcel.writeString(foto);
        parcel.writeString(curso);
    }
}
