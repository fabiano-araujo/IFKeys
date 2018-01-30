package br.com.fabiano.snapdark.ifkeys.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.fabiano.snapdark.ifkeys.Main;
import br.com.fabiano.snapdark.ifkeys.R;
import br.com.fabiano.snapdark.ifkeys.model.Reserva;
import br.com.fabiano.snapdark.ifkeys.utils.Constants;
import br.com.fabiano.snapdark.ifkeys.utils.FragControl;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.AlertUtil;

public class ReservaFrag extends Fragment {
    private Toolbar toolbar;
    private Button btnReservar;
    private ImageView imageView;
    private TextView txtTitle, txtNumero, txtEstado,txtCampus, txtReservado, txtEntregue,txtPor;
    View view;
    private Activity activity;
    private Reserva reserva;


    public ReservaFrag(){}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reserva, container, false);
        view.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {}});
        activity = getActivity();

        Bundle bundle = getArguments();
        reserva = bundle.getParcelable("reserva");
        getIntance(view);

        if (!reserva.sala.disponivel){
            btnReservar.setText("Ocupada");
            btnReservar.setEnabled(false);
        }
        if (reserva.usuario.equals(((Main)activity).logged.matricula)){
            if (reserva.confirmada){
                if (!reserva.entregue){
                    btnReservar.setText("Entregar");
                    btnReservar.setOnClickListener(null);
                    btnReservar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }else {
                btnReservar.setText("Solicitada");
                btnReservar.setOnClickListener(null);
                btnReservar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder cancel = new AlertDialog.Builder(activity);
                        cancel.setMessage("Cancelar solicitação?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("Não",null);
                        AlertUtil.showAlert(cancel,activity);
                    }
                });
            }
        }
        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*((Main)getActivity()).itemClicado(-1, Constants.ADD_PRODUTO,
                        FragControl.getADDProdutoFrag(reserva.id),true,false,false);*/
            }
        });

        txtTitle.setText("N°"+ reserva.sala.numero+". "+ reserva.sala.nome);
        txtNumero.setText("Número: "+ reserva.sala.numero);
        txtCampus.setText(reserva.sala.campus);
        txtReservado.setText("Reservado em: "+reserva.dt_start);
        txtEntregue.setText("Entregue em: "+reserva.dt_end);
        if (((Main)activity).logged.matricula.equals(reserva.usuario)){
            txtPor.setVisibility(View.GONE);
        }
        txtPor.setText("Reservado por: "+reserva.usuario);

        if (reserva.sala.disponivel){
            txtEstado.setText("Disponível");
        }else{
            txtEstado.setText("Ocupada");
        }
        return view;
    }
    public void getIntance(View view){
        txtCampus = view.findViewById(R.id.txtCampus);
        txtEstado = view.findViewById(R.id.txtEstado);
        txtNumero = view.findViewById(R.id.txtNumero);
        txtTitle = view.findViewById(R.id.txtSala);

        txtReservado = view.findViewById(R.id.txtReservado);
        txtEntregue = view.findViewById(R.id.txtEntregue);
        txtPor = view.findViewById(R.id.txtPor);

        toolbar = (Toolbar) view.findViewById(R.id.tbDetalhes);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Main)getActivity()).onBackPressed();
            }
        });
        imageView = (ImageView) view.findViewById(R.id.ivImage);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemAddFav:
                        break;
                    case R.id.item_serach:
                        ((Main)activity).itemClicado(-1, Constants.SEARCHFRAG, FragControl.getSearchFrag(),true,false,true);
                        break;

                }
                return true;
            }
        });
        btnReservar = view.findViewById(R.id.btnReservar);
    }
}

