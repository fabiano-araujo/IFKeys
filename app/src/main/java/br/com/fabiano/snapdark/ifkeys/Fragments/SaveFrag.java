package br.com.fabiano.snapdark.ifkeys.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;
import java.util.List;

public class SaveFrag extends Fragment {
    public LongSparseArray<List> lists = new LongSparseArray<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}

