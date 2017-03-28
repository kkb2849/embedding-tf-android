package com.nbt.embeddingtf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class MainActivity extends AppCompatActivity {

    private EditText inputBox;
    private TextView outputBox;

    private TensorFlowInferenceInterface inferenceInterface;
    private static final String MODEL_FILE = "file:///android_asset/frozen_graph.pb";
    private static final String INPUT_NODE = "input_x";
    private static final String OUTPUT_NODE = "hypothesis";
    private static final int[] INPUT_SIZE = {1, 1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputBox = (EditText)findViewById(R.id.inputValue);
        outputBox = (TextView)findViewById(R.id.hView);
        Button calculateButton = (Button)findViewById(R.id.calBtn);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runModel();
            }
        });
        initMyModel();
    }

    private void initMyModel() {
        inferenceInterface = new TensorFlowInferenceInterface();
        inferenceInterface.initializeTensorFlow(getAssets(), MODEL_FILE);
    }

    private void runModel(){
        float[] inputFloats = new float[1];
        inputFloats[0] = Float.parseFloat(inputBox.getText().toString());
        inferenceInterface.fillNodeFloat(INPUT_NODE, INPUT_SIZE, inputFloats);
        inferenceInterface.runInference(new String[] {OUTPUT_NODE});

        float[] res = {1};
        inferenceInterface.readNodeFloat(OUTPUT_NODE, res);
        outputBox.setText("Output : "+res[0]);
    }
}
