package com.example.clothesfortheweather101;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.clothesfortheweather101.data.ClothesContract.LookEntry;

public class LookCursorAdapter extends CursorAdapter {
    public LookCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.look_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView temperatureTextView = view.findViewById(R.id.temperatureTextView);
        TextView clothesTypeTextView = view.findViewById(R.id.clothesTypeTextView);
        TextView clothesTextView = view.findViewById(R.id.clothesTextView);

        String temperature = cursor.getString(cursor.getColumnIndexOrThrow(LookEntry.COLUMN_TEMPERATURE));
        int clothesType = cursor.getInt(cursor.getColumnIndexOrThrow(LookEntry.COLUMN_CLOTHES_TYPE));
        String clothes = cursor.getString(cursor.getColumnIndexOrThrow(LookEntry.COLUMN_CLOTHE));

        temperatureTextView.setText(temperature);
        switch (clothesType) {
            case LookEntry.CLOTHES_TOP:
                clothesTypeTextView.setText("верх");
                break;
            case LookEntry.CLOTHES_HEAD:
                clothesTypeTextView.setText("голова");
                break;
            case LookEntry.CLOTHES_LEGS:
                clothesTypeTextView.setText("ноги");
                break;
            case LookEntry.CLOTHES_SHOES:
                clothesTypeTextView.setText("обувь");
                break;
            case LookEntry.CLOTHES_HANDS:
                clothesTypeTextView.setText("кисти");
                break;
        }
        clothesTextView.setText(clothes);

    }
}
