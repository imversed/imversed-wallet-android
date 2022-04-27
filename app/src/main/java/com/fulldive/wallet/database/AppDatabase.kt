package com.fulldive.wallet.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fulldive.wallet.models.WalletAccount
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [WalletAccount::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountsDao(): WalletAccountDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val DATABASE_NAME = "imversed.db"

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        val passphrase: ByteArray = SQLiteDatabase.getBytes(
                            ("${context.packageName}_$DATABASE_NAME").toCharArray()
                        )
                        val factory = SupportFactory(passphrase)
                        val builder = Room
                            .databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                DATABASE_NAME
                            )
                            .openHelperFactory(factory)
                            .allowMainThreadQueries()   // XXX: when accounts will be refactored
                        INSTANCE = builder.build()
                    }
                }
            }
            return INSTANCE
        }
    }
}