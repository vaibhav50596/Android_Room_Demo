package ca.medibeeinc.androidroomdemo;

//A DAO (data access object) validates your SQL at compile-time and associates it with a method.
//In your Room DAO, you use handy annotations, like @Insert, to represent the most common database operations!
//Room uses the DAO to create a clean API for your code.
//The DAO must be an interface or abstract class. By default, all queries must be executed on a separate thread.
//Room creates each DAO implementation at compile time.

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao                                                    //The @Dao annotation identifies it as a DAO class for Room.
public interface WordDao {                              //WordDao is an interface; DAOs must either be interfaces or abstract classes.

    //@Insert is a special DAO method annotation where you don't have to provide any SQL! There are also @Delete and @Update
    @Insert(onConflict = OnConflictStrategy.IGNORE)     //allowing the insert of the same word multiple times by passing conflict resolution strategy
    void insert(Word word);                             //Declares a method to insert one word

    @Query("DELETE FROM word_table")                    //There is no convenience annotation for deleting multiple entities, so it's annotated with the generic @Query. SQL query needs to be provided to @Query
    void deleteAll();                                   //declares a method to delete all the words.

    @Query("SELECT * from word_table ORDER BY word ASC")//Returns a list of words sorted in ascending order.
    LiveData<List<Word>> getAlphabetizedWords();                  //A method to get all the words and have it return a List of Words
}
