package com.example.lerari.ihmproject.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lerari.ihmproject.Entities.Etape;
import com.example.lerari.ihmproject.Entities.Recette;
import com.example.lerari.ihmproject.ItemAdapters.EtapeItemAdapter;
import com.example.lerari.ihmproject.ItemAdapters.IngredientItemAdapter;
import com.example.lerari.ihmproject.R;
import com.example.lerari.ihmproject.Utilities.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

public class EtapeActivity extends AppCompatActivity implements RecognitionListener {

    TextToSpeech tts;
    Recette recette;

    ImageView recetteImg;
    TextView recette_name, recette_nbrPersonne, recette_duree;
    ListView listViewEtape, listViewIngredient;
    ScrollView scrollView;
    ImageButton b1;
    private SpeechRecognizer speech = null;
    private String LOG_TAG = "VoiceRecognition";
    private int currentStep= 0;
    private EditText e1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etape);

        Thread myThread = new Thread(new MyServerThread());
        myThread.start();

        e1 = new EditText(this);

        scrollView= findViewById(R.id.scrollView);
        b1= findViewById(R.id.button);
        recetteImg = findViewById(R.id.recette_img);
        recette_name = findViewById(R.id.recette_name);
        recette_nbrPersonne = findViewById(R.id.recette_nbrPersonne);
        recette_duree = findViewById(R.id.recette_duree);


        recette = (Recette) getIntent().getSerializableExtra("recette");
        recetteImg.setImageResource(recette.getImage());
        recette_name.setText(recette.getNom());
        recette_nbrPersonne.setText(String.valueOf(recette.getNbrPersonne()) + " personnes");
        recette_duree.setText(recette.getDuree());

        /* Etape Adapter */
        EtapeItemAdapter etapesAdapter = new EtapeItemAdapter(this, recette.getEtapes());
        listViewEtape = findViewById(R.id.listViewEtape);
        listViewEtape.setAdapter(etapesAdapter);
        Utility.setDynamicHeight(listViewEtape);


        /* Ingredient Adapter */
        IngredientItemAdapter ingredientAdapter = new IngredientItemAdapter(this, recette.getIngredients());
        listViewIngredient = findViewById(R.id.listViewIngredient);
        listViewIngredient.setAdapter(ingredientAdapter);
        Utility.setDynamicHeight(listViewIngredient);


        /* configuration du textToSpeech object */
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
                // à la fin de la détection de la fin de l'étape -> lancement de la reconnaissance vocale
                System.out.println("Lecture de l'étape ");
                //SystemClock.sleep(recette.getEtapes().get(currentStep).getDureeEtape()); // Fabien : supprimer l'attente (seconde) et remplacer par le listner de la chaine de carractère (bluetooth)
                e1.addTextChangedListener(new TextWatcher() {

                      @Override
                      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                      }

                      @Override
                      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                      }

                      @Override
                      public void afterTextChanged(Editable editable) {
                          if(editable.toString().equals("ok")){




                              // lancement de la reconnaissance vocale
                              runOnUiThread(new Runnable() {

                                  @Override
                                  public void run() {
                                      System.out.println("Fin attente --> Lancement de la reconnaissance vocale ");
                                      Toast.makeText(getApplicationContext(),"in run on thread",Toast.LENGTH_SHORT).show();
                                      speech = SpeechRecognizer.createSpeechRecognizer(EtapeActivity.this);
                                      speech.setRecognitionListener(EtapeActivity.this);

                                      Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                      intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                      intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                                      try {
                                          Toast.makeText(getApplicationContext(),"in try run on thread",Toast.LENGTH_SHORT).show();
                                          System.out.println("Fin attente --> Lancement de la reconnaissance vocale 2 ");
                                          speech.startListening(intent);
                                      } catch (ActivityNotFoundException a) {

                                      }
                                  }});
                          }
                      }
                  });

                /*System.out.println("Fin attente --> Lancement de la reconnaissance vocale ");

                // lancement de la reconnaissance vocale
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
                    */
            }
        });

         b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStep= 0;
                scrollToNextEtape(currentStep);
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

    private void scrollToNextEtape (int currentStep){
        int y = (int) listViewEtape.getChildAt(currentStep).getY() + 600; // position de l'étape courante + longueur de l'item (= 600)
        scrollView.smoothScrollTo(0, y );
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

        // Résultat de la reconnaissance vocale : si c'est égale à Next passer à l'étape suivante
        if (matches.get(0).equals("next") ){
            Log.i("étape suivante ", "Text en entrée correspond à 'Next' --------------");
            currentStep++;
            if (currentStep < recette.getEtapes().size()) {
                scrollToNextEtape(currentStep);
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

    class MyServerThread implements Runnable{

        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;
        String message;
        Handler h = new Handler();

        @Override
        public void run() {
            try {
                ss = new ServerSocket(7801);
                Log.wtf("server","create new server " + ss.getLocalSocketAddress().toString());
                while (true) {
                    s = ss.accept();
                    isr = new InputStreamReader(s.getInputStream());
                    br = new BufferedReader(isr);
                    message = br.readLine();

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"server running",Toast.LENGTH_SHORT).show();
                            e1.setText(message);
                        }
                    });
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }




}
