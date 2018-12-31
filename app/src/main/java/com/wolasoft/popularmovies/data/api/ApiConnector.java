package com.wolasoft.popularmovies.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConnector {
    private static final String HOST = "http://api.themoviedb.org/3/";
    private static final String API_KEY = "your_api_key";
    private static final String API_KEY_PARAM = "api_key";

    private static final Gson gson = new GsonBuilder()
            .create();

    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(HOST)
                    .addConverterFactory(GsonConverterFactory.create(gson));


    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    public static <S> S createRetrofitService(Class<S> serviceClass) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request originalRequest = chain.request();
                HttpUrl originalHttpUrl = originalRequest.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();

                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .url(url)
                        .method(originalRequest.method(), originalRequest.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.retryOnConnectionFailure(true);
        httpClient.addInterceptor(logging);

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

}
