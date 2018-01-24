package br.com.fabiano.snapdark.ifkeys;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import br.com.fabiano.snapdark.ifkeys.Fragments.Enviar;
import br.com.fabiano.snapdark.ifkeys.Fragments.HistoricoReservas;
import br.com.fabiano.snapdark.ifkeys.Fragments.Login;
import br.com.fabiano.snapdark.ifkeys.Fragments.TiposSalas;
import br.com.fabiano.snapdark.ifkeys.Fragments.SaveFrag;
import br.com.fabiano.snapdark.ifkeys.Fragments.SobreFrag;
import br.com.fabiano.snapdark.ifkeys.model.User;
import br.com.fabiano.snapdark.ifkeys.utils.Constants;
import br.com.fabiano.snapdark.ifkeys.utils.FragControl;
import br.com.fabiano.snapdark.ifkeys.utils.GlideApp;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.AlertUtil;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.Control;
import br.com.fabiano.snapdark.ifkeys.utils.helpers.StaticValues;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    public CoordinatorLayout coordinatorLayout;
    public FragmentTransaction transaction;
    int itemSelect;
    FragmentManager fm;
    private FrameLayout fragment_conteudo;
    public ArrayList<String> tagsInBackStage = new ArrayList<>();
    //public Configuracao configuracao;
    public boolean initBottomSheet = false;
    public boolean pausado = false;
    private String pathImg = null;
    public WeakReference<Main> mainIsRunning;
    public ContentResolver contentResolver;
    public SaveFrag saveFrag;
    public User logged = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
        mainIsRunning = new WeakReference<Main>(this);
        contentResolver = getContentResolver();


        fm = getSupportFragmentManager();
        getColors();
        instance();

        if (savedInstanceState != null) {
            logged = savedInstanceState.getParcelable("logged");
            initBottomSheet = savedInstanceState.getBoolean("initBottomSheet");
            tagsInBackStage = savedInstanceState.getStringArrayList("tags");
            itemSelect = savedInstanceState.getInt("itemSelect");
        }

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.itemSalas).setChecked(true);

        if (savedInstanceState == null) {
            itemSelect = Constants.TIPOS_SALA;
            if (getIntent().getIntExtra("itemSelect", -1) != -1) {
                itemSelect = getIntent().getIntExtra("itemSelect", -1);

            }/* else if (Control.isOnline(this)) {
                itemClicado(R.id.itemHome, itemSelect, new MainFragment(), false, true, false);
            }*/
            itemClicado(R.id.itemSalas, itemSelect, new TiposSalas(), false, true, false);
        }

        boolean alarmeAtivo = (PendingIntent.getBroadcast(this, 0, new Intent("ALARME_WHATLISTEN"), PendingIntent.FLAG_NO_CREATE) == null);
        if (alarmeAtivo) {
            Intent itAlarm = new Intent("ALARME_WHATLISTEN");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, itAlarm, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.HOUR, 3);
            AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarme.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        getHeader();
    }

    View vHead;
    public void getHeader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                navigationView.removeHeaderView(vHead);
                vHead = getLayoutInflater().inflate(R.layout.drawer_header,null);
                LinearLayout llItemUser = vHead.findViewById(R.id.llItemUser);
                Button btnEntrar = vHead.findViewById(R.id.btnEntrar);
                if(logged == null){
                    btnEntrar.setVisibility(View.VISIBLE);
                    llItemUser.setVisibility(View.GONE);
                    btnEntrar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemClicado(-1, Constants.LOGIN,new Login(),true,false,true);
                        }
                    });
                }else {
                    btnEntrar.setVisibility(View.GONE);
                    llItemUser.setVisibility(View.VISIBLE);
                    TextView txtNome = vHead.findViewById(R.id.txtNome);
                    TextView txtEMail = vHead.findViewById(R.id.txtEmail);
                    txtEMail.setText(logged.matricula);
                    txtNome.setText(logged.nome);
                    ImageView ivUser = vHead.findViewById(R.id.ivUser);

                    GlideApp.with(Main.this).load(logged.foto).placeholder(R.drawable.background).thumbnail( 0.5f )
                            .format(DecodeFormat.PREFER_RGB_565)
                            .skipMemoryCache(true)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerCrop().into(ivUser);
                }
                navigationView.addHeaderView(vHead);
            }
        });
    }

    private void getColors() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
    }
    public SaveFrag getSaveFragment(){
        if (saveFrag == null){
            saveFrag = (SaveFrag) fm.findFragmentByTag("data");
            // create the fragment and data the first time
            if (saveFrag == null) {
                // add the fragment
                saveFrag = new SaveFrag();
                fm.beginTransaction().add(saveFrag,"data").commit();
                // load the data from the web
            }
        }
        return saveFrag;
    }
    public void itemClicado(final int menuItem, final int itemSelect, final Fragment fragment, final boolean addToBackStage,
                            final boolean replace, final boolean clearTop){
        drawerLayout.closeDrawer(GravityCompat.START);

        int time = 0;
        if (menuItem != -1){
            time = 265;
        }
        StaticValues.getInstance().getDurationHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                transaction = fm.beginTransaction();
                if (menuItem != -1){
                    try{
                        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }catch (Exception e){e.printStackTrace();}
                    Main.this.itemSelect = itemSelect;
                    if (menuItem != R.id.itemSobre && menuItem != R.id.itemReportar){
                        navigationView.getMenu().findItem(menuItem).setChecked(true);
                    }
                }
                if(clearTop){
                    popupItemBackState(itemSelect);
                }
                if (replace){
                    transaction.replace(R.id.fragment_conteudo, fragment,itemSelect+"");
                }else{
                    transaction.add(R.id.fragment_conteudo, fragment,itemSelect+"");
                }
                if (addToBackStage){
                    transaction.addToBackStack(itemSelect+"");
                    AlertUtil.log("addToBackStage","true");
                    if (tagsInBackStage == null){
                        tagsInBackStage = new ArrayList<>();
                    }
                    tagsInBackStage.add(itemSelect+"");
                }
                transaction.commitAllowingStateLoss();
            }
        },time);
    }
    public void popupItemBackState(int itemSelect){
        try{
            if (tagsInBackStage != null){
                int positionToClearTop = tagsInBackStage.indexOf(itemSelect+"");
                if (positionToClearTop != -1){
                    for (int i = positionToClearTop; i < tagsInBackStage.size(); i++) {
                        fm.popBackStack (tagsInBackStage.get(i), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    for (int i = positionToClearTop; i < tagsInBackStage.size(); i++) {
                        tagsInBackStage.remove(i);
                    }
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.itemSalas:
                itemClicado(R.id.itemSalas, Constants.TIPOS_SALA,new TiposSalas(),false,true,false);
                break;
            case R.id.itemHistorico:
                itemClicado(R.id.itemHistorico, Constants.HISTORICO,new HistoricoReservas(),false,true,false);
                break;
            case R.id.itemSobre:
                itemClicado(R.id.itemSobre, Constants.SOBRE,new SobreFrag(),false,true,false);
                break;
            case R.id.itemConfigurar:
                /*itemClicado(R.id.itemConfigurar, Constants.CONFIFIGURACAOFRAG,new Configuracoes(),false,true,false);*/
                break;
            case R.id.itemShare:
                Control.share(323,"",getResources(),Main.this);
                break;
            case R.id.itemReportar:
                itemClicado(R.id.itemReportar, Constants.REPORTAR,new Enviar(),false,true,false);
                break;
            /*case R.id.itemEntrar:
                Control.start(this,Login.class,drawerLayout);
                break;*/
            case R.id.item_ad:
                /*Intent itt = new Intent(Main.this,Oferrwall.class);
                startActivity(itt);*/
                /*if (mInterstitial.isLoaded()){
                    mInterstitial.show();
                }else{
                    mInterstitial.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            mInterstitial.show();
                        }
                    });
                }*/
                break;
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*if (requestCode == Constants.PERMITION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                try{
                    if (permissions[i].equalsIgnoreCase(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED){

                    }
                }catch (Exception e){e.printStackTrace();}
                try{
                    if (permissions[i].equalsIgnoreCase(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_PICK)
                                .setType("image*//*"), "Selecione uma imagem"), ImageUtil.IMAGEM_INTERNA);
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        }else if (requestCode == Constants.PERMITION_WRITE_UPDATE){
            for (int i = 0; i < permissions.length; i++) {
                try{
                    if (permissions[i].equalsIgnoreCase(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        //Control.verificarPorAtualizacao(this,true);
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        }*/
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void instance(){
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        fragment_conteudo = (FrameLayout) findViewById(R.id.fragment_conteudo);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_serach:
                itemClicado(-1, Constants.SEARCHFRAG, FragControl.getSearchFrag(),true,false,true);
                break;
        }
        if (toggle == null || toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (fm == null){
            fm = getSupportFragmentManager();
        }
        if (fm.getBackStackEntryCount() == 0){
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
                alBuilder.setMessage(getString(R.string.deseja_sair)).setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(tagsInBackStage != null){
                            tagsInBackStage.clear();
                        }
                        Control.cancelAllTags(StaticValues.getInstance().getOkHttpClient());
                        clearLists();
                        Main.super.onBackPressed();
                    }
                }).setNegativeButton(getString(R.string.nao),null).setTitle(getString(R.string.sair));
                AlertUtil.showAlert(alBuilder,this);
            }
        }else{
            if (tagsInBackStage != null){
                if (tagsInBackStage.size() > 0){
                    String tag = tagsInBackStage.get(tagsInBackStage.size()-1);
                    fm.popBackStack (tag,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    tagsInBackStage.remove(tagsInBackStage.size()-1);
                }
            }
        }
        //bottomSheet.setMusicControlVisibility(true);
        //findViewById(R.id.llAdmobBottom).setVisibility(View.VISIBLE);
    }

    private void clearLists() {
        SaveFrag saveFrag = getSaveFragment();
        saveFrag.lists.clear();
        try {
            fm.popBackStack ("data", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }catch (Exception e){e.printStackTrace();}
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            outState.putParcelable("logged",logged);
            outState.putStringArrayList("tags",tagsInBackStage);
            outState.putInt("itemSelect",itemSelect);
            outState.putBoolean("initBottomSheet",initBottomSheet);
            super.onSaveInstanceState(outState);
        }catch (Exception e){e.printStackTrace();}
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        try {
            if (resultCode == RESULT_OK){
                /*if(requestCode == ImageUtil.IMAGEM_INTERNA) {
                    AlertUtil.Log("IMAGEM_INTERNA","IMAGEM_INTERNA");
                    FotoFrag fotoFrag = (FotoFrag) getSupportFragmentManager().findFragmentByTag(Constants.FOTO_FRAG + "");
                    fotoFrag.addFoto(ImageUtil.getRealPathFromURI(this, intent.getData()));
                }*/
            }
        }catch (Exception e){
            AlertUtil.showAlert(Snackbar.make(fragment_conteudo, getString(R.string.um_erro_desconhecido)
                    , Snackbar.LENGTH_LONG),this);
            e.printStackTrace();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        pausado = false;
    }
    @Override
    public void onPause() {
        /*try{
            PlayerFrag player = (PlayerFrag) getSupportFragmentManager().findFragmentByTag(Control.PLAYERFRAG + "");
            player.player.pause();
        }catch (Exception e){e.printStackTrace();}*/
        pausado = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        try{
            super.onResume();
            pausado = false;
        }catch (Exception e){}
    }
    @Override
    public void onDestroy() {
        pausado = true;
        try {
            AlertUtil.dismissWithCheck(AlertUtil.progressDialog);
            AlertUtil.dismissWithCheck(AlertUtil.alertDialog);
        }catch (Exception e){}
        try{
            mainIsRunning.clear();
            mainIsRunning = null;
        }catch (Exception e){e.printStackTrace();}
        super.onDestroy();
    }
    @Override
    public void onStop() {
        super.onStop();
        pausado = true;
    }
}