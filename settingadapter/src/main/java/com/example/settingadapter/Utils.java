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
package com.example.settingadapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Common functionality.
 */
public class Utils {


    private static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }




    /**
     * Get the String representation of the object.
     * @param object object
     * @return String value of param
     */
    public static String parseObject(Object object) {

        if (object instanceof String)
            return (String) object;

        if (object instanceof Boolean)
            return (Boolean)object ? "1" : "0";

        if (object instanceof Integer)
            return String.format(Locale.getDefault(), "%d", (Integer) object);

        return "";
    }


    // Date Helpers
    public static int getHour(Date date) {
        return getCalendar(date).get(Calendar.HOUR);
    }

    public static int getMinute(Date date) {
        return getCalendar(date).get(Calendar.MINUTE);
    }

    /**
     * Parse string into date with time.
     * @param setting setting value with format <c>HH:mm</c>
     * @return java.util.Date
     */
    public static Date getTimeFromSetting(String setting) {
        DateFormat format = new SimpleDateFormat("HH:mm", Locale.US);
        try {
            return format.parse(setting);
        } catch (ParseException exception) {
            return new Date();
        }
    }

    /**
     * Format date into string for time value.
     * @param date java.util.Date
     * @return String value with format <c>hh:mm a</c>
     */
    public static String stringForSettingTimeDisplay(Date date) {
        DateFormat format = new SimpleDateFormat("hh:mm a", Locale.US);
        return format.format(date);
    }
}
