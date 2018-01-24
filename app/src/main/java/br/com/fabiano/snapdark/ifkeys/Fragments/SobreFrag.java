package br.com.fabiano.snapdark.ifkeys.Fragments;

/**
 * Created by Fabiano on 09/03/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.fabiano.snapdark.ifkeys.Main;
import br.com.fabiano.snapdark.ifkeys.R;

public class SobreFrag extends Fragment {
    public Button btnAtualizar;
    public Toolbar toolbar;
    View view;
    private FragmentActivity activity;

    public SobreFrag(){}
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_sobre, container, false);
        view.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {}});
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        toolbar.setTitle(getString(R.string.sobre));
        ((AppCompatActivity)activity).setSupportActionBar(toolbar);


        DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawerLayout);
        ((Main)activity).toggle = new ActionBarDrawerToggle(activity,drawerLayout, toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(((Main)activity).toggle);
        ((Main)activity).toggle.syncState();

        TextView txtVersao = (TextView) view.findViewById(R.id.txtVersao);
        try {
            txtVersao.setText(txtVersao.getText()+" "+getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return view;
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
}


