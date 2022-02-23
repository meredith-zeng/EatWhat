package com.example.eatwhat.mainActivityFragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.eatwhat.R;

import java.util.ArrayList;

public class RestaurantFragment extends Fragment {

    private ArrayList<String> categoryList;
    private ArrayList<String> statesList;

    private String selectedCategory;
    private String selectedState;
    private String selectedCity;

    public RestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String [] categoryArray = {"Chinese", "American", "Italian", "French", "Korean", "Japanese"};
        categoryList = new ArrayList<>();
        for (int i = 0; i < categoryArray.length; i++) {
            categoryList.add(categoryArray[i]);
        }

        String [] statesArray = { "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut",
                "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
                "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
                "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey",
                "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon",
                "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "ennessee", "Texas", "Utah",
                "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
        statesList = new ArrayList<>();
        for (int i = 0; i < statesArray.length; i++) {
            statesList.add(statesArray[i]);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        TextView categoryView = view.findViewById(R.id.selectCategoryView);
        TextView statesView = view.findViewById(R.id.selectStateView);

        createSpinnerDialog(categoryView, categoryList, "category");
        createSpinnerDialog(statesView, statesList, "state");
        return view;
    }

    private void createSpinnerDialog(TextView textview, ArrayList<String> list, String type) {
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.restaurant_spinner_search);
                dialog.getWindow().setLayout(800, 1200);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                // Initialize and assign variable
                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);

                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        textview.setText(adapter.getItem(position));
                        switch (type) {
                            case "category":
                                selectedCategory = adapter.getItem(position);
                                break;
                            case "state":
                                selectedState = adapter.getItem(position);
                                break;
                            case "city":
                                selectedCity = adapter.getItem(position);
                                break;
                        }

                        //System.out.println(selectedCategory + "  " + selectedState + "  " + selectedCity);
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}