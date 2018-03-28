package tn.rnu.fmt.zoozfmt;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar1);
        new Downloader().execute();
    }
    private class Downloader extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //set PB proiperties
            progressBar.setMax(100);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //update progressbar
            progressBar.setProgress(values[0]);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            for (int i = 0; i < 100; i++) {
                publishProgress(i);
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            //Toast.makeText(getApplicationContext(), "PB finished", Toast.LENGTH_LONG).show();

        }
    }
}
