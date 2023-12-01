package com.example.appmaytinhlogbook3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MainActivity extends AppCompatActivity {

    private TextView workingsTV;
    private TextView resultsTV;
    private String workings = "";
    private ScriptEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTextViews();
        engine = new ScriptEngineManager().getEngineByName("rhino");
    }

    private void initTextViews() {
        workingsTV = findViewById(R.id.workingsTextView);
        resultsTV = findViewById(R.id.resultTextView);
    }

    private void setWorkings(String givenValue) {
        workings += givenValue;
        workingsTV.setText(workings);
    }

    public void equalsOnClick(View view) {
        try {
            Object result = engine.eval(workings);
            resultsTV.setText(String.valueOf(result));
        } catch (ScriptException e) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearOnClick(View view) {
        workingsTV.setText("");
        workings = "";
        resultsTV.setText("");
    }

    public void bracketsOnClick(View view) {
        String bracket = (workings.endsWith("(")) ? ")" : "(";
        setWorkings(bracket);
    }

    public void powerOfOnClick(View view) {
        setWorkings("^");
    }

    public void operatorOnClick(View view) {
        String operator = ((TextView) view).getText().toString();
        setWorkings(operator);
    }

    public void numberOnClick(View view) {
        String number = ((TextView) view).getText().toString();
        setWorkings(number);
    }

    public void decimalOnClick(View view) {
        setWorkings(".");
    }
}
