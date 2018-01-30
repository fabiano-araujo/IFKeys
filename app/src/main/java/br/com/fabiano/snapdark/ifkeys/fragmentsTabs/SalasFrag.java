package br.com.fabiano.snapdark.ifkeys.fragmentsTabs;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.fabiano.snapdark.ifkeys.Main;
import br.com.fabiano.snapdark.ifkeys.R;
import br.com.fabiano.snapdark.ifkeys.model.Reserva;
import br.com.fabiano.snapdark.ifkeys.model.Sala;
import br.com.fabiano.snapdark.ifkeys.utils.Constants;
import br.com.fabiano.snapdark.ifkeys.utils.adapters_rv.AdapterHisReserva;
import br.com.fabiano.snapdark.ifkeys.utils.adapters_rv.AdapterSalas;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.AlertUtil;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.Control;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.StaticValues;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class SalasFrag extends Fragment {
    private FastScrollRecyclerView recyclerView;
    private List<Sala> salas = null;
    private Button btnTryAgain;
    private FrameLayout frameLayout;
    private String conexao_ruim;
    LinearLayout llNoData;
    View view;
    String link = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean btnClicked = false;
    private boolean pagination = false;
    private AdapterSalas adapterSalas;
    private AdapterHisReserva adapterHisReserva;
    private long tipoSala = -1;
    private List<Reserva> reservadas;

    public SalasFrag(){}
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.salas, container, false);
        conexao_ruim = getResources().getString(R.string.conexao_ruim);
        if (tipoSala == Constants.SALAS_DISPONIVEIS){
            salas = new ArrayList<>();
        }else{
            reservadas = new ArrayList<>();
        }
        if (getArguments() != null){
            tipoSala = getArguments().getLong("matricula",-1);
        }
        if (savedInstanceState != null){
            salas = ((Main)getActivity()).getSaveFragment().lists.get(tipoSala);
        }
        instance();
        if (savedInstanceState != null && salas.size() > 0){
            /*adapterTop = new AdapterTop(getActivity(), salas,null,tab);
            recyclerView.setAdapter(adapterTop);*/
            setOK();
        }else{
            onItemSelected();
        }
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClicked = true;
                onItemSelected();
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (tipoSala == Constants.SALAS_DISPONIVEIS){
                    if (salas.size() == llm.findLastCompletelyVisibleItemPosition()+1){
                        paganation();
                    }
                }else{
                    if (reservadas.size() == llm.findLastCompletelyVisibleItemPosition()+1){
                        paganation();
                    }
                }
            }
        });

        return view;
    }

    private void instance() {
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        llNoData = view.findViewById(R.id.llNoData);
        btnTryAgain = view.findViewById(R.id.btnTryAgain);
        btnTryAgain.setVisibility(View.VISIBLE);
        frameLayout = view.findViewById(R.id.flProgressBar);
        ((ProgressBar)view.findViewById(R.id.progressBar)).getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        //frameLayout.setVisibility(View.GONE);

        recyclerView =view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setSmoothScrollbarEnabled(true);

        recyclerView.setLayoutManager(llm);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorSecond,
                R.color.orange);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initItems();
            }
        });
    }
    public int getSizeList(){
        if (tipoSala == Constants.SALAS_DISPONIVEIS){
            return salas.size();
        }else{
            return reservadas.size();
        }
    }
    public void paganation(){
        if (Control.isOnline(getContext()) && getSizeList() >= 10){
            if (!pagination){
                AlertUtil.log("reflesh","true");
                pagination = true;
                link = Control.URL_SEVER+"/ifkeys/salaApi.php";
                AlertUtil.log("linkPag",link);
                reflesh(true);

                FormBody.Builder formBuilder = new FormBody.Builder()
                        .add("limit","10");
                formBuilder.add("index", salas.size()+"");
                formBuilder.add("operation","2");
                formBuilder.add("key","-k^(CA>lU!j[Xc#");
                formBuilder.add("campus",((Main)getActivity()).logged.campus);

                if (tipoSala != -1){
                    if (tipoSala == Constants.SALAS_DISPONIVEIS){
                        formBuilder.add("disponivel","true");
                    }else{
                        formBuilder.add("disponivel","false");
                    }
                }

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
                        reflesh(false);
                    }

                    @Override
                    public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                        try{
                            final String r = response.body().string();
                            try {
                                AlertUtil.log("reflesh","false");
                                reflesh(false);
                                JSONObject object = new JSONObject(r);
                                if (object.getBoolean("success")){
                                    if (AlertUtil.isRunning(getActivity())){
                                        if (tipoSala == Constants.SALAS_DISPONIVEIS) {
                                            List<Sala> ls = Control.getSalas(r);
                                            for (int i = 0; i < ls.size(); i++) {
                                                adapterSalas.add(adapterSalas.getItemCount(),ls.get(i));
                                            }
                                            ls.clear();
                                            ls = null;
                                        } else {
                                            List<Reserva> ls = Control.getReservas(r);
                                            for (int i = 0; i < ls.size(); i++) {
                                                adapterHisReserva.add(adapterHisReserva.getItemCount(),ls.get(i));
                                            }
                                            ls.clear();
                                            ls = null;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }catch (Exception e){e.printStackTrace();}
                    }
                });
                pagination = false;
            }
        }else{
            reflesh(false);
        }
    }

    private void reflesh(final boolean show) {
        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(show);
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    public void onItemSelected(){
        /*llNoData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);*/
        try{
            if (Control.isOnline(getContext())) {
                initItems();
            }else{
                showErroWifi(getString(R.string.desconectado));
            }
        }catch (Exception e){e.printStackTrace();}
    }
    public void setOK(){
        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    frameLayout.setVisibility(View.GONE);
                    llNoData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }
    public void showErroWifi(final String mensage){
        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try{
                        llNoData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.GONE);
                        TextView txtMensage = llNoData.findViewById(R.id.txtMensage);
                        txtMensage.setText(mensage);
                        if (btnClicked){
                            AlertUtil.showAlert(Toast.makeText(getActivity(),getResources().
                                    getString(R.string.desconectado), Toast.LENGTH_LONG),getActivity());
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void initItems(){
        llNoData.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        if (Control.isOnline(getContext())){
            link = Control.URL_SEVER+"/ifkeys/salaApi.php";

            FormBody.Builder formBuilder = new FormBody.Builder()
                    .add("limit","10");
            formBuilder.add("index","0");
            formBuilder.add("operation","2");
            formBuilder.add("key","-k^(CA>lU!j[Xc#");
            formBuilder.add("campus","jc");

            if (tipoSala == Constants.SALAS_DISPONIVEIS){
                formBuilder.add("disponivel","true");
            }else{
                formBuilder.add("disponivel","false");
            }

            RequestBody formBody = formBuilder.build();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(link).tag(Constants.REQUEST)
                    .post(formBody)
                    .build();

            StaticValues.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    showErroWifi(conexao_ruim);
                }

                @Override
                public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                    try {
                        final String r = response.body().string();
                        AlertUtil.log("responseMus", r + "");
                        try {
                            JSONObject object = new JSONObject(r);
                            if (object.getBoolean("success")) {
                                if (AlertUtil.isRunning(getActivity())) {
                                    if (tipoSala == Constants.SALAS_DISPONIVEIS) {
                                        setSalas(r);
                                    } else {
                                        setReservadas(r);
                                    }
                                }
                            } else {
                                showErroWifi(getString(R.string.nada_encontrado));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void setSalas(String r) {
        salas = Control.getSalas(r);
        if (salas.size() == 0){
            if (tipoSala != -1){
                showErroWifi("Nenhuma sala!");
                btnTryAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }else{
                showErroWifi(getString(R.string.nada_encontrado));
            }
        }else{
            adapterSalas = new AdapterSalas(getActivity(), salas);
            recyclerView.setAdapter(adapterSalas);
            setOK();
        }
    }

    public void setReservadas(String r) {
        reservadas = Control.getReservas(r);
        if (reservadas.size() == 0){
            if (tipoSala != -1){
                showErroWifi("Nenhuma sala!");
                btnTryAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }else{
                showErroWifi(getString(R.string.nada_encontrado));
            }
        }else{
            adapterHisReserva = new AdapterHisReserva(getActivity(), reservadas);
            recyclerView.setAdapter(adapterHisReserva);
            setOK();
        }
    }
    @Override
    public void onDestroyView() {
        Control.unbindDrawables(view);
        view = null;
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ((Main)getActivity()).getSaveFragment().lists.get(tipoSala,salas);
    }
}

