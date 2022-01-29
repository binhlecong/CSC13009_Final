package com.hcmus.csc13009.smartenglish.data.api;

import com.hcmus.csc13009.smartenglish.data.api.pojos.Example;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApi {
    @GET("/api/v2/entries/en/{word}")
    Call<ArrayList<Example>> getExample(@Path("word") String word);
}
