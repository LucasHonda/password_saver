package lucashonda.com.br.password_saver.dao;

import android.content.Context;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import lucashonda.com.br.password_saver.model.usuario;

public class daoUsuario {

    private Realm realm;

    public daoUsuario(Context context) {
        initializeRealm(context);
    }

    private void initializeRealm(Context context) {
        realm.init(context);

        RealmConfiguration.Builder realmBuilder = new RealmConfiguration.Builder();

        realmBuilder.directory(new File(context.getFilesDir().toString()));
        realmBuilder.name("passwordSaver.realm");

        RealmConfiguration config = realmBuilder.build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getInstance(config);
    }

    public void insert(String user, String senha, boolean loged) {

        realm.executeTransaction(realm -> {

            usuario u = realm.createObject(usuario.class);

            u.setUsuario(user);
            u.setSenha(senha);
            u.setLoged(loged);

        });
    }

    public boolean get() {
        RealmResults<usuario> results = realm.where(usuario.class).findAll();
        return results.size() > 1 ? true : false;
    }
}
