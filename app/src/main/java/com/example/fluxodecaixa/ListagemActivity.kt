package com.example.fluxodecaixa

import RecordAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fluxodecaixa.database.DatabaseHandler
import com.example.fluxodecaixa.databinding.ActivityListagemBinding
import com.example.fluxodecaixa.databinding.ActivityMainBinding

class ListagemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListagemBinding
    private lateinit var adapter: RecordAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListagemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView

        val transacoes = DatabaseHandler(this).list()

        adapter = RecordAdapter(this, transacoes)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}