package br.ufc.quixada.usoroomdatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import br.ufc.quixada.usoroomdatabase.models.Pessoa;
import br.ufc.quixada.usoroomdatabase.R;

public class PessoaAdapter extends RecyclerView.Adapter<PessoaAdapter.PessoaViewHolder> {

    private List<Pessoa> pessoas;

    public PessoaAdapter(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    @NonNull
    @Override
    public PessoaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pessoa, parent, false);
        return new PessoaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PessoaViewHolder holder, int position) {
        Pessoa pessoa = pessoas.get(position);
        holder.tituloTextView.setText(pessoa.nome);
        holder.descricaoTextView.setText(pessoa.curso);
    }

    @Override
    public int getItemCount() {
        return pessoas.size();
    }

    public void removePessoa(int position) {
        pessoas.remove(position);
        notifyItemRemoved(position);
    }

    public void addPessoa(Pessoa pessoa) {
        pessoas.add(pessoa);
        notifyItemInserted(pessoas.size() - 1);
    }

    public Pessoa getPessoaAt(int position) {
        return pessoas.get(position);
    }

    public class PessoaViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView, descricaoTextView;

        public PessoaViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            descricaoTextView = itemView.findViewById(R.id.descricaoTextView);

        }
    }
}
