package com.example.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        ProdutoEntity::class,
        LojaConfigEntity::class,
        UsuarioEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(ProdutoConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao
    abstract fun lojaConfigDao(): LojaConfigDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE loja_config RENAME TO loja_config_v1")
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS loja_config (
                        id INTEGER NOT NULL,
                        nomeDaLoja TEXT NOT NULL,
                        whatsappDaLoja TEXT NOT NULL,
                        senhaMaster TEXT NOT NULL,
                        PRIMARY KEY(id)
                    )
                    """.trimIndent()
                )
                database.execSQL(
                    """
                    INSERT OR REPLACE INTO loja_config (id, nomeDaLoja, whatsappDaLoja, senhaMaster)
                    SELECT 1, nomeDaLoja, whatsappDaLoja, senhaMaster
                    FROM loja_config_v1
                    LIMIT 1
                    """.trimIndent()
                )
                database.execSQL("DROP TABLE loja_config_v1")
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS usuarios_room (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        nome TEXT NOT NULL,
                        telefone TEXT NOT NULL,
                        isAdmin INTEGER NOT NULL,
                        isSuperAdmin INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                database.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS index_usuarios_room_telefone " +
                        "ON usuarios_room (telefone)"
                )
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "casa_da_faxina.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
