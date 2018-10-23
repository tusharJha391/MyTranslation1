package com.example.android.mytranslation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/9/18.
 */

public class UserListActivity  extends AppCompatActivity {

    RecyclerView recyclerView;
    Context context=this;
    Gson gson;
    CardView cv;
    TextView emptyView;
    SqlLiteHelper sqlLiteHelper;
    ArrayList<TranslationFav> kTranslationView = new ArrayList<TranslationFav>();
    int size;
    CustomRecyclerAdapter customRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyView = (TextView) findViewById(R.id.emptyElement);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle("Favourite");
        }
        createList();

    }


    private void createList() {
        size =MainActivity.mTransView.size();
        if (size>=1) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        sqlLiteHelper = new SqlLiteHelper(context);
        //sqlLiteHelper.

        /*String ilt = MainActivity.mTransView.get(size-1).getLanguageInputText();
        String iln = MainActivity.mTransView.get(size-1).getLanguageInputName();
        String olt = MainActivity.mTransView.get(size-1).getTanslationOutputText();
        String oln = MainActivity.mTransView.get(size-1).getLanguageOutputName();
        TranslationFav translationFav = new TranslationFav(ilt,iln,olt,oln);
        kTranslationView.add(translationFav);*/
        /*SharedPreferences prefs = getSharedPreferences("key", Context.MODE_PRIVATE);
        String httpParamJSONList = prefs.getString("your_prefes_key", "");
        ArrayList<TranslationFav> arrayList = new Gson().fromJson(httpParamJSONList,new TypeToken<ArrayList<TranslationFav>>() {
        }.getType());*/



       MyAsyncTask myAsyncTask=new MyAsyncTask();
       myAsyncTask.execute();




    }

    public class MyAsyncTask extends AsyncTask<Void, Void, ArrayList<TranslationFav>> {

        @Override
        protected ArrayList<TranslationFav> doInBackground(Void... voids) {
            return sqlLiteHelper.dataFetch();
        }

        @Override
        protected void onPostExecute(ArrayList<TranslationFav> translationFavs) {
            super.onPostExecute(translationFavs);
            if (translationFavs.size()>0 && recyclerView !=null && emptyView!=null) {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                customRecyclerAdapter = new CustomRecyclerAdapter(UserListActivity.this, translationFavs);
                recyclerView.setAdapter(customRecyclerAdapter);
            } else {
                if (recyclerView !=null && emptyView!=null) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
