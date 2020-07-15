package ca.medibeeinc.androidroomdemo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_table")     //Each @Entity class represents a SQLite table.
// Annotate your class declaration to indicate that it's an entity.
// You can specify the name of the table if you want it to be different from the name of the class.
// This names the table "word_table".
public class Word {

    @PrimaryKey                     //Every entity needs a primary key. To keep things simple, each word acts as its own primary key.
    @NonNull                        //Denotes that a parameter, field, or method return value can never be null.
    @ColumnInfo(name = "word")      //Specify the name of the column in the table if you want it to be different from the name of the member variable.
    private String mWord;

    public Word(@NonNull String word) {
        this.mWord = word;
    }

    public String getWord() {
        return this.mWord;
    }
}
