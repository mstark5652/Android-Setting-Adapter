package com.example.settingadapterexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.settingadapter.model.Setting;
import com.example.settingadapter.ui.SettingItemAdapter;
import com.example.settingadapter.ui.SettingItemFragment;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SettingItemAdapter.OnSettingChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SettingItemFragment settingItemFragment = (SettingItemFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment_setting_item);
        settingItemFragment.setSettingList(buildSettings());
        settingItemFragment.setOnSettingChangeListener(this);

    }


    private java.util.List<Setting> buildSettings() {
        List<Setting> results = new java.util.ArrayList<>();
        List<String> dayItems = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

        results.add(Setting.Builder.with(this).setType(Setting.HEADER).setTitle("This is my time header").build());
        results.add(Setting.Builder.with(this).setType(Setting.TIME).setKey("NEW_TIME").setTitle("Pick a time:").build());
        results.add(Setting.Builder.with(this).setType(Setting.TIME).setKey("EXISTING_TIME").setTitle("Pick a new time:").setValue("15:30").build());
        results.add(Setting.Builder.with(this).setType(Setting.HEADER).setTitle("This is my second header").build());
        results.add(Setting.Builder.with(this).setType(Setting.LIST).setKey("DAY_OF_WEEK").setTitle("Pick a day: ").setOptions(dayItems).build());
        results.add(Setting.Builder.with(this).setType(Setting.INPUT).setKey("NAME_INPUT").setTitle("Enter name: ").build());
        results.add(Setting.Builder.with(this).setType(Setting.FOOTER).setTitle("This is my footer").build());

        return results;
    }

    @Override
    public void settingItemChanged(int type, String key, String newValue) {
        Toast.makeText(this,
                String.format(Locale.getDefault(), "Setting with key %s value has changed to %s", key, newValue),
                Toast.LENGTH_LONG)
                .show();
    }
}
