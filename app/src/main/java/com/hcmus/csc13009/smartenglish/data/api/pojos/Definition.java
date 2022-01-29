package com.hcmus.csc13009.smartenglish.data.api.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Definition {

    @SerializedName("definition")
    @Expose
    private String definition;
    @SerializedName("synonyms")
    @Expose
    private List<String> synonyms = null;
    @SerializedName("antonyms")
    @Expose
    private List<Object> antonyms = null;
    @SerializedName("example")
    @Expose
    private String example;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<Object> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(List<Object> antonyms) {
        this.antonyms = antonyms;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

}