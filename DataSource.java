package com.example.android.rxsearch;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;




public abstract class DataSource {


    /**
     * This method simply returns a new ArrayList of Strings that contains list of students, that
     * are displaying in the MainActivity
     *
     * @return ArrayList of students, where we search
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
     * getSearchDataObservable is called by the initPublisher to display data that contains
     * right sequence of characters
     *
     * @param searchKey A String of characters that users type in the SearchView
     * @return An Observable that emits found names
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
