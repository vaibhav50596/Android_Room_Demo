package ca.medibeeinc.androidroomdemo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

//A Repository class abstracts access to multiple data sources.
//The Repository is not part of the Architecture Components libraries, but is a suggested best practice for code separation and architecture.
//A Repository class provides a clean API for data access to the rest of the application.

//A Repository manages queries and allows you to use multiple backends.
//In the most common example, the Repository implements the logic for deciding whether to fetch data from a network or use results cached in a local database.
class WordRepository {
    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAlphabetizedWords();
    }

    LiveData<List<Word>> getmAllWords() {
        return mAllWords;
    }

    void insert(Word word) {
        WordRoomDatabase.databaseWriterExecutor.execute(() -> {
            mWordDao.insert(word);
        });
    }

    void deleteAll() {
        WordRoomDatabase.databaseWriterExecutor.execute(() -> {
            mWordDao.deleteAll();
        });
    }
}

//The DAO is passed into the repository constructor as opposed to the whole database.
//This is because you only need access to the DAO, since it contains all the read/write methods for the database.
//There's no need to expose the entire database to the repository.
//The getAllWords method returns the LiveData list of words from Room;
//we can do this because of how we defined the getAlphabetizedWords method to return LiveData in the "The LiveData class" step.
//Room executes all queries on a separate thread.
//Then observed LiveData will notify the observer on the main thread when the data has changed.
//We need to not run the insert on the main thread, so we use the ExecutorService we created in the WordRoomDatabase to perform
//the insert on a background thread.