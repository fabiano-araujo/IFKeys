package br.com.fabiano.snapdark.ifkeys.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

//import com.mopub.mobileads.MoPubView;
;import br.com.fabiano.snapdark.ifkeys.Main;
import br.com.fabiano.snapdark.ifkeys.R;
import br.com.fabiano.snapdark.ifkeys.menus.MenuSalas;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.Control;

public class TiposSalas extends Fragment {
    private TabLayout tabLayout;
    private ViewPager vpMainMenu;
    private MenuSalas myPagerMenuMain;
    private Toolbar toolbar;
    private FragmentActivity activity;
    private DrawerLayout drawerLayout;
    //private MoPubView moPubView;

    View view;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.maim_fragment, null, false);
        view.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {}});
        activity = getActivity();
        instance(view);
        /*moPubView = (MoPubView) view.findViewById(R.id.moPubView);
        moPubView.setAdUnitId("276a4c15670c4c7e84b616b519503380");
        moPubView.loadAd();*/
        tabLayout.addTab(tabLayout.newTab().setText("Disponíveis"));
        tabLayout.addTab(tabLayout.newTab().setText("Ocupadas"));

        myPagerMenuMain = new MenuSalas(getChildFragmentManager(),tabLayout.getTabCount());
        vpMainMenu.setAdapter(myPagerMenuMain);


        vpMainMenu.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpMainMenu.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        return view;
    }

    private void instance(View view) {
        vpMainMenu =view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tlMainMenu);
        tabLayout.setVisibility(View.VISIBLE);


        toolbar = view.findViewById(R.id.tbMainMenu);
        toolbar.setTitle("Salas");

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        drawerLayout= (DrawerLayout) activity.findViewById(R.id.drawerLayout);
        ((Main)activity).toggle = new ActionBarDrawerToggle(activity,drawerLayout, toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(((Main)activity).toggle);
        ((Main)activity).toggle.syncState();
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
    public void onDestroyView() {
        Control.unbindDrawables(view);
        view = null;
        super.onDestroyView();
    }
}

