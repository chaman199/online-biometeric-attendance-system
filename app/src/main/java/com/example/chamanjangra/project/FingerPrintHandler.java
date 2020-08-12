package com.example.chamanjangra.project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by chamanjangra on 19/08/2017.
 */

class FingerPrintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;
    public FingerPrintHandler(Context context)
    {
        this.context=context;
    }
    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject crypyoobject) {

        CancellationSignal cancel=new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fingerprintManager.authenticate(crypyoobject,cancel,0,this,null);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Toast.makeText(context,"FingerPrint authantication failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        context.startActivity(new Intent(context,SuccessAuth.class));
//    ((MainActivity) context.getApplicationContext()).finish();
        MainActivity ms=new MainActivity();
        ms.finish();
    }
}

