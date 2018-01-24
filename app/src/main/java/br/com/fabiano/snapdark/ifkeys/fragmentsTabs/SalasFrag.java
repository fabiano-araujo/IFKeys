package br.com.fabiano.snapdark.ifkeys.fragmentsTabs;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import br.com.fabiano.snapdark.ifkeys.model.Sala;
import br.com.fabiano.snapdark.ifkeys.utils.Constants;
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
    private List<Sala> salas = new ArrayList<>();
    private Button btnTryAgain;
    private FrameLayout frameLayout;
    private String conexao_ruim;
    LinearLayout llNoData;
    private LinearLayout llTab;
    View view;
    private Sala sala;
    String link = null;
    private Toolbar toolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean btnClicked = false;
    private boolean pagination = false;
    private AdapterSalas adapterSalas;
    private long tipoSala = -1;

    public SalasFrag(){}
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lojas, container, false);
        conexao_ruim = getResources().getString(R.string.conexao_ruim);
        if (getArguments() != null){
            sala = getArguments().getParcelable("sala");
            tipoSala = getArguments().getLong("matricula",-1);
        }
        if (savedInstanceState != null){
            salas = ((Main)getActivity()).getSaveFragment().lists.get(tipoSala);
        }
        instance();
        if (getArguments().getBoolean("showToolbar")){
            toolbar.setVisibility(View.VISIBLE);
        }
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
                if (salas.size() == llm.findLastCompletelyVisibleItemPosition()+1){
                    paganation();
                }
            }
        });

        return view;
    }

    private void instance() {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("SalasFrag");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Main)getActivity()).onBackPressed();
            }
        });

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        llNoData = view.findViewById(R.id.llNoData);
        btnTryAgain = view.findViewById(R.id.btnTryAgain);
        btnTryAgain.setVisibility(View.VISIBLE);
        llTab = view.findViewById(R.id.llTab);
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
    public void paganation(){
        if (Control.isOnline(getContext()) && salas.size() >= 10){
            if (!pagination){
                pagination = true;
                link = Control.URL_SEVER+"/ifkeys/salaApi.php";
                AlertUtil.log("linkPag",link);
                reflesh(true);

                FormBody.Builder formBuilder = new FormBody.Builder()
                        .add("limit","10");
                formBuilder.add("index", salas.size()+"");
                formBuilder.add("operation","2");
                formBuilder.add("key","-k^(CA>lU!j[Xc#");
                formBuilder.add("campus","jc");

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
                                        List<Sala> ls = Control.getSalas(r);
                                        for (int i = 0; i < ls.size(); i++) {
                                            adapterSalas.add(adapterSalas.getItemCount(),ls.get(i));
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
                    try{
                        final String r = response.body().string();
                        AlertUtil.log("responseMus",r+"");
                        try {
                            JSONObject object = new JSONObject(r);
                            if (object.getBoolean("success")){
                                if (AlertUtil.isRunning(getActivity())){
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

