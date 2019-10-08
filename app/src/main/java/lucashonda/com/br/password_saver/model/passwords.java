package lucashonda.com.br.password_saver.model;

import io.realm.RealmObject;

public class passwords extends RealmObject {

    private String Servico;
    private String Usuario;
    private String Senha;

    public String getServico() {
        return Servico;
    }

    public void setServico(String servico) {
        Servico = servico;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }
}
