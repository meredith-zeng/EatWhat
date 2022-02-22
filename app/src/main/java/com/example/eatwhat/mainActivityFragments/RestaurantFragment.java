package com.example.eatwhat.mainActivityFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.eatwhat.R;

import java.util.ArrayList;
import java.util.List;


public class RestaurantFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String[] category = {"Chinese", "American", "French", "Chinese", "American", "French", "Chinese", "American", "French","Chinese", "American", "French"};
        String[] states = {"California", "Virginia", "Ohio"};
        String[] cities = {"My Profile", "My Posts", "Review History"};
        final List<String> categoryList = new ArrayList<>();
        final List<String> statesList = new ArrayList<>();
        final List<String> citiesList = new ArrayList<>();

        for (int i = 0; i < category.length; i++) {
            categoryList.add(category[i]);
        }

        for (int i = 0; i < states.length; i++) {
            statesList.add(states[i]);
        }

        for (int i = 0; i < states.length; i++) {
            citiesList.add(cities[i]);
        }

        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        Spinner categorySpinner = (Spinner)view.findViewById(R.id.category_spinner);
        Spinner statesSpinner = (Spinner)view.findViewById(R.id.states_spinner);
        Spinner citiesSpinner = (Spinner)view.findViewById(R.id.cities_spinner);
        ArrayAdapter categoryAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, categoryList);
        ArrayAdapter statesAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, statesList);
        ArrayAdapter citiesAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, citiesList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        statesSpinner.setAdapter(statesAdapter);
        citiesSpinner.setAdapter(citiesAdapter);
        return view;
    }
}