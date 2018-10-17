package com.example.benja.myapplication.Tabs;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import com.example.benja.myapplication.Utils.Api;
import com.example.benja.myapplication.Utils.Top_API.TopArticle;
import com.example.benja.myapplication.Utils.Top_API.TopArticleList;
import com.example.benja.myapplication.Utils.ListItem;
import com.example.benja.myapplication.MyAdapter;
import com.example.benja.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TopStoriesTab extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.top_stories_tab, container, false);
        TextView tv = rootView.findViewById(R.id.section_label);


        recyclerView = rootView.findViewById(R.id.fragment_main_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listItems = new ArrayList<>();
        adapter = new MyAdapter(listItems, getContext());




        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Api api = retrofit.create(Api.class);

        Call<TopArticleList> call = api.getTopArticles();

        recyclerView.setAdapter(adapter);


        call.enqueue(new Callback<TopArticleList>() {
            @Override
            public void onResponse(Call<TopArticleList> call, retrofit2.Response<TopArticleList> response) {
                TopArticleList articles = response.body();
                final List<TopArticle> theListOfArticles = articles.getArticles();

                for (int i = 0; i < articles.getArticles().size(); i++) {

                    int limit = theListOfArticles.get(i).getMultimedia().size();
                    if (limit != 0) {

                            ListItem listItem = new ListItem(theListOfArticles.get(i).getSection(),
                                    theListOfArticles.get(i).getSubsection(),
                                    theListOfArticles.get(i).getTitle(),
                                    "Today",
                                    theListOfArticles.get(i).getMultimedia().get(0).getUrl(),
                                    //.replace("https://", "http://")
                                    getContext());

                            listItems.add(listItem);



                    } else {
                        ListItem listItem = new ListItem(theListOfArticles.get(i).getSection(),
                                theListOfArticles.get(i).getSubsection(),
                                theListOfArticles.get(i).getTitle(),
                                "Today",
                                "http://static01.nyt.com/images/2018/10/09/briefing/100918evening-briefing-promo/100918evening-briefing-promo-thumbStandard.jpg",
                                getContext());
                        listItems.add(listItem);
                    }
                }

                Log.d("isthisworking", "link: " + theListOfArticles.get(0).getMultimedia().get(0).getUrl());


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TopArticleList> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("JSON", t.getMessage());
            }
        });

        return rootView;
    }

}


