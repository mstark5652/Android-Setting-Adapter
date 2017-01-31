/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Michael Stark
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.settingadapter.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;

import com.example.settingadapter.R;
import com.example.settingadapter.Utils;
import com.example.settingadapter.model.Setting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * TODO: add description
 */
public class SettingItemFragment extends Fragment implements SettingItemAdapter.OnSettingActionListener {

    // Constants
    public static final String SETTING_LIST = "SettingListKey";
    public static final String TITLE = "SettingTitle";

    // Private Variables
    private RecyclerView recyclerView;
    private SettingItemAdapter settingItemAdapter;

    private List<Setting> settingList = new ArrayList<>();
    private SettingItemAdapter.OnSettingChangeListener settingChangeListener;

    private Dialog listDialog;
    private boolean shouldRefresh = false;

    // Private Variables

    // Public Variables

    // Fragment Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_item, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.setting_item_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        settingItemAdapter = new SettingItemAdapter(getContext(), settingList, this);
        recyclerView.setAdapter(settingItemAdapter);
        recyclerView.addItemDecoration(new EmptyDividerItem());

        return view;
    }

    // Private Methods

    // Public Methods


    /**
     * Set the settings to display to the user.
     * @param settingList collection of {@link Setting}
     */
    public void setSettingList(List<Setting> settingList) {
        if (settingList == null) {
            this.settingList = new ArrayList<>();
        } else {
            this.settingList = settingList;
        }

        this.settingItemAdapter.setSettingList(this.settingList);
    }

    /**
     * Set a listener to receive callbacks when a setting has changed values.
     * @param listener {@link com.example.settingadapter.ui.SettingItemAdapter.OnSettingChangeListener}
     */
    public void setOnSettingChangeListener(SettingItemAdapter.OnSettingChangeListener listener) {
        this.settingChangeListener = listener;
    }

    @Override
    public void settingItemClick(final int type, final int index) {
        final Setting setting = settingList.get(index);
        switch (type) {
            case Setting.DATE:

                break;
            case Setting.TIME:
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        String value = String.format(Locale.getDefault(), "%d:%d", hour, min);
                        setting.setValue(value);
                        shouldRefresh = true;
                        settingItemChanged(type, index, value);
                    }
                };
                //Context context, TimePickerDialog.OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView)
                Date time = Utils.getTimeFromSetting(setting.getValue());
                int hour = Utils.getHour(time);
                int min = Utils.getMinute(time);

                TimePickerDialog timeDialog = new TimePickerDialog(getContext(), timeSetListener, hour, min, false);
                timeDialog.show();
                break;
            case Setting.LIST:
                listDialog = new Dialog(getContext());
                listDialog.setContentView(R.layout.dialog_list);
                ListView listView = (ListView) listDialog.findViewById(R.id.dialog_listView);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, setting.getOptions());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        setting.setValue(setting.getOptions().get(i));
                        shouldRefresh = true;
                        settingItemChanged(type, index, setting.getOptions().get(i));
                    }
                });
                listDialog.setCancelable(true);
                listDialog.show();
                break;
            case Setting.SELECTABLE_ITEM:
                if (settingChangeListener != null) {
                    settingChangeListener.settingItemChanged(type, setting.getKey(), setting.getValue());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void settingItemChanged(int type, int index, Object newValue) {
        String value = Utils.parseObject(newValue);

        Setting selected = settingList.get(index);
        if (selected != null) {
            if (selected.getKey() != null && !selected.getKey().isEmpty()) {
                selected.setValue(value);
                if (settingChangeListener != null) {
                    settingChangeListener.settingItemChanged(type, selected.getKey(), value);
                }
            }
        }
        if (shouldRefresh) {
            if (listDialog != null) {
                listDialog.dismiss();
            }
            settingItemAdapter.setSettingList(this.settingList);
            shouldRefresh = false;
        }
    }
}
