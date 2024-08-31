package com.example.fluxodecaixa.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fluxodecaixa.entity.Transacao

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ${TABLE_NAME} ( _id INTEGER PRIMARY KEY AUTOINCREMENT, isCredit INTEGER, detail TEXT, value FLOAT, date TEXT )")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${TABLE_NAME}")
        onCreate(db)
    }

    fun insert(transacao: Transacao) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put("isCredit", transacao.isCredit)
        values.put("detail", transacao.detail)
        values.put("value", transacao.value)
        values.put("date", transacao.date)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun list(): ArrayList<Transacao> {
        val db = this.readableDatabase
        val values = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val transacoes = ArrayList<Transacao>()

        while (values.moveToNext()) {
            val transacao = Transacao(
                values.getInt(ID),
                values.getInt(IS_CREDIT) == 1,
                values.getString(DETAIL),
                values.getFloat(VALUE),
                values.getString(DATE)
            )
            transacoes.add(transacao)

        }
        return transacoes
    }

    @SuppressLint("Range")
    fun calculateBalance(): Float {
        val db = this.readableDatabase

        val query =
            "SELECT SUM(CASE WHEN isCredit = 1 THEN value ELSE 0 END) AS credit, " +
                "SUM(CASE WHEN isCredit = 0 THEN value ELSE 0 END) AS debit " +
                "FROM $TABLE_NAME"

        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val credit = cursor.getFloat(cursor.getColumnIndex("credit"))
        val debit = cursor.getFloat(cursor.getColumnIndex("debit"))
        val balance = credit - debit;
        cursor.close()
        db.close()

        return balance
    }

    companion object {
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "transacao"
        const val ID = 0
        const val IS_CREDIT = 1
        const val DETAIL = 2
        const val VALUE = 3
        const val DATE = 4
    }
}