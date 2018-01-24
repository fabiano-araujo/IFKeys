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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

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


public class AddLoja extends Fragment {

    Button button;
    EditText editNome,edtEmail,edtTele,edtSenha;
    TextInputLayout iptNome,iptEmail,iptTele,iptSenha;
    Toolbar toolbar;
    View view;
    private DrawerLayout drawerLayout;
    private Activity activity;
    LinearLayout llRoot;

    public AddLoja(){}
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cadastrar, container, false);
        view.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {}});
        activity = getActivity();
        instance();
        final User user = new User();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage("Carregando...");
                AlertUtil.showAlert(progressDialog,activity);
                if (Control.check(iptNome,editNome,1,getActivity().getString(R.string.digite_seu_comentario)) &&
                        Control.isValidEmailAddress(edtEmail,iptEmail) &&
                        Control.check(iptTele,edtTele,1,"Digite seu Telefone!") &&
                        Control.check(iptSenha,edtSenha,5,"A senha tem que ter no mínimo 6 caracteres!") ){
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    formBuilder.add("operation","0");
                    formBuilder.add("key","-k^(CA>lU!j[Xc#");
                    user.nome = editNome.getText().toString();
                    user.email = edtEmail.getText().toString();
                    user.campus = edtTele.getText().toString();
                    user.senha = edtSenha.getText().toString();
                    user.matricula = "Comum";

                    formBuilder.add("nome",user.nome);
                    formBuilder.add("email",user.email);
                    formBuilder.add("campus",user.campus);
                    formBuilder.add("senha",user.senha);
                    formBuilder.add("matricula","Comum");

                    RequestBody formBody = formBuilder.build();

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(Control.URL_SEVER+"/ecomerce/usuarioDAO.php").tag(Constants.REQUEST)
                            .post(formBody)
                            .build();

                    StaticValues.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            call.cancel();
                            try {
                                AlertUtil.dismissWithCheck(progressDialog);
                                AlertUtil.showAlert(Snackbar.make(llRoot,getString(R.string.desconectado),
                                        Snackbar.LENGTH_LONG),activity);
                            }catch (Exception e1){e1.printStackTrace();}
                        }

                        @Override
                        public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                            try{
                                final String r = response.body().string();
                                try{
                                    AlertUtil.dismissWithCheck(progressDialog);
                                    JSONObject object = new JSONObject(r);
                                   // user.id = object.getInt("id_user");
                                    ((Main)activity).logged = user;
                                    ((Main)activity).getHeader();
                                    AlertUtil.showAlert(Toast.makeText(activity,"Cadastrado com sucesso!", Toast.LENGTH_LONG),activity);
                                    ((Main)activity).popupItemBackState(Constants.LOGIN);
                                    ((Main)activity).onBackPressed();
                                }catch (Exception e){
                                    e.printStackTrace();
                                    JSONObject object = new JSONObject(r);
                                    if (object.getString("error").equals("0")){
                                        AlertUtil.showAlert(Snackbar.make(llRoot,getString(R.string.email_usado),
                                                Snackbar.LENGTH_LONG),activity);
                                    }else{
                                        AlertUtil.showAlert(Snackbar.make(llRoot,getString(R.string.um_erro_desconhecido),
                                                Snackbar.LENGTH_LONG),activity);
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                AlertUtil.showAlert(Snackbar.make(llRoot,getString(R.string.um_erro_desconhecido),
                                        Snackbar.LENGTH_LONG),activity);
                            }
                        }
                    });
                }
            }
        });
        return view;
    }

    private void instance() {
        llRoot = view.findViewById(R.id.llRoot);
        button = (Button) view.findViewById(R.id.btnEntrar);
        editNome = (EditText) view.findViewById(R.id.edtNome);
        iptNome = (TextInputLayout) view.findViewById(R.id.iptNome);

        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        iptEmail = (TextInputLayout) view.findViewById(R.id.iptEmail);

        edtTele= (EditText) view.findViewById(R.id.edtTelefone);
        iptTele = (TextInputLayout) view.findViewById(R.id.iptTelefone);

        edtSenha= (EditText) view.findViewById(R.id.edtSenha);
        iptSenha = (TextInputLayout) view.findViewById(R.id.iptSenha);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        toolbar.setTitle(getString(R.string.cadastrar));
        ((AppCompatActivity)activity).setSupportActionBar(toolbar);
        getActivity().findViewById(R.id.flProgressBar).setVisibility(View.GONE);

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

