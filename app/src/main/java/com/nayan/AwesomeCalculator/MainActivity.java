package com.nayan.AwesomeCalculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int[] numericButtons = {R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
                                    R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
                                    R.id.button00,R.id.buttonPi};

    private int[] operatorButtons = {R.id.buttonMultiply, R.id.buttonAddition, R.id.buttonSubtraction,
                                    R.id.buttonDivide, R.id.buttonModulus};

    private Button buttonCube;
    private Button buttonPower;
    private Button button2power;
    private Button buttonSin;
    private Button buttonTan;
    private Button buttonCos;
    private Button buttonLog;
    private Button buttonLn;
    private Button buttonCubicRoot;
    private Button buttonSquare;
    private Button button1ny;
    private Button buttonSqrt;
    private Button buttonEular;

    private LinearLayout llScientific, llScientific2;

    private Button buttonScientific;

    private TextView tvResult;
    private TextView tvInput;
    private RecyclerView recyclerViewHistory;
    private boolean isScientific = false;
    private boolean lastNumeric;
    private boolean stateError;
    private boolean lastDot;
    private boolean isHistoryListOpen = false;

    private int leftBracketCount = 0, rightBracketCount = 0;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private int onEqualEventsCount = 0;
    private int eventsCount = 0;

    private HistoryAdapter historyAdapter;
    private ArrayList<HistoryModel> historyModelArrayList;


    private InterstitialAd mInterstitialAd = null;
    private AdView adView;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("TAG", "onCreate: initialized");

        // load Banner Ads
        loadAdMobBanner();

        // if a shared preference is already stored
        readDataFromSharedPreference();

        //  Find the views
        initViews();

        // Load data of created history
        loadData();

        // load Interstitial ads
        loadAdmobInterstitial();


        //  Find and OnClickListener on Numeric Buttons
        setNumericOnClickListener();

        //  Find and set OnClickListener on Operator Buttons
        setOperatorOnClickListener();

        buttonCube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("^(3)");
                    leftBracketCount++;
                    rightBracketCount++;
                }
            }
        });

        buttonSin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("*sin(");
                }else{
                    tvInput.append("sin(");
                }
                leftBracketCount++;
                lastNumeric = false;
            }
        });

        buttonCos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("*cos(");
                }else{
                    tvInput.append("cos(");
                }
                leftBracketCount++;
                lastNumeric = false;
            }
        });

        buttonTan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("*tan(");
                }else{
                    tvInput.append("tan(");
                }
                leftBracketCount++;
                lastNumeric = false;
            }
        });

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("*log(");
                }else{
                    tvInput.append("log(");
                }
                leftBracketCount++;
                lastNumeric = false;
            }
        });

        buttonLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("*ln(");
                }else{
                    tvInput.append("ln(");
                }
                leftBracketCount++;
                lastNumeric = false;
            }
        });

        buttonCubicRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("*3√(");
                }else{
                    tvInput.append("3√(");
                }
                leftBracketCount++;
                lastNumeric = false;
            }
        });

        buttonPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("^(");
                    leftBracketCount++;
                    lastNumeric = false;
                }else{
                    Toast.makeText(MainActivity.this,"Empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button2power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("*2^(");
                }else{
                    tvInput.append("2^(");
                }
                leftBracketCount++;
                lastNumeric = false;
            }
        });

        buttonSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("^(2)");
                    leftBracketCount++;
                    rightBracketCount++;
                }else{
                    Toast.makeText(MainActivity.this,"Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button1ny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("*1/(");
                }else{
                    tvInput.append("1/(");
                }
                leftBracketCount++;
                lastNumeric = false;
            }
        });

        buttonSqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("*√(");
                }else{
                    tvInput.append("√(");
                }
                leftBracketCount++;
                lastNumeric = false;
            }
        });

        buttonEular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().toString().length() != 0 && lastNumeric){
                    tvInput.append("*e");
                }else{
                    tvInput.append("e");
                }
            }
        });

        buttonScientific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isScientific){
                    llScientific.setVisibility(View.VISIBLE);
                    llScientific2.setVisibility(View.VISIBLE);
                    isScientific = true;
                }else{
                    llScientific.setVisibility(View.GONE);
                    llScientific2.setVisibility(View.GONE);
                    isScientific = false;
                }
            }
        });


        tvInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                scrollToRight();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //  Erase one character
        findViewById(R.id.iconErase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvInput.getText().length() != 0){
                    if(
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("1") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("2") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("3") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("4") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("5") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("6") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("7") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("8") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("9") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("0")||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("π")

                    ){
                        lastNumeric = true;
                    }

                    if(
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("%") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("*") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("+") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("-") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("/") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("√") ||
                            tvInput.getText().subSequence(0, tvInput.length()-1).toString().endsWith("(")
                    ){
                        lastNumeric = false;
                    }

                    tvInput.setText(tvInput.getText().subSequence(0,tvInput.length()-1));

                }
            }
        });

        //  Show Calculation History
        findViewById(R.id.iconHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: " + isHistoryListOpen);
                if(!isHistoryListOpen){
                    findViewById(R.id.rvHistory).setVisibility(View.VISIBLE);
                    loadData();
                    Log.d("TAG", "onClick: recyclerView is called");
                    buildRecyclerView();
                    isHistoryListOpen = true;
                }else {
                    findViewById(R.id.rvHistory).setVisibility(View.GONE);
                    isHistoryListOpen = false;
                }
            }
        });


        findViewById(R.id.clearHistoryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });

        //  Open Age Calculation Activity
        findViewById(R.id.iconAge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AgeCalculationActivity.class);
                if (mInterstitialAd != null && eventsCount > 4) {
                    eventsCount = 0;
                    majorEvent(eventsCount);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            startActivity(intent);
                            mInterstitialAd=null;
                            loadAdmobInterstitial();
                        }
                    });
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    eventsCount++;
                    majorEvent(eventsCount);
                    startActivity(intent);
                    Log.d("TAG", "openIvHistory: else history loadAdmobInterstitial started");
                    loadAdmobInterstitial();
                    Log.d("TAG", "openIvHistory: else history loadAdmobInterstitial ended");
                }
            }
        });

        // banner ad
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void readDataFromSharedPreference() {
        Log.d("TAG", "readDataFromSharedPreference: initialized");
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        eventsCount = mPreferences.getInt("no_of_events",0);
        Log.d("TAG", "readDataFromSharedPreference: " + eventsCount);
    }

    private void loadAdMobBanner() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void loadAdmobInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getString(R.string.interstitial), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.d("TAG", "onAdLoaded " + mInterstitialAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAG", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });

        Log.d("TAG", "loadAdmobInterstitial: status of ads " + mInterstitialAd);
    }

    private void deleteData() {
        Log.d("TAG", "deleteData: delete history invoked");
        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        Log.d("TAG", "deleteData: delete completed" + historyModelArrayList.size());
        buildRecyclerView();
        if(historyModelArrayList != null){
            historyModelArrayList.clear();
        }
    }

    private void buildRecyclerView() {
        historyAdapter = new HistoryAdapter(historyModelArrayList, MainActivity.this);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerViewHistory.setAdapter(historyAdapter);
        Log.d("TAG", "buildRecyclerView: " + historyModelArrayList.size());
    }

    private void setOperatorOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if the currentState is Error don't append the operator
                // if the last input is numeric only then append the operator
                if(lastNumeric && !stateError){
                    Button button = (Button) view;
                    tvInput.append(button.getText());
                    lastNumeric = false;
                    lastDot = false; // reset the dot flag
//                    scrollToRight();
                }
            }
        };

        // attaching the listener to all the numeric buttons
        for(int id : operatorButtons){
            findViewById(id).setOnClickListener(listener);
        }

        // decimal values
        findViewById(R.id.buttonDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lastNumeric && !stateError && !lastDot){
                    tvInput.append(".");
                    lastNumeric = false;
                    lastDot = false;
                }
//                scrollToRight();
            }
        });

        // All CLEAR AC button
        findViewById(R.id.buttonClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvInput.setText("");
                tvResult.setText("");
                lastNumeric = false;
                stateError = false;
                lastDot = false;
                leftBracketCount = 0;
                rightBracketCount = 0;
            }
        });

        // EqualsTo button
        findViewById(R.id.buttonEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEqual();
            }
        });


        // Voice Command onClickListener
        findViewById(R.id.iconMicrophone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stateError){
                    tvInput.setText(R.string.tryAgain);
                }else{
                    promptSpeechInput();
                }
                lastNumeric = true;
            }
        });

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL
                ,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE
                ,Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT
                ,getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);

        }catch (ActivityNotFoundException exception){
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String change = result.get(0);
                    change = change.replace("x","*");
                    change = change.replace("X","*");
                    change = change.replace("add","+");
                    change = change.replace("addition","+");
                    change = change.replace("sub","-");
                    change = change.replace("subtraction","-");
                    change = change.replace("plus","+");
                    change = change.replace("minus","-");
                    change = change.replace("times","*");
                    change = change.replace("into","*");
                    change = change.replace("multiply by","*");
                    change = change.replace("divide by","/");
                    change = change.replace("divide","/");
                    change = change.replace("equal","=");

                    if(change.contains("=")){
                        change = change.replace("=","");
                     }
                     tvInput.setText(change);
                     onEqual();
                }
                break;
        }



    }

    private void onEqual() {

        if(leftBracketCount != rightBracketCount){
            Toast.makeText(MainActivity.this,"Invalid Format", Toast.LENGTH_SHORT).show();
            return;
        }else{
            lastNumeric =true;
        }

        if(lastNumeric && !stateError){
            String txt = tvInput.getText().toString();

            if(txt.contains("log")){
                txt = txt.replace("log","log10");
            }
            if(txt.contains("ln")){
                txt = txt.replace("ln","log");
            }

            if(txt.contains("3√")){
                txt = txt.replace("3√","cbrt");
            }
            if(txt.contains("√")){
                txt = txt.replace("√","sqrt");
            }

            Log.d("TAG", "onEqual: leftBracket" + leftBracketCount +" right Bracket" + rightBracketCount );
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                int countLeft = (int) txt.chars().filter(ch -> ch == '(').count();
//                int countRight = (int) txt.chars().filter(ch -> ch == ')').count();
//
//                if(countLeft != countRight){
//                    txt
//                }
//            }


            try {
                Expression expression = null;
                try {
                    expression = new ExpressionBuilder(txt).build();
                    double result = expression.evaluate();
                    // Log.d("TAG", "onEqual: " + Double.toString(result).length());

                    tvResult.setText(Double.toString(result));

                    saveData();   //  Create History of Calculations
                    onEqualEventCount();

                }catch (Exception e){
//                    Log.d("TAG", "onEqual: " + e);
                    tvInput.setText(R.string.errorMsg);
                }

            }catch (ArithmeticException exception){
                tvInput.setText(R.string.errorMsg);
                stateError = true;
                lastNumeric = false;
            }
        }
    }

    private void majorEvent(int i){
        Log.d("TAG", "majorEvent: major event:" + i);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        mEditor.putInt("no_of_events", i);
        mEditor.apply();
    }

    private void onEqualEventCount() {
        onEqualEventsCount++;
        Log.d("TAG", "onEqualEventCount: minor event:" + onEqualEventsCount);

        if(onEqualEventsCount >5){
            onEqualEventsCount = 0;
            eventsCount++;
            Log.d("TAG", "onEqualEventCount: major event:" + eventsCount);
            mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            mEditor = mPreferences.edit();
            mEditor.putInt("no_of_events", eventsCount);
            mEditor.apply();
        }
    }

    private void saveData() {

        HistoryModel historyModel =
                new HistoryModel(tvInput.getText().toString(),tvResult.getText().toString());
        historyModelArrayList.add(historyModel);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(historyModelArrayList);
        editor.putString("history list", json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("history list", null);
        Type type = new TypeToken<ArrayList<HistoryModel>>() {}.getType();
        historyModelArrayList = gson.fromJson(json, type);

        if(historyModelArrayList == null){
            historyModelArrayList = new ArrayList<>();
        }

    }

    private void setNumericOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // append and set text of clicked button on input textview
                Button button = (Button) view;
                if(stateError){
                    tvInput.setText(button.getText());
                    stateError = false;
                }else{
                    tvInput.append(button.getText());
                }
                // flag
                lastNumeric = true;
//                scrollToRight();
            }
        };

        // attaching the listener to all the numeric buttons
        for(int id : numericButtons){
            findViewById(id).setOnClickListener(listener);
        }


        findViewById(R.id.buttonLeftBracket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stateError){
                    tvInput.setText("(");
                    stateError = false;
                }else{
                    tvInput.append("(");
                }
                // flag
                leftBracketCount ++;
                lastNumeric = false;
            }
        });

        findViewById(R.id.buttonRightBracket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stateError){
                    tvInput.setText(")");
                    stateError = false;
                }else{
                    tvInput.append(")");
                }
                // flag
                rightBracketCount ++;
                lastNumeric = false;
            }
        });

    }

    private void scrollToRight(){
        findViewById(R.id.horizontalScrollViewInput).post(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.horizontalScrollViewInput).scrollTo(tvInput.getRight(),tvInput.getTop());
            }
        });
    }

    private void initViews() {
        recyclerViewHistory = findViewById(R.id.RvHistory);
        tvResult = findViewById(R.id.tvResult);
        tvInput = findViewById(R.id.tvInput);
        adView = findViewById(R.id.adView);

        buttonCube = findViewById(R.id.buttonCube);
        buttonPower = findViewById(R.id.buttonPower);
        button2power = findViewById(R.id.button2power);
        buttonSin = findViewById(R.id.buttonSin);
        buttonTan = findViewById(R.id.buttonTan);
        buttonCos = findViewById(R.id.buttonCos);
        buttonLog = findViewById(R.id.buttonLog);
        buttonLn = findViewById(R.id.buttonLn);
        buttonCubicRoot = findViewById(R.id.buttonCubicRoot);
        buttonSquare = findViewById(R.id.buttonSquare);
        button1ny = findViewById(R.id.button1nY);
        buttonSqrt = findViewById(R.id.buttonSqrt);
        buttonEular = findViewById(R.id.buttonEular);
        buttonScientific = findViewById(R.id.buttonScientificCalculator);

        llScientific = findViewById(R.id.llRest);
        llScientific2 = findViewById(R.id.llScientificButtons);


    }

}