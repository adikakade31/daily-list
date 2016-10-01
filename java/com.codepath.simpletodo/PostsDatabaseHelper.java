package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.simpletodo.Model.Note;
import com.codepath.simpletodo.Model.Todo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditikakadebansal on 9/22/16.
 */
public class PostsDatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "SimpleTodo";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_NOTE = "Notes";
    public static final String TABLE_TODO = "Todos";

    // List Table Columns
    public static final String PK = "primaryKey";
    public static final String DESCRIPTION = "description";
    public static final String DATE ="date";
    public static final String DEADLINE = "deadline";


    private static PostsDatabaseHelper sInstance;

    public static synchronized PostsDatabaseHelper getInstance(Context context) {
         if (sInstance == null) {
            sInstance = new PostsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private PostsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

   @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, %s VARCHAR(30))"
                ,TABLE_NOTE, PK, DESCRIPTION, DATE);
       db.execSQL(CREATE_NOTES_TABLE);

       String CREATE_TODOS_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                       "%s TEXT, %s VARCHAR(30), %s VARCHAR(30))"
               ,TABLE_TODO, PK, DESCRIPTION, DATE, DEADLINE);
       db.execSQL(CREATE_TODOS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
     Get all entities from the database
      */

    public List<ISimpleTodoEntity> getAllEntities(String date, String tabType) {
        List<ISimpleTodoEntity> entities = new ArrayList<>();
        String SELECT_QUERY="";
        switch(tabType){
            case "notes" :
                SELECT_QUERY =
                        String.format("SELECT * FROM %s where %s = '%s'",
                                TABLE_NOTE, DATE, date);
                break;
            case "todos" :
                try{
                    String convertedDate = CommonDateFunctions.convertDateToYYYYMMDD(date);
                    String percentage = "%";
                    SELECT_QUERY = String.format("Select * from %s where %s LIKE '%s%s'",TABLE_TODO,DEADLINE,convertedDate,percentage);
                }
                catch(ParseException parseException){
                    parseException.printStackTrace();
                }
                break;
        }

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ISimpleTodoEntity entity = null;
                    switch(tabType) {
                        case "notes" :
                            entity = Note.getInstance(cursor);
                            break;
                        case "todos" :
                            entity = Todo.getInstance(cursor);
                            break;
                    }

                    entities.add(entity);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            //Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return entities;
    }

    /*
    Get a single note in the database
     */
    public ISimpleTodoEntity getEntity(long primaryKey, String entityType) {
        SQLiteDatabase db = getReadableDatabase();

        String tableName="";
        switch(entityType){
            case "notes" :
                tableName = TABLE_NOTE;
                break;
            case "todos" :
                tableName = TABLE_TODO;
                break;
        }
        String SELECT_QUERY =
                String.format("SELECT * FROM %s where %s = %d",
                        tableName, PK, primaryKey);

        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        ISimpleTodoEntity result = null;
        try {
            if (cursor.moveToFirst()) {
                ISimpleTodoEntity entity = null;
                switch(entityType) {
                    case "notes":
                        result = Note.getInstance(cursor);
                    case "todos":
                        result = Todo.getInstance(cursor);
                }
            }
        } catch (Exception e) {
            //Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    /*
    Add a single note in the database
     */
    public Note addNote(String noteDescription, String date) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DESCRIPTION, noteDescription);
            values.put(DATE, date);
            long noteId = db.insertOrThrow(TABLE_NOTE, null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
            return new Note(noteDescription, noteId, date);
        } catch (Exception e) {
            db.endTransaction();//Log.d(TAG, "Error while trying to add post to database");
        }
        return null;
    }

    /*
    Add a single to-do in the database
     */
    public Todo addTodo(String noteDescription, String date, String deadline) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DESCRIPTION, noteDescription);
            values.put(DATE, date);
            values.put(DEADLINE, deadline);
            long noteId = db.insertOrThrow(TABLE_TODO, null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
            return new Todo(noteDescription, noteId, date, deadline);
        } catch (Exception e) {
            db.endTransaction();//Log.d(TAG, "Error while trying to add post to database");
        }
        return null;
    }



    /*
    Update a single note in the database
       */
    public Note updateSingleNote(Note toUpdate, String description) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            String strFilter = PK+"=" + toUpdate.getPrimaryKey();
            ContentValues args = new ContentValues();
            args.put(DESCRIPTION, description);
            db.update(TABLE_NOTE, args, strFilter, null);
            toUpdate.setDescription(description);

        }
        catch (Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
            throw e;
            //return false;
        }
        return toUpdate;
    }

    /*
    Update a single note in the database
       */
    public Todo updateSingleTodo(Todo toUpdate, String description, String deadline) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            String strFilter = PK+"=" + toUpdate.getPrimaryKey();
            ContentValues args = new ContentValues();
            args.put(DESCRIPTION, description);
            args.put(DEADLINE, deadline);
            db.update(TABLE_TODO, args, strFilter, null);
            toUpdate.setDescription(description);
            toUpdate.setDeadline(deadline);
        }
        catch (Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
            throw e;
            //return false;
        }
        return toUpdate;
    }
    /*
    Delete an entity from the database
     */

    public boolean delete(ISimpleTodoEntity entity) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.delete(entity.getTableName(), PK + "=" + entity.getPrimaryKey(), null) > 0;
        }
        catch (Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
            throw e;
        }
    }


    /*
     Delete all notes in the database
      */
    public void deleteAllNotes() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NOTE, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            //Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }
    /*
    Delete table from the database
     */
    public void deleteSchema() {
        SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);

    }
}
