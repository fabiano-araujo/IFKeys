package br.com.fabiano.snapdark.ifkeys.utils.adapters_rv;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.List;

import br.com.fabiano.snapdark.ifkeys.Main;
import br.com.fabiano.snapdark.ifkeys.R;
import br.com.fabiano.snapdark.ifkeys.model.Reserva;
import br.com.fabiano.snapdark.ifkeys.utils.Constants;
import br.com.fabiano.snapdark.ifkeys.utils.FragControl;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.AlertUtil;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.Control;

/**
 * Created by fabiano on 14/11/15.
 */
public class AdapterHisReserva extends RecyclerView.Adapter<AdapterHisReserva.MyViewHolder> implements FastScrollRecyclerView.SectionedAdapter {
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    public boolean playlist = false;
    private List<Reserva> reservas;
    public AdapterHisReserva(Activity mContext, List<Reserva> reservas) {
        this.mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = mContext;
        this.reservas = reservas;
    }


     @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
         View view = null;
         MyViewHolder holder = null;
         switch (viewType) {
             case 0:
                 AlertUtil.log("MyViewHolder","1");
                 view = mLayoutInflater.inflate(R.layout.item_reserva, viewGroup, false);
                 holder =  new MyViewHolder(view);
                 break;
             case 1:
                 AlertUtil.log("MyViewHolder","2");
                 view = mLayoutInflater.inflate(R.layout.item_reserva_left, viewGroup, false);
                 holder =  new MyViewHolder(view);
                 break;
         }
         return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        final Reserva reserva = reservas.get(position);
        myViewHolder.txtTitle.setText(reserva.sala.nome);
        if(reserva.sala.disponivel){
            myViewHolder.txtSubTitle.setText("Disponível");
        }else {
            myViewHolder.txtSubTitle.setText("Ocupada");
        }
        myViewHolder.txtFirst.setText(reserva.sala.numero+"");


        myViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            ((Main)mContext).itemClicado(-1, Constants.SALA,
                    FragControl.getReservaFrag(reserva),true,false,false);

            }
        });
        //setOnLongClick(position,myViewHolder.view,reserva);
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public List getList() {
         return reservas;
    }

    public void add(int position,Reserva reserva){
        reservas.add(position, reserva);
        notifyItemInserted(position);
        /*notifyItemRangeChanged(position, cardList.size());
        notifyDataSetChanged();*/
    }
    public void remove(int position){
        reservas.remove(position);
        notifyItemRemoved(position);
        /*notifyItemRangeChanged(position, cardList.size());
        notifyDataSetChanged();*/
    }
    @Override
    public int getItemViewType(int position) {
        if (position%2 == 0){
            return 0;
        }else{
            return 1;
        }
    }

    public String getSectionName(int position) {
        String show = "#";
        try{
            show = reservas.get(position).sala.nome.substring(0, 1);
            Integer.parseInt(show);
            return "#";
        }catch (Exception e){e.printStackTrace();}
        if (!Character.isLetter(show.charAt(0))){
            return "#";
        }
        return Control.removerAcentos(show);
    }
    public class MyViewHolder2 extends RecyclerView.ViewHolder{
        public Toolbar toolbar;
        public View view;
        public TextView txtTitle;
        public TextView txtSubTitle;
        public TextView txtFirst;

        public MyViewHolder2(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtSubTitle = (TextView) itemView.findViewById(R.id.txtSubTitle);
            txtFirst = (TextView) itemView.findViewById(R.id.txtFistLetter);
            view = itemView;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public Toolbar toolbar;
        public View view;
        public TextView txtTitle;
        public TextView txtSubTitle;
        public TextView txtFirst;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtSubTitle = (TextView) itemView.findViewById(R.id.txtSubTitle);
            txtFirst = (TextView) itemView.findViewById(R.id.txtFistLetter);
            view = itemView;
        }
    }
}


