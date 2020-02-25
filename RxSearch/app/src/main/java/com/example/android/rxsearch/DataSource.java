package com.example.android.rxsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

/**
 *
 */
public abstract class DataSource {
    /**
     *
     * @return
     */
    public static ArrayList<String> getStudentList() {
        //
        List<String> studentList = Arrays.asList(
                "Anna",
                "Bob",
                "Den",
                "Frodo",
                "Fenvik",
                "Goldman",
                "God",
                "Gena",
                "Harris",
                "Inna",
                "Kate",
                "Lana",
                "Man",
                "Nike",
                "Nutella",
                "Orc");

        return new ArrayList<String>(studentList);
    }

    /**
     *
     * @param searchKey
     * @return
     */
    public static Observable<ArrayList<String>> getSearchDataObservable(String searchKey) {
        ArrayList<String> searchData = getStudentList();
        ArrayList<String> searchResult = new ArrayList<>();

        searchKey = searchKey.toUpperCase();

        //
        for (String name : searchData) {
            if (name.toUpperCase().contains(searchKey)) {
                searchResult.add(name);
            }
        }

        return Observable.just(searchResult);
    }
}
