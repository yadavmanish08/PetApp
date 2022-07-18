package com.example.petapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.petapp.data.PetContract.PetEntry;
import com.example.petapp.data.PetDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
private static final int PET_LOADER= 0;
PetCursorAdapter mCursorAdapter;

    private PetDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper= new PetDbHelper(this);

        // displayDatabaseInfo();


         ListView petListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
         View emptyView = findViewById(R.id.empty_view);
         petListView.setEmptyView(emptyView);

         mCursorAdapter= new PetCursorAdapter(this,null);
         petListView.setAdapter(mCursorAdapter);

         petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                 Intent intent= new Intent(MainActivity.this,EditActivity.class);

                 Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);
                 intent.setData(currentPetUri);
                 startActivity(intent);
             }
         });

         //kick off the loader

       //getLoaderManager().initLoader(PET_LOADER,null, this);
        LoaderManager.getInstance(this).initLoader(0, null, this);


    }

   /** @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    } */
  /* Temporary helper method to display information in the onscreen TextView about
  the state of pets database
   */

   /** private void displayDatabaseInfo(){
        // PetDbHelper mDbHelper =new PetDbHelper(this);
       // SQLiteDatabase db= mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

// Filter results WHERE "title" = 'My Title'
        // String selection =PetEntry.COLUMN_PET_NAME + " = ?";
        //String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor


         /** Cursor cursor = db.query(
         PetEntry.TABLE_NAME,   // The table to query
         projection,             // The array of columns to return (pass null to get all)
         null ,            // The columns for the WHERE clause
         null,         // The values for the WHERE clause
         null,                   // don't group the rows
         null,                   // don't filter by row groups
         null             // The sort order
         );
          */


       /** Cursor cursor = getContentResolver().query(PetEntry.CONTENT_URI,projection,null,null,null);
         ListView petListView = (ListView) findViewById(R.id.list);  */
       /** TextView displayView = (TextView) findViewById(R.id.text_view_pet); */

        // second modification commenting when cursorAdapter is introduced
       /** try {
            displayView.setText("number of rows in pets database table: "+ cursor.getCount() + " pets.\n\n");


            displayView.append(PetEntry._ID + " - " +
                    PetEntry.COLUMN_PET_NAME + " - " + PetEntry.COLUMN_PET_BREED +" - "+ "\n");

            int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
            int BreedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBreed = cursor.getString(BreedColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName+ " - "+currentBreed));
            }



        } finally {
            cursor.close();
        }
        */
        // PetCursorAdapter adapter = new PetCursorAdapter(this, cursor);
        // petListView.setAdapter(adapter);
   // }


    private void insertPet(){
         //SQLiteDatabase db= mDbHelper.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME,"Toto");
        values.put(PetEntry.COLUMN_PET_BREED,"german shepard");
        values.put(PetEntry.COLUMN_PET_GENDER,PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT,7);
       // long newRowId=db.insert(PetEntry.TABLE_NAME,null,values);
        // Log.v("catalogActivity","New row Id"+newRowId);
        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_catalog, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertPet();
                //displayDatabaseInfo();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllPets();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection ={
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED
        };
        return new CursorLoader(this,
             PetEntry.CONTENT_URI,
             projection,
             null,
             null,
             null
                );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
      mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
       mCursorAdapter.swapCursor(null);
    }

    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }
}


