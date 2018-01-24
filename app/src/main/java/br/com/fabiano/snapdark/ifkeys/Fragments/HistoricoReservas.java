package br.com.fabiano.snapdark.ifkeys.Fragments;

/**
 * Created by Fabiano on 09/03/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class HistoricoReservas extends Fragment {
    private FastScrollRecyclerView recyclerView;
    private AdapterHisReserva adapterHisReserva;
    private List<Reserva> reservas;
    private LinearLayout llNoData;
    private boolean pagination = false;
    private FrameLayout frameLayout;
    private Toolbar toolbar;
    View view;
    private DrawerLayout drawerLayout;
    private boolean btnClicked = false;
    String link = null;
    private Activity activity;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public HistoricoReservas(){}
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_historico, container, false);
        view.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {}});
        activity = getActivity();
        instance(view);
        if (savedInstanceState != null){
            reservas = savedInstanceState.getParcelableArrayList("reservas");
        }
        if (savedInstanceState != null && reservas.size() > 0){
            adapterHisReserva = new AdapterHisReserva(getActivity(), reservas);

            recyclerView.setAdapter(adapterHisReserva);
            setOK();
        }else{
            initItems();
        }
        return view;
    }
    private void instance(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorSecond,
                R.color.orange);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AlertUtil.showAlert(Toast.makeText(getActivity(),getString(R.string.atualizado),
                        Toast.LENGTH_LONG),getActivity());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.historico));
        llNoData =  view.findViewById(R.id.llNoData);
        llNoData.setVisibility(View.GONE);

        recyclerView = (FastScrollRecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        frameLayout = view.findViewById(R.id.flProgressBar);
        frameLayout.setVisibility(View.GONE);
        ((AppCompatActivity)activity).setSupportActionBar(toolbar);
        ((ProgressBar)view.findViewById(R.id.progressBar)).getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        drawerLayout= (DrawerLayout) activity.findViewById(R.id.drawerLayout);
        ((Main)activity).toggle = new ActionBarDrawerToggle(activity,drawerLayout, toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(((Main)activity).toggle);
        ((Main)activity).toggle.syncState();
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
    public void paganation(){
        if (Control.isOnline(getContext()) && reservas.size() >= 10){
            if (!pagination){
                pagination = true;
                link = Control.URL_SEVER+"/ifkeys/reservaApi.php";
                AlertUtil.log("linkPag",link);
                reflesh(true);

                FormBody.Builder formBuilder = new FormBody.Builder()
                        .add("limit","10");
                formBuilder.add("index", reservas.size()+"");
                formBuilder.add("operation","2");
                formBuilder.add("key","-k^(CA>lU!j[Xc#");
                formBuilder.add("usuario",((Main)activity).logged.matricula);


                RequestBody formBody = formBuilder.build();

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(link).tag(Constants.REQUEST)
                        .post(formBody)
                        .build();

                StaticValues.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        call.cancel();
                        reflesh(false);
                    }

                    @Override
                    public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                        try{
                            final String r = response.body().string();
                            try {
                                reflesh(false);
                                JSONObject object = new JSONObject(r);
                                if (object.getBoolean("success")){
                                    if (AlertUtil.isRunning(getActivity())){
                                        List<Reserva> ls = Control.getReservas(r);
                                        for (int i = 0; i < ls.size(); i++) {
                                            adapterHisReserva.add(adapterHisReserva.getItemCount(),ls.get(i));
                                        }
                                        ls.clear();
                                        ls = null;
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
        }
    }

    public void initItems(){
        llNoData.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        if (Control.isOnline(getContext())){
            link = Control.URL_SEVER+"/ifkeys/reservaApi.php";

            FormBody.Builder formBuilder = new FormBody.Builder()
                    .add("limit","10");
            formBuilder.add("index","0");
            formBuilder.add("operation","2");
            formBuilder.add("key","-k^(CA>lU!j[Xc#");
            formBuilder.add("usuario",((Main)activity).logged.matricula);

            RequestBody formBody = formBuilder.build();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(link).tag(Constants.REQUEST)
                    .post(formBody)
                    .build();

            StaticValues.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    showErroWifi(getResources().getString(R.string.conexao_ruim));
                }

                @Override
                public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                    try{
                        final String r = response.body().string();
                        AlertUtil.log("responseMus",r+"");
                        try {
                            JSONObject object = new JSONObject(r);
                            if (object.getBoolean("success")){
                                if (AlertUtil.isRunning(getActivity())){
                                    reservas = Control.getReservas(r);
                                    if (reservas.size() == 0){
                                        showErroWifi(getString(R.string.nada_encontrado));
                                    }else{
                                        AlertUtil.log("responseMus",reservas.size()+"");
                                        adapterHisReserva = new AdapterHisReserva(getActivity(), reservas);
                                        recyclerView.setAdapter(adapterHisReserva);
                                        setOK();
                                    }
                                }
                            }else {
                                showErroWifi(getString(R.string.nada_encontrado));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            });
        }
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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            if (context != null){
                activity = (FragmentActivity) context;
            }
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        if (reservas != null){
            reservas.clear();
        }
        recyclerView.setAdapter(null);
        Control.unbindDrawables(view);
        view = null;
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("reservas", (ArrayList<? extends Parcelable>) reservas);
        super.onSaveInstanceState(outState);
    }
}


