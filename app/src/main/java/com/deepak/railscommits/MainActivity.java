package com.deepak.railscommits;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CommitsAdapter.MyOnclickListener,  SearchView.OnQueryTextListener{
    private static ArrayList<CommitsData> commitsDataArrayList = new ArrayList<>();
    private CommitsAdapter cAdapter;
    ProgressDialog pDialog;
    private RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerview.setLayoutManager(layoutManager);
        cAdapter = new CommitsAdapter(commitsDataArrayList,MainActivity.this);
        recyclerview.setAdapter(cAdapter);

        if(commitsDataArrayList==null||commitsDataArrayList.size()==0)  sendRequest();
    }
    private void sendRequest(){
        pDialog = new ProgressDialog(MainActivity.this);
        if(commitsDataArrayList==null||commitsDataArrayList.size()==0) {
            pDialog.setMessage(AppConstants.LOADING);
            pDialog.show();
            pDialog.setCancelable(false);
        }

        JsonArrayRequest req = new JsonArrayRequest(AppConstants.url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(AppConstants.LoktraLog, response.toString());
                        pDialog.hide();
                        showJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(AppConstants.LoktraLog, AppConstants.ERROR + error.getMessage());
                pDialog.hide();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(req);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //To prevent window leaks
        if(pDialog!=null)
            pDialog.cancel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void openMessage(int position){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.addToBackStack(null);
        MessageDialogFragment dialog =  MessageDialogFragment.newInstance(commitsDataArrayList.get(position).getMessage());
        dialog.show(manager, AppConstants.Dialog);

    }
    private void showJSON(JSONArray json){
        if (json != null) {
            try {
                //Get JSON response by converting JSONArray into String
                CommitsData restaurant;
                String name,locality;
                String noOfCoupons;
                for(int i = 0; i < json.length(); i++) {
                    JSONObject obj = json.getJSONObject(i);
                    JSONObject obj2 = obj.getJSONObject(AppConstants.Commit);
                    JSONObject obj3 = obj2.getJSONObject(AppConstants.Author);
                    locality=obj2.get(AppConstants.Msg).toString();
                    noOfCoupons=obj.get(AppConstants.Commit_Number).toString();
                    name=obj3.get(AppConstants.Name).toString();
                    restaurant = new CommitsData(noOfCoupons,name,locality,false);
                    commitsDataArrayList.add(restaurant);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(MainActivity.this, AppConstants.ERROR_PARSING, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, AppConstants.SOMETHING_WENT_WRONG, Toast.LENGTH_LONG).show();
            }
            cAdapter.notifyDataSetChanged();
        }
        //When JSON is null
        else {
            Toast.makeText(MainActivity.this,AppConstants.UNEXPECTED_ERROR,Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        cAdapter.setFilter(commitsDataArrayList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return true;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        final ArrayList<CommitsData> filteredModelList = filter(commitsDataArrayList, newText);
        cAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    private ArrayList<CommitsData> filter(ArrayList<CommitsData> models, String query) {
        query = query.toLowerCase();

        final ArrayList<CommitsData> filteredModelList = new ArrayList<>();
        for (CommitsData model : models) {
            final String text = model.getUserName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
