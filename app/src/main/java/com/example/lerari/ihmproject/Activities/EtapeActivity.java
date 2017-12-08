package com.example.lerari.ihmproject.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.lerari.ihmproject.Entities.Etape;
import com.example.lerari.ihmproject.Entities.Recette;
import com.example.lerari.ihmproject.ItemAdapters.EtapeItemAdapter;
import com.example.lerari.ihmproject.ItemAdapters.IngredientItemAdapter;
import com.example.lerari.ihmproject.R;

import java.util.ArrayList;
import java.util.Locale;

public class EtapeActivity extends AppCompatActivity implements RecognitionListener {

    TextToSpeech tts;
    Recette recette;

    ImageView recetteImg;
    ListView listViewEtape;
    ListView listViewIngredient;
    ImageButton b1;
    private SpeechRecognizer speech = null;
    private String LOG_TAG = "VoiceRecognition";
    private int currentStep= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etape);
        b1= findViewById(R.id.button);
        recetteImg = findViewById(R.id.recette_img);

        recette = (Recette) getIntent().getSerializableExtra("recette");
        recetteImg.setImageResource(recette.getImage());

        /* Etape Adapter */
        EtapeItemAdapter etapesAdapter = new EtapeItemAdapter(this, recette.getEtapes());
        listViewEtape = findViewById(R.id.listViewEtape);
        listViewEtape.setAdapter(etapesAdapter);



        /* Ingredient Adapter */
        IngredientItemAdapter ingredientAdapter = new IngredientItemAdapter(this, recette.getIngredients());
        listViewIngredient = findViewById(R.id.listViewIngredient);
        listViewIngredient.setAdapter(ingredientAdapter);


         tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.FRENCH);
                }
            }
         });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // Nothing
            }

            @Override
            public void onError(String utteranceId) {
                // Nothing
            }

            @Override
            public void onDone(String utteranceId) {

                System.out.println("Lecture de l'étape --> lancement de la reconnaisance vocal ");
                SystemClock.sleep(recette.getEtapes().get(currentStep).getDurreEtape());
                System.out.println("Fin attente ");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        speech = SpeechRecognizer.createSpeechRecognizer(EtapeActivity.this);
                        speech.setRecognitionListener(EtapeActivity.this);

                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                        try {
                            speech.startListening(intent);
                        } catch (ActivityNotFoundException a) {

                        }
                    }});
            }
        });

         b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               readStep(currentStep);

            }
         });

    }

    public void onPause(){
        if(tts !=null){
            tts.stop();
            tts.shutdown();
            Log.i("TextToSpeech", "destroy");
        }
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
        super.onPause();
    }


    private void readStep(int current){
        tts.speak(recette.getEtapes().get(current).getEtape(), TextToSpeech.QUEUE_FLUSH, null, "Engine.KEY_PARAM_UTTERANCE_ID");
        Log.i("reading ",recette.getEtapes().get(current).getEtape());
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.i(LOG_TAG, "onBufferReceived: " + bytes);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if (matches.get(0).equals("next") ){
            Log.i("étape suivante ", "ok --------------");
            currentStep++;
            if (currentStep < recette.getEtapes().size()) {
                listViewEtape.smoothScrollToPosition(currentStep);
                readStep(currentStep);
            }


        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.i(LOG_TAG, "onEvent");
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }


}
