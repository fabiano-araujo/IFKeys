package br.com.fabiano.snapdark.ifkeys.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import br.com.fabiano.snapdark.ifkeys.model.Sala;
import br.com.fabiano.snapdark.ifkeys.utils.Constants;
import br.com.fabiano.snapdark.ifkeys.utils.FragControl;

public class SalaFrag extends Fragment {
    private Toolbar toolbar;
    private Button btnReservar;
    private ImageView imageView;
    private TextView txtTitle, txtNumero, txtEstado,txtCampus;
    View view;
    private int position = 0;
    private Activity activity;
    private Sala sala;


    public SalaFrag(){}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sala, container, false);
        view.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {}});
        activity = getActivity();

        Bundle bundle = getArguments();
        sala = bundle.getParcelable("sala");
        getIntance(view);

        if (!sala.disponivel){
            btnReservar.setText("Ocupada");
            btnReservar.setEnabled(false);
        }
        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*((Main)getActivity()).itemClicado(-1, Constants.ADD_PRODUTO,
                        FragControl.getADDProdutoFrag(sala.id),true,false,false);*/
            }
        });
        txtTitle.setText("N°"+sala.numero+". "+sala.nome);
        txtNumero.setText("Número: "+sala.numero);
        txtCampus.setText(sala.campus);
        if (sala.disponivel){
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

