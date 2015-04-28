package converter.uniovi.es.converter;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateRateTask extends AsyncTask<String, Void, String> {

    private final int CSV_FIELDS = 6;
    private final int CSV_RATE_FIELD = 1;

    @Override
    protected void onPostExecute(String result) {
        double mEuroToDollar;
        try {
            mEuroToDollar = Double.parseDouble(result);
        }
        catch (NumberFormatException nfe) {
            return;
        }
        new MainActivity().setEuroToDollar(mEuroToDollar);
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return getCurrencyRateUsdRate(urls[0]);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String getCurrencyRateUsdRate(String url) throws IOException {
        return parseDataFromNetwork(readStream(openUrl(url)));
    }

    private String parseDataFromNetwork(String data) {
        String[] lineData = data.split(",");
        if (lineData.length != CSV_FIELDS) {
            return null;
        }
        return lineData[CSV_RATE_FIELD];
    }

    protected InputStream openUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

    protected String readStream(InputStream urlStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(urlStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }
}
