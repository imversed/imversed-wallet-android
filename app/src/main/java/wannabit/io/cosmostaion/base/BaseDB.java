package wannabit.io.cosmostaion.base;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class BaseDB extends SQLiteOpenHelper {

    private static BaseDB instance;

    static public synchronized BaseDB getInstance(Context context) {
        if (instance == null) {
            instance = new BaseDB(context, BaseConstant.DB_NAME, null, BaseConstant.DB_VERSION);
        }
        return instance;
    }

    public BaseDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE [" + BaseConstant.DB_TABLE_ACCOUNT +
                "] ([id] INTEGER PRIMARY KEY AUTOINCREMENT, [uuid] TEXT, [nickName] TEXT, [address] TEXT, [baseChain] INTEGER, " +
                "[hasPrivateKey] INTEGER DEFAULT 0, [resource] TEXT, [spec] TEXT, [fromMnemonic] INTEGER DEFAULT 0, [path] INTEGER DEFAULT 0, " +
                "[sequenceNumber] INTEGER, [accountNumber] INTEGER, [msize] INTEGER, [importTime] INTEGER, [lastTotal] TEXT, " +
                "[customPath] INTEGER DEFAULT 0)");

        sqLiteDatabase.execSQL("CREATE TABLE [" + BaseConstant.DB_TABLE_BALANCE +
                "] ([id] INTEGER PRIMARY KEY AUTOINCREMENT, [accountId] INTEGER, [symbol] TEXT, [balance] TEXT, [fetchTime] INTEGER, [frozen] TEXT, [locked] TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE [" + BaseConstant.DB_TABLE_BONDING +
                "] ([id] INTEGER PRIMARY KEY AUTOINCREMENT, [accountId] INTEGER, [validatorAddress] TEXT, [shares] TEXT, [fetchTime] INTEGER)");

        sqLiteDatabase.execSQL("CREATE TABLE [" + BaseConstant.DB_TABLE_UNBONDING +
                "] ([id] INTEGER PRIMARY KEY AUTOINCREMENT, [accountId] INTEGER, [validatorAddress] TEXT, [creationHeight] TEXT, [completionTime] INTEGER, " +
                "[initialBalance] TEXT, [balance] TEXT, [fetchTime] INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
