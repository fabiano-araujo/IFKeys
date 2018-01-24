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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import br.com.fabiano.snapdark.ifkeys.Main;
import br.com.fabiano.snapdark.ifkeys.R;
import br.com.fabiano.snapdark.ifkeys.model.Produto;
import br.com.fabiano.snapdark.ifkeys.model.User;
import br.com.fabiano.snapdark.ifkeys.utils.Constants;
import br.com.fabiano.snapdark.ifkeys.utils.FragControl;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.AlertUtil;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.Control;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.StaticValues;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;

;

public class AddProduto extends Fragment {

    Button button;
    EditText editNome, edtPreco, edtDesc, edtEstoque;
    TextInputLayout iptNome, iptPreco, iptDesc, iptEstoque;
    AppCompatSpinner spnCondicao,spnCategoria,spnUnidade;
    Toolbar toolbar;
    View view;
    private DrawerLayout drawerLayout;
    private Activity activity;
    LinearLayout llRoot;
    int condicao= 0, id_loja = -1;
    String categoria,unidade;
    public AddProduto(){}
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_produto, container, false);
        view.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {}});
        activity = getActivity();
        instance();
        final User user = new User();

        spnCondicao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                condicao = spnCondicao.getSelectedItemPosition();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spnCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = spnCategoria.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spnUnidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unidade = spnUnidade.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage("Carregando...");
                AlertUtil.showAlert(progressDialog,activity);
                if (Control.check(iptNome,editNome,0,"Digite o título do produto!") &&
                        Control.check(iptPreco, edtPreco,0,"Digite o preço do produto!") &&
                        Control.check(iptDesc, edtDesc,10,"Digite uma descrição maior!") &&
                        Control.check(iptEstoque, edtEstoque,0,"Qual é o estoque do produto?") ){
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    formBuilder.add("operation","0");
                    formBuilder.add("key","-k^(CA>lU!j[Xc#");
                    formBuilder.add("nome", editNome.getText().toString());
                    formBuilder.add("preco",edtPreco.getText().toString());
                    formBuilder.add("descricao",edtDesc.getText().toString());
                    formBuilder.add("estoque",edtEstoque.getText().toString());
                    formBuilder.add("estado",condicao+"");
                    formBuilder.add("categoria",categoria+"");
                    formBuilder.add("unidade",unidade+"");
                    formBuilder.add("id_loja",id_loja+"");

                    RequestBody formBody = formBuilder.build();

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(Control.URL_SEVER+"/ecomerce/ProdutoDAO.php").tag(Constants.REQUEST)
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
                                AlertUtil.log("responseMus",r+"");
                                try{
                                    AlertUtil.dismissWithCheck(progressDialog);
                                    JSONObject object = new JSONObject(r);
                                    Produto produto = new Produto();
                                    produto.id = object.getInt("id_produto");
                                    FragControl.getProdutoFrag(produto);
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
                }else{
                    AlertUtil.dismissWithCheck(progressDialog);
                }
            }
        });
        return view;
    }

    private void instance() {
        llRoot = view.findViewById(R.id.llRoot);
        spnCategoria = view.findViewById(R.id.spnCategoria);
        spnCondicao = view.findViewById(R.id.spnCondicao);
        spnUnidade = view.findViewById(R.id.spnUnidade);

        ArrayAdapter<String> adapterCondicao = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item);
        adapterCondicao.add("Novo");
        adapterCondicao.add("Usado");
        adapterCondicao.add(activity.getString(R.string.nao_carregar));
        adapterCondicao.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spnCondicao.setAdapter(adapterCondicao);

        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item);
        adapterCategoria.add("Alimentos");
        adapterCategoria.add("Brinquedos");
        adapterCategoria.add("Eletrônicos");
        adapterCategoria.add("Games");
        adapterCategoria.add("Informática");
        adapterCategoria.add("Livros");
        adapterCategoria.add("Serviços");
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spnCategoria.setAdapter(adapterCategoria);

        ArrayAdapter<String> adapterUni = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item);
        adapterUni.add("Unidades");
        adapterUni.add("Kg");
        adapterUni.add("L");
        adapterUni.add("cm");
        adapterUni.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spnUnidade.setAdapter(adapterUni);


        button = (Button) view.findViewById(R.id.btnAdd);
        editNome = (EditText) view.findViewById(R.id.edtNome);
        iptNome = (TextInputLayout) view.findViewById(R.id.iptNome);

        edtPreco = (EditText) view.findViewById(R.id.edtPreco);
        iptPreco = (TextInputLayout) view.findViewById(R.id.iptPreco);

        edtDesc = (EditText) view.findViewById(R.id.edtDesc);
        iptDesc = (TextInputLayout) view.findViewById(R.id.iptDesc);

        edtEstoque = (EditText) view.findViewById(R.id.edtEstoque);
        iptEstoque = (TextInputLayout) view.findViewById(R.id.iptEstoque);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        toolbar.setTitle("Cadastrar novo produto");
        ((AppCompatActivity)activity).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Main)getActivity()).onBackPressed();
            }
        });
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

