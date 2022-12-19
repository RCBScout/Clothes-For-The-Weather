package com.vitaliymatr.clothesfortheweather101;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import com.vitaliymatr.clothesfortheweather101.data.ClothesContract.LookEntry;

public class AddLook extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDIT_LOOK_LOADER = 111;
    Uri currentLookUri;

    private Spinner comfortSpinner;
    private Spinner clothesTypeSpinner;
    private int comfort = LookEntry.COMFORT_NORMAL;
    private EditText temperatureEditText;
    private EditText clothesEditText;
    private int clothesType = LookEntry.CLOTHES_TOP;
    private ArrayAdapter spinnerAdapter;
    private ArrayAdapter clothesTypeAdapter;
    boolean fromMainActivity;
    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_look);

        Intent intent = getIntent();
        currentLookUri = intent.getData();
        fromMainActivity = intent.getBooleanExtra("fromMainActivity", false);

        if(currentLookUri == null) {
            invalidateOptionsMenu();
        } else {
            setTitle("Редактирование записи");
            getSupportLoaderManager().initLoader(EDIT_LOOK_LOADER, null,this);
        }

        comfortSpinner = findViewById(R.id.comfortSpinner);
        clothesTypeSpinner = findViewById(R.id.clothesTypeSpinner);
        temperatureEditText = findViewById(R.id.temperatureEditText);
        clothesEditText = findViewById(R.id.clothesEditText);

        spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.array_comfort, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comfortSpinner.setAdapter(spinnerAdapter);

        clothesTypeAdapter = ArrayAdapter.createFromResource(this,R.array.array_clothes, android.R.layout.simple_spinner_item);
        clothesTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clothesTypeSpinner.setAdapter(clothesTypeAdapter);

        comfortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedComfort = (String) parent.getItemAtPosition(position);
                if(!TextUtils.isEmpty(selectedComfort)){
                    if(selectedComfort.equals("Прохладно")){
                        comfort = LookEntry.COMFORT_COOL;
                    } else if (selectedComfort.equals("Жарковато")){
                        comfort = LookEntry.COMFORT_WARM;
                    } else if (selectedComfort.equals("Холодно")){
                        comfort = LookEntry.COMFORT_COLD;
                    }  else if (selectedComfort.equals("Жарко")){
                        comfort = LookEntry.COMFORT_HOT;
                    }else {
                        comfort = LookEntry.COMFORT_NORMAL;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                comfort = LookEntry.COMFORT_NORMAL;
            }
        });

        clothesTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedClothes = (String) parent.getItemAtPosition(position);
                if(!TextUtils.isEmpty(selectedClothes)){
                    if(selectedClothes.equals("Голова")){
                        clothesType = LookEntry.CLOTHES_HEAD;
                    } else if (selectedClothes.equals("Ноги")){
                        clothesType = LookEntry.CLOTHES_LEGS;
                    } else if (selectedClothes.equals("Обувь")){
                        clothesType = LookEntry.CLOTHES_SHOES;
                    }  else if (selectedClothes.equals("Кисти")){
                        clothesType = LookEntry.CLOTHES_HANDS;
                    }else {
                        clothesType = LookEntry.CLOTHES_TOP;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                clothesType = LookEntry.CLOTHES_TOP;
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if(currentLookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_look);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.look_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_look:
                saveLook ();
                return true;
            case R.id.delete_look:
                showDeleteLookDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveLook () {

        int temperature;
        try {
            temperature = Integer.parseInt(temperatureEditText.getText().toString());
        }catch (NumberFormatException nef) {
            Toast.makeText(this,
                    "Введите целочисленное значение температуры",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String clothes = clothesEditText.getText().toString();

        if (temperature > 57 || temperature < -92){
            Toast.makeText(this,
                    "Введите значение температуры для земных условий",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(clothes)) {
             Toast.makeText(this,"Введите одежду", Toast.LENGTH_LONG).show();
             return;

        }

        if (comfort == LookEntry.COMFORT_COOL) {
            temperature = temperature + 2;
            comfort = LookEntry.COMFORT_NORMAL;
        } else if (comfort == LookEntry.COMFORT_WARM) {
            temperature = temperature - 2;
            comfort = LookEntry.COMFORT_NORMAL;
        } else if (comfort == LookEntry.COMFORT_COLD) {
            temperature = temperature + 4;
            comfort = LookEntry.COMFORT_NORMAL;
        } else if (comfort == LookEntry.COMFORT_HOT) {
            temperature = temperature - 4;
            comfort = LookEntry.COMFORT_NORMAL;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(LookEntry.COLUMN_COMFORT,comfort);
        contentValues.put(LookEntry.COLUMN_TEMPERATURE,temperature);
        contentValues.put(LookEntry.COLUMN_CLOTHES_TYPE, clothesType);
        contentValues.put(LookEntry.COLUMN_CLOTHE,clothes);


        if (currentLookUri == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(LookEntry.CONTENT_URI, contentValues);

            if (uri == null) {
                Toast.makeText(this,
                        "Ввод данных в таблицу почему-то не получился",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Данные сохранены", Toast.LENGTH_LONG).show();
            }
            if (fromMainActivity) {
                Intent intent = new Intent ( this, MainActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity ( intent );
            } else {
                Intent intent = new Intent ( this, List.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity ( intent );
            }

        } else {
            int rowsChanged = getContentResolver().update(currentLookUri, contentValues, null, null);
            if (rowsChanged == 0) {
                Toast.makeText(this,
                        "Сохранение данных в таблицу почему-то не получилось",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Данные обновлены", Toast.LENGTH_LONG).show();
            }
            if (fromMainActivity) {
                Intent intent = new Intent ( this, MainActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity ( intent );
            } else {
                Intent intent = new Intent ( this, List.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity ( intent );
            }

        }

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                LookEntry._ID,
                LookEntry.COLUMN_COMFORT,
                LookEntry.COLUMN_TEMPERATURE,
                LookEntry.COLUMN_CLOTHES_TYPE,
                LookEntry.COLUMN_CLOTHE,

        };
        return new CursorLoader(this,
                currentLookUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if(cursor.moveToFirst()){
            int comfortColumnIndex = cursor.getColumnIndex(
                    LookEntry.COLUMN_COMFORT
            );
            int temperatureColumnIndex = cursor.getColumnIndex(
                    LookEntry.COLUMN_TEMPERATURE
            );
            int clothesTypeColumnIndex = cursor.getColumnIndex(
                    LookEntry.COLUMN_CLOTHES_TYPE
            );
            int clothesColumnIndex = cursor.getColumnIndex(
                    LookEntry.COLUMN_CLOTHE
            );

            int comfort = cursor.getInt(comfortColumnIndex);
            int temperature = cursor.getInt(temperatureColumnIndex);
            int clothesType = cursor.getInt(clothesTypeColumnIndex);
            String clothes = cursor.getString(clothesColumnIndex);

            temperatureEditText.setText(String.valueOf(temperature));
            clothesEditText.setText(clothes);

            switch (comfort) {
                case LookEntry.COMFORT_NORMAL:
                    comfortSpinner.setSelection(0);
                    break;
                case LookEntry.COMFORT_COOL:
                    comfortSpinner.setSelection(1);
                    break;
                case LookEntry.COMFORT_WARM:
                    comfortSpinner.setSelection(2);
                    break;
                case LookEntry.COMFORT_COLD:
                    comfortSpinner.setSelection(3);
                    break;
                case LookEntry.COMFORT_HOT:
                    comfortSpinner.setSelection(4);
                    break;
            }
            switch (clothesType) {
                case LookEntry.CLOTHES_TOP:
                    clothesTypeSpinner.setSelection(0);
                    break;
                case LookEntry.CLOTHES_HEAD:
                    clothesTypeSpinner.setSelection(1);
                    break;
                case LookEntry.CLOTHES_LEGS:
                    clothesTypeSpinner.setSelection(2);
                    break;
                case LookEntry.CLOTHES_SHOES:
                    clothesTypeSpinner.setSelection(3);
                    break;
                case LookEntry.CLOTHES_HANDS:
                    clothesTypeSpinner.setSelection(4);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void showDeleteLookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы хотите удалить запись?");
        builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteLook();
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteLook() {
        if (currentLookUri != null) {
            int rowsDeleted = getContentResolver().delete(currentLookUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, "Удаление не получилось", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Запись удалена",
                        Toast.LENGTH_LONG).show();
            }
            finish();
        }

    }

}