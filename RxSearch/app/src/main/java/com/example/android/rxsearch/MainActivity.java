package com.example.android.rxsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private PublishSubject<String> mPublisher;
    private StudentAdapter mAdapter;

    private TextView mTitleText;
    private SearchView mSearchStudentView;
    private RecyclerView mStudentNamesList;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // A new publisher that emits items to currently subscribed Observers
        mPublisher = PublishSubject.create();
        mTitleText = findViewById(R.id.title_text_view);

        // A new adapter that we use to show data (student's names)
        mAdapter = new StudentAdapter();
        mAdapter.setStudentNames(DataSource.getStudentList());

        // A search view for our activity
        mSearchStudentView = findViewById(R.id.sv_search_student);
        mSearchStudentView.setMaxWidth(Integer.MAX_VALUE);
        mSearchStudentView.setOnSearchClickListener(v -> mTitleText.setVisibility(View.GONE));
        mSearchStudentView.setOnCloseListener(() -> {
            mTitleText.setVisibility(View.VISIBLE);
            return false;
        });


        // A recycler view for our activity
        mStudentNamesList = findViewById(R.id.rv_student_list);
        mStudentNamesList.setHasFixedSize(true);
        initStudentNamesRecyclerView(mStudentNamesList, mAdapter);
        initSearchStudentView(mSearchStudentView, mPublisher);

        initPublisher(mPublisher, mAdapter);

        fab = findViewById(R.id.floating_action_button);
        fab.bringToFront();
        fab.setOnClickListener(v -> {
            Context context = MainActivity.this;
            Class destinationActivity = ChildActivity.class;
            Intent intent = new Intent(context, destinationActivity);
            startActivity(intent);
        });

    }


    /**
     * This method simply creates a new LayoutManager and a new Adapter, and sets LayoutManager
     * on this adapter.
     * A LayoutManager is responsible for measuring and positioning item views
     * within a RecyclerView as well as determining the policy for when to recycle item
     * views that are no longer visible to the user.
     *
     * @param studentNamesRecyclerView A RecyclerView on which we are setting an adapter.
     * @param adapter                  An adapter that we setting on a RecyclerView
     */
    private void initStudentNamesRecyclerView(@NonNull RecyclerView studentNamesRecyclerView, @NonNull StudentAdapter adapter) {

        studentNamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        studentNamesRecyclerView.setAdapter(adapter);
        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
    }


    /**
     * This method just callbacks for changes to the query text.
     *
     * @param searchView A SearchView where users type this query text
     * @param publisher  A PublishSubject that we to emit data to currently
     *                   subscribed Observers.
     */
    private void initSearchStudentView(@NonNull SearchView searchView, @NonNull PublishSubject publisher) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                publisher.onComplete();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                publisher.onNext(newText);

                return true;
            }
        });
    }


    /**
     * In this method we use an operator debounce in order to filter our found student names
     * emitted by the source Observable that are rapidly followed by another emitted item. Also we
     * use distinctUntilChanged in order to filter our Observable by allowing items through
     * that have not already been emitted. Then we use "switchMap" to transform items emitted by
     * an Observable into an Observable and unsubscribe previous observers once a new Observer
     * will subscribe. After we use subscribeOn and observeOn in order to instruct an Observable
     * to do its work(subscribeOn) and send notifications to observers(observeOn)on a particular
     * Scheduler. And in the end we use subscribe operator to connect our observer to an Observable.
     *
     * @param publisher A PublishSubject that we to emit data to currently subscribed Observers.
     * @param adapter   An adapter that we have set on our RecyclerView.
     */

    private void initPublisher(@NonNull PublishSubject<String> publisher, @NonNull StudentAdapter adapter) {
        publisher.debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap((Function<String, ObservableSource<ArrayList<String>>>)
                        DataSource::getSearchDataObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(names -> {
                    adapter.clearStudentNames();
                    adapter.setStudentNames(names);
                });
    }
}
