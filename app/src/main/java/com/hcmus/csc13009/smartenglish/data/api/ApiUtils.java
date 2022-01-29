package com.hcmus.csc13009.smartenglish.data.api;


public class ApiUtils {
    public static final String BASE_URL = "https://api.dictionaryapi.dev";

    public static DictionaryApi getRetrofitClient() {
        return RetrofitClient.getClient(BASE_URL).create(DictionaryApi.class);
    }
}
