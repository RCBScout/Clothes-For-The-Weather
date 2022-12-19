package com.vitaliymatr.clothesfortheweather101;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import com.vitaliymatr.clothesfortheweather101.data.ClothesContract.LookEntry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOOK_LOADER = 123;
    LookCursorAdapter lookCursorAdapter;
    ListView dataListView;
    private EditText temperatureEditText;
    private Button button;
    private FloatingActionButton floatingActionButton;
    private TextView emptyTextView;
    private int temperature;
    private int temperature1;
    private int temperature2;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
                        .build());
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        dataListView = findViewById(R.id.dataListView);
        button = findViewById(R.id.temperatureButton);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        temperatureEditText = findViewById(R.id.temperatureEditText);
        emptyTextView = findViewById(R.id.emptyTextView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayData();
            }

        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean fromMainActivity = true;
                Intent intent = new Intent(MainActivity.this, AddLook.class);
                intent.putExtra("fromMainActivity", fromMainActivity);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

    }

    private void displayData() {
        try {
            temperature = Integer.parseInt(temperatureEditText.getText().toString());
            temperature1 = temperature - 1;
            temperature2 = temperature + 1;
        }catch (NumberFormatException nef) {
            Toast.makeText(this,
                    "Введите целочисленное значение температуры",
                    Toast.LENGTH_LONG).show();
            return;
        }

        lookCursorAdapter = new LookCursorAdapter(this, null, false);

        dataListView.setAdapter(lookCursorAdapter);

        dataListView.computeScroll();

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean fromMainActivity = true;
                Intent intent = new Intent(MainActivity.this, AddLook.class);
                Uri currentLookUri = ContentUris.withAppendedId(LookEntry.CONTENT_URI, id);
                intent.setData(currentLookUri);
                intent.putExtra("fromMainActivity", fromMainActivity);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        getSupportLoaderManager().restartLoader(LOOK_LOADER, null,this);
        emptyTextView.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.show_list:
                Intent openList = new Intent(this, List.class);
                startActivity ( openList );
                return true;
            case R.id.show_info:
                Intent openInfo = new Intent(this, InfoActivity.class);
                startActivity(openInfo);
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                LookEntry._ID,
                LookEntry.COLUMN_TEMPERATURE,
                LookEntry.COLUMN_CLOTHES_TYPE,
                LookEntry.COLUMN_CLOTHE
        };

        CursorLoader cursorLoader = new CursorLoader(this,
                LookEntry.CONTENT_URI,
                projection,
                LookEntry.COLUMN_TEMPERATURE + " = ? or " + LookEntry.COLUMN_TEMPERATURE + " = ? or " + LookEntry.COLUMN_TEMPERATURE + " = ?",
                new String[] {"" + temperature, "" + temperature1, "" + temperature2},
                LookEntry.COLUMN_TEMPERATURE + " ASC"
        );

        return cursorLoader;

    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        lookCursorAdapter.swapCursor(data);
        Log.d("dataListViewisShown", "I am asdfs" + lookCursorAdapter.isEmpty());
        if (lookCursorAdapter.isEmpty()) {
            emptyTextView.setText(R.string.table_is_empty);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        lookCursorAdapter.swapCursor(null);
    }
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}