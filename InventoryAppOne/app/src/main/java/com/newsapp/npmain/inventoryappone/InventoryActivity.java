package com.newsapp.npmain.inventoryappone;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.newsapp.npmain.inventoryappone.data.ProductDbHelper;

import java.util.Locale;

import static com.newsapp.npmain.inventoryappone.data.ProductContract.ProductEntry.*;

public class InventoryActivity extends AppCompatActivity
{
    private ProductDbHelper productDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        productDbHelper = new ProductDbHelper(this);
        insertProducts();
        displayDatabaseInfo();
    }

    private void insertProducts()
    {
        SQLiteDatabase db = productDbHelper.getWritableDatabase();
        insertRowDetail( new ContentValues(), db, "The Wonky Donkey", 799, 25, "Scholastic Inc", "(800)-555-1212"  );
        insertRowDetail( new ContentValues(), db, "The President Is Missing", 2400, 1057, "Little, Brown and Company and Knopf", "(800)-555-1778"  );
        insertRowDetail( new ContentValues(), db, "A Higher Loyalty: Truth, Lies, and Leadership", 2399, 47, "Flatiron Books", "(888)-555-1325"  );
        insertRowDetail( new ContentValues(), db, "The Outsider", 1860, 1057, null, null );
        insertRowDetail( new ContentValues(), db, "The Woman In The Window", -5, -10, "HarperCollins Publishers", null );
    }

    private void insertRowDetail(ContentValues values, SQLiteDatabase db, String productName, int productPrice, int productQuantity, String supplierName, String supplierPhone)
    {
        values.put(COLUMN_PRODUCT_NAME, productName);
        if (productPrice > 0 )
        {
            values.put(COLUMN_PRODUCT_PRICE, productPrice);
        }
        if (productQuantity > 0)
        {
            values.put(COLUMN_PRODUCT_QUANTITY, productQuantity);
        }

        if (supplierName != null )
        {
            values.put(COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
        }
        if (supplierPhone != null )
        {
            values.put(COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhone);
        }
        long newRowId = db.insert(TABLE_NAME, null, values);
        if (newRowId == -1)
        {
            Toast.makeText(this, "Error saving product", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Product saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    private void displayDatabaseInfo()
    {
        SQLiteDatabase db = productDbHelper.getReadableDatabase();
        String[] projection = {_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_QUANTITY,
                COLUMN_PRODUCT_SUPPLIER_NAME,
                COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);
        try
        {
            TextView productView = findViewById(R.id.textview_inventory);
            productView.setText("Number Of Inventory Items in Products Table: " + cursor.getCount());
            int productIdColumnIndex = cursor.getColumnIndex(_ID);
            int productNameColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
            int productPriceColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_PRICE);
            int productQuantityColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY);
            int productSupplierColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_SUPPLIER_NAME);
            int productSupplierPhoneColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);
            productView.append(String.format(Locale.US, "\n\n%10s%15s%15s%15s%15s%15s\n\n",
                    _ID,
                    COLUMN_PRODUCT_NAME,
                    COLUMN_PRODUCT_PRICE,
                    COLUMN_PRODUCT_QUANTITY,
                    COLUMN_PRODUCT_SUPPLIER_NAME,
                    COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER));
            while (cursor.moveToNext())
            {
                productView.append(String.format(Locale.US,"%10d%15s%15d%15d%15s%15s\n\n",
                        cursor.getInt(productIdColumnIndex),
                        cursor.getString(productNameColumnIndex),
                        cursor.getInt(productPriceColumnIndex),
                        cursor.getInt(productQuantityColumnIndex),
                        cursor.getString(productSupplierColumnIndex),
                        cursor.getString(productSupplierPhoneColumnIndex)));
            }
        }
        finally
        {
            cursor.close();
        }
    }
}
