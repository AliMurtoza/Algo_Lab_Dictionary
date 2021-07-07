/*Author : Sadnan Kibria Kawshik
* Roll-AE-15
* Assignment Topic : English to Bangla Dictionary Using Perfect Hashing
* Description: Here I used 2 2d array to implement 2 level hashing . My Keyset includes English Words and Bangla
* Words. So one array is used for storing Bangla words and other array is used for storing equivalent english word.
* Here there are some collisions but it is minimal as taking the square size of array for each slot minimizes the
* collision number to less than 1/2.The number of keys in my data set is 1000.No firebase or SQL database is used in
* this implementation*/





package com.example.englishtobangladictionary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerview;/*Recycler view that holds dictionary item*/
    private ExampleAdapter mAdapter;
    private  RecyclerView.LayoutManager mLayoutmanager;
    int[] frequency= new int[101];/*Frequency of each slot*/
    ArrayList<String[]>hash_table_english = new ArrayList<String[]>(101);/*Hash table to hold english word*/
    ArrayList<String[]>hash_table_bangla = new ArrayList<String[]>(101);/*Hashtable to hold Bangla Word*/
    ArrayList<ExampleItem> exampleItemArrayList = new ArrayList<ExampleItem>();

    public int initial_hash_function(String x) {/*Initial Hash Function to map keys*/
        int p = 0,a=10,b=23;
        int slot;
        //System.out.println(x);
        for (int i = 0; i < x.length(); i++) {
            int z=x.charAt(i)-'a'+1;
            p = (26*p+z)%82595523;
        }

        slot=p%101;
        return slot;

    }

    public int secondary_hash_function(String x,int m)/*Inner hash function to hash within slot*/
    {
        int p = 0, slot,a=1,b=0;
        //System.out.println(x);

        for (int i = 0; i < x.length(); i++) {
            int z=x.charAt(i)-'a'+1;
            p = (p*26+z)%82595523;
        }
        //System.out.println("Sum = "+p);

        slot=p%m;
        //System.out.println("Slot = "+slot);
        return slot;
    }




    public void setFrequency()/*Counting the frequency of each slot*/

    {
        String data = "";
        StringBuffer buffer = new StringBuffer();
        InputStream is = null;
        try {
            is = getAssets().open("input.txt");/*Input Set consists of 1000 words*/
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        int i=0;
        if(is != null) {
            try {
                while ((data = reader.readLine()) != null) {

                    int wordStart = 0;
                    int wordEnd = data.indexOf(",");
                    String eng_word = data.substring(wordStart,wordEnd);
                    int p=initial_hash_function(eng_word.toLowerCase());
                    frequency[p]++;
                   // String bangla_Word = data.substring(wordEnd+1,data.length());
                  //  Log.d("x",eng_word+" = "+bangla_Word);


                }
                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void size_allocation_hash_table()/*Allocating size mj=nj^2 for jth slot*/
    {

        for(int i=0;i<101;i++)
        {
            hash_table_english.add(new String[frequency[i]*frequency[i]]);
            hash_table_bangla.add(new String[frequency[i]*frequency[i]]);

            // System.out.println("Size of Hashtable "+i+"is = "+hash_table_english.get(i).length);
        }
    }

    public void insert ()/*Inserting the keys into both hash tables*/
    {
        String data = "";
        StringBuffer buffer = new StringBuffer();
        InputStream is = null;
        try {
            is = getAssets().open("input.txt");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        int i=0;
        if(is != null) {
            try {
                while ((data = reader.readLine()) != null) {


                    int wordStart = 0;
                    int wordEnd = data.indexOf(",");
                    String eng_word = data.substring(wordStart,wordEnd);
                    int p=initial_hash_function(eng_word.toLowerCase());
                    int q=secondary_hash_function(eng_word.toLowerCase(),hash_table_english.get(p).length);

                    String bangla_Word = data.substring(wordEnd+1,data.length());
                    Log.d("y",data);

                    hash_table_english.get(p)[q]=eng_word;
                    hash_table_bangla.get(p)[q]=bangla_Word;
                    exampleItemArrayList.add(new ExampleItem(hash_table_english.get(p)[q],hash_table_bangla.get(p)[q]));
                    /*filling the exampleArray List for display purpose*/


                }
                is.close();
                Log.d("Item", String.valueOf(exampleItemArrayList.size()));/*No. of elements in dictionary*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void getFrequency()
    {
        for(int i=0;i<101;i++)
        {
            System.out.println("Frequency of slot "+i + "  is = "+frequency[i]);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Dictionary");
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        setFrequency();
        size_allocation_hash_table();
        insert();

        mRecyclerview=findViewById(R.id.my_recyclerview);
        mRecyclerview.setHasFixedSize(true);
        mLayoutmanager= new LinearLayoutManager(this);
        mAdapter= new ExampleAdapter(exampleItemArrayList);

        mRecyclerview.setLayoutManager(mLayoutmanager);
        mRecyclerview.setAdapter(mAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_search_menu,menu);

        MenuItem menuItem  = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return  true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit ?")

                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

}