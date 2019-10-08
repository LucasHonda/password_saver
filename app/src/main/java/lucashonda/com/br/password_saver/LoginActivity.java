package lucashonda.com.br.password_saver;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lucashonda.com.br.password_saver.interfaces.IActivitys;
import lucashonda.com.br.password_saver.utils.dialogAuth;

public class LoginActivity extends AppCompatActivity implements IActivitys {

    private static String TAG = "Login Activity";

    private EditText tvUsuario;
    private EditText tvSenha;
    private Button btnEntrar;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {

            context = this;

            setComponents();
            setComponentsValues();

        } catch (Exception ex) {
            Log.e(TAG, "onCreate: ", ex);
        }
    }

    @Override
    public void setComponents() {
        tvUsuario = findViewById(R.id.tvUsuario);
        //tvSenha = findViewById(R.id.tvSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
    }

    @Override
    public void setComponentsValues() {
        btnEntrar.setOnClickListener((View v) -> new dialogAuth().show(context, this));
    }

    @Override
    public void setCarregaDaos() {

    }
}
