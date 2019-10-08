package lucashonda.com.br.password_saver.model;

import io.realm.RealmObject;

public class usuario extends RealmObject {
    private String usuario;
    private String senha;
    private boolean loged;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isLoged() {
        return loged;
    }

    public void setLoged(boolean loged) {
        this.loged = loged;
    }
}
