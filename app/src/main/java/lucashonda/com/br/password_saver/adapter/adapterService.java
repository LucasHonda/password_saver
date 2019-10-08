package lucashonda.com.br.password_saver.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import lucashonda.com.br.password_saver.R;
import lucashonda.com.br.password_saver.dao.daoPasswords;
import lucashonda.com.br.password_saver.holders.holderService;
import lucashonda.com.br.password_saver.model.passwords;

public class adapterService extends RecyclerView.Adapter<holderService> {

    private static final String TAG = "Adapter Service";

    private List<passwords> mArr;
    private Context context;
    private Activity activity;

    private passwords mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public adapterService(List<passwords> mArr, Context context, Activity activity) {
        this.mArr = mArr;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public holderService onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new holderService(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_service, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holderService view, int i) {
        passwords p;
        try {

            p = mArr.get(i);

            view.tvServico.setText(p.getServico());
            view.tvUsuario.setText(p.getUsuario());

            view.llPrincipal.setOnClickListener((View v) -> {

                View dView = View.inflate(context, R.layout.dialog_add, null);

                EditText tvServico = dView.findViewById(R.id.tvUsuario);
                EditText tvUsuario = dView.findViewById(R.id.tvUsuario);
                EditText tvSenha = dView.findViewById(R.id.tvSenha);

                tvServico.setText(p.getServico());
                tvUsuario.setText(p.getUsuario());
                tvSenha.setText(p.getSenha());

                tvServico.setEnabled(false);
                tvUsuario.setEnabled(false);
                tvSenha.setEnabled(false);

                new AlertDialog.Builder(context)
                        .setView(dView)
                        .setTitle("Informações")
                        .setNeutralButton("Copiar Senha", ((dialog, which) -> {
                            ClipboardManager clipMan = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipMan.setPrimaryClip(ClipData.newPlainText("label", p.getSenha()));
                            Toast.makeText(context, "Senha copiada!", Toast.LENGTH_SHORT).show();
                        }))
                        .setPositiveButton("OK", (dialog1, which) -> dialog1.dismiss())
                        .setCancelable(false)
                        .show();

            });

        } catch (Exception ex) {
            Log.e(TAG, "onBindViewHolder: ", ex);
        }
    }

    @Override
    public int getItemCount() {
        return mArr.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = mArr.get(position);
        mRecentlyDeletedItemPosition = position;
        mArr.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = activity.findViewById(R.id.clPrincipal);
        CharSequence charSequence;
        Snackbar snackbar = Snackbar.make(view, "Item removido.", Snackbar.LENGTH_LONG);
        snackbar.setAction("Desfazer", v -> undoDelete());
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (mRecentlyDeletedItemPosition != -1 && mRecentlyDeletedItem != null)
                    new daoPasswords(context).delete(mRecentlyDeletedItem.getServico(), mRecentlyDeletedItem.getUsuario(), mRecentlyDeletedItem.getSenha());
            }

            @Override
            public void onShown(Snackbar snackbar) {
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        mArr.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
        mRecentlyDeletedItemPosition = -1;
        mRecentlyDeletedItem = null;
    }
}
