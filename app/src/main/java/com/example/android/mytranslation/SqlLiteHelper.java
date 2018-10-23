package com.example.android.mytranslation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by android on 6/9/18.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;


public class SqlLiteHelper extends SQLiteOpenHelper {
    TranslationFav translationFav = new TranslationFav();
    ArrayList<TranslationFav> another_array = new ArrayList<TranslationFav>();
    private static final String DATABASE_NAME = "dictionaryDB";
    private static final String TABLE_NAME = "Student";
    private static final String COL_INPUT_TEXT = "inputText";
    private static final String COL_INPUT_LANGUAGE = "inputLanguage";
    private static final String COL_OUTPUT_TEXT = "outputText";
    private static final String COL_OUTPUT_LANGUAGE = "outputLanguage";
    SQLiteDatabase sqLiteDatabase;

    public SqlLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (inputText TEXT, inputLanguage TEXT, outputText TEXT, outputLanguage TEXT)");
        Log.e("created", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
    }

    public boolean dataInsert(String inputText, String inputLanguage, String outputText, String outputLanguage) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_INPUT_TEXT, inputText);
            contentValues.put(COL_INPUT_LANGUAGE, inputLanguage);
            contentValues.put(COL_OUTPUT_TEXT, outputText);
            contentValues.put(COL_OUTPUT_LANGUAGE, outputLanguage);
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            /*translationFav.setLanguageInputText(inputText);
            translationFav.setLanguageInputName(inputLanguage);
            translationFav.setTanslationOutputText(outputText);
            translationFav.setLanguageOutputName(outputLanguage);
            another_array.add(translationFav);*/
            return true;
    }

    public ArrayList<TranslationFav> dataFetch() {
        ArrayList<TranslationFav> another_array = new ArrayList<TranslationFav>();
        /*String[] columns = new String[] { COL_INPUT_TEXT, COL_INPUT_LANGUAGE, COL_OUTPUT_TEXT, COL_OUTPUT_LANGUAGE};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns,
                null, null, null, null, null);
*/
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // get  the  data into array,or class variable
                TranslationFav translationFav = new TranslationFav();
                translationFav.setLanguageInputText(cursor.getString(0));
                translationFav.setLanguageInputName(cursor.getString(1));
                translationFav.setTanslationOutputText(cursor.getString(2));
                translationFav.setLanguageOutputName(cursor.getString(3));
                another_array.add(translationFav);
            } while (cursor.moveToNext());
        }
        db.close();

        /*while (cursor.moveToNext()) {
            TranslationFav translationFav = new TranslationFav();
            translationFav.setLanguageInputText(cursor.getColumnName(0));
            translationFav.setLanguageInputText(cursor.getColumnName(1));
            translationFav.setLanguageInputText(cursor.getColumnName(2));
            translationFav.setLanguageInputText(cursor.getColumnName(3));
            another_array.add(translationFav);

        }*/
        return another_array;
    }

    public void dataDelete(String outputResult) {
        SQLiteDatabase db = this.getWritableDatabase();
        //int in = sqLiteDatabase.delete(TABLE_NAME, "Id=?", new String[]{String.valueOf(id)});
        try {
            sqLiteDatabase.delete(TABLE_NAME, "outputText=?",new String[]{String.valueOf(outputResult)});

        } catch (Exception e) {

            Log.d(TAG, "Error while trying to delete all posts and users");

        } finally {
            db.close();
        }

        //db.close();
        //return in;
    }

}