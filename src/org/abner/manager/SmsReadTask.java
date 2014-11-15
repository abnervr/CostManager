package org.abner.manager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class SmsReadTask extends AsyncTask<Object, Void, String> {

    public interface OnFinishListener {

        void onFinish();

    }

    private final Context context;

    private ProgressDialog progressDialog;
    //private Handler handler;

    private final OnFinishListener listener;

    public SmsReadTask(Context context, OnFinishListener listener) {
        this.context = context;
        if (listener == null && context instanceof OnFinishListener) {
            this.listener = (OnFinishListener) context;
        } else {
            this.listener = listener;
        }
    }

    public SmsReadTask(Context context) {
        this(context, null);
    }

    @Override
    protected void onPreExecute() {
        if (listener == null) {
            /*handler = new Handler(new Handler.Callback() {

                @Override
                public boolean handleMessage(Message msg) {
                    progressDialog.setMessage(msg.getData().getString("MESSAGE"));
                    return true;
                }
            });*/

            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Sincronizando...");
            progressDialog.setMessage("Aguarde");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            return getUnreadMessages();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            if (result instanceof String) {
                builder.setIcon(android.R.drawable.stat_notify_sync);
                builder.setTitle("Sincronização concluída");
                builder.setMessage(result);
            }
            builder.show();
        } else if (listener != null) {
            listener.onFinish();
        }
    }

    public String getUnreadMessages() {
        return new SmsReader(context).readNew();
    }

}
