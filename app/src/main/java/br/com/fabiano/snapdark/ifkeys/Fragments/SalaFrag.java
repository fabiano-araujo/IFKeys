package br.com.fabiano.snapdark.ifkeys.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

import br.com.fabiano.snapdark.ifkeys.Main;
import br.com.fabiano.snapdark.ifkeys.R;
import br.com.fabiano.snapdark.ifkeys.model.RangeTimePickerDialog;
import br.com.fabiano.snapdark.ifkeys.model.Reserva;
import br.com.fabiano.snapdark.ifkeys.model.Sala;
import br.com.fabiano.snapdark.ifkeys.utils.Constants;
import br.com.fabiano.snapdark.ifkeys.utils.FragControl;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.AlertUtil;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.Control;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.StaticValues;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class SalaFrag extends Fragment {
    private Toolbar toolbar;
    private Button btnReservar;
    private TextView txtTitle, txtNumero, txtEstado,txtCampus;
    View view;
    private Activity activity;
    private Sala sala;
    private boolean qrcode = false;


    public SalaFrag(){}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sala, container, false);
        view.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {}});
        activity = getActivity();
        Bundle bundle = getArguments();
        sala = bundle.getParcelable("sala");
        qrcode = bundle.getBoolean("qrcode");
        getIntance(view);

        AlertUtil.log("base64",Base64.encodeToString((sala.id+";,"+sala.nome+";,"+sala.numero+";,"+sala.campus).getBytes(),Base64.DEFAULT));
        if (!sala.disponivel){
            btnReservar.setText("Ocupada");
            btnReservar.setEnabled(false);
        }
        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                solicitar();
                                AlertUtil.log("data",hourOfDay + ":" + minute);
                            }
                        },hour,minute,true
                );
                dpd.setMinTime(new Timepoint(hour,minute));
                dpd.setMaxTime(new Timepoint(23,0));
                dpd.setTitle("Hora prevista de entrega");
                dpd.show(activity.getFragmentManager(),"TimePickerDialog");
            }
        });

        txtTitle.setText("N°"+sala.numero+". "+sala.nome);
        txtNumero.setText("Número: "+sala.numero);
        txtCampus.setText(sala.campus);
        if (qrcode){
            txtEstado.setText("Verificando...");
        }else{
            if (sala.disponivel){
                txtEstado.setText("Disponível");
            }else{
                txtEstado.setText("Ocupada");
            }
        }
        return view;
    }

    private void solicitar() {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Reservando...");
        AlertUtil.showAlert(progressDialog,activity);

        String link = Control.URL_SEVER+"/ifkeys/reservaApi.php";

        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("operation","0");
        formBuilder.add("key","-k^(CA>lU!j[Xc#");


        RequestBody formBody = formBuilder.build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(link).tag(Constants.REQUEST)
                .post(formBody)
                .build();

        StaticValues.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                AlertUtil.log("reflesh","false");
                AlertUtil.dismissWithCheck(progressDialog);
            }

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                try{
                    final String r = response.body().string();
                    try {
                        AlertUtil.dismissWithCheck(progressDialog);
                        JSONObject object = new JSONObject(r);
                        if (object.getBoolean("success")){
                            if (AlertUtil.isRunning(getActivity())){

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
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

