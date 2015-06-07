package org.abner.manager.activities.settings;

import java.util.Set;

import org.abner.manager.R;
import org.abner.manager.Settings;
import org.abner.manager.SmsReader;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        MultiSelectListPreference smsNumbersPreference = (MultiSelectListPreference) findPreference(Settings.SMS_NUMBERS);
        Set<String> smsNumbers = new SmsReader(getActivity()).getSmsNumbers();
        CharSequence[] entryValues = smsNumbers.toArray(new String[smsNumbers.size()]);
        smsNumbersPreference.setEntryValues(entryValues);
        smsNumbersPreference.setEntries(entryValues);

        Set<String> numbers = Settings.getSmsNumbers();
        if (!numbers.isEmpty()) {
            smsNumbersPreference.setSummary(numbers.toString());
        }

        Preference preference = findPreference(Settings.SMS_CREDIT_IDENTIFIER);
        preference.setSummary(Settings.getCreditIdentifier());

        preference = findPreference(Settings.SMS_DEBIT_IDENTIFIER);
        preference.setSummary(Settings.getDebitIdentifier());

        preference = findPreference(Settings.SMS_DEBIT_IDENTIFIER);
        preference.setSummary(Settings.getDebitIdentifier());

        preference = findPreference(Settings.SMS_IGNORE_IDENTIFIER);
        preference.setSummary(Settings.getIgnoreIdentifier());

        preference = findPreference(Settings.SMS_STORE_START);
        preference.setSummary(Settings.getStoreStart());

        preference = findPreference(Settings.SMS_STORE_END);
        preference.setSummary(Settings.getStoreEnd());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference != null) {
            preference.setSummary(sharedPreferences.getString(key, ""));
        }
    }
}
