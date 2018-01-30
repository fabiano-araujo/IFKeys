package br.com.fabiano.snapdark.ifkeys.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import br.com.fabiano.snapdark.ifkeys.Main;
import br.com.fabiano.snapdark.ifkeys.R;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.AlertUtil;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.Control;

public class Enviar extends Fragment{
    RadioGroup radioGroup;
    RadioButton radioButtonChecked;
    Button button;
    String versao;
    EditText editText;
    TextInputLayout textInputLayout;
    Toolbar toolbar;
    View view;
    private DrawerLayout drawerLayout;
    private Activity activity;

    public Enviar(){}
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.enviar, container, false);
        view.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {}});
        activity = getActivity();
        setRetainInstance(true);
        button = (Button) view.findViewById(R.id.btnEntrar);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio);
        editText = (EditText) view.findViewById(R.id.edtComentario);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayout);
        toolbar = (Toolbar) view.findViewById(R.id.tbMainMenu);


        toolbar.setTitle(getString(R.string.reportar));
        ((AppCompatActivity)activity).setSupportActionBar(toolbar);

        drawerLayout= (DrawerLayout) activity.findViewById(R.id.drawerLayout);
        ((Main)activity).toggle = new ActionBarDrawerToggle(activity,drawerLayout, toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(((Main)activity).toggle);
        ((Main)activity).toggle.syncState();

        versao = null;
        try {
             versao = getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Control.check(textInputLayout,editText,1,getActivity().getString(R.string.digite_seu_comentario))){
                    radioButtonChecked = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"developerapps2017@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, radioButtonChecked.getText());
                    String comentario = editText.getText().toString();
                    comentario+= "\n ---------------------------------\n " +
                            "app :"+versao+"\n"+
                            "Android: "+ Build.VERSION.RELEASE+" \n " +
                            "Api: "+ Build.VERSION.SDK_INT;
                    i.putExtra(Intent.EXTRA_TEXT ,comentario);
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        AlertUtil.showAlert(Toast.makeText(getActivity(), "There are no email clients installed."
                                , Toast.LENGTH_SHORT),activity);
                    }
                }
            }
        });
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

