package fds.hai811i.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Utilisateurs.db";
    private static final String TABLE_NAME = "users_table";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "LOGIN TEXT, PASSWORD TEXT, NOM TEXT, PRENOM TEXT, DOB TEXT, PHONE TEXT, EMAIL TEXT, INTERESTS TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String login, String password, String nom, String prenom, String dob, String phone, String email, String interests) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LOGIN", login);
        contentValues.put("PASSWORD", password);
        contentValues.put("NOM", nom);
        contentValues.put("PRENOM", prenom);
        contentValues.put("DOB", dob);
        contentValues.put("PHONE", phone);
        contentValues.put("EMAIL", email);
        contentValues.put("INTERESTS", interests);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_NAME + "'");
    }
}