package hu.ait.android.keely.migrainetracker.Data;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hu.ait.android.keely.migrainetracker.R;

/**
 * Use AsyncTask for getting the weather
 */
public class HttpGetTask extends AsyncTask<String, Void, String> {

    private Context ctx;

    public HttpGetTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream();

                int ch;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((ch = is.read()) != -1) {
                    bos.write(ch);
                }

                result = new String(bos.toByteArray());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }


        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Intent intentBrResult = new Intent(ctx.getString(R.string.FILTER_RESULT));
        intentBrResult.putExtra(ctx.getString(R.string.KEY_RESULT), result);
        ctx.sendBroadcast(intentBrResult);
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(intentBrResult);

    }
}
