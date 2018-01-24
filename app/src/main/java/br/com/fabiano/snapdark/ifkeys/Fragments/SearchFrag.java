package br.com.fabiano.snapdark.ifkeys.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import br.com.fabiano.snapdark.ifkeys.Main;
import br.com.fabiano.snapdark.ifkeys.R;
import br.com.fabiano.snapdark.ifkeys.utils.Constants;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.AlertUtil;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.Control;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.StaticValues;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;

public class SearchFrag extends Fragment {
    private String[] partes;
    private FrameLayout flProgress;
    private LinearLayout llNada;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    String search = "";
    private Handler durationHandler = new Handler();
    private int position;
    private ImageView ivSearchAudio;
    View view;
    String result = "";
    private Activity activity;
    SearchView searchView;
    private ScrollView svSuggest,svNoWifi;
    private LinearLayout llSuggest;
    private int cNet = 0;

    public SearchFrag(){}
    private ViewPager pager;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        setRetainInstance(true);

        view = inflater.inflate(R.layout.activity_search, container, false);
        view.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {}});
        instance(view);
        searchView.setSubmitButtonEnabled(false);

        if (savedInstanceState != null){
            /*search = savedInstanceState.getString("search");
            partes = savedInstanceState.getStringArray("partes");
            musicas = savedInstanceState.getParcelableArrayList("todasAsMusicas");*/
            //setViewPager();
        }
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        durationHandler.postDelayed(pesquisar, 50);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void instance(View view) {
        ivSearchAudio = view.findViewById(R.id.ivSearchAudio);
        llSuggest = view.findViewById(R.id.llSuggest);
        svSuggest = view.findViewById(R.id.svSuggest);
        svNoWifi = view.findViewById(R.id.svNoWifi);
        tabLayout = view.findViewById(R.id.tlSearch);
        flProgress= view.findViewById(R.id.flProgressBar);
        flProgress.setVisibility(View.GONE);
        llNada = view.findViewById(R.id.llNoData);
        pager = view.findViewById(R.id.pager);
        toolbar = view.findViewById(R.id.tbSearch);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (activity).onBackPressed();
                Control.hideSoftKeyboard(activity);
            }
        });

        searchView = view.findViewById(R.id.search);
// Sets searchable configuration defined in searchable.xml for this SearchView
        SearchManager searchManager =
                (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                svSuggest.setVisibility(View.GONE);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Control.cancelCallWithTag(StaticValues.getInstance().getOkHttpClient(),"search");
                AlertUtil.log("onQueryText",query+"");
                naoPesquise = true;
                svSuggest.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                Control.hideSoftKeyboard(activity);
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                AlertUtil.log("onQueryText",query+"");
                return true;
            }
        });
        searchView.setIconified(false);
    }
    private boolean naoPesquise = false;
    private boolean stopSearch = false;
    private Runnable pesquisar = new Runnable() {
        public void run() {
            final String txt = searchView.getQuery().toString().trim();
            if (!txt.equals(search) && txt.length() > 0){
                search = txt;
                if (!naoPesquise){
                    try{
                        okhttp3.Request request = new okhttp3.Request.Builder()
                        /*        .url("https://suggestqueries.google.com/complete/search?client=firefox&q="
                                        +Control.getLinkValid(search)).tag("search")*/
                                .cacheControl(new CacheControl.Builder().noCache().build())
                                .build();

                        StaticValues.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                call.cancel();
                            }

                            @Override
                            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                                try{
                                    final String output = response.body().string();
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                String out = output.replace("]","").replace("[","").replace("\"","");
                                                partes = out.split(",");
                                                setSuggest(partes);
                                            }catch (Exception e){e.printStackTrace();}
                                        }
                                    });

                                }catch (Exception e){e.printStackTrace();}
                            }
                        });
                    }catch (Exception e){e.printStackTrace();}
                }
            }
            naoPesquise = false;
            if (!stopSearch){
                durationHandler.postDelayed(this, 1300);
            }
        }
    };
    public void setSuggest(String[] partes){
        if (partes.length > 1){
            llSuggest.removeAllViews();
            for (int i = 1; i < partes.length; i++) {
                View view = activity.getLayoutInflater().inflate(R.layout.item_pesquisa,null);
                final TextView txtTitle = (TextView) view.findViewById(R.id.txtSearch);
                txtTitle.setText(partes[i]);
                ImageView ivComplete = (ImageView) view.findViewById(R.id.ivComplete);
                ivComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchView.setQuery(txtTitle.getText().toString(),false);
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchView.setQuery(txtTitle.getText().toString(),true);
                        svSuggest.setVisibility(View.GONE);
                        naoPesquise = true;
                    }
                });
                llSuggest.addView(view);
            }
            svSuggest.setVisibility(View.VISIBLE);
        }else {
            svSuggest.setVisibility(View.GONE);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            if (context != null){
                activity = (Activity)context;
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*if (requestCode == Constants.PERMITION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE)
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    search(search);
                }
            }
        }*/
    }
    public void search(final String q){
        try {
            if (Control.isOnline(activity)){
                if (q.trim().length() > 0){
                    pager.setVisibility(View.GONE);
                    llNada.setVisibility(View.GONE);
                    flProgress.setVisibility(View.VISIBLE);
                    String link;

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("")
                            .cacheControl(new CacheControl.Builder().noCache().build())
                            .build();

                    StaticValues.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            call.cancel();
                            try{
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            if (cNet < 2){
                                                search(q);
                                            }else{
                                                svNoWifi.setVisibility(View.VISIBLE);
                                                flProgress.setVisibility(View.GONE);
                                                //setViewPager();
                                            }
                                            cNet++;
                                        }catch (Exception e1){e1.printStackTrace();}
                                    }
                                });
                            }catch (Exception e1){e.printStackTrace();}
                        }

                        @Override
                        public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                            try{
                                result = response.body().string();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{

                                            tabLayout.setVisibility(View.VISIBLE);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            //setViewPager();
                                        }
                                    }
                                });

                            }catch (Exception e){e.printStackTrace();}
                        }
                    });
                }
            }else{
                //setViewPager();
            }
        }catch (Exception e){e.printStackTrace();}
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSearch = true;
    }
}

