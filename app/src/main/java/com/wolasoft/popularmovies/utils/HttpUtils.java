package com.wolasoft.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    private static final String TAG = HttpUtils.class.getSimpleName();
    private static final String API_BASE_URI = "https://www.themoviedb.org/movie";
    private static final String API_KEY = "";
    private static final String API_KEY_PARAM = "api_key";

    private URL buildMoviesUrl(String path) {
        String customUrl = API_BASE_URI + "/" + path;
        Uri moviesDbUrl = Uri.parse(customUrl).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(moviesDbUrl.toString());
        } catch (Exception error) {
            Log.e(TAG, error.getMessage());
        }

        return url;
    }

    public String requestApi(String path) {
        URL builtUrl = buildMoviesUrl(path);

        HttpURLConnection connection = null;
        String response = null;

        try {
            String connectionMethod = "GET";
            connection = (HttpURLConnection) builtUrl.openConnection();
            connection.setRequestMethod(connectionMethod);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
                StringBuilder responseBuffer = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    responseBuffer.append(line);
                }

                bufferedReader.close();
                inputStream.close();
                response = responseBuffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return response;
    }
}
