package br.com.fabiano.snapdark.ifkeys.utils.helpers;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.com.fabiano.snapdark.ifkeys.R;
import br.com.fabiano.snapdark.ifkeys.model.Reserva;
import br.com.fabiano.snapdark.ifkeys.model.Sala;
import br.com.fabiano.snapdark.ifkeys.model.User;
import br.com.fabiano.snapdark.ifkeys.utils.GlideApp;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by Fabiano on 28/07/2016.
 */
public class Control {
    //public static final String URL_SEVER = "http://192.168.1.101";
    //public static final String URL_SEVER = "http://whatlisten.890m.com";
    //public static final String URL_SEVER = "http://192.168.1.127";
    public static final String URL_SEVER = "http://snapdark.com";

    public static Uri shareAPK(String bundle_id, Activity activity) {
        Uri apkURI = null;
        try{
            File f1;
            File f2 = null;

            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            final List pkgAppsList = activity.getPackageManager().queryIntentActivities(mainIntent, 0);
            int z = 0;
            for (Object object : pkgAppsList) {

                ResolveInfo info = (ResolveInfo) object;
                if (info.activityInfo.packageName.equals(bundle_id)) {

                    f1 = new File(info.activityInfo.applicationInfo.publicSourceDir);
                    try {

                        String file_name = info.loadLabel(activity.getPackageManager()).toString();
                        Log.d("file_name--", " " + file_name);

                        f2 = new File(Environment.getExternalStorageDirectory().toString() + "/sala/apk");
                        f2.mkdirs();
                        f2 = new File(f2.getPath() + "/" + file_name + ".apk");
                        f2.createNewFile();

                        InputStream in = new FileInputStream(f1);

                        OutputStream out = new FileOutputStream(f2);

                        // byte[] buf = new byte[1024];
                        byte[] buf = new byte[4096];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                        System.out.println("File copied.");
                    } catch (FileNotFoundException ex) {
                        System.out.println(ex.getMessage() + " in the specified directory.");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                apkURI = FileProvider.getUriForFile(
                        activity, activity.getApplicationContext().getPackageName() + ".provider", f2);
            }else {
                apkURI = Uri.fromFile(f2);
            }
        }catch (Exception e){e.printStackTrace();}
        return apkURI;
    }

    public static Intent getFacebookPageIntent(Context context) {
        Intent browserIntent;
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;

            boolean activated =  packageManager.getApplicationInfo("com.facebook.katana", 0).enabled;
            if(activated){
                if ((versionCode >= 3002850)) {
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=https://m.facebook.com/AppWhatlisten"));
                    return browserIntent;
                } else {
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/BljfWj7RqNp"));
                    return browserIntent;
                }
            }else{
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/AppWhatlisten"));
                return browserIntent;
            }
        } catch (PackageManager.NameNotFoundException e) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/AppWhatlisten"));
            return browserIntent;
        }
    }
    /*static boolean atualizado = true;
    public static boolean verificarPorAtualizacao(final Activity activity, final boolean taAtualizado){
        if (permitions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE,activity.getString(
                R.string.storage_mensage_permition_player), Constants.PERMITION_WRITE_UPDATE)){
            ProgressDialog progressDialog = null;
            if (taAtualizado){
                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage(activity.getString(R.string.carregando));
                AlertUtil.showAlert(progressDialog,activity);
            }
            final ProgressDialog finalProgressDialog = progressDialog;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final int currentCode = activity.getPackageManager()
                                .getPackageInfo(activity.getPackageName(), 0).versionCode;
                        HttpConnections http = new HttpConnections();
                        String r = http.getVersao2("https://drive.google.com/file/d/0B9zG9zCc73pLZ25mT1pOLUhZOE0/edit");

                        final int versionServer = Integer.parseInt(r);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(AlertUtil.isRunning(activity)){
                                    if (finalProgressDialog != null && !activity.isFinishing()){
                                        //finalProgressDialog.dismiss();
                                        AlertUtil.dismissWithCheck(finalProgressDialog);
                                    }
                                    AlertDialog.Builder alBuilder = new AlertDialog.Builder(activity);
                                    if (currentCode < versionServer){
                                        atualizado = false;
                                        alBuilder.setMessage(activity.getString(R.string.versao_desatualizada))
                                                .setNegativeButton(activity.getString(R.string.agora_nao), null)
                                                .setPositiveButton(activity.getString(R.string.atualizar),
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                Intent it = new Intent(activity,UpdateService.class);
                                                                it.putExtra("update",true);
                                                                it.putExtra("version",versionServer);
                                                                activity.startService(it);
                                                            }
                                                        }).setTitle(activity.getString(R.string.atualizar)+" app");
                                        AlertUtil.showAlert(alBuilder,activity);
                                        atualizado = false;
                                    }else {
                                        if(!(activity).isFinishing() && taAtualizado){
                                            alBuilder.setMessage(activity.getString(R.string.atualizado))
                                                    .setPositiveButton(activity.getString(R.string.ok),null);
                                            AlertUtil.showAlert(alBuilder,activity);
                                        }
                                    }
                                }
                            }
                        });
                        http = null;
                    } catch (Exception e) {
                        try {
                            AlertUtil.showAlert(Toast.makeText(activity,activity.getString(R.string.
                                    um_erro_desconhecido),Toast.LENGTH_LONG),activity);
                            if ((finalProgressDialog != null) && finalProgressDialog.isShowing()){
                                //finalProgressDialog.dismiss();
                                AlertUtil.dismissWithCheck(finalProgressDialog);
                            }
                        }catch (Exception e1){}
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return atualizado;
    }*/
    public static void cancelAllTags(OkHttpClient okHttpClient){
        Control.cancelCallWithTag(okHttpClient,null);
        Control.cancelCallWithTag(okHttpClient,"search");
    }
    public static void cancelCallWithTag(OkHttpClient client, String tag) {
        try {
            // A call may transition from queue -> running. Remove queued Calls first.
            for(Call call : client.dispatcher().queuedCalls()) {
                if(call.request().tag().equals(tag))
                    call.cancel();
            }
            for(Call call : client.dispatcher().runningCalls()) {
                if(call.request().tag().equals(tag))
                    call.cancel();
            }
        }catch (Exception e){e.printStackTrace();}
    }
    public static void getAlbumart(Long album_id,ImageView imageView,Fragment mContext) {
        try {
            final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            final Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
            GlideApp.with(mContext).load(uri).placeholder(R.drawable.background)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop().into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setFoto(final String foto, final ImageView imageView, final Fragment mContext, int quem){
        AlertUtil.log("qualidadeMethod"," "+foto);
        try{
            try {
                long idFoto = Long.valueOf(foto);
                getAlbumart(idFoto,imageView,mContext);
            }catch (Exception e){
                if (foto.contains("https://i.ytimg.com")){
                    ViewTreeObserver vto = imageView.getViewTreeObserver();
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        public boolean onPreDraw() {
                            imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                            try{
                                int size = (int) (imageView.getMeasuredWidth());
                                if (size == 0){
                                    size = 200;
                                }
                                GlideApp.with(mContext).load(foto).placeholder(R.drawable.background).thumbnail( 0.5f )
                                        .format(DecodeFormat.PREFER_RGB_565)
                                        .skipMemoryCache(true)
                                        .dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                        .override(size,size)
                                        .centerCrop().into(imageView);
                            }catch (Exception e1){e1.printStackTrace();}
                            return true;
                        }
                    });
                }else if(foto.startsWith("http")){
                    GlideApp.with(mContext).load(foto).placeholder(R.drawable.background).thumbnail( 0.5f )
                            .format(DecodeFormat.PREFER_RGB_565)
                            .skipMemoryCache(true)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerCrop().into(imageView);
                }else{
                    GlideApp.with(mContext).load(new File(foto)).placeholder(R.drawable.background).thumbnail( 0.5f )
                            .format(DecodeFormat.PREFER_RGB_565)
                            .skipMemoryCache(true)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerCrop().into(imageView);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static boolean isValidEmailAddress(EditText editText, TextInputLayout textInputLayout) {
        String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

        boolean isValid = editText.getText().toString().matches(EMAIL_REGEX);
        if (isValid){
            textInputLayout.setErrorEnabled(false);
            return true;
        }else{
            textInputLayout.setError("Email inválido!");
            editText.requestFocus();
            return false;
        }
    }
    public static boolean check(TextInputLayout textInputLayout, EditText editText, int condicao, String mensage){
        boolean isValid = true;
        if(editText.getText().toString().trim().length() <= condicao){
            textInputLayout.setError(mensage);
            editText.requestFocus();
            isValid = false;
        }else{
            textInputLayout.setErrorEnabled(false);
        }
        return  isValid;
    }
    public static Bitmap getBitmapFromURL(String src) {
        InputStream input = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(src);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            Bitmap myBitmap = ImageUtil.decodeSampledBitmapFromResourceMemOpt(input,260,300);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }finally {
            try {
                input.close();
                connection.disconnect();
                input = null;
                connection = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File moverArquivo(File arquivo){
        File diretorioDestino = new File(Environment.getExternalStorageDirectory().getPath()+"/Whatlisten/temp");

        // Move o arquivo para o novo diretorio
        File novo = new File(diretorioDestino, arquivo.getName());
        boolean sucesso = arquivo.renameTo(novo);
        if (sucesso) {
            arquivo.delete();
            System.out.println("Arquivo movido para '" + diretorioDestino.getAbsolutePath() + "'");
        } else {
            System.out.println("Erro ao mover arquivo '" + arquivo.getAbsolutePath() + "' para '"
                    + diretorioDestino.getAbsolutePath() + "'");
        }
        return novo;
    }

    public static void scan(File out, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(out);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        } else {
            context.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }
    }
    public static Drawable setColorIcon(Drawable drawable, int color){
        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        return drawable;
    }

    public static boolean isOnline(Context context) {
        try{
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }catch (Exception e){
            return false;
        }
    }
    public static String removeEntre(String s, String start, String last){
        try{
            int iStart = s.lastIndexOf(start);
            if (iStart != -1){
                int iLast = s.lastIndexOf(last);
                if (iLast != -1){
                    String init = s.substring(0,iStart);
                    String fim = "";
                    try{
                        fim = s.substring((iLast+1),s.length());
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    //AlertUtil.Log("removeYout",s+" || "+init+" || "+fim+" || "+iLast+" || "+s.length());
                    s = init+fim;
                }
            }
        }catch (Exception e){e.printStackTrace();}
        return  s;
    }
    public static AlertDialog.Builder alert(Context context, String mensagem){
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(context);
        alBuilder.setMessage(mensagem).setNeutralButton("Okey",null);
        AlertUtil.showAlert(alBuilder,context);
        return alBuilder;
    }

    public static User getUser(String result) throws JSONException {
        User logged = new User();
        JSONObject object = new JSONObject(result);
        //logged.versao = object.getString("versao");
        logged.nome = object.getString("nome_usual");
        logged.matricula = object.getString("matricula");
        logged.email = object.getString("email");
        logged.foto = "https://suap.ifrn.edu.br"+object.getString("url_foto_75x100");
        logged.tipo_vinculo = object.getString("tipo_vinculo");
        try {
            object = object.getJSONObject("vinculo");
            logged.nomeCompleto = object.getString("nome");
            logged.curso = object.getString("curso");
            logged.campus = object.getString("campus");
        }catch (Exception e){}
        return logged;
    }
    public static boolean permitions(final Activity context, final String permition, String mensage, int PERMITION_CODE){
        //Manifest.permission.ACCESS_FINE_LOCATION
        if(ContextCompat.checkSelfPermission(context,permition) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,permition)){
                //permiçao já foi negada
                if(!((Activity) context).isFinishing()) {
                    AlertDialog.Builder alBuilder = new AlertDialog.Builder(context);
                    alBuilder.setMessage(mensage).setTitle("Permição").setNegativeButton("Cancelar",null).setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(context,new String[]{permition},1);
                        }
                    });
                    AlertUtil.showAlert(alBuilder,context);
                }
            }else{
                //primeira vez
                ActivityCompat.requestPermissions(context,new String[]{permition},PERMITION_CODE);
            }
            return false;
        }else{
            //já tem permição
            return true;
        }
    }


    public static String removeUrl(String commentstr) {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
            commentstr = commentstr.replaceAll(m.group(i),"").trim();
            i++;
        }
        return commentstr.replaceAll(" ","%20");
    }
    public static void start(Context context, Class classe, DrawerLayout drawerLayout){
        drawerLayout.closeDrawer(GravityCompat.START);
        if (!context.getClass().getName().equals(classe.getName())){
            context.startActivity(new Intent(context, classe));
            ((Activity)context).finish();
        }
    }
    public static void share(int code, String mensage, Resources resources, Activity activity) {
        try {
            if (mensage.equals("")){
                mensage = resources.getString(R.string.compartilhar_mensage);
            }
            Intent minhaIntent = new Intent();
            minhaIntent.setAction(Intent.ACTION_SEND);
            minhaIntent.putExtra(Intent.EXTRA_SUBJECT,resources.getString(R.string.melhor_app));

            String link = "http://whatlisten.uptodown.com/android";

            minhaIntent.putExtra(Intent.EXTRA_TEXT, mensage+"\n"+resources.getString(R.string.baixe_aqui)+": "+link+"\n #whatlistenApp");
            minhaIntent.setType("text/plain");

            activity.startActivityForResult(Intent.createChooser(minhaIntent,resources.getString(R.string.compartilhar)), code);
        }catch (Exception e){e.printStackTrace();}
    }

