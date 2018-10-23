package com.example.android.mytranslation;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.android.mytranslation.YandexTranslation.text;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,TextToSpeech.OnInitListener {
    SharedPreferences shared;
    Gson gson;
    Context context=this;
    Handler handler;
    TextView getTranslation;
    Spinner spinner_input;
    Spinner drawableSpinner;
    private TextToSpeech tts;
    ImageView mSaveText;
    ImageView mPasteButtonSecond;


    ImageButton mCopyText;
    ImageButton mPasteButton;
    ImageButton mSharingButton;
    String inputLanFullName;
    String outputLanFullName;
    EditText mEditText;
    Button mButton;
    ImageButton mCompare;
    ImageButton mClear;
    ImageButton mSpeechButton;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    YandexTranslation.Translation getTrans;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private boolean doubleBackToExitPressedOnce = false;
    private int isDrawerOpen = 1;


    public static ArrayList<TranslationFav> mTransView = new ArrayList<TranslationFav>();
    Spinner mSpinner;
    ImageButton mMicrophone;
    String sourceText;
    String sourceLang ;
    String destinationLang;
    String key = "trnsl.1.1.20180813T114503Z.a8b9e491f443c7c6.aa15b175ca09c8508afef510a52094b149fba65d";
    YandexTranslation translation = new YandexTranslation();
    String[] langName;
    String[] langCode;

    ArrayList<TranslationFav> transListView;
    TinyDB tinydb;
    TranslationFav translationFav;
    SqlLiteHelper sqlLiteHelper;

    /*String[] langName = getResources().getStringArray(R.array.lang_name);
    String[] langCode = getResources().getStringArray(R.array.lang_code);*/

    int flags[] = {R.drawable.flag_southafrikan,R.drawable.flag_of_albania,R.drawable.flag_ethopia,R.drawable.sa
            ,R.drawable.flag_armenian,R.drawable.flag_of_azerbaijan,R.drawable.flag_of_bashkortostan,R.drawable.flag_basque_councry
            ,R.drawable.belarusian,R.drawable.flag_bangladesh,R.drawable.flag_of_bosnia_and_herzegovina,R.drawable.flag_bulgaria
            ,R.drawable.flag_of_myanmar,R.drawable.flag_andorra,R.drawable.flag_of_cuba,R.drawable.flag_china
            ,R.drawable.flag_croatia,R.drawable.flag_czech,R.drawable.flag_denmark_danish,R.drawable.flag_neatherland_dutch
            ,R.drawable.great_britain_i,R.drawable.flag_of_esperanto,R.drawable.flag_of_estonia,R.drawable.flag_of_finland
            ,R.drawable.flag_of_france,R.drawable.flag_spain,R.drawable.flag_geogerian,R.drawable.flag_germany
            ,R.drawable.flag_greece,R.drawable.indian_flag_small,R.drawable.flag_of_haiti,R.drawable.flag_of_israel
            ,R.drawable.flag_russia,R.drawable.indian_flag_small,R.drawable.flag_hungarian,R.drawable.flag_iceland
            ,R.drawable.flag_of_indonesia,R.drawable.flag_of_ireland,R.drawable.flag_italy,R.drawable.flag_japan
            ,R.drawable.flag_of_indonesia,R.drawable.indian_flag_small,R.drawable.kz_flag,R.drawable.cambodian_flag_small
            ,R.drawable.flag_korea,R.drawable.krygyz,R.drawable.flag_lao,R.drawable.vatican_latin
            ,R.drawable.flag_latvia,R.drawable.flag_of_lithuania,R.drawable.flag_luxemborg,R.drawable.flag_of_macedonia
            ,R.drawable.flag_of_madagascar,R.drawable.flag_malay,R.drawable.indian_flag_small,R.drawable.malta
            ,R.drawable.flag_newzealand_maori,R.drawable.indian_flag_small,R.drawable.flag_russia,R.drawable.flag_of_mongolia
            ,R.drawable.nepal_flag,R.drawable.flag_norway,R.drawable.flag_of_aruba,R.drawable.iran
            ,R.drawable.flag_pollish,R.drawable.portugal,R.drawable.indian_flag_small,R.drawable.flag_romania
            ,R.drawable.flag_russia,R.drawable.flag_of_scotland,R.drawable.serbia,R.drawable.flag_of_sri_lanka
            ,R.drawable.flag_of_slovakia,R.drawable.flag_of_slovenia,R.drawable.flag_spain,R.drawable.flag_sudan
            ,R.drawable.flag_of_tanzania,R.drawable.flag_of_sweden,R.drawable.phillipines,R.drawable.tajik
            ,R.drawable.indian_flag_small,R.drawable.flag_russia,R.drawable.indian_flag_small,R.drawable.flag_of_thailand
            ,R.drawable.flag_of_turkey,R.drawable.flag_russia,R.drawable.flag_of_ukraine,R.drawable.flag_of_pakistan
            ,R.drawable.uzbek,R.drawable.flag_vietnamese,R.drawable.flag_english,R.drawable.flag_southafrikan
            ,R.drawable.flag_of_israel};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TranslationFav translationFav;
        sqlLiteHelper = new SqlLiteHelper(context);
        mMicrophone = (ImageButton) findViewById(R.id.speech_icon);
        mSharingButton = (ImageButton) findViewById(R.id.sharing_button);
        langName = getResources().getStringArray(R.array.lang_name);
        Arrays.sort(langName);
        langCode = getResources().getStringArray(R.array.lang_code);
        String textToBeTranslated = "Hello world, yeah I know it is stereotye.";
        String languagePair = "en-fr"; //English to French ("<source_language>-<target_language>")
        getTranslation = (TextView)findViewById(R.id.get_translation);
        mCopyText = (ImageButton) findViewById(R.id.copy_translator);
        mPasteButton = (ImageButton)findViewById(R.id.image_paste);
        mPasteButtonSecond = (ImageView)findViewById(R.id.paste_button);
        mCompare = (ImageButton) findViewById(R.id.compare);
        mSaveText = (ImageView) findViewById(R.id.save_text);
        mSaveText.setEnabled(false);


        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayout ll = (LinearLayout)onCreatePanelView(R.id.linear_nav);
        nv = (NavigationView)findViewById(R.id.nv);
        //drawableSpinner = (Spinner) nv.getMenu().findItem(R.id.navigation_spinner).getActionView();
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.account:
                        Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();
                        dl.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.favorite:
                        Intent intent = new Intent(MainActivity.this,UserListActivity.class);
                        startActivity(intent);
                        //Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rate_apps:
                        Toast.makeText(MainActivity.this, "Rate App",Toast.LENGTH_SHORT).show();

                        break;

                    case R.id.more_apps:
                        Toast.makeText(MainActivity.this, "More App",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }
                return false;
            }
        });

        mMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });

        mCopyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getString = getTranslation.getText().toString();
                ClipData clip = ClipData.newPlainText(null, getString);
                if (clipboard != null && !getString.isEmpty()) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context,"Text Copied",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,"Text is empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mPasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String pasteData = "";
                if (clipboard == null) return;
                ClipData clip = clipboard.getPrimaryClip();
                if (clip == null) return;
                ClipData.Item item = clip.getItemAt(0);
                if (item == null) {Toast.makeText(context,"Clipboard is empty",Toast.LENGTH_SHORT).show();return;}
                item = clipboard.getPrimaryClip().getItemAt(0);
                pasteData=item.getText().toString();
                mEditText.setText(pasteData);
                mEditText.setSelection(mEditText.getText().length());



            }
        });
        mPasteButtonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String pasteData = "";
                if (clipboard == null) return;
                ClipData clip = clipboard.getPrimaryClip();
                if (clip == null) return;
                ClipData.Item item = clip.getItemAt(0);
                if (item == null) {Toast.makeText(context,"Clipboard is empty",Toast.LENGTH_SHORT).show();return;}
                item = clipboard.getPrimaryClip().getItemAt(0);
                pasteData=item.getText().toString();
                mEditText.setText(pasteData);
                mEditText.setSelection(mEditText.getText().length());

            }
        });

        mSharingButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!getTranslation.getText().toString().isEmpty())
               shareIt();
           }
       });

        mEditText = (EditText)findViewById(R.id.input_text);
        mButton = (Button)findViewById(R.id.get_translator_button);
        mClear = (ImageButton) findViewById(R.id.clear_button);
        mSpinner = (Spinner)findViewById(R.id.spinner);
        spinner_input = (Spinner)findViewById(R.id.input_language);
        mSpinner.setOnItemSelectedListener(this);
        spinner_input.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.lang_name,
                        android.R.layout.simple_spinner_item);
        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),flags,langName);



        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mSpinner.setAdapter(customAdapter);

        ArrayAdapter<CharSequence> sequenceArrayAdapter = ArrayAdapter
                .createFromResource(this,R.array.lang_name,
                        android.R.layout.simple_spinner_item);
        sequenceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_input.setAdapter(customAdapter);

        spinner_input.setSelection(20);



        //sourceText = String.valueOf(mEditText.getText());
        //Executing the translation function

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSaveText.setEnabled(true);
                mSaveText.setImageResource(R.drawable.ic_save_black_24dp);
                sourceText = String.valueOf(mEditText.getText());
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);






                translation.setKey(key)
                        .getTextObservable(sourceText, sourceLang, destinationLang)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onCompleted() {
                                getTranslation.setText(text);


                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String text) {

                            }
                        });

            }
        });
        init();


        mCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int langOnePosition;
                int langTwoPosition;
                langOnePosition = spinner_input.getSelectedItemPosition();
                langTwoPosition = mSpinner.getSelectedItemPosition();
                spinner_input.setSelection(langTwoPosition);
                mSpinner.setSelection(langOnePosition);


            }
        });
         //tinydb = new TinyDB(context);
         //gson = new Gson();

         transListView = new ArrayList<TranslationFav>();
        mSaveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translationFav = new TranslationFav();

                mSaveText.setEnabled(false);
                mSaveText.setImageResource(R.drawable.ic_save_grey_24dp);

                translationFav.setLanguageInputText(mEditText.getText().toString());
                translationFav.setLanguageInputName(inputLanFullName);
                translationFav.setTanslationOutputText(getTranslation.getText().toString());
                translationFav.setLanguageOutputName(outputLanFullName);
                transListView.add(translationFav);
                boolean isInserted=sqlLiteHelper.dataInsert(mEditText.getText().toString(),inputLanFullName,getTranslation.getText().toString(),outputLanFullName);

                mTransView.add(translationFav);
                Toast.makeText(context, "Text saved", Toast.LENGTH_SHORT).show();
                /*SharedPreferences prefs = getSharedPreferences("key", Context.MODE_PRIVATE);
                String jsonText = gson.toJson(mTransView);
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString("key",jsonText);
                prefsEditor.apply();*/


            }


        });

    }

    /*private void packagesharedPreferences() {
        SharedPreferences.Editor editor = shared.edit();
        Set<TranslationFav> set = new HashSet<>();
        set.addAll(mTransView);
        //editor.putStringSet("key",set);
        editor.apply();
    }*/


    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getTranslation.getText().toString();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void init() {
        tts = new TextToSpeech(MainActivity.this,this);
        mSpeechButton = (ImageButton) findViewById(R.id.text_speech_button);
        mSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut();
            }
        });


    }

    private void speakOut() {
        String speechText = getTranslation.getText().toString();
        tts.speak(speechText,TextToSpeech.QUEUE_FLUSH,null);

    }

    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hi speak something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mEditText.setText(result.get(0));
                    mEditText.setSelection(mEditText.getText().length());
                }
                break;
            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
            isDrawerOpen = 1;
            return;
        } else {
            isDrawerOpen = 0;
            //super.onBackPressed();
        }
        if (doubleBackToExitPressedOnce && isDrawerOpen == 0) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner mSpinner = (Spinner)parent;
        Spinner spinner_input = (Spinner)parent;
        if (mSpinner.getId() == R.id.spinner) {
            for (int i=0; i<langName.length; i++){
                if (i == position) {
                    outputLanFullName=langName[i];

                    destinationLang = langCode[i];
                }
            }
        }
        if (spinner_input.getId() == R.id.input_language) {
            for (int j=0; j<langName.length; j++) {
                if (j == position){
                    inputLanFullName=langName[j];
                    sourceLang = langCode[j];


                }
            }
        }



    }


    


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void clickClear(View view) {
        getTranslation.setText("");
        mEditText.setText("");
        mSaveText.setEnabled(false);
        mSaveText.setImageResource(R.drawable.ic_save_grey_24dp);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS && destinationLang!=null){
            final Locale[] availableLocales=Locale.getAvailableLocales();
            for(final Locale locale : availableLocales){
                if (destinationLang.equalsIgnoreCase(locale.getLanguage())) {
                    String speechLanguage = locale.getLanguage();
                    int result = tts.setLanguage(Locale.forLanguageTag(speechLanguage));
                    //Language is not Supported
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Intent installTTSIntent = new Intent();
                        installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        startActivity(installTTSIntent);
                    } else {
                        mSpeechButton.setEnabled(true);
                        speakOut();
                    }

                }
            }
        } else {
           // Toast.makeText(context,"Initialization failed",Toast.LENGTH_SHORT).show();
            return;
        }

    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            super.onDestroy();
        }
    }
}
