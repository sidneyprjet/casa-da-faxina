package com.example.myapplication.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.data.model.Usuario

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "casadafaxina.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_USUARIOS = "usuarios"
        const val COLUMN_ID = "id"
        const val COLUMN_NOME = "nome"
        const val COLUMN_TELEFONE = "telefone"
        const val COLUMN_CRIADO_EM = "criado_em"
        const val COLUMN_IS_VISITANTE = "is_visitante"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USUARIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOME TEXT NOT NULL,
                $COLUMN_TELEFONE TEXT NOT NULL,
                $COLUMN_CRIADO_EM INTEGER NOT NULL,
                $COLUMN_IS_VISITANTE INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }

    fun inserirUsuario(usuario: Usuario): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOME, usuario.nome)
            put(COLUMN_TELEFONE, usuario.telefone)
            put(COLUMN_CRIADO_EM, usuario.criadoEm)
            put(COLUMN_IS_VISITANTE, if (usuario.isVisitante) 1 else 0)
        }
        return db.insert(TABLE_USUARIOS, null, values)
    }

    fun obterUltimoUsuario(): Usuario? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_ID DESC",
            "1"
        )

        return cursor.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val nome = it.getString(it.getColumnIndexOrThrow(COLUMN_NOME))
                val telefone = it.getString(it.getColumnIndexOrThrow(COLUMN_TELEFONE))
                val criadoEm = it.getLong(it.getColumnIndexOrThrow(COLUMN_CRIADO_EM))
                val isVisitante = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_VISITANTE)) == 1

                Usuario(id, nome, telefone, criadoEm, isVisitante)
            } else {
                null
            }
        }
    }

    fun listarUsuarios(): List<Usuario> {
        val lista = mutableListOf<Usuario>()
        val db = readableDatabase
        val cursor = db.query(TABLE_USUARIOS, null, null, null, null, null, "$COLUMN_ID ASC")

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val nome = it.getString(it.getColumnIndexOrThrow(COLUMN_NOME))
                val telefone = it.getString(it.getColumnIndexOrThrow(COLUMN_TELEFONE))
                val criadoEm = it.getLong(it.getColumnIndexOrThrow(COLUMN_CRIADO_EM))
                val isVisitante = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_VISITANTE)) == 1

                lista.add(Usuario(id, nome, telefone, criadoEm, isVisitante))
            }
        }
        return lista
    }

    /**
     * Exclui todos os registros locais em conformidade com o Art. 18 da LGPD
     * (Direito à eliminação dos dados pessoais a pedido do titular).
     */
    fun limparTodosDados(): Int {
        val db = writableDatabase
        return db.delete(TABLE_USUARIOS, null, null)
    }

    /**
     * Atualiza os dados locais do usuário em conformidade com a LGPD
     * (Direito à retificação e atualização dos dados pessoais).
     */
    fun atualizarUsuario(id: Long, nome: String, telefone: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOME, nome)
            put(COLUMN_TELEFONE, telefone)
        }
        return db.update(TABLE_USUARIOS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
