package com.example.report;
/*
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBhelper extends SQLiteOpenHelper {

    public DBhelper(Context context) {
        super(context, "Report.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table emails(name TEXT primary key)");//template name
        db.execSQL("create Table template_options(name TEXT primary key, type TEXT, defalt BOOLEAN, location BOOLEAN, " +
                "synced BOOLEAN)");//template name
        for (int i = 0; i<10;i++) {
            db.execSQL("ALTER TABLE emails ADD nr"+i+" TEXT DEFAULT ''");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists emails");
        db.execSQL("drop Table if exists template_options");

        onCreate(db);
    }

    public Boolean insertTemplate(Template template) {
        SQLiteDatabase DB = this.getWritableDatabase();
        List<String> row = findIfFound("email",template.name);
        if(row.size()>0) {
            for (String i : template.email) {//update template
                if (!row.contains(i)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("nr"+row.indexOf(""), i);
                    DB.update("emails", contentValues, "email = ?", new String[]{});
                    return true;
                }
            }
        }
        return (insert(DB, template)>-1);//new template
    }

    public long insert(SQLiteDatabase db, Template template) {
        ContentValues contentValues = new ContentValues();
        for (String i : template.email) {
            contentValues.put("nr"+template.email.indexOf(i), i);
        }
        return db.insert(template.name, null,contentValues);
    }

    public Template getTemplate(String name){
        return new Template(findIfFound("email",name), name);
    }

    public List<String> findIfFound(String table, String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from "+table+" where name = ?", new String[] {name});
        List<String> row = new ArrayList<>();
        if(cursor.getCount()>0) {
            for (int element = 0; element < cursor.getCount(); element++) {
                row.add(cursor.getString(element));
            }
        }
        cursor.close();
        return row;
    }

    public List<Template> getAllTemplate() {
        List<Template> templateList = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select name from email", new String[]{});
        for(int i = 0; i < cursor.getCount(); i++) {
            templateList.add(getTemplate(cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();
        return templateList;
    }
    public void removeEmail(String name, String email){
        List<String> row = findIfFound("email", name);
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nr"+row.indexOf(email),"");
        DB.update("emails", contentValues, "name = ?", new String[]{name});
    }
    public void deleteAll() {
        //remove row where name = name
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.rawQuery("Delete from emails", new String[]{}).close();
    }

    public void removeTemplate(String name) {
        //remove row where name = name
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.rawQuery("Delete from emails where name = ?", new String[]{name}).close();
    }
}
*/