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
    private static final String API_BASE_URL = "http://api.themoviedb.org/3/movie";
    public static final String API_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String API_KEY = "invalid_key";
    private static final String API_KEY_PARAM = "api_key";

    private static URL buildMoviesUrl(String path) {
        String customUrl = API_BASE_URL + "/" + path;
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

    public static String requestApi(String path) {
        URL builtUrl = buildMoviesUrl(path);

        Log.d("REQUEST", builtUrl.toString());

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

                response = responseBuffer.toString();
                bufferedReader.close();
                inputStream.close();
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
