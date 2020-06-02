package com.example.newsapk;

import android.text.TextUtils;
import android.util.Log;

import com.example.newsapk.model.News;

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
import java.util.ArrayList;
import java.util.List;

class QryUtils {
    private static final String LOG_TAG = QryUtils.class.getSimpleName();
    private static final int RESPONCE_OK = 200;

    private QryUtils() {
    }

    static List<News> fetch_news(String str_url_query) {
        URL str_url = create_URL(str_url_query);
        String JSON_responce = null;
        try {
            JSON_responce = make_HTTP_req(str_url);
        } catch (IOException e) {
            Log.e(LOG_TAG, String.valueOf(R.string.http_error_message), e);
        }
        List<News> news = extract_news(JSON_responce);

        // Return the list of {@link Earthquake}s
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL create_URL(String str_str_url) {
        URL str_url = null;
        try {
            str_url = new URL(str_str_url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem  in building the URL ", e);
        }
        return str_url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */

    private static String make_HTTP_req(URL str_url) throws IOException {
        String JSON_responce = "";
        if (str_url == null) {
            return JSON_responce;
        }
        HttpURLConnection str_url_conn = null;
        InputStream ip_stream = null;
        try {
            str_url_conn = (HttpURLConnection) str_url.openConnection();
            str_url_conn.setReadTimeout(10000);
            str_url_conn.setConnectTimeout(20000);
            str_url_conn.setRequestMethod("GET");
            str_url_conn.connect();

            if (str_url_conn.getResponseCode() == RESPONCE_OK) {
                ip_stream = str_url_conn.getInputStream();
                JSON_responce = read_stream(ip_stream);
            } else {
                Log.e(LOG_TAG, "Error of  response code:" + str_url_conn.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results", e);
        } finally {
            if (str_url_conn != null) {
                str_url_conn.disconnect();
            }
            if (ip_stream != null) {
                ip_stream.close();
            }
        }

        return JSON_responce;
    }

    private static String read_stream(InputStream ip_stream) throws IOException {
        StringBuilder op_stream = new StringBuilder();
        if (ip_stream != null) {
            InputStreamReader ip_stream_reader = new InputStreamReader(ip_stream, Charset.forName("UTF-8"));
            BufferedReader bfr_reader = new BufferedReader(ip_stream_reader);
            String current = bfr_reader.readLine();
            while (current != null) {
                op_stream.append(current);
                current = bfr_reader.readLine();
            }
        }
        return op_stream.toString();
    }

    private static List<News> extract_news(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> news_list = new ArrayList<>();
        try {
            JSONObject JSON_Responce = new JSONObject(newsJSON);

            // extract the JSONObject associated with the key called "response"
            JSONObject response_object = JSON_Responce.getJSONObject("response");
            JSONArray JSON_arr = response_object.getJSONArray("results");

            for (int i = 0; i < JSON_arr.length(); i++) {
                JSONObject current_obj = JSON_arr.getJSONObject(i);
                String str_title = current_obj.getString("webTitle");

                String str_section = current_obj.getString("sectionName");

                String str_date = current_obj.getString("webPublicationDate");
                String str_url = current_obj.getString("webUrl");


                StringBuilder str_author = new StringBuilder();
                JSONArray author_Arr = current_obj.getJSONArray("tags");
                if (author_Arr != null && author_Arr.length() > 0) {
                    // for each  str_author list them
                    int x = 1;
                    for (int j = 0; j < author_Arr.length(); j++) {
                        // get a single  str_author at position j in  str_author(s) list
                        JSONObject authors = author_Arr.getJSONObject(j);

                        // extract the value associated with the key called "webTitle"
                        String authr_listed = authors.optString("webTitle");

                        // list all authors separated by tabs

                        if (author_Arr.length() > 1) {
                            str_author.append(x + ". " + authr_listed + "\n");
                            // if there is only 1  str_author, then list just that  str_author
                        } else {
                            str_author.append("1. " + authr_listed);
                        }
                        x++;
                    }
                } else {
                    str_author.replace(0, 3, "No Authors");
                }

                String str_img = null;
                if (current_obj.has("fields")) {
                    JSONObject fiel_obj = current_obj.getJSONObject("fields");
                    if (fiel_obj.has("thumbnail")) {
                        str_img = fiel_obj.getString("thumbnail");
                    }
                } else {
                    str_img = null;
                }
                News news_item = new News(str_title, str_section, str_author.toString(), str_date, str_img, str_url);
                news_list.add(news_item);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        return news_list;
    }
}