package com.vitaliymatr.clothesfortheweather101;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import com.vitaliymatr.clothesfortheweather101.data.ClothesContract.LookEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class List extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOOK_LOADER = 123;
    LookCursorAdapter lookCursorAdapter;

    ListView dataListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        dataListView = findViewById(R.id.dataListView);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton1);

        lookCursorAdapter = new LookCursorAdapter(this, null, false);
        dataListView.setAdapter(lookCursorAdapter);

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(List.this, AddLook.class);
                Uri currentLookUri = ContentUris.withAppendedId(LookEntry.CONTENT_URI, id);
                intent.setData(currentLookUri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this, AddLook.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
        getSupportLoaderManager().initLoader(LOOK_LOADER, null,this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                LookEntry._ID,
                LookEntry.COLUMN_TEMPERATURE,
                LookEntry.COLUMN_CLOTHES_TYPE,
                LookEntry.COLUMN_CLOTHE
        };

        CursorLoader cursorLoader = new CursorLoader(this,
                LookEntry.CONTENT_URI,
                projection,
                null,
                null,
                LookEntry.COLUMN_TEMPERATURE + " ASC"
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        lookCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        lookCursorAdapter.swapCursor(null);
    }
}