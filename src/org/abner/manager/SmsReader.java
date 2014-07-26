package org.abner.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.abner.manager.model.sms.Sms;
import org.abner.manager.repository.sms.SmsRepository;
import org.abner.manager.repository.sms.dao.SmsDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SmsReader extends BroadcastReceiver {

    public static String ADDRESS = "address";
    public static String BODY = "body";
    public static String DATE_SENT = "date_sent";
    public static String SERVICE_CENTER = "service_center";

    private final Context context;

    public SmsReader(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        readNew();
    }

    public Set<String> getSmsNumbers() {
        String[] projection = new String[] {ADDRESS, BODY, SERVICE_CENTER};

        String selection = getIdentifiersFilter();
        Cursor cursor = context.getContentResolver()
                        .query(Uri.parse("content://sms/inbox"),
                                        projection, selection,
                                        null, "date_sent");

        Set<String> numbers = new HashSet<String>();
        if (cursor.moveToFirst()) {
            do {
                String body = cursor.getString(cursor.getColumnIndex(BODY)).toLowerCase(Locale.US);
                String number = cursor.getString(cursor.getColumnIndex(ADDRESS));

                if (findValue(body) != null) {
                    numbers.add(number);
                }
            } while (cursor.moveToNext());
        }
        return numbers;
    }

    public BigDecimal findValue(String body) {
        if (body != null) {
            List<Double> values = new ArrayList<Double>();
            for (String part : body.split("\\s")) {
                try {
                    if (part.indexOf(',') == part.length() - 3) {
                        part = part.replace(',', '.');
                    }
                    values.add(Double.parseDouble(part));
                } catch (NumberFormatException ignored) {}
            }
            if (values.size() >= 1) {
                return new BigDecimal(values.get(0));
            }
        }
        return null;
    }

    public String readNew() {
        Set<String> smsNumbers = Settings.getSmsNumbers();

        if (!Settings.isReadSms() || smsNumbers.isEmpty()) {
            return "Configure corretamente o monitoramento de SMS.";
        }
        SmsRepository smsRepository = new SmsDao(context);

        Sms lastSms = smsRepository.findLastSms(smsNumbers.toArray(new String[smsNumbers.size()]));
        String[] projection = new String[] {
                        ADDRESS,
                        BODY,
                        DATE_SENT,
                        SERVICE_CENTER};

        String selection = "address in (";
        for (String number : smsNumbers) {
            if (!selection.endsWith("(")) {
                selection += ",";
            }
            selection += number;
        }
        selection += ")";

        String filter = getIdentifiersFilter();
        if (filter != null) {
            selection += " and (" + filter + ")";
        }

        if (lastSms != null) {
            selection += " and date_sent > " + lastSms.getDateSent().getTime();
        }

        Cursor cursor = context.getContentResolver()
                        .query(Uri.parse("content://sms/inbox"),
                                        projection, selection,
                                        null, "date_sent");

        if (cursor == null) {
            return "Configure corretamente o monitoramento de SMS.";
        }

        return readSms(cursor);
    }

    private String readSms(Cursor cursor) {
        SmsRepository smsRepository = new SmsDao(context);
        try {
            int count = 0;
            if (cursor.moveToFirst()) {
                do {
                    int index = cursor.getColumnIndex("address");
                    String address = cursor.getString(index);

                    index = cursor.getColumnIndex("body");
                    String body = cursor.getString(index);

                    index = cursor.getColumnIndex("service_center");
                    String serviceCenter = cursor.getString(index);

                    index = cursor.getColumnIndex("date_sent");
                    Date dateSent = new Date(cursor.getLong(index));

                    Sms sms = new Sms();
                    sms.setAddress(address);
                    sms.setBody(body);
                    sms.setDateSent(dateSent);
                    sms.setServiceCenter(serviceCenter);
                    if (body != null) {

                        boolean containsIgnore = containsIgnore(body);
                        if (containsIgnore) {
                            sms.setDebito(null);
                        } else {
                            boolean containsCredit = containsCredit(body);
                            boolean containsDebit = containsDebit(body);
                            if (containsCredit && !containsDebit) {
                                sms.setDebito(false);
                            } else if (!containsCredit && containsDebit) {
                                sms.setDebito(true);
                            } else if (containsCredit && containsDebit) {
                                Log.w("sms", "No identifier found: " + body);
                            }
                        }
                    }
                    smsRepository.insert(sms);
                    count++;
                } while (cursor.moveToNext());
            }
            if (count > 0) {
                return count + " registro(s) adicionado(s).";
            } else {
                return "Nenhum registro encontrado.";
            }
        } finally {
            cursor.close();
        }
    }

    private boolean containsCredit(String body) {
        return containsKeys(body, Settings.getCreditIdentifier());
    }

    private boolean containsDebit(String body) {
        return containsKeys(body, Settings.getDebitIdentifier());
    }

    private boolean containsIgnore(String body) {
        return containsKeys(body, Settings.getIgnoreIdentifier());
    }

    private boolean containsKeys(String body, String key) {
        body = body.toUpperCase(Locale.US);
        for (String identifier : key.split(",")) {
            if (body.contains(identifier.toUpperCase(Locale.US))) {
                return true;
            }
        }
        return false;
    }

    private String getIdentifiersFilter() {
        String like = "upper(BODY) like '%";

        String selection = null;
        for (String id : Settings.getCreditIdentifier().split(",")) {
            if (selection == null) {
                selection = "";
            } else {
                selection += " or ";
            }
            selection += like + id.toUpperCase(Locale.US) + "%'";
        }
        for (String id : Settings.getDebitIdentifier().split(",")) {
            if (selection == null) {
                selection = "";
            } else {
                selection += " or ";
            }
            selection += like + id.toUpperCase(Locale.US) + "%'";
        }
        return selection;
    }

}
