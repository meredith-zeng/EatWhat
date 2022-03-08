package com.example.eatwhat.mainActivityFragments;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatwhat.R;
import com.example.eatwhat.activity.restaurant.RestaurantPageActivity;
import com.example.eatwhat.activity.user.SignInActivity;
import com.example.eatwhat.adapter.RestaurantAdapter;
import com.example.eatwhat.cardview.RestaurantCard;
import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;
import com.example.eatwhat.service.RestaurantPojo.Business;
import com.example.eatwhat.service.RestaurantPojo.Restaurant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

    int offset = 0, limit = 5, totalNum = 20;
    private ArrayList<String> categoryList;
    ArrayList<RestaurantCard> restaurantCardArrayList = new ArrayList<>();
    private String TAG = "RestaurantFragment: ";
    private RecyclerView recyclerView;
    private ProgressBar loadingPB;

    private String selectedCategory = null;
    private String selectedCity = "Santa Clara";
    private String sortBy = null;
    private boolean ifRating = false;
    private boolean ifReviewCount = false;
    private String inputRestaurantName = null;

    //location realted variable
    private FusedLocationProviderClient fusedLocationClient;
    private double longitude = 0;
    private double latitude = 0;

    public RestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.restaurant_recyclerview);
        getLocation();
        setSortBy(view);
        createSpinners(view, container);
        initData();
        pullUpToRefresh(view);
        initSearchRestaurantEditText(view);
        confirmInputRestaurant(view);
        return view;
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG + "From getLocation", "no permission");
                    return;
                }

                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    Geocoder gc = new Geocoder(getContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
                        selectedCity = addresses.get(0).getLocality().toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void reset() {
        offset = 0;
        limit = 5;
        totalNum = 20;
        restaurantCardArrayList.clear();
        initData();
    }

    private void setSortBy(View view) {
        CheckedTextView ratingCheckedTextView = view.findViewById(R.id.sort_by_rating);
        CheckedTextView reviewCountTextView = view.findViewById(R.id.sort_by_review_count);
        ratingCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingCheckedTextView.getCurrentTextColor() == Color.parseColor("#978C8C")) {
                    ratingCheckedTextView.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    ratingCheckedTextView.setTextColor(Color.parseColor("#FFFFFF"));
                    ifRating = true;
                }
                else {
                    ratingCheckedTextView.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    ratingCheckedTextView.setTextColor(Color.parseColor("#978C8C"));
                    ifRating = false;
                }

                if (reviewCountTextView.getCurrentTextColor() == Color.parseColor("#FFFFFF")) {
                    reviewCountTextView.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    reviewCountTextView.setTextColor(Color.parseColor("#978C8C"));
                    ifReviewCount = false;
                }

                if (!(ifReviewCount || ifRating)) {
                    sortBy = null;
                }
                else {
                    sortBy = "rating";
                }
                reset();
            }
        });

        reviewCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reviewCountTextView.getCurrentTextColor() == Color.parseColor("#978C8C")) {
                    reviewCountTextView.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    reviewCountTextView.setTextColor(Color.parseColor("#FFFFFF"));
                    ifReviewCount = true;
                }
                else {
                    reviewCountTextView.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    reviewCountTextView.setTextColor(Color.parseColor("#978C8C"));
                    ifReviewCount = false;
                }

                if (ratingCheckedTextView.getCurrentTextColor() == Color.parseColor("#FFFFFF")) {
                    ratingCheckedTextView.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    ratingCheckedTextView.setTextColor(Color.parseColor("#978C8C"));
                    ifRating = false;
                }

                if (!(ifReviewCount || ifRating)) {
                    sortBy = null;
                }
                else {
                    sortBy = "rating";
                }
                reset();
            }
        });
    }

    public void putArguments(Bundle args) {
        longitude = args.getDouble("Longitude");
        latitude = args.getDouble("Latitude");
        System.out.println("receive" + longitude + "   " + latitude);
        Geocoder gc = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
            selectedCity = addresses.get(0).getLocality().toString();
        } catch (Exception e ) {
            e.printStackTrace();
        }

       reset();
    }

    private void pullUpToRefresh(View rootView) {
        NestedScrollView nestedSV = (NestedScrollView) rootView.findViewById(R.id.swipe_container);
        loadingPB = (ProgressBar)rootView.findViewById(R.id.pb_loading);
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    offset += limit;
                    // on below line we are making our progress bar visible.
                    loadingPB.setVisibility(View.VISIBLE);
                    if (offset +limit > 30) {
                        Log.d(TAG + "From pullUpToRefresh", "offset +limit > 30");
                        Toast.makeText(getContext(), "Reach to bottom", Toast.LENGTH_SHORT).show();
                        loadingPB.setVisibility(View.GONE);
                    }
                    else if (offset + limit < totalNum) {
                        Log.d(TAG + "From pullUpToRefresh", "offset + limit < totalNum");
                        initData();
                    }else {
                        Log.d(TAG + "From pullUpToRefresh", "limit = totalNum - offset");
                        limit = totalNum - offset;
                        initData();
                    }
                }
            }
        });
    }

    private void createSpinners(View view, ViewGroup container) {
        String [] categoryArray  = new String[]{"tradamerican", "asianfusion", "brazilian",
                "barbeque", "buffets", "burgers", "cafes",
                "cheesesteaks", "chinese", "chicken_wings", "creperies", "dimsum", "diners",
                "hotdogs", "foodstands", "french", "german", "gluten_free", "greek", "indpak",
                "irish", "italian", "japanese", "korean", "latin", "raw_food", "mediterranean",
                "mexican", "russian", "salad", "pizza", "steak", "thai",
                "seafood", "spanish", "vegetarian", "vietnamese"};

        categoryList = new ArrayList<>();
        for (int i = 0; i < categoryArray.length; i++) {
            categoryList.add(categoryArray[i]);
        }


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
                                Log.e(TAG+ "choose category", selectedCategory);

                                if (selectedCategory != null) {
                                    reset();
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

    private void initData(){
        RetrofitClient retrofitClient = new RetrofitClient();
        RestaurantService methods = retrofitClient.getRetrofit().create(RestaurantService.class);

        Call<Restaurant> call = methods.queryRestaurantByCategory(inputRestaurantName, selectedCity, selectedCategory, sortBy, limit, offset);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                System.out.println(response);
                if (response.code() == 200){
                    Log.d(TAG + "From onReponse", "code 200");
                    totalNum = response.body().getTotal();
                    for (Business business: response.body().getBusinesses()){
                        RestaurantCard restaurantCard = new RestaurantCard(business.getImageUrl(), business.getName(), business.getCategories().get(0).getTitle(), business.getRating(), business.getId());
                        restaurantCardArrayList.add(restaurantCard);
                    }

                    if (response.body().getBusinesses().size() == 0) {
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Reach to bottom", Toast.LENGTH_SHORT).show();
                    }
                    initRecycleView(restaurantCardArrayList);
                    Log.d(TAG + "From onReponse", "Finished");
                }
                else {
                    loadingPB.setVisibility(View.GONE);
                    //Toast.makeText(getContext(), "Network Error", Toast.LENGTH_LONG).show();
                    Log.d(TAG+ "From onReponse", "code not 200");
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
                    intent.putExtra("id", restaurantCardArrayList.get(position).getId());
                    getContext().startActivity(intent);
                }
            });

            recyclerView.setAdapter(restaurantAdapter);
    }

    private void initSearchRestaurantEditText(View view) {
        EditText inputName = (EditText) view.findViewById(R.id.search_restaurant_name);
        inputName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int ActionId, KeyEvent keyEvent) {
//                if (ActionId == EditorInfo.IME_ACTION_DONE
//                        || ActionId == EditorInfo.IME_ACTION_SEARCH
//                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
//                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENVELOPE) {
                    inputRestaurantName = inputName.getText().toString();
               // }
                return false;
            }
        });
    }

    private void confirmInputRestaurant(View view) {
        Button search = (Button) view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(inputRestaurantName);
                reset();
                initData();
            }
        });
    }
}