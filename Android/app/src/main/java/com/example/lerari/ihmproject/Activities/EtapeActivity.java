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

public class EtapeActivity extends AppCompatActivity  {

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
    private EditText socketMessage;
    private TextView txvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etape);

        Thread myThread = new Thread(new MyServerThread());
        myThread.start();

        socketMessage = new EditText(this);
        txvResult = new TextView(this);

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

        socketMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().equals("ok")) {
                    tts.speak("avez-vous terminez vôtre étape ?", TextToSpeech.QUEUE_FLUSH, null, "Engine.KEY_PARAM_UTTERANCE_ID");
                    while (tts.isSpeaking()){
                    }
                    getSpeechInput(findViewById(android.R.id.content));
                }
            }
        });

        txvResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("oui")){
                    Log.i("étape suivante ", "Text en entrée correspond à 'Next' --------------");
                    currentStep++;
                    if (currentStep < recette.getEtapes().size()) {
                        scrollToNextEtape(currentStep);
                        readStep(currentStep);
                    }
                }
            }
        });

        /* configuration du textToSpeech object */
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.FRENCH);
                }
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

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);

        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txvResult.setText(result.get(0));

                }
                break;
        }
    }


    private void readStep(int current){
        tts.speak(recette.getEtapes().get(current).getEtape(), TextToSpeech.QUEUE_FLUSH, null, "Engine.KEY_PARAM_UTTERANCE_ID");
        Log.i("reading ",recette.getEtapes().get(current).getEtape());
    }


    private void scrollToNextEtape (int currentStep){
        int y = (int) listViewEtape.getChildAt(currentStep+1).getY() + 600; // position de l'étape courante + longueur de l'item (= 600)
        scrollView.smoothScrollTo(0, y );
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
                            //Toast.makeText(getApplicationContext(),"server running",Toast.LENGTH_SHORT).show();
                            socketMessage.setText(message);
                        }
                    });
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }




}
