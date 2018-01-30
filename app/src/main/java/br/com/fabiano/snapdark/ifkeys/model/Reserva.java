package br.com.fabiano.snapdark.ifkeys.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fabiano on 23/01/2018.
 */

public class Reserva implements Parcelable {
    public long id_reserva;
    public Sala sala;
    public String dt_start;
    public String dt_end;
    public String usuario;
    public boolean confirmada;
    public boolean entregue;

    public Reserva(){}

    protected Reserva(Parcel in) {
        id_reserva = in.readLong();
        sala = in.readParcelable(Sala.class.getClassLoader());
        dt_start = in.readString();
        dt_end = in.readString();
        usuario = in.readString();
        confirmada = in.readByte() != 0;
        entregue = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id_reserva);
        dest.writeParcelable(sala, flags);
        dest.writeString(dt_start);
        dest.writeString(dt_end);
        dest.writeString(usuario);
        dest.writeByte((byte) (confirmada ? 1 : 0));
        dest.writeByte((byte) (entregue ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reserva> CREATOR = new Creator<Reserva>() {
        @Override
        public Reserva createFromParcel(Parcel in) {
            return new Reserva(in);
        }

        @Override
        public Reserva[] newArray(int size) {
            return new Reserva[size];
        }
    };
}
