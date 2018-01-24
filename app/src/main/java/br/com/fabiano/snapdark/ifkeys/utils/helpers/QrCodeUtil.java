package br.com.fabiano.snapdark.ifkeys.utils.helpers;

import android.os.Environment;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Fabiano on 18/01/2018.
 */

public class QrCodeUtil {
    public static String gerarQrCode() throws IOException {
        String path = Environment.getExternalStorageDirectory().getPath()+"/IFkeys/";
        if (!new File(path).exists()) {
            (new File(path)).mkdirs();
        }
        //path = path+"/"+prova.getAvaliacao().getAssunto()+".jpg";
        FileOutputStream f = new FileOutputStream(path);

        ByteArrayOutputStream out = QRCode.from("key number").to(ImageType.JPG).withSize(100,100).stream();


        f.write(out.toByteArray());
        f.close();
        return path;
    }
}
