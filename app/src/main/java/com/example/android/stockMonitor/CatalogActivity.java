package com.example.android.stockMonitor;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.stockMonitor.data.DataDbHelper;

import static com.example.android.stockMonitor.data.DataContract.ProductEntry.COLUMN_PRODUCT_IMAGE;
import static com.example.android.stockMonitor.data.DataContract.ProductEntry.COLUMN_PRODUCT_NAME;
import static com.example.android.stockMonitor.data.DataContract.ProductEntry.COLUMN_PRODUCT_PRICE;
import static com.example.android.stockMonitor.data.DataContract.ProductEntry.COLUMN_PRODUCT_QUANTITY;
import static com.example.android.stockMonitor.data.DataContract.ProductEntry.CONTENT_URI;
import static com.example.android.stockMonitor.data.DataContract.ProductEntry.TABLE_NAME;
import static com.example.android.stockMonitor.data.DataContract.ProductEntry._ID;


/*
*created by moses
 */

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PRODUCT_LOADER = 0;
    private DataCursorAdapter adapter = new DataCursorAdapter(this, null);

    ListView productListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the stock data
        productListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_catalog);
        productListView.setEmptyView(emptyView);

        // Create empty adapter
        adapter = new DataCursorAdapter(this, null);
        productListView.setAdapter(adapter);

        //setting filterable list
        productListView.setAdapter(adapter);

        //Enable filtering in ListView
        productListView.setTextFilterEnabled(true);

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {

                DataDbHelper mDbHelper = new DataDbHelper(CatalogActivity.this);
                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                // in real life, do something more secure than concatenation
                // but it will depend on your schema
                // This is the query that will run on filtering
                String query = "SELECT "+COLUMN_PRODUCT_NAME+" FROM "+TABLE_NAME
                        + " where "+COLUMN_PRODUCT_NAME+" like '%" + constraint + "%' "
                        + "ORDER BY NAME ASC";
                return db.rawQuery(query, null);
            }
        });


        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pAdapterView, View pView, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditActivity.class);
                Uri currentItemUri = ContentUris.withAppendedId(CONTENT_URI, id);
                intent.setData(currentItemUri);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//         Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_catalog, menu);
//
//        MenuItem actionSearch = menu.findItem(R.id.search);
//
//        SearchView searchViewEditText = (SearchView) actionSearch.getActionView();
//        searchViewEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                adapter.getFilter().filter(query);
//                adapter.notifyDataSetChanged();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                adapter.notifyDataSetChanged();
//                return true;
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_all_items) {
            showDeleteConfirmationDialog();
            return true;
        }
        if (id == R.id.current_price) {
            Intent intent = new Intent(CatalogActivity.this, CurrentMarketPrices.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.user_manual)
        {
            Intent usermanual = new Intent(CatalogActivity.this, Usermanual.class);
            startActivity(usermanual);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_items);
        builder.setPositiveButton(R.string.action_continue, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
                deleteAllProducts();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the product.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from products database");
    }


    public void onListItemClick(long id) {
        Intent intent = new Intent(CatalogActivity.this, EditActivity.class);

        Uri currentItemUri = ContentUris.withAppendedId(CONTENT_URI, id);
        intent.setData(currentItemUri);

        startActivity(intent);
    }

    public void onSaleButtonClick(long id, int quantity) {
        Uri currentItemUri = ContentUris.withAppendedId(CONTENT_URI, id);

        int newQuantity = quantity > 0 ? quantity - 1 : 0;

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_QUANTITY, newQuantity);

        int rowsAffected = getContentResolver().update(currentItemUri, values, null, null);
        if (quantity == newQuantity) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(this, getString(R.string.out_of_stock), Toast.LENGTH_SHORT).show();
        } else if (quantity != newQuantity) {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.updated_item),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                _ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_QUANTITY,
                COLUMN_PRODUCT_IMAGE};

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, CONTENT_URI,
                projection,
                null,
                null,
                COLUMN_PRODUCT_QUANTITY + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapter.swapCursor(null);
    }

    public boolean searchProduct(String product) {
        DataDbHelper mDbHelper = new DataDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PRODUCT_NAME + " LIKE '%" + product + "%'";
        Cursor cursor = db.rawQuery(query, null);
        Log.i("Moses Items", "searchProduct: " + cursor);
        cursor.close();
        return true;
    }
}
