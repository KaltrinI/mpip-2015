package mk.ukim.finki.mpip.listviewshowcase.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoDbOpenHelper extends SQLiteOpenHelper {

  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_LAST_NAME = "last_name";
  public static final String COLUMN_VISITS = "visitors";
  public static final String TABLE_NAME = "Persons";

  private static final int DATABASE_VERSION = 1;

  private static final String DATABASE_NAME = "PersonsDatabase.db";

  private static final String DATABASE_CREATE = String
    .format("create table %s (%s  integer primary key autoincrement, "
        + "%s text not null, %s text not null, %s integer default 0);",
      TABLE_NAME, COLUMN_ID, COLUMN_NAME, COLUMN_LAST_NAME, COLUMN_VISITS);

  public ToDoDbOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Don't do this in real end-user applications
    db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
    onCreate(db);
  }

}
