    package br.com.fabiano.snapdark.ifkeys.menus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.fabiano.snapdark.ifkeys.fragmentsTabs.SalasFrag;
import br.com.fabiano.snapdark.ifkeys.utils.Constants;


    public class MenuSalas extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public MenuSalas(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        SalasFrag salasFrag = new SalasFrag();
        switch (position) {
            case 0:
                bundle.putLong("matricula", Constants.SALAS_DISPONIVEIS);
                salasFrag.setArguments(bundle);
                return salasFrag;
            case 1:
                bundle.putLong("matricula", Constants.SALAS_OCUPADAS);
                salasFrag.setArguments(bundle);
                return salasFrag;

            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
