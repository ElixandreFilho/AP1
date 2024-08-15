package br.ufc.quixada.usoroomdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import br.ufc.quixada.usoroomdatabase.database.AppDatabase;
import br.ufc.quixada.usoroomdatabase.models.Pessoa;

public class AddItemActivity extends AppCompatActivity {

    private EditText tituloEditText, descricaoEditText;
    private Button saveButton, showListButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        tituloEditText = findViewById(R.id.tituloEditText);
        descricaoEditText = findViewById(R.id.descricaoEditText);
        saveButton = findViewById(R.id.saveButton);
        showListButton = findViewById(R.id.showListButton);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "pessoa-database").allowMainThreadQueries().build();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = tituloEditText.getText().toString();
                String descricao = descricaoEditText.getText().toString();

                if (!TextUtils.isEmpty(titulo) && !TextUtils.isEmpty(descricao)) {
                    Pessoa novaPessoa = new Pessoa(titulo, descricao, R.drawable.foto2);
                    db.pessoaDao().insertAll(novaPessoa);

                    tituloEditText.setText("");
                    descricaoEditText.setText("");
                }
            }
        });

        showListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
