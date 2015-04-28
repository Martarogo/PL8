package converter.uniovi.es.converter;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    private final String UPDATE_URL = "http://download.finance.yahoo.com/d/quotes.csv?s=EURUSD=X&f=sl1d1t1ba&e=.csv";
    private static double mEuroToDollar;
    private EditText mEditTextEuros;
    private EditText mEditTextDollars;

    public void setEuroToDollar(double value) {
        mEuroToDollar = value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditTextEuros = (EditText) findViewById(R.id.editTextEuros);
        mEditTextDollars = (EditText) findViewById(R.id.editTextDollars);


        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        new UpdateRateTask().execute(UPDATE_URL);
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 60000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickToEuros(View v) {
        convert(mEditTextDollars, mEditTextEuros, 1/mEuroToDollar);
    }

    public void onClickToDollars(View v) {
        convert(mEditTextEuros, mEditTextDollars, mEuroToDollar);
    }


    public void convert(EditText editTextSource, EditText editTextDestination, double ConversionFactor) {

        String StringSource = editTextSource.getText().toString();

        double NumberSource;
        try {
            NumberSource = Double.parseDouble(StringSource);
        }
        catch (NumberFormatException nfe) {
            return;
        }

        double NumberDestination = NumberSource * ConversionFactor;

        String StringDestination = Double.toString(NumberDestination);

        editTextDestination.setText(StringDestination);
    }
}