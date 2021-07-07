package com.example.englishtobangladictionary;

public class ExampleItem {

    private  String english_word;
    private String bangla_word;

    public ExampleItem(String english_word,String bangla_word)
    {
        this.english_word=english_word;
        this.bangla_word=bangla_word;
    }

    public String getBangla_word() {
        return bangla_word;
    }

    public String getEnglish_word() {
        return english_word;
    }
}
