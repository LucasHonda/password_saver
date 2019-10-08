package lucashonda.com.br.password_saver.interfaces;

import android.os.CancellationSignal;

public interface IHandler {

    void executaAcao(Boolean bool, String msg, CancellationSignal cancellationSignal);
}
