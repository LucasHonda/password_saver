package lucashonda.com.br.password_saver.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

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

import lucashonda.com.br.password_saver.MainActivity;
import lucashonda.com.br.password_saver.R;
import lucashonda.com.br.password_saver.dao.daoUsuario;

public class dialogAuth {

    private static String TAG = "Dialog Auth";

    private static final String KEY_NAME = "password_saver";

    private KeyStore keyStore;
    private Cipher cipher;

    private int count = 0;

    public void show(Context context, Activity activity) {

        count = 0;

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_auth, null);

        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        ImageView ivFinger = view.findViewById(R.id.ivFinger);

        ivFinger.setImageDrawable(context.getResources().getDrawable(R.drawable.finger));

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();

        btnCancelar.setOnClickListener((View v) -> dialog.dismiss());

        dialog.setOnShowListener(dialog1 -> {

            KeyguardManager keyguardManager = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
            FingerprintManager fingerprintManager = (FingerprintManager) activity.getSystemService(Context.FINGERPRINT_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {
                new AlertDialog.Builder(context).setTitle("Aviso").setMessage("Não há suporte para o sensor biométrico!").setPositiveButton("OK", (dialog2, which) -> dialog2.dismiss()).show();
            } else {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(context).setTitle("Aviso").setMessage("Não foi permitido usar o sensor biométrico!").setPositiveButton("OK", (dialog2, which) -> dialog2.dismiss()).show();
                } else {
                    if (!keyguardManager.isKeyguardSecure()) {
                        new AlertDialog.Builder(context).setTitle("Aviso").setMessage("Não foi definido nenhum tipo de senha!").setPositiveButton("OK", (dialog2, which) -> dialog2.dismiss()).show();
                    } else {
                        generateKey();

                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintHandler helper = new FingerprintHandler(context, (bool, msg, cancellationSignal) -> {

                                if (dialog.isShowing()) {
                                    if (bool) {
                                        ivFinger.setImageDrawable(context.getResources().getDrawable(R.drawable.finger_pass));

                                        Intent intent = new Intent(activity, MainActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("user", "adm");
                                        intent.putExtras(bundle);
                                        dialog1.dismiss();
                                        activity.finishAffinity();
                                        activity.startActivity(intent);
                                    } else {
                                        if (count < 2) {
                                            count++;
                                            final Animation animShake = AnimationUtils.loadAnimation(context, R.anim.shake);
                                            ivFinger.setImageDrawable(context.getResources().getDrawable(R.drawable.finger_error));
                                            ivFinger.startAnimation(animShake);
                                        } else {
                                            cancellationSignal.cancel();
                                            dialog1.dismiss();
                                        }
                                    }
                                } else {
                                    cancellationSignal.cancel();
                                }

                            });

                            if (!dialog.isShowing()) {
                                helper.cancel();
                            } else {
                                helper.startAuth(fingerprintManager, cryptoObject);
                            }
                        }
                    }
                }
            }
        });

        dialog.show();

    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            Log.e(TAG, "generateKey: ", e);
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
