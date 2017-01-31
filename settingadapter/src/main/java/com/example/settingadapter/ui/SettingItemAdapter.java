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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.settingadapter.R;
import com.example.settingadapter.Utils;
import com.example.settingadapter.model.Setting;

import java.util.List;

/**
 * Adapter to build out views for each list item in recycler view.
 * Uses types to determine which view to inflate.
 */
public class SettingItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Constants

    // Private Variables
    private LayoutInflater inflater;
    private List<Setting> settingList;
    private OnSettingActionListener settingListener;

    // Public Variables

    // Constructors

    public SettingItemAdapter(Context context, List<Setting> settingList, OnSettingActionListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.settingList = settingList;
        this.settingListener = listener;
    }

    public void setSettingList(List<Setting> settingList) {
        this.settingList = settingList;
        notifyDataSetChanged();
    }

    // Private Methods

    // Public Methods


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Setting.FOOTER:
                view = inflater.inflate(R.layout.recycler_item_setting_footer, parent, false);
                return new SimpleViewHolder(view);
            case Setting.HEADER:
                view = inflater.inflate(R.layout.recycler_item_setting_header, parent, false);
                return new SimpleViewHolder(view);
            case Setting.SELECTABLE_ITEM:
                view = inflater.inflate(R.layout.recycler_item_setting_simple, parent, false);
                return new SimpleViewHolder(view);
            case Setting.SWITCH:
                view = inflater.inflate(R.layout.recycler_item_setting_switch, parent, false);
                return new SwitchViewHolder(view);
            case Setting.LIST:
                view = inflater.inflate(R.layout.recycler_item_setting_list, parent, false);
                return new ListViewHolder(view);
            case Setting.DATE:
            case Setting.TIME:
                view = inflater.inflate(R.layout.recycler_item_setting_datetime, parent, false);
                return new DateTimeViewHolder(view);
            case Setting.INPUT:
                view = inflater.inflate(R.layout.recycler_item_setting_input, parent, false);
                return new InputViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Setting setting = settingList.get(holder.getAdapterPosition());

        // set on click listener for holder
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingListener != null)
                    settingListener.settingItemClick(holder.getItemViewType(), holder.getAdapterPosition());
            }
        });

        if (holder instanceof SimpleViewHolder) {
            SimpleViewHolder simpleHolder = (SimpleViewHolder) holder;
            simpleHolder.setText(setting.getTitle());
        } else if (holder instanceof SwitchViewHolder) {
            SwitchViewHolder switchViewHolder = (SwitchViewHolder) holder;
            switchViewHolder.setText(setting.getTitle());
            switchViewHolder.setChecked(setting.getValue().equals("1"));
            switchViewHolder.setCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (settingListener != null)
                        settingListener.settingItemChanged(Setting.SWITCH, holder.getAdapterPosition(), (isChecked ? "1" : "0"));
                }
            });
        } else if (holder instanceof ListViewHolder) {
            ListViewHolder listViewHolder = (ListViewHolder) holder;
            listViewHolder.setTitleText(setting.getTitle());
            listViewHolder.setValueText(setting.getValue());
        } else if (holder instanceof DateTimeViewHolder) {
            DateTimeViewHolder dateTimeViewHolder = (DateTimeViewHolder) holder;
            String value = setting.getValue();
            if (setting.getType() == Setting.DATE) {
                value = setting.getValue();
            } else if (setting.getType() == Setting.TIME) {
                java.util.Date time = Utils.getTimeFromSetting(setting.getValue());
                value = Utils.stringForSettingTimeDisplay(time);
            }
            dateTimeViewHolder.setValueText(value);
            dateTimeViewHolder.setTitleText(setting.getTitle());
        } else if (holder instanceof InputViewHolder) {
            InputViewHolder inputViewHolder = (InputViewHolder) holder;

            inputViewHolder.setTitleText(setting.getTitle());
            inputViewHolder.setValueText(setting.getValue());

            inputViewHolder.setTextWatcher(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if (settingListener != null)
                        settingListener.settingItemChanged(holder.getItemViewType(), holder.getAdapterPosition(), s.toString());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (settingList == null) {
            return 0;
        }
        return settingList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= -1 && position < settingList.size()) {
            Setting setting = settingList.get(position);
            return setting.getType();
        }
        return 0;
    }



    // Setting Action Listener for calling view

    interface OnSettingActionListener {
        /**
         * All items will call this method no matter the type.
         * @param type {@link Setting#type}
         * @param index index of item in {@link #settingList}
         */
        void settingItemClick(int type, int index);

        /**
         * Notify a change in items.
         * @param type {@link Setting#type}
         * @param index index of item in {@link #settingList}
         * @param newValue the new selected value
         */
        void settingItemChanged(int type, int index, Object newValue);
    }

    public interface OnSettingChangeListener {

        /**
         * Notify a change in items.
         * @param type {@link Setting#type}
         * @param key key of item in {@link #settingList}
         * @param newValue the new selected value
         */
        void settingItemChanged(int type, String key, String newValue);
    }



    // View Holders

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        SimpleViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycler_setting_simple_title);

        }

        void setText(String text) {
            if (textView != null) {
                textView.setText(text);
            }
        }
    }

    private class SwitchViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private Switch switchView;

        SwitchViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycler_setting_switch_textView);
            switchView = (Switch) itemView.findViewById(R.id.recycler_setting_switch_switch);
        }

        void setText(String text) {
            if (textView != null) {
                textView.setText(text);
            }
        }

        void setChecked(boolean value) {
            if (switchView != null) {
                switchView.setChecked(value);
            }
        }

        void setCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
            if (switchView != null) {
                switchView.setOnCheckedChangeListener(listener);
            }
        }
    }

    private class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewValue;

        ListViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.recycler_setting_list_textView_title);
            textViewValue = (TextView) itemView.findViewById(R.id.recycler_setting_list_textView_value);
        }

        void setTitleText(String text) {
            if (textViewTitle != null) {
                textViewTitle.setText(text);
            }
        }

        void setValueText(String text) {
            if (textViewValue != null) {
                textViewValue.setText(text);
            }
        }
    }

    private class InputViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private EditText editText;

        InputViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycler_setting_input_textView);
            editText = (EditText) itemView.findViewById(R.id.recycler_setting_input_editText);
        }

        void setTitleText(String text) {
            if (textView != null) {
                textView.setText(text);
            }
        }

        void setValueText(String text) {
            if (editText != null) {
                editText.setText(text);
            }
        }

        void setTextWatcher(TextWatcher textWatcher) {
            if (editText != null) {
                editText.addTextChangedListener(textWatcher);
            }
        }
    }

    private class DateTimeViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewValue;

        DateTimeViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.recycler_setting_datetime_textView_title);
            textViewValue = (TextView) itemView.findViewById(R.id.recycler_setting_datetime_textView_value);
        }

        void setTitleText(String text) {
            if (textViewTitle != null) {
                textViewTitle.setText(text);
            }
        }

        void setValueText(String text) {
            if (textViewValue != null) {
                textViewValue.setText(text);
            }
        }
    }

}

