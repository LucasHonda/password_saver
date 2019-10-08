package lucashonda.com.br.password_saver.model;

import io.realm.RealmObject;

public class passwords extends RealmObject {

    private String user;
    private String servico;
    private String usuarioServico;
    private String senha;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getUsuarioServico() {
        return usuarioServico;
    }

    public void setUsuarioServico(String usuarioServico) {
        this.usuarioServico = usuarioServico;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
