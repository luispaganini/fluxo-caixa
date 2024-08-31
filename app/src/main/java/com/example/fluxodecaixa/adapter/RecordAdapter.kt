import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fluxodecaixa.R
import com.example.fluxodecaixa.entity.Transacao

class RecordAdapter(
    private val context: Context,
    private val transacoes: List<Transacao>
) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        // Infla o layout do item da lista
        val view = LayoutInflater.from(context).inflate(R.layout.element_list, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val transacao = transacoes[position]

        // Configura o tipo com cor
        holder.textViewTipo.text = if (transacao.isCredit) "C" else "D"
        holder.textViewTipo.setTextColor(
            if (transacao.isCredit) context.getColor(android.R.color.holo_green_dark)
            else context.getColor(android.R.color.holo_red_dark)
        )

        // Configura os outros campos
        holder.textViewData.text = transacao.date
        holder.textViewDetalhe.text = transacao.detail
        holder.textViewValor.text = String.format("R$ %.2f", transacao.value)
    }

    override fun getItemCount(): Int {
        return transacoes.size
    }

    class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTipo: TextView = itemView.findViewById(R.id.textViewTipo)
        val textViewData: TextView = itemView.findViewById(R.id.textViewData)
        val textViewDetalhe: TextView = itemView.findViewById(R.id.textViewDetalhe)
        val textViewValor: TextView = itemView.findViewById(R.id.textViewValor)
    }
}
