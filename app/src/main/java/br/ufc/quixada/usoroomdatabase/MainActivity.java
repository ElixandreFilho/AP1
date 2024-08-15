package br.ufc.quixada.usoroomdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import java.util.List;
import br.ufc.quixada.usoroomdatabase.database.AppDatabase;
import br.ufc.quixada.usoroomdatabase.models.Pessoa;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private PessoaAdapter pessoaAdapter;
    private RecyclerView recyclerView;
    private Button createNewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        createNewButton = findViewById(R.id.createNewButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "pessoa-database").allowMainThreadQueries().build();

        List<Pessoa> pessoas = db.pessoaDao().getAllPessoas();
        pessoaAdapter = new PessoaAdapter(pessoas);
        recyclerView.setAdapter(pessoaAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Pessoa pessoaToRemove = pessoaAdapter.getPessoaAt(position);

                db.pessoaDao().delete(pessoaToRemove);
                pessoaAdapter.removePessoa(position);
            }
        }).attachToRecyclerView(recyclerView);

        createNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });
    }
}
