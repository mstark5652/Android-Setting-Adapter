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
package com.example.settingadapter.model;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Setting model to display different types
 * of settings/preferences to the user.
 */
public class Setting implements Parcelable {

    // Constants
    public static final int HEADER = 0;
    public static final int SWITCH = 1;
    public static final int LIST = 2;
    public static final int SELECTABLE_ITEM = 3;
    public static final int INPUT = 4;
    public static final int DATE = 5; // TODO: 1/30/17 implement
    public static final int TIME = 6;
    public static final int FOOTER = 7;

    // Private Variables

    private int type = -1;
    private String key;
    private String title;
    private String value;
    private List<String> options = new ArrayList<>();

    // Public Variables

    // Constructors

    public Setting() { }

    public Setting(int type, String key, String title, String value, List<String> options) {
        this.type = type;
        this.key = key;
        this.title = title;
        this.value = value;
        this.options = options;
    }

    // Private Methods

    // Public Methods


    // Getters


    public int getType() {
        return this.type;
    }

    public String getKey() {
        return this.key;
    }

    public String getTitle() {
        if (title == null)
            return "";
        return this.title;
    }

    public String getValue() {
        if (value == null)
            return "";

        return this.value;
    }

    public List<String> getOptions() {
        if (options == null)
            return new ArrayList<>();

        return options;
    }

    // Setters

    public void setType(int type) {
        if (type > FOOTER || type < HEADER)
            throw new UnsupportedOperationException("Setting Adapter could not find a valid type. Found type of  " + type);

        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Set options for setting item
     * @param options options for user to select
     */
    public void setOptions(List<String> options) {
        this.options = options;
    }

    // Builder Methods

    public static class Builder {
        // Private variables

        private Context context;
        private int type = -1;
        private String key = "";
        private String title = "";
        private String value = "";
        private List<String> options = new ArrayList<>();

        // Constructors
        public Builder() { }

        public Builder(Context context) {
            this.context = context;
        }

        // Private Methods

        /**
         * Check that Builder provided attributes is enough to create a setting item.
         */
        private void checkValidity() {
            if (type < HEADER || type > FOOTER) {
                throw new UnsupportedOperationException("Setting Adapter could not find a valid type. Found type of  " + type);
            }

            if ((type != HEADER && type != FOOTER) && (key == null || key.isEmpty())) {
                throw new UnsupportedOperationException("Setting Adapter could not find a valid key.");
            }
        }

        // Public Methods


        public static Builder with(@NonNull Context context) {
            return new Builder(context);
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setKey(@NonNull String key) {
            this.key = key;
            return this;
        }

        public Builder setTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(@StringRes int resId) {
            try {
                this.title = context.getString(resId);
            } catch (Resources.NotFoundException exception) {
                throw new Resources.NotFoundException("SettingAdapter could not find string resource was not found. Please provide a valid resource id.");
            }
            return this;
        }

        public Builder setValue(@NonNull String value) {
            this.value = value;
            return this;
        }

        public Builder setValue(@StringRes int resId) {
            try {
                this.value = context.getString(resId);
            } catch (Resources.NotFoundException exception) {
                throw new Resources.NotFoundException("SettingAdapter could not find string resource was not found. Please provide a valid resource id.");
            }
            return this;
        }

        public Builder setOptions(List<String> options) {
            if (options == null) {
                this.options = new ArrayList<>();
            } else {
                this.options = options;
            }
            return this;
        }

        public Setting build() {
            checkValidity();


            return new Setting(type, key, title, value, options);
        }
    }

    // Parcel Implementation

    private Setting(Parcel in) {
        type = in.readInt();
        key = in.readString();
        title = in.readString();
        value = in.readString();
        in.readList(options, null);
    }


    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(key);
        dest.writeString(title);
        dest.writeString(value);
        dest.writeList(options);

    }


    public static final Creator<Setting> CREATOR = new Creator<Setting>() {
        public Setting createFromParcel(Parcel in) {
            return new Setting(in);
        }

        public Setting[] newArray(int size) {
            return new Setting[0];
        }
    };

}
