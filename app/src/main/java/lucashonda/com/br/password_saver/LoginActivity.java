package lucashonda.com.br.password_saver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lucashonda.com.br.password_saver.dao.daoUsuario;
import lucashonda.com.br.password_saver.interfaces.IActivitys;
import lucashonda.com.br.password_saver.utils.dialogAuth;

public class LoginActivity extends AppCompatActivity implements IActivitys {

    private static String TAG = "Login Activity";

    /**
     * Components
     * */
    private com.google.android.material.textfield.TextInputLayout lblSenha;
    private com.google.android.material.textfield.TextInputLayout lblUsuario;
    private EditText tvUsuario;
    private EditText tvSenha;
    private Button btnEntrar;

    /**
     * Variables
     * */
    private Context context;
    private boolean byPassword = false;

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
        lblSenha = findViewById(R.id.lblSenha);
        lblUsuario = findViewById(R.id.lblUsuario);
        tvUsuario = findViewById(R.id.tvUsuario);
        tvSenha = findViewById(R.id.tvSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
    }

    @Override
    public void setComponentsValues() {
        lblSenha.setVisibility(View.GONE);
        tvUsuario.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                byPassword = true;
                lblSenha.setVisibility(View.VISIBLE);
            }
        });
        btnEntrar.setOnClickListener((View v) -> {
            if (byPassword) {

                boolean ret = new daoUsuario(this).validUser(tvUsuario.getText().toString(), tvSenha.getText().toString());

                if (ret) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", tvUsuario.getText().toString());
                    intent.putExtras(bundle);
                    finishAffinity();
                    startActivity(intent);
                } else {
                    lblUsuario.setError("Usuário inválido");
                    lblUsuario.setErrorEnabled(true);
                    lblSenha.setError("Senha inválida");
                    lblSenha.setErrorEnabled(true);
                    tvUsuario.requestFocus();
                }

            } else
                new dialogAuth().show(context, this);
        });
    }

    @Override
    public void setCarregaDaos() {

    }
}
