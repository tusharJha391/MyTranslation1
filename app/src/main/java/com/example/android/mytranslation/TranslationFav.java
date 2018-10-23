package com.example.android.mytranslation;

/**
 * Created by android on 1/9/18.
 */

public class TranslationFav {
    String languageInputText;
    String languageInputName;
    String tanslationOutputText;
    String languageOutputName;

    public TranslationFav(){

    }
    public TranslationFav(String languageInputText, String languageInputName, String tanslationOutputText, String languageOutputName) {
        this.languageInputText = languageInputText;
        this.languageInputName = languageInputName;
        this.tanslationOutputText = tanslationOutputText;
        this.languageOutputName = languageOutputName;
    }

    public String getLanguageInputText() {
        return languageInputText;
    }

    public void setLanguageInputText(String languageInputText) {
        this.languageInputText = languageInputText;
    }

    public String getLanguageInputName() {
        return languageInputName;
    }

    public void setLanguageInputName(String languageInputName) {
        this.languageInputName = languageInputName;
    }

    public String getTanslationOutputText() {
        return tanslationOutputText;
    }

    public void setTanslationOutputText(String tanslationOutputText) {
        this.tanslationOutputText = tanslationOutputText;
    }

    public String getLanguageOutputName() {
        return languageOutputName;

    }

    public void setLanguageOutputName(String languageOutputName) {
        this.languageOutputName = languageOutputName;
    }
}
