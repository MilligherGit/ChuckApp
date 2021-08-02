package com.example.chuckapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class JokesFragment extends Fragment {

    EditText count;
    Button btn_reload;

    String quoteString = "";
    ArrayList<String> jokes = new ArrayList<String>();

    ListView listView;

    public static JokesFragment getInstance(){
        JokesFragment jokesFragment = new JokesFragment();
        return jokesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_jokes, container, false);

        listView = view.findViewById(R.id.list_view);
        count = view.findViewById(R.id.jokes_count_edit);
        btn_reload = view.findViewById(R.id.btn_reload);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, jokes);
        listView.setAdapter(arrayAdapter);

        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jokes_count = count.getText().toString().trim();
                if(jokes_count.equals("") || jokes_count == null || jokes_count.equals("0"))
                    Toast.makeText(getContext(), "Enter the number of jokes", Toast.LENGTH_LONG).show();
                else {
                    jokes.clear();
                    for (int i = 0; i < Integer.parseInt(count.getText().toString()); i++)
                    {
                        new QuoteLoader().execute();
                    }
                }
            }
        });

        return view;
    }


    private class QuoteLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            String jsonString = getJson("https://api.icndb.com/jokes/random");

            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                quoteString = jsonObject.getString("value");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            quoteString = "";
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!quoteString.equals("")) {
                quoteString = quoteString.substring(quoteString.indexOf("joke\":") + 7, quoteString.indexOf(",\"categories\""));
                jokes.add(quoteString);
                listView.invalidateViews();
            }
        }
    }

    private String getJson(String link) {
        String stream = "";
        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                stream = sb.toString();

                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

}