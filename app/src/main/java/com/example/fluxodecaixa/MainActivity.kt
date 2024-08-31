package com.example.fluxodecaixa

import RecordAdapter
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fluxodecaixa.database.DatabaseHandler
import com.example.fluxodecaixa.databinding.ActivityMainBinding
import com.example.fluxodecaixa.entity.Transacao
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etDataLanc.setOnClickListener {fnDateLaunch()}
        binding.btLancar.setOnClickListener { btnLancar() }
        binding.btVerLancamentos.setOnClickListener { btnVerLancamentos() }
        binding.btSaldo.setOnClickListener { btnSaldo() }

        database = DatabaseHandler(this).writableDatabase

        setupTipoSpinner()
    }

    private fun setupTipoSpinner() {
        val tipoSpinner = binding.spTipo
        val tipos = listOf("Crédito", "Débito")
        val tipoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipos)
        tipoSpinner.adapter = tipoAdapter

        // Configura o listener para detectar quando um item é selecionado
        tipoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedTipo = tipos[position]
                setupDetalheSpinner(selectedTipo)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupDetalheSpinner(tipo: String) {
        val detalheSpinner = binding.spDetalhe
        val detalhes = if (tipo == "Débito")
            listOf("Alimentação", "Transporte", "Saúde", "Moradia")
         else
            listOf("Salário", "Extras")

        val detalheAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, detalhes)
        detalheSpinner.adapter = detalheAdapter
    }

    private fun fnDateLaunch() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                var monthValue = "";
                if (selectedMonth >= 9)
                    monthValue = (selectedMonth + 1).toString()
                else
                    monthValue = "0" + (selectedMonth + 1)


                val formattedDate = "$selectedDay/$monthValue/$selectedYear"
                binding.etDataLanc.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun btnLancar() {
        val isCredit = binding.spTipo.selectedItemPosition == 0
        val detail = binding.spDetalhe.selectedItem.toString()
        val value = binding.etValor.text.toString().toFloat()
        val date = binding.etDataLanc.text.toString()


        val transacao = Transacao(0, isCredit, detail, value, date)
        DatabaseHandler(this).insert(transacao)
        binding.etValor.text?.clear()
        binding.etDataLanc.text?.clear()
        binding.spTipo.setSelection(0)
        binding.spDetalhe.setSelection(0)

        Snackbar.make(binding.root, "Lançamento cadastrado com sucesso!", Snackbar.LENGTH_SHORT).show()
    }

    private fun btnVerLancamentos() {
        val intent = Intent(this, ListagemActivity::class.java)
        startActivity(intent)
    }

    private fun btnSaldo() {
        val saldo = DatabaseHandler(this).calculateBalance()

        showSaldoDialog(saldo)
    }

    private fun showSaldoDialog(saldo: Float) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Saldo Atual")
        builder.setMessage("Seu saldo é: R$ $saldo")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

}