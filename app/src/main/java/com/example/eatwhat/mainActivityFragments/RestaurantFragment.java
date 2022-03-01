package com.example.eatwhat.mainActivityFragments;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

//import butterknife.BindView;
//import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantFragment extends Fragment  {

    int offset = 0, limit = 5, totalNum = 20;
    private ArrayList<String> categoryList;
    private ArrayList<String> sortConditionList;
    ArrayList<RestaurantCard> restaurantCardArrayList = new ArrayList<>();

    private String selectedCategory = null;
    private String selectedSortCondition = null;
    private RecyclerView recyclerView;
    private ProgressBar loadingPB;

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
        pullUpToRefresh(view);
        return view;
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
                        Toast.makeText(getContext(), "Reach to bottom", Toast.LENGTH_SHORT).show();
                        loadingPB.setVisibility(View.GONE);
                    }
                    else if (offset + limit < totalNum) {
                        initData();
                    }else {
                        limit = totalNum - offset;
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

        String [] sortConditionArray = { "Rating", "$-$$", "$-$$$", "$$ - $$$", "$$ - $$$$", "$$$ - $$$$$"};
        sortConditionList = new ArrayList<>();
        for (int i = 0; i < sortConditionArray.length; i++) {
            sortConditionList.add(sortConditionArray[i]);
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
                                Log.e("choose category", selectedCategory);

                                if (selectedCategory != null) {
                                    offset = 0;
                                    limit = 5;
                                    totalNum = 20;
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



    private void initData(){
        RetrofitClient retrofitClient = new RetrofitClient();
        RestaurantService methods = retrofitClient.getRetrofit().create(RestaurantService.class);

        Call<Restaurant> call = methods.queryRestaurantByCategory("Santa Clara", selectedCategory,  limit, offset);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                System.out.println(response);
                if (response.code() == 200){
                    System.out.println("Network " + response.code());
                    totalNum = response.body().getTotal();
                    for (Business business: response.body().getBusinesses()){
                        RestaurantCard restaurantCard = new RestaurantCard(business.getImageUrl(), business.getName(), business.getCategories().get(0).getTitle(), false, business.getId());
                        restaurantCardArrayList.add(restaurantCard);
                    }

                    if (response.body().getBusinesses().size() == 0) {
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Reach to bottom", Toast.LENGTH_SHORT).show();
                    }
                    initRecycleView(restaurantCardArrayList);
                }
                else {
                    loadingPB.setVisibility(View.GONE);
                    //Toast.makeText(getContext(), "Network Error", Toast.LENGTH_LONG).show();
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
                    intent.putExtra("id", restaurantCardArrayList.get(position).getId());
                    getContext().startActivity(intent);
                }
            });

            recyclerView.setAdapter(restaurantAdapter);
    }
}