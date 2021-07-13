package com.example.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.room.Adapter.RecyclerViewAdapter;
import com.example.room.Model.Repo;
import com.example.room.Repo.RepoDatabaseClient;
import com.example.room.Repo.RepoRoom;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String FETCHURL = "https://restcountries.eu/rest/v2/region/asia";
    private RecyclerView recyclerview;
    private ArrayList<Repo> arrayList;
    private RecyclerViewAdapter adapter;
    private ProgressBar pb;
    private JSONArray responseArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = findViewById(R.id.pb);
        pb.setVisibility(View.GONE);

        recyclerview = findViewById(R.id.recyclerview);
        arrayList = new ArrayList<Repo>();
        adapter = new RecyclerViewAdapter(this, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setAdapter(adapter);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting() && arrayList != null) {
            fetchFromServer();
        } else {
            fetchFromRoom();
        }
    }
    private void fetchFromRoom() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<RepoRoom> recipeList = RepoDatabaseClient.getInstance(MainActivity.this).getAppDatabase().repoDao().getAll();
                arrayList.clear();
                for (RepoRoom recipe: recipeList) {
                    Repo repo = new Repo(
                      recipe.getId(), recipe.getName(), recipe.getCapital(), recipe.getFlag(),
                            recipe.getRegion(), recipe.getSubRegion(), recipe.getPopulation(),
                            recipe.getBorders(), recipe.getLanguages()
                    );
                    arrayList.add(repo);
                }
                // refreshing recycler view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();
    }

    private void fetchFromServer() {
        pb.setVisibility(View.VISIBLE);

        JsonArrayRequest request = new JsonArrayRequest(FETCHURL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the menu! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        responseArray = response;
                            //System.out.println(response.getJSONObject(0).getJSONArray("languages").getJSONObject(2).getString("name"));
                        try{
                            for(int i =0; i<response.length(); i++){
                                List<String> repos = new ArrayList<>();
                                repos.add(response.getJSONObject(i).getString("name"));
                                repos.add(response.getJSONObject(i).getString("capital"));
                                repos.add(response.getJSONObject(i).getString("flag"));
                                repos.add(response.getJSONObject(i).getString("region"));
                                repos.add(response.getJSONObject(i).getString("subregion"));
                                repos.add(String.valueOf(response.getJSONObject(i).getInt("population")));
                                String borders = "";
                                for(int k = 0; k<response.getJSONObject(i).getJSONArray("borders").length(); k++){
                                    if(k == response.getJSONObject(i).getJSONArray("borders").length() - 1){
                                       borders +=  response.getJSONObject(i).getJSONArray("borders").get(k);
                                    }else{
                                        borders +=  response.getJSONObject(i).getJSONArray("borders").get(k) + ",";
                                    }
                                }
                                if(borders == " " || borders == ""){
                                    borders = "Not available";
                                }
                                repos.add(borders);
                                String languages = "";
                                for(int k = 0; k < response.getJSONObject(i).getJSONArray("languages").length(); k++){
                                    if(k ==response.getJSONObject(i).getJSONArray("languages").length() - 1){
                                        languages += response.getJSONObject(i).getJSONArray("languages").getJSONObject(k).getString("name");
                                    }else{
                                        languages += response.getJSONObject(i).getJSONArray("languages").getJSONObject(k).getString("name") + ", ";
                                    }
                                }
                                if(languages == " " || languages == ""){
                                    languages = "Not available";
                                }
                                repos.add(languages);
                                Repo repo = new Repo(
                                        i, repos.get(0),repos.get(1), repos.get(2), repos.get(3),
                                        repos.get(4), Integer.parseInt(repos.get(5)), repos.get(6), repos.get(7)
                                );
                                arrayList.add(repo);
                            }
//                            repos = new Gson().fromJson(response.toString(), new TypeToken<List<Repo>>() {
//                            }.getType());
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        // adding data to cart list
                        System.out.println(arrayList.size());

                        // refreshing recycler view
                        adapter.notifyDataSetChanged();

                        pb.setVisibility(View.GONE);

                        saveTask();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        request.setShouldCache(false);

        requestQueue.add(request);
    }


    private void saveTask() {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
//                int jump = repos.size() / responseArray.length();
                for (int i = 0; i < arrayList.size(); i = i++) {
                    RepoRoom recipe= new RepoRoom();
                    recipe.setName(arrayList.get(i).getName());
                    recipe.setCapital(arrayList.get(i).getCapital());
                    recipe.setFlag(arrayList.get(i).getFlag());
                    recipe.setRegion(arrayList.get(i).getRegion());
                    recipe.setSubRegion(arrayList.get(i).getSubRegion());
                    recipe.setPopulation(arrayList.get(i).getPopulation());
                    recipe.setBorders(arrayList.get(i).getBorders());
                    recipe.setLanguages(arrayList.get(i).getLanguages());
                    RepoDatabaseClient.getInstance(getApplicationContext()).getAppDatabase().repoDao().insert(recipe);
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }
}