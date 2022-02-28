package com.example.eatwhat.mainActivityFragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatwhat.R;
import com.example.eatwhat.activity.restaurant.RestaurantPageActivity;
import com.example.eatwhat.adapter.RestaurantAdapter;
import com.example.eatwhat.cardview.RestaurantCard;
import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;
import com.example.eatwhat.service.pojo.Business;
import com.example.eatwhat.service.pojo.Restaurant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import butterknife.BindView;
//import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantFragment extends Fragment  {

    int count = 0, limit = 4;
    private ArrayList<String> categoryList;
    private ArrayList<String> sortConditionList;
    ArrayList<RestaurantCard> restaurantCardArrayList = new ArrayList<>();

    private String selectedCategory = null;
    private String selectedCity = "Blacksburg";
    private String selectedSortCondition = null;

    private FusedLocationProviderClient fusedLocationClient;
    private RecyclerView recyclerView;

    public RestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        getCurrentLocation();
        createSpinners(view, container);
        recyclerView = (RecyclerView) view.findViewById(R.id.restaurant_recyclerview);
        initData();
        pullUpToRefresh(view);
        return view;
    }

    private void pullUpToRefresh(View rootView) {
        NestedScrollView nestedSV = (NestedScrollView) rootView.findViewById(R.id.swipe_container);
        ProgressBar loadingPB = (ProgressBar)rootView.findViewById(R.id.pb_loading);
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    count += limit;
                    // on below line we are making our progress bar visible.
                    loadingPB.setVisibility(View.VISIBLE);
                    if (count < 100) {
                        // on below line we are again calling
                        // a method to load data in our array list.
                        initData();
                    }
                }
            }
        });
    }

    private void createSpinners(View view, ViewGroup container) {
        String [] categoryArray  = new String[]{"tradamerican", "arabic", "asianfusion", "brazilian",
                "barbeque", "breakfast_brunch", "british", "buffets", "burgers", "cafes",
                "cheesesteaks", "chinese", "chicken_wings", "creperies", "dimsum", "diners",
                "hotdogs", "foodstands", "french", "german", "gluten_free", "greek", "indpak",
                "irish", "italian", "japanese", "korean", "latin", "raw_food", "mediterranean",
                "mexican", "russian", "salad", "pizza", "steak", "thai",
                "seafood", "spanish", "vegetarian", "vietnamese"};

        categoryList = new ArrayList<>();
        for (int i = 0; i < categoryArray.length; i++) {
            categoryList.add(categoryArray[i]);
        }

//        String [] sortConditionArray = { "Rating", "$-$$", "$-$$$", "$$ - $$$", "$$ - $$$$", "$$$ - $$$$$"};
//        sortConditionList = new ArrayList<>();
//        for (int i = 0; i < sortConditionArray.length; i++) {
//            sortConditionList.add(sortConditionArray[i]);
//        }

        TextView categoryView = view.findViewById(R.id.selectCategoryView);
        createSpinnerDialog(categoryView, categoryList, "category");
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
                                Log.e("choose category", selectedCategory);

                                if (selectedCategory != null) {
                                    count = 0;
                                    limit = 2;
                                    restaurantCardArrayList.clear();
                                    initData();
                                }
                                break;
                            case "sort":
                                selectedSortCondition = adapter.getItem(position);
                                Log.e("choose location", selectedSortCondition);
                                if (selectedCategory != null && selectedSortCondition != null) {
                                    restaurantCardArrayList.clear();
                                    initData();
                                }
                                break;
                        }

                        dialog.dismiss();
                    }
                });
            }
        });
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("do not have permission");
            return;
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    double myLong = location.getLongitude();
                    double myLat = location.getLatitude() ;
                    LatLng latLng = new LatLng(myLat, myLong);
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                    try {
                        List<Address> addresses = null;
                        addresses = geocoder.getFromLocation(myLat, myLong, 1);
                        selectedCity = addresses.get(0).getLocality().toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    private void initData(){
        RetrofitClient retrofitClient = new RetrofitClient();
        RestaurantService methods = retrofitClient.getRetrofit().create(RestaurantService.class);
        System.out.println(selectedCity + "    " + selectedCategory);
        Call<Restaurant> call = methods.queryRestaurantByCategory(selectedCity, selectedCategory,  2, count);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.code() == 200){
                    System.out.println("Network " + response.code());
                    System.out.println(response.toString());
                    for (Business business: response.body().getBusinesses()){
                        RestaurantCard restaurantCard = new RestaurantCard(business.getImageUrl(), business.getName(), business.getCategories().toString(), false);
                        System.out.println(restaurantCard.getTitle());
                        restaurantCardArrayList.add(restaurantCard);
                    }
                    System.out.println("finished");
                    initRecycleView(restaurantCardArrayList);
                }
                else {
                    System.out.println("Network " + response.code());
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
            restaurantAdapter.setRecyclerViewOnItemClickListener(new RestaurantAdapter.RecyclerViewOnItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
                    Intent intent = new Intent(getActivity(), RestaurantPageActivity.class);
                    intent.putExtra("title", restaurantCardArrayList.get(position).getTitle());
                    intent.putExtra("content", restaurantCardArrayList.get(position).getContent());
                    intent.putExtra("imageUrl", restaurantCardArrayList.get(position).getRestaurantImageUrl());
                    getContext().startActivity(intent);
                }
            });

            recyclerView.setAdapter(restaurantAdapter);
    }

}