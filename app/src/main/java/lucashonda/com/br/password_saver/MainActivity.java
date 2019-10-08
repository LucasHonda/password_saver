package lucashonda.com.br.password_saver;

import android.app.AlertDialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lucashonda.com.br.password_saver.adapter.adapterService;
import lucashonda.com.br.password_saver.dao.daoPasswords;
import lucashonda.com.br.password_saver.model.passwords;
import lucashonda.com.br.password_saver.utils.SwipeToDeleteCallback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Class";

    private Context context;

    private Toolbar toolbar;
    private RecyclerView lstServices;
    private TextView tvEmpty;
    private ProgressBar loading;

    private List<passwords> mArr;
    private adapterService adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            context = this;

            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            setComponents();
            setComponentsValues();
            carregaDados();

        } catch (Exception ex) {
            Log.e(TAG, "onCreate: ", ex);
        }
    }

    private void setComponents() {
        lstServices = findViewById(R.id.lstServices);
        tvEmpty = findViewById(R.id.tvEmpty);
        loading = findViewById(R.id.loading);
    }

    private void setComponentsValues() {
        tvEmpty.setVisibility(View.GONE);
        lstServices.setLayoutManager(new LinearLayoutManager(context));
        lstServices.getRecycledViewPool().setMaxRecycledViews(0,0);
        mArr = new ArrayList<>();
        adapter = new adapterService(mArr, context, this);
        lstServices.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(lstServices);
    }

    private void carregaDados() {
        daoPasswords dPasswords = new daoPasswords(context);
        mArr.addAll(dPasswords.getAll());
        adapter.notifyDataSetChanged();

        loading.setVisibility(View.GONE);

        if (mArr.isEmpty())
            tvEmpty.setVisibility(View.VISIBLE);

    }

    private void clickAdd() {

        View view = View.inflate(context, R.layout.dialog_add, null);

        EditText tvServico = view.findViewById(R.id.tvServico);
        EditText tvUsuario = view.findViewById(R.id.tvUsuario);
        EditText tvSenha = view.findViewById(R.id.tvSenha);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setTitle("Novo Cadastro")
                .setPositiveButton("Salvar", null)
                .setNegativeButton("Cancelar", (dialog1, which) -> dialog1.dismiss())
                .setCancelable(false)
                .show();

        Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        positive.setOnClickListener((View v) -> {
            if (!tvServico.getText().toString().isEmpty() && !tvUsuario.getText().toString().isEmpty() && !tvSenha.getText().toString().isEmpty()) {
                new daoPasswords(context).insert(tvServico.getText().toString(), tvUsuario.getText().toString(), tvSenha.getText().toString());
                passwords p = new passwords();
                p.setServico(tvServico.getText().toString());
                p.setUsuario(tvUsuario.getText().toString());
                p.setSenha(tvSenha.getText().toString());
                mArr.add(p);
                adapter.notifyDataSetChanged();
                tvEmpty.setVisibility(View.GONE);
                dialog.dismiss();
            } else
                new AlertDialog.Builder(context).setTitle("Aviso").setMessage("Existem campos em branco!").setPositiveButton("OK", (dialog1, which) -> dialog1.dismiss()).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_services, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnAdd) {
            clickAdd();
            return  true;
        } else if (id == R.id.mnSync) {
            return true;
        }/* else if (id == R.id.mnUser) {

        }*/

        return super.onOptionsItemSelected(item);
    }
}