    public static Drawable setColor(Drawable drawable, int color){
        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        return drawable;
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
    public static void unbindDrawables(View view) {
        try{
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                if (!(view instanceof AdapterView<?>))
                    ((ViewGroup) view).removeAllViews();
            }
        }catch (Exception e){}
    }

    public static void hideSoftKeyboard(Activity activity) {
        if(activity.getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.
                    getSystemService(activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static List<Sala> getSalas(String r) {
        try {
            JSONArray items = new JSONObject(r).getJSONArray("salas");
            List<Sala> salas = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                Sala sala = new Sala();
                sala.id = item.getInt("id_sala");
                sala.nome = item.getString("nome");
                sala.numero = item.getInt("numero");
                sala.disponivel = item.getBoolean("disponivel");
                sala.campus = item.getString("campus");
                salas.add(sala);
            }
            return salas;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<Reserva> getReservas(String r) {
        List<Reserva> Reservas = new ArrayList<>();
        try {
            JSONArray items = new JSONObject(r).getJSONArray("Reservas");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                Reserva reserva = new Reserva();
                reserva.id_reserva = item.getLong("id_reserva");
                reserva.dt_start = item.getString("dt_start");
                reserva.dt_end = item.getString("dt_end");

                JSONObject itemSala = item.getJSONObject("sala");

                Sala sala = new Sala();
                sala.id = itemSala.getInt("id_sala");
                sala.nome = itemSala.getString("nome");
                sala.numero = itemSala.getInt("numero");
                sala.disponivel = itemSala.getBoolean("disponivel");
                sala.campus = itemSala.getString("campus");

                reserva.sala = sala;
                reserva.usuario = item.getString("usuario");
                Reservas.add(reserva);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Reservas;
    }
}
