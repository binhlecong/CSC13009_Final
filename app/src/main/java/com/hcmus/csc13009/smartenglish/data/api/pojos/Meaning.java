package com.hcmus.csc13009.smartenglish.data.api.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Meaning {

    @SerializedName("partOfSpeech")
    @Expose
    private String partOfSpeech;
    @SerializedName("definitions")
    @Expose
    private List<Definition> definitions = null;

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

}