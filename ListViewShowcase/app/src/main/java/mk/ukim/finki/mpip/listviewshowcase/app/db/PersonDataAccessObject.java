package mk.ukim.finki.mpip.listviewshowcase.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import mk.ukim.finki.mpip.listviewshowcase.app.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ristes on 11/25/15.
 */
public class PersonDataAccessObject {
  private String[] allColumns = {
    ToDoDbOpenHelper.COLUMN_ID,
    ToDoDbOpenHelper.COLUMN_NAME,
    ToDoDbOpenHelper.COLUMN_LAST_NAME,
    ToDoDbOpenHelper.COLUMN_VISITS
  };

  // Database fields
  private SQLiteDatabase database;

  private ToDoDbOpenHelper dbHelper;

  public PersonDataAccessObject(Context context) {
    dbHelper = new ToDoDbOpenHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    database.close();
    dbHelper.close();
  }

  public boolean insert(Person item) {

    if (item.id != null) {
      return update(item);
    }

    long insertId = database.insert(ToDoDbOpenHelper.TABLE_NAME, null,
      itemToContentValues(item));

    if (insertId > 0) {
      item.id = insertId;
      return true;
    } else {
      return false;
    }

  }

  public boolean update(Person item) {
    long numRowsAffected = database.update(ToDoDbOpenHelper.TABLE_NAME,
      itemToContentValues(item), ToDoDbOpenHelper.COLUMN_ID + " = "
        + item.id, null);
    return numRowsAffected > 0;
  }

  public List<Person> getAllItems() {
    List<Person> items = new ArrayList<Person>();

    Cursor cursor = database.query(ToDoDbOpenHelper.TABLE_NAME, allColumns,
      null, null, null, null, ToDoDbOpenHelper.COLUMN_VISITS + " DESC");

    if (cursor.moveToFirst()) {
      do {
        items.add(cursorToItem(cursor));
      } while (cursor.moveToNext());
    }
    cursor.close();
    return items;
  }

  public Person getById(long id) {

    Cursor cursor = database.query(ToDoDbOpenHelper.TABLE_NAME, allColumns,
      ToDoDbOpenHelper.COLUMN_ID + " = " + id, null, null,
      null, null);
    try {
      if (cursor.moveToFirst()) {
        return cursorToItem(cursor);
      } else {
        // no items found
        return null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    } finally {
      cursor.close();
    }

  }

  protected Person cursorToItem(Cursor cursor) {
    Person item = new Person();
    item.id = cursor.getLong(
      cursor.getColumnIndex(ToDoDbOpenHelper.COLUMN_ID)
    );

    item.name = cursor.getString(
      cursor.getColumnIndex(ToDoDbOpenHelper.COLUMN_NAME)
    );

    item.lastName = cursor.getString(
      cursor.getColumnIndex(ToDoDbOpenHelper.COLUMN_LAST_NAME)
    );

    item.visits = cursor.getInt(
      cursor.getColumnIndex(ToDoDbOpenHelper.COLUMN_VISITS)
    );

    return item;
  }

  protected ContentValues itemToContentValues(Person item) {
    ContentValues values = new ContentValues();
    if (item.id != null) {
      values.put(ToDoDbOpenHelper.COLUMN_ID, item.id);
    }
    values.put(ToDoDbOpenHelper.COLUMN_NAME, item.name);
    values.put(ToDoDbOpenHelper.COLUMN_LAST_NAME, item.lastName);
    values.put(ToDoDbOpenHelper.COLUMN_VISITS, item.visits);
    return values;
  }
}
