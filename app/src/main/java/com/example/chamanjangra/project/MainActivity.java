package com.example.chamanjangra.project;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.fingerprint.FingerprintManager;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

TextView textView;
    private Cipher cipher;
    private final String KEY_NAME = "chaman";
    private KeyStore keyStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView) findViewById(R.id.fingerprinttext);
        textView.setText("please place your finger on scanner to mark the attendance for "+getIntent().getStringExtra("btnvalue"));
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (!fingerprintManager.isHardwareDetected()) {
            Toast.makeText(this, "fingerprint is not available", Toast.LENGTH_SHORT).show();
        } else {
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                Toast.makeText(this, "please register one of the fingerprint in setting", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            } else {
                if (!keyguardManager.isKeyguardSecure()) {
                    Toast.makeText(this, "look for key security is active", Toast.LENGTH_SHORT).show();
                } else {
                    keygen();
                }
                if (cipherInit()) {
                    FingerprintManager.CryptoObject crypyoobject = new FingerprintManager.CryptoObject(cipher);
                    FingerPrintHandler fingerprinthandler = new FingerPrintHandler(this);
                    fingerprinthandler.startAuthentication(fingerprintManager, crypyoobject);
                }
            }
        }
    }
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try {
            keyStore.load(null);
            SecretKey secretkey = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, secretkey);
            return true;
        }
        catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return false;

        }
    }





    public void keygen() {
        try {
            keyStore=KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        KeyGenerator keygenerator = null;
        try {
            keygenerator=KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            keyStore.load(null);
            keygenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());
            keygenerator.generateKey();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this,Home.class));
        finish();
    }
}
