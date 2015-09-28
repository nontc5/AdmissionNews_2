package utcc.nontchaiyakarn.admissionnews;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by nontchaiyakarn on 9/26/15 AD.
 */
public class UsageTABLE {

    private MySQLiteOpenHelper objMySQLiteOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSQLiteDatabase;

    public static final String USAGE_TABLE = "usageTABLE";
    public static final String COLUMN_ID_USAGE = "_id";
    public static final String COLUMN_SEQ = "Usage_seq";
    public static final String COLUMN_DATAID = "Usage_DataID";


    public UsageTABLE(Context context) {

        objMySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        writeSqLiteDatabase = objMySQLiteOpenHelper.getWritableDatabase();
        readSQLiteDatabase = objMySQLiteOpenHelper.getReadableDatabase();

    }   // Constructor

    public long addNewUsage(String strSeq, String strDataID) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_SEQ, strSeq);
        objContentValues.put(COLUMN_DATAID, strDataID);

        return writeSqLiteDatabase.insert(USAGE_TABLE, null, objContentValues);
    }

}   // Main Class
