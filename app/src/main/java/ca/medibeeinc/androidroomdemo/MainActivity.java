package ca.medibeeinc.androidroomdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //To display the current contents of the database, add an observer that observes the LiveData in the ViewModel
    //Whenever the data changes, the onChanged() callback is invoked, which calls the adapter's setWords() method
    //to update the adapter's cached data and refresh the displayed list.
    private WordViewModel mWordViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Use ViewModelProvider to associate your ViewModel with your Activity.
        //When your Activity first starts, the ViewModelProviders will create the ViewModel.
        //When the activity is destroyed, for example through a configuration change, the ViewModel persists.
        //When the activity is re-created, the ViewModelProviders return the existing ViewModel.
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        //add an observer for the LiveData returned by getAlphabetizedWords()
        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {
                //The onChanged() method fires when the observed data changes and the activity is in the foreground
                adapter.setWords(words);        // Update the cached copy of the words in the adapter
                System.out.println("Updated words: " + words.toString());
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });

        Button button = findViewById(R.id.delete_all_button);
        button.setOnClickListener(v -> {
            mWordViewModel.deleteAll();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }
}