package com.example.ahmedakram.ovicnews;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }
    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> news = extractFeatureFromJson(jsonResponse);
        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder ();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader (inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader (inputStreamReader);
            String line = reader.readLine ();
            while (line != null) {
                output.append (line);
                line = reader.readLine ();
            }
        }
        return output.toString ();
    }

    private static List<News> extractFeatureFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<News> newsArray = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject response = jsonObject.getJSONObject ("response");
            JSONArray results = response.getJSONArray ("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentNews = results.getJSONObject(i);
                String title = currentNews.getString("webTitle");
                String author;
                JSONArray authorArray = currentNews.getJSONArray ("tags");
                if (authorArray.length () != 0) {
                    JSONObject currentAuthor = authorArray.getJSONObject (0);
                    author = currentAuthor.getString ("webTitle");
                } else {
                    author = "Anonymous";
                }
                String section = currentNews.getString("sectionName");
                String url = currentNews.getString("webUrl");
                String date = currentNews.getString("webPublicationDate");
                News news = new News(title,author , section, url, date);
                newsArray.add(news);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return newsArray;
    }

}
