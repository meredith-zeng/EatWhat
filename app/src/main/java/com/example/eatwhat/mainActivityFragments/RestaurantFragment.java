package com.example.eatwhat.mainActivityFragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatwhat.R;
import com.example.eatwhat.adapter.RestaurantAdapter;
import com.example.eatwhat.cardview.PostCard;
import com.example.eatwhat.cardview.RestaurantCard;
import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;
import com.example.eatwhat.service.pojo.Business;
import com.example.eatwhat.service.pojo.Restaurant;

import java.util.ArrayList;

//import butterknife.BindView;
//import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantFragment extends Fragment {

    private ArrayList<String> categoryList;
    private ArrayList<String> statesList;

    private String selectedCategory;
    private String selectedState;
    private String selectedCity;

    private RecyclerView recyclerView;

    public RestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        createSpinners(view, container);
        recyclerView = (RecyclerView) view.findViewById(R.id.restaurant_recyclerview);
        initData();
        return view;
    }

    private void createSpinners(View view, ViewGroup container) {
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

        TextView categoryView = view.findViewById(R.id.selectCategoryView);
        TextView statesView = view.findViewById(R.id.selectStateView);

        createSpinnerDialog(categoryView, categoryList, "category");
        createSpinnerDialog(statesView, statesList, "state");

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

    private void initData(){
//        String imageUrl = "https://s3-media3.fl.yelpcdn.com/bphoto/XUS57sY4C2BUUjiP2-vLqw/o.jpg";
//        restaurantCardArrayList.add(new RestaurantCard(imageUrl, "title", "content", false));
//        restaurantCardArrayList.add(new RestaurantCard(imageUrl, "title", "content", false));
//        restaurantCardArrayList.add(new RestaurantCard(imageUrl, "title", "content", false));
//        restaurantCardArrayList.add(new RestaurantCard(imageUrl, "title", "content", false));
//        restaurantCardArrayList.add(new RestaurantCard(imageUrl, "title", "content", false));
//        restaurantCardArrayList.add(new RestaurantCard(imageUrl, "title", "content", false));
//        restaurantCardArrayList.add(new RestaurantCard(imageUrl, "title", "content", false));

        RetrofitClient retrofitClient = new RetrofitClient();

        ArrayList<RestaurantCard> restaurantCardArrayList = new ArrayList<>();
        RestaurantService methods = retrofitClient.getRetrofit().create(RestaurantService.class);
        String location = "Santa Clara University";
        Call<Restaurant> call = methods.queryRestaurantByLocation(location, 10, 1);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                Log.e("Restaurant card Test", response.body() + " ");
                if (response.code() == 200){

                    for (Business business: response.body().getBusinesses()){
                        RestaurantCard restaurantCard = new RestaurantCard(business.getImageUrl(), business.getName(), business.getCategories().toString(), false);
                        restaurantCardArrayList.add(restaurantCard);
                    }
                    initRecycleView(restaurantCardArrayList);
                }

            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {

            }
        });
    }

    private void initRecycleView(ArrayList<RestaurantCard> restaurantCardArrayList) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            RestaurantAdapter restaurantAdapter = new RestaurantAdapter(getActivity(), restaurantCardArrayList);
            recyclerView.setAdapter(restaurantAdapter);

    }

}