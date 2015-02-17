package apitutorial.bryanthornbury.com.apitutorial;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends ActionBarActivity {

    private Button pressButton;
    private EditText textField;
    private TextView echoText;

    private final String API_KEY = "d9dd1dafad3001854d8ced3da067ffb9";
    private final String F2F_URL = "http://food2fork.com/api/search?key=%s&q=%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        pressButton = (Button) findViewById(R.id.button);
        textField = (EditText) findViewById(R.id.editText);
        echoText = (TextView) findViewById(R.id.textView);

        //textField.setText("Enter words here.");

        pressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = textField.getText().toString();

                URL url = null;
                try {
                    url = new URL(getUrl(API_KEY, query));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                String contents = null;

                try {
                    StringBuilder sb = new StringBuilder();

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    url.openConnection().getInputStream()
                            )
                    );

                    String input;

                    while((input = reader.readLine()) != null)
                        sb.append(input);

                    contents = sb.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject root = null;

                try {
                    root = new JSONObject(contents);
                    JSONArray recipes = root.getJSONArray("recipes");
                    JSONObject first = recipes.getJSONObject(0);
                    Log.d("TAG", root.toString());
                    String title = first.getString("title");

                    echoText.setText(title);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public String getUrl(String key, String query) {
        try {
            return String.format(F2F_URL, key, URLEncoder.encode(query, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
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
}
