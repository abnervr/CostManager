package org.abner.manager;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import android.content.SharedPreferences;

public class Settings {

    public static final String READ_SMS = "read_sms";
    public static final String SMS_NUMBERS = "sms_numbers";
    public static final String SMS_CREDIT_IDENTIFIER = "sms_credit_identifier";
    public static final String SMS_DEBIT_IDENTIFIER = "sms_debit_identifier";
    public static final String SMS_IGNORE_IDENTIFIER = "sms_ignore_identifier";
    public static final String SMS_STORE_START = "sms_store_start";
    public static final String SMS_STORE_END = "sms_store_end";

    private static SharedPreferences preferences;

    public static void loadSetting(SharedPreferences preferences) {
        Settings.preferences = preferences;
    }

    public static boolean isReadSms() {
        return preferences.getBoolean(READ_SMS, false);
    }

    public static Set<String> getSmsNumbers() {
        return preferences.getStringSet(SMS_NUMBERS, new HashSet<String>());
    }

    public static String getCreditIdentifier() {
        return preferences.getString(SMS_CREDIT_IDENTIFIER, "").toLowerCase(Locale.US);
    }

    public static String getDebitIdentifier() {
        return preferences.getString(SMS_DEBIT_IDENTIFIER, "").toLowerCase(Locale.US);
    }

    public static String getIgnoreIdentifier() {
        return preferences.getString(SMS_IGNORE_IDENTIFIER, "").toLowerCase(Locale.US);
    }

    public static String getStoreStart() {
        return preferences.getString(SMS_STORE_START, "").toLowerCase(Locale.US);
    }

    public static String getStoreEnd() {
        return preferences.getString(SMS_STORE_END, "").toLowerCase(Locale.US);
    }
}
