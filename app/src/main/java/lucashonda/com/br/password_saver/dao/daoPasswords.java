package lucashonda.com.br.password_saver.dao;

import android.content.Context;

import java.io.File;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import lucashonda.com.br.password_saver.model.passwords;

public class daoPasswords {

    private Realm realm;

    public daoPasswords(Context context) {
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

    public void insert(String servico, String usuario, String senha) {

        realm.executeTransaction(realm -> {

            passwords p = realm.createObject(passwords.class);

            p.setServico(servico);
            p.setUsuario(usuario);
            p.setSenha(senha);

        });
    }

    public void delete(String servico, String usuario, String senha) {

        realm.beginTransaction();

        passwords p = realm.where(passwords.class)
                .equalTo("Servico", servico)
                .equalTo("Usuario", usuario)
                .equalTo("Senha", senha)
                .findFirst();

        p.deleteFromRealm();

        realm.commitTransaction();

    }

    public List<passwords> getAll() {
        return realm.copyFromRealm(realm.where(passwords.class).findAll());
    }
}
