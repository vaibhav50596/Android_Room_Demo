package ca.medibeeinc.androidroomdemo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//What is a Room database?
//Room is a database layer on top of an SQLite database.
//Room takes care of mundane tasks that you used to handle with an SQLiteOpenHelper.
//Room uses the DAO to issue queries to its database.
//By default, to avoid poor UI performance, Room doesn't allow you to issue queries on the main thread. When Room queries return LiveData, the queries are automatically run asynchronously on a background thread.
//Room provides compile-time checks of SQLite statements.
//Room database class must be abstract and extend RoomDatabase.
//Note: When you modify the database schema, you'll need to update the version number and define a migration strategy
//For a sample, a destroy and re-create strategy can be sufficient. But, for a real app, you must implement a migration strategy:
//https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929

@Database(entities = {Word.class}, version = 1, exportSchema = false)  //Each entity corresponds to a table that will be created in the database
//Database migrations are beyond the scope of this codelab, so we set exportSchema to false here to avoid a build warning.
//In a real app, you should consider setting a directory for Room to use to export the schema so you can check the current schema into your version control system.
public abstract class WordRoomDatabase extends RoomDatabase {

    public abstract WordDao wordDao();  //The database exposes DAOs through an abstract "getter" method for each @Dao

    private static volatile WordRoomDatabase INSTANCE;  //We've defined a singleton, WordRoomDatabase, to prevent having multiple instances of the database opened at the same time
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);  //creating an ExecutorService with a fixed thread pool that you will use to run db operations asynchronously on a background thread

    //getDatabase() returns the singleton and creates DB only once, using Room's DatabaseBuilder to create RoomDatabase object
    static WordRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WordRoomDatabase.class, "word_database").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            //If you want to keep data through app restarts, comment out the following block
            databaseWriterExecutor.execute(() -> {
                //Populate the database in the background, If you want to start with more words, just add them.
                WordDao dao = INSTANCE.wordDao();
                //dao.deleteAll();

                Word word = new Word("Hello - Default word added");
                dao.insert(word);
            });
        }
    };
}
