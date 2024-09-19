package br.ufc.quixada.usoroomdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

import br.ufc.quixada.usoroomdatabase.database.AppDatabase;
import br.ufc.quixada.usoroomdatabase.models.Agendamento;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private AgendamentoAdapter agendamentoAdapter;
    private RecyclerView recyclerView;
    private Button criarNovoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        criarNovoButton = findViewById(R.id.criarNovoButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "agendamento-database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        List<Agendamento> agendamentos = db.agendamentoDao().getAllAgendamentos();
        Log.d("agendamentos", "agendamentos carregados");
        for (int i = 0; i < agendamentos.size(); i++) {
            Log.d("Task", "Array element at index " + i + ": " + agendamentos.get(i));
        }
        agendamentoAdapter = new AgendamentoAdapter(agendamentos);
        recyclerView.setAdapter(agendamentoAdapter);

        // Configura o botão "Criar Novo" para abrir a AddItemActivity
        criarNovoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        // Adiciona o ItemTouchHelper para permitir remoção com swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // Não precisamos de drag & drop
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Obtém a posição do item
                int position = viewHolder.getAdapterPosition();

                // Remove o agendamento do banco de dados
                Agendamento agendamentoRemovido = agendamentoAdapter.getAgendamentoAt(position);
                db.agendamentoDao().delete(agendamentoRemovido);

                // Remove o item da lista no Adapter
                agendamentoAdapter.removeAgendamento(position);

                // Exibe a mensagem "Agendamento Excluído!"
                Toast.makeText(MainActivity.this, "Agendamento Excluído!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Atualiza a lista de agendamentos quando a Activity for retomada
        List<Agendamento> agendamentosAtualizadas = db.agendamentoDao().getAllAgendamentos();
        agendamentoAdapter = new AgendamentoAdapter(agendamentosAtualizadas);
        recyclerView.setAdapter(agendamentoAdapter);
    }
}
