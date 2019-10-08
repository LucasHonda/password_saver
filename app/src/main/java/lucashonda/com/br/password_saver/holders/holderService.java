package lucashonda.com.br.password_saver.holders;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import lucashonda.com.br.password_saver.R;

public class holderService extends RecyclerView.ViewHolder {

    public ConstraintLayout llPrincipal;
    public TextView tvServico;
    public TextView tvUsuario;

    public holderService(@NonNull View itemView) {
        super(itemView);
        llPrincipal = itemView.findViewById(R.id.llPrincipal);
        tvServico = itemView.findViewById(R.id.tvService);
        tvUsuario = itemView.findViewById(R.id.tvUsuario);
    }
}
