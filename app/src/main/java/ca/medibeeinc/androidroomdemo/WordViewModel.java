package ca.medibeeinc.androidroomdemo;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

//The ViewModel's role is to provide data to the UI and survive configuration changes.
//A ViewModel acts as a communication center between the Repository and the UI.
//You can also use a ViewModel to share data between fragments.

//A ViewModel holds your app's UI data in a lifecycle-conscious way that survives configuration changes.
//Separating your app's UI data from your Activity and Fragment classes lets you better follow the single responsibility principle.
// - Your activities and fragments are responsible for drawing data to the screen
// - while your ViewModel can take care of holding and processing all the data needed for the UI
public class WordViewModel extends AndroidViewModel {        //Created a class called WordViewModel that gets the Application as a parameter and extends AndroidViewModel

    private WordRepository mRepository;                     //Added a private member variable to hold a reference to the repository
    private LiveData<List<Word>> mAllWords;

    public WordViewModel(Application application) {         //Implemented a constructor that creates the WordRepository
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getmAllWords();             //initialized the mAllWords LiveData using the repository
    }

    LiveData<List<Word>> getAllWords() {                    //Added a getAllWords() method to return a cached list of words
        return mAllWords;
    }

    public void insert(Word word) {                         //Created a wrapper insert() method that calls the Repository's insert() method
        mRepository.insert(word);                           //implementation of insert() is encapsulated from the UI
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
}
