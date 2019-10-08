package lucashonda.com.br.password_saver.utils;

import android.content.Intent;

import lucashonda.com.br.password_saver.LoginActivity;
import lucashonda.com.br.password_saver.MainActivity;
import lucashonda.com.br.password_saver.dao.daoUsuario;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Intent i;

        i = new Intent(this, LoginActivity.class);

        /*if (new daoUsuario(this).get())
            i = new Intent(this, MainActivity.class);
        else
            i = new Intent(this, LoginActivity.class);
        */

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
