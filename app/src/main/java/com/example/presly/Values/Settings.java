package com.example.presly.Values;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.example.presly.Places.PlaceDatabase;
import com.example.presly.R;

public class Settings extends PreferenceActivity {

    private ListPreference listPreferenceUnit;
    private ListPreference listPreferenceRadius;


    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        Context preferenceContext = this.getApplicationContext();

        preferenceContext.setTheme(R.style.AppThemePref);

        listPreferenceRadius = (ListPreference) getPreferenceManager().findPreference("radiusDistancePref");
        listPreferenceUnit = (ListPreference) getPreferenceManager().findPreference("unitDistancePref");
        Preference preference = getPreferenceManager().findPreference("clearFavoritesPref");

        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder builder = new AlertDialog.Builder(preference.getContext());  // alert dialog
                builder.setTitle(getString(R.string.delete_favorites_title))
                        .setMessage(getString(R.string.delete_all_fav_question))
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;

                            }
                        })
                        .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PlaceDatabase placeDatabase = new PlaceDatabase(getApplicationContext());
                                placeDatabase.deleteAllFavoritePlaces();
                                Intent intent = new Intent();
                                intent.putExtra("clearFavorite", true);
                                setResult(RESULT_OK, intent);
                                finish();

                                return;

                            }
                        });
                builder.setCancelable(false);
                builder.create().show();

                return false;
            }
        });
        //registering and set the on click event for the list preference:
        listPreferenceUnit.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                String preferenceValue = newValue.toString();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Settings",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("unitDistanceList",preferenceValue);
                editor.apply();

                return true;

            }
        });

        //registering and set the on click event for the list preference:
        listPreferenceRadius.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                String preferenceValue = newValue.toString();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Settings",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("radiusDistanceList",preferenceValue);
                editor.apply();

                return true;

            }
        });

    }

    protected void onListItemClick(ListView l, View v, int position, long id) {

        switch (position) {
            case 1:
                listPreferenceUnit.findIndexOfValue("Kilometers");
                listPreferenceUnit.findIndexOfValue("Miles");
                break;
            case 2:
                listPreferenceRadius.findIndexOfValue("2");
                listPreferenceRadius.findIndexOfValue("3");
                listPreferenceRadius.findIndexOfValue("5");
                break;
        }

        super.onListItemClick(l, v, position, id);

    }

}
