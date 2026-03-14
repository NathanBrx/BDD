package fds.hai811i.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Utilisateurs.db";
    private static final String TABLE_USERS = "users_table";
    private static final String TABLE_PLANNING = "planning_table";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "LOGIN TEXT, PASSWORD TEXT, NOM TEXT, PRENOM TEXT, DOB TEXT, PHONE TEXT, EMAIL TEXT, INTERESTS TEXT)";
        db.execSQL(createUsersTable);

        String createPlanningTable = "CREATE TABLE " + TABLE_PLANNING + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USER_LOGIN TEXT, DATE TEXT, CRENEAU_1 TEXT, CRENEAU_2 TEXT, CRENEAU_3 TEXT, CRENEAU_4 TEXT)";
        db.execSQL(createPlanningTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANNING);
        onCreate(db);
    }

    public boolean insertUser(String login, String password, String nom, String prenom, String dob, String phone, String email, String interests) {
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

        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERS);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_USERS + "'");
    }

    public boolean checkUser(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE LOGIN=?", new String[]{login});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    public boolean checkUserAndPass(String login, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE LOGIN=? AND PASSWORD=?", new String[]{login, password});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean insertPlanning(String login, String date, String c1, String c2, String c3, String c4) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_LOGIN", login);
        contentValues.put("DATE", date);
        contentValues.put("CRENEAU_1", c1);
        contentValues.put("CRENEAU_2", c2);
        contentValues.put("CRENEAU_3", c3);
        contentValues.put("CRENEAU_4", c4);

        long result = db.insert(TABLE_PLANNING, null, contentValues);
        return result != -1;
    }

    public String[] getPlanningForDate(String login, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT CRENEAU_1, CRENEAU_2, CRENEAU_3, CRENEAU_4 FROM " + TABLE_PLANNING + " WHERE USER_LOGIN=? AND DATE=?", new String[]{login, date});

        if (cursor.moveToFirst()) {
            String[] creneaux = new String[]{
                    cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)
            };
            cursor.close();
            return creneaux;
        }
        cursor.close();
        return null;
    }

    public boolean deletePlanningForDate(String login, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_PLANNING, "USER_LOGIN=? AND DATE=?", new String[]{login, date});

        return deletedRows > 0;
    }
}