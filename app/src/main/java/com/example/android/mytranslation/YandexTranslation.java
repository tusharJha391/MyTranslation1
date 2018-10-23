package com.example.android.mytranslation;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by android on 13/8/18.
 */

public class YandexTranslation {

    public static String ERROR_MESSAGE = "Can't translate!";
    private static String mKey;
    static String text;

    public YandexTranslation setKey(String key) {
        mKey = key;
        return this;
    }
    public Translation getTranslation(String sourceText, String sourceLang, String destinationLang) throws Throwable {

        String yandexUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + mKey
                + "&text=" + sourceText + "&lang=" + sourceLang + "-" + destinationLang;
        yandexUrl=yandexUrl.replace(" ", "%20");
        URL url = new URL(yandexUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        InputStream inputStream = conn.getInputStream();

        String result = convertInputStreamToString(inputStream);

        inputStream.close();
        conn.disconnect();

        return Translation.fromJson(result);
    }

    public Observable<Translation> getTranslationObservable(final String sourceText, final String sourceLang, final String destinationLang) {
        return Observable.create(new Observable.OnSubscribe<Translation>() {
            @Override
            public void call(Subscriber<? super Translation> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {

                        Translation translation = getTranslation(sourceText, sourceLang, destinationLang);
                        subscriber.onNext(translation);
                        subscriber.onCompleted();
                    }
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        });
    }
    public Observable<String> getTextObservable(final String sourceText, final String sourceLang, final String destinationLang) {
        return getTranslationObservable(sourceText, sourceLang, destinationLang)
                .map(new Func1<Translation, String>() {
                    @Override
                    public String call(Translation translation) {
                        if (Translation.hasTranslation(translation)){
                            text = translation.translations.get(0);
                            return translation.translations.get(0);
                        }
                        return ERROR_MESSAGE;
                    }
                });
    }

    public static class Translation {
        public int code;
        public String lang;
        public List<String> translations;

        public static Translation fromJson(String json) throws JSONException {
            Translation translation = new Translation();
            JSONObject jsonObj = new JSONObject(json);
            translation.code = jsonObj.getInt("code");
            translation.lang = jsonObj.getString("lang");
            JSONArray text = jsonObj.getJSONArray("text");
            translation.translations = fromJsonArray(text);
            return translation;
        }

        public static boolean hasTranslation(Translation t){
            return t != null && t.translations != null && t.translations.size() != 0;
        }
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
    private static List<String> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<String> list = new ArrayList<>();
        if (jsonArray != null && jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                String text = jsonArray.getString(i);
                list.add(text);
            }
        }
        return list;
    }

    private static String request(String URL) throws IOException {
        URL url = new URL(URL);
        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla");

        InputStream inStream = urlConn.getInputStream();

        String recieved = new BufferedReader(new InputStreamReader(inStream)).readLine();

        inStream.close();
        return recieved;
    }

    public static Map<String, String> getLangs() throws IOException {
        String langs;
        langs = request("https://translate.yandex.net/api/v1.5/tr.json/getLangs?key=" + mKey + "&ui=en");
        langs = langs.substring(langs.indexOf("langs")+7);
        langs = langs.substring(0, langs.length()-1);

        String[] splitLangs = langs.split(",");

        Map<String, String> languages = new HashMap<String, String>();
        for (String s : splitLangs) {
            String[] s2 = s.split(":");

            String key = s2[0].substring(1, s2[0].length()-1);
            String value = s2[1].substring(1, s2[1].length()-1);

            languages.put(key, value);
        }
        return languages;
    }

    public static String translate(String text, String sourceLang, String targetLang) throws IOException {
        String response = request("https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + mKey + "&text=" + text + "&lang=" + sourceLang + "-" + targetLang);
        return response.substring(response.indexOf("text")+8, response.length()-3);
    }

    public static String detectLanguage(String text) throws IOException {
        String response = request("https://translate.yandex.net/api/v1.5/tr.json/detect?key=" + mKey + "&text=" + text);
        return response.substring(response.indexOf("lang")+7, response.length()-2);
    }

    public static String getKey(Map<String, String> map, String value) {
        for (String key : map.keySet()) {
            if (map.get(key).equalsIgnoreCase(value)) {
                return key;
            }
        }
        return null;
    }


}
