package br.com.fabiano.snapdark.ifkeys.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import br.com.fabiano.snapdark.ifkeys.Main;
import br.com.fabiano.snapdark.ifkeys.R;
import br.com.fabiano.snapdark.ifkeys.model.User;
import br.com.fabiano.snapdark.ifkeys.utils.Constants;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.AlertUtil;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.Control;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.StaticValues;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Login extends Fragment {
    Button button,btnCadastrar;
    EditText editEmail,edtSenha;
    TextInputLayout iptEmail,iptSenha;
    Toolbar toolbar;
    View view;
    private DrawerLayout drawerLayout;
    private Activity activity;
    LinearLayout llRoot;
    boolean erro = false;
    User user;
    public Login(){}
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_login, container, false);
        view.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {}});
        activity = getActivity();
        instance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Control.check(iptEmail,editEmail,0,"Digite sua matrícula!") &&
                        Control.check(iptSenha,edtSenha,5,"A senha tem que ter no mínimo 6 caracteres!") ){
                    final ProgressDialog progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("Carregando...");
                    AlertUtil.showAlert(progressDialog,activity);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                FormBody.Builder formBuilder = new FormBody.Builder();
                                formBuilder.add("username",editEmail.getText().toString());
                                formBuilder.add("password",edtSenha.getText().toString());

                                RequestBody formBody = formBuilder.build();


                                okhttp3.Request request = new okhttp3.Request.Builder()
                                        .url("https://suap.ifrn.edu.br/api/v2/autenticacao/token/").tag(Constants.REQUEST)
                                        .post(formBody)
                                        .build();

                                Response response = StaticValues.getInstance().getOkHttpClient().newCall(request).execute();

                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String token = jsonObject.getString("token");


                                request = new okhttp3.Request.Builder()
                                        .url("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-dados/")
                                        .tag(Constants.REQUEST)
                                        .addHeader("Authorization", "JWT "+token)
                                        .build();

                                response = StaticValues.getInstance().getOkHttpClient().newCall(request).execute();
                                String r = response.body().string();
                                user = Control.getUser(r);
                                Log.i("resultado",user.nome+" "+user.matricula);
                            }catch (Exception e){
                                e.printStackTrace();
                                erro = true;
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertUtil.dismissWithCheck(progressDialog);
                                    if (erro){
                                        AlertUtil.showAlert(Snackbar.make(llRoot,getString(R.string.email_or_senha_incorretos),
                                                Snackbar.LENGTH_LONG),activity);
                                    }else{
                                        ((Main)activity).logged = user;
                                        Control.hideSoftKeyboard(activity);
                                        ((Main)activity).getHeader();
                                        ((Main)activity).onBackPressed();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        return view;
    }

    private void instance() {
        btnCadastrar = view.findViewById(R.id.btnCadastrar);
        llRoot = view.findViewById(R.id.llRoot);
        button = (Button) view.findViewById(R.id.btnEntrar);
        editEmail = view.findViewById(R.id.edtEmail);
        iptEmail = view.findViewById(R.id.iptEmail);

        edtSenha = view.findViewById(R.id.edtSenha);
        iptSenha = view.findViewById(R.id.iptSenha);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Entrar");
        ((AppCompatActivity)activity).setSupportActionBar(toolbar);

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
        Control.unbindDrawables(view.findViewById(R.id.llRoot));
        super.onDestroyView();
    }
}

