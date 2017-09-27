package com.dev.ramon.sqlitecrud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dev.ramon.sqlitecrud.objects.Course;

import java.util.ArrayList;

/**
 * Created by ramondev on 9/24/17.
 */

public class BDSQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CourseDB";
    private static final String TABLE = "course";
    private static final String[] COLUMN = {"id","name","description","classHours","registerDate","status"};

    public BDSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE course ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "name TEXT,"+
                "description TEXT,"+
                "classHours INTEGER,"+
                "registerDate DATETIME,"+
                "status BIT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS course");
        this.onCreate(sqLiteDatabase);
    }

    public void addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", course.getName());
        values.put("description", course.getDescription());
        values.put("classHours", new Integer(course.getClassHours()));
        values.put("registerDate", course.getRegisterDate());
        values.put("status", new Byte((byte)(course.getStatus()==true ? 1 : 0)));
        db.insert(TABLE, null, values);
        db.close();
    }

    public Course getCourse(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE, // a. tabela
                COLUMN, // b. colunas
                " id = ?", // c. colunas para comparar
                new String[] { String.valueOf(id) }, // d. parâmetros
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Course course = parceCourse(cursor);
            return course;
        }
    }

    public ArrayList<Course> getCourses() {
        ArrayList<Course> list = new ArrayList<Course>();
        String query = "SELECT * FROM " + TABLE + " ORDER BY registerDate DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Course course = parceCourse(cursor);
                list.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private Course parceCourse(Cursor cursor) {
        Course course = new Course();
        course.setId(Integer.parseInt(cursor.getString(0)));
        course.setName(cursor.getString(1));
        course.setDescription(cursor.getString(2));
        course.setClassHours(Integer.parseInt(cursor.getString(3)));
        course.setRegisterDate(cursor.getString(4));
        course.setStatus(Integer.parseInt(cursor.getString(5))==1);
        return course;
    }
}
