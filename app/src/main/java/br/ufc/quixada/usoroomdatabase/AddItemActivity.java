package br.ufc.quixada.usoroomdatabase;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import br.ufc.quixada.usoroomdatabase.database.AppDatabase;
import br.ufc.quixada.usoroomdatabase.models.Agendamento;
import br.ufc.quixada.usoroomdatabase.models.Pessoa;

public class AddItemActivity extends AppCompatActivity {

    private EditText clienteEditText, dataHoraEditText;
    private Button saveButton, showItemsButton;
    private Calendar calendar = Calendar.getInstance();
    private AppDatabase db;
    private List<String> horariosPermitidos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        clienteEditText = findViewById(R.id.clienteEditText);
        dataHoraEditText = findViewById(R.id.dataHoraEditText);
        saveButton = findViewById(R.id.saveButton);
        showItemsButton = findViewById(R.id.showListButton);
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "agendamento-database")
                .fallbackToDestructiveMigration() // Adiciona essa linha para recriar o banco
                .allowMainThreadQueries()
                .build();
        // Configura a lista de horários permitidos
        setHorariosPermitidos();

        dataHoraEditText.setOnClickListener(v -> showDateTimePicker());

        saveButton.setOnClickListener(v -> {
            String cliente = clienteEditText.getText().toString();
            String dataHora = dataHoraEditText.getText().toString();

            if (cliente.isEmpty() || dataHora.isEmpty()) {
                Toast.makeText(AddItemActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

//            if (!isHorarioDisponivel(dataHora)) {
//                Toast.makeText(AddItemActivity.this, "Horário já ocupado!", Toast.LENGTH_SHORT).show();
//                return;
//            }

            Agendamento novoAgendamento = new Agendamento(cliente, dataHora);
            db.agendamentoDao().insertAll(novoAgendamento);
            List<Agendamento> ages = db.agendamentoDao().getAllAgendamentos();
            Log.d("agendamentos", "agendamenxtos");
            for (int i = 0; i < ages.size(); i++) {
                Log.d("Task", "Array element at index " + i + ": " + ages.get(i));
            }
            Toast.makeText(AddItemActivity.this, "Agendamento salvo!", Toast.LENGTH_SHORT).show();
            clienteEditText.setText("");
            dataHoraEditText.setText("");
        });

        showItemsButton.setOnClickListener(v -> finish());  // Retorna para a lista de itens
    }

    // Configura os horários permitidos
    private void setHorariosPermitidos() {
        // Manhã (8h-12h)
        for (int i = 8; i <= 12; i++) {
            horariosPermitidos.add(String.format("%02d:00", i));
        }
        // Tarde (14h-17h)
        for (int i = 14; i <= 17; i++) {
            horariosPermitidos.add(String.format("%02d:00", i));
        }
    }

    // Exibe o DatePicker e o TimePicker
    @SuppressLint("DefaultLocale")
    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        new DatePickerDialog(AddItemActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(year, monthOfYear, dayOfMonth);

            new TimePickerDialog(AddItemActivity.this, (timeView, hourOfDay, minute) -> {
                String horarioSelecionado = String.format("%02d:00", hourOfDay);  // Sempre usa 00 minutos

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, 0);  // Sempre define 00 minutos

                dataHoraEditText.setText(String.format("%02d/%02d/%04d %02d:00",
                        dayOfMonth, (monthOfYear + 1), year, hourOfDay));
//                if (horariosPermitidos.contains(horarioSelecionado)) {
//                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                    calendar.set(Calendar.MINUTE, 0);  // Sempre define 00 minutos
//
//                    dataHoraEditText.setText(String.format("%02d/%02d/%04d %02d:00",
//                            dayOfMonth, (monthOfYear + 1), year, hourOfDay));
//                } else {
//                    Toast.makeText(AddItemActivity.this, "Horário inválido. Escolha um horário válido.", Toast.LENGTH_SHORT).show();
//                }

            }, currentDate.get(Calendar.HOUR_OF_DAY), 0, true).show();  // Define os minutos como 0

        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Verifica se o horário está disponível
//    private boolean isHorarioDisponivel(String dataHora) {
//        List<Agendamento> pessoas = db.agendamentoDao().getAllAgendamentos();
//        int count = 0;
//
//        for (Agendamento p : pessoas) {
//            if (p.data.equals(dataHora)) {  // Usa o campo "curso" para armazenar data e hora
//                count++;
//                if (count >= 9) {  // Limite de 9 agendamentos por dia
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
}
