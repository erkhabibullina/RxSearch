package com.example.android.rxsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.SearchView;

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

    private SearchView mSearchStudentView;
    private RecyclerView mStudentNamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FIXME (1) local or private global all fields with "m-" prefix?
        //
        mPublisher = PublishSubject.create();
        //
        mAdapter = new StudentAdapter();
        mAdapter.setStudentNames(DataSource.getStudentList());

        //
        mSearchStudentView = findViewById(R.id.sv_search_student);
        //
        mStudentNamesList = findViewById(R.id.rv_student_list);

        initStudentNamesRecyclerView(mStudentNamesList, mAdapter);
        initSearchStudentView(mSearchStudentView, mPublisher);

        initPublisher(mPublisher, mAdapter);
    }

    /**
     *
     * @param studentNamesRecyclerView
     * @param adapter
     */
    private void initStudentNamesRecyclerView(@NonNull RecyclerView studentNamesRecyclerView, @NonNull StudentAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        studentNamesRecyclerView.setAdapter(adapter);
        studentNamesRecyclerView.setLayoutManager(layoutManager);
    }


    /**
     *
     * @param searchView
     * @param publisher
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
     *
     * @param publisher
     * @param adapter
     */
    private void initPublisher(@NonNull PublishSubject<String> publisher, @NonNull StudentAdapter adapter) {
        publisher.debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap((Function<String, ObservableSource<ArrayList<String>>>) key ->
                        DataSource.getSearchDataObservable(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(names -> {
                    adapter.clearStudentNames();
                    adapter.setStudentNames(names);
                });
    }
}
