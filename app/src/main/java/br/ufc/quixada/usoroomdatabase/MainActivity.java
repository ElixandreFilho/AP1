package br.ufc.quixada.usoroomdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import java.util.List;
import br.ufc.quixada.usoroomdatabase.database.AppDatabase;
import br.ufc.quixada.usoroomdatabase.models.Agendamento;
import br.ufc.quixada.usoroomdatabase.models.Pessoa;

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
                .fallbackToDestructiveMigration() // Adiciona essa linha para recriar o banco
                .allowMainThreadQueries()
                .build();


        List<Agendamento> agendamentos = db.agendamentoDao().getAllAgendamentos();
        agendamentoAdapter = new AgendamentoAdapter(agendamentos);
        recyclerView.setAdapter(agendamentoAdapter);;

        // Configura o botão "Criar Novo" para abrir a AddItemActivity
        criarNovoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });
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
