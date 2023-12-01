public class CalculatorActivity extends AppCompatActivity {

    private TextView workingsTextView;
    private TextView resultsTextView;
    private String workings = "";
    private ScriptEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        initializeTextViews();
        engine = new ScriptEngineManager().getEngineByName("rhino");
    }

    private void initializeTextViews() {
        workingsTextView = findViewById(R.id.workingsTextView);
        resultsTextView = findViewById(R.id.resultsTextView);
    }

    private void updateWorkings(String value) {
        workings += value;
        workingsTextView.setText(workings);
    }

    public void equalsButtonOnClick(View view) {
        try {
            Object result = engine.eval(workings);
            resultsTextView.setText(String.valueOf(result));
        } catch (ScriptException e) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearButtonOnClick(View view) {
        workingsTextView.setText("");
        workings = "";
        resultsTextView.setText("");
    }

    public void bracketsButtonOnClick(View view) {
        String bracket = (workings.endsWith("(")) ? ")" : "(";
        updateWorkings(bracket);
    }

    public void powerButtonOnClick(View view) {
        updateWorkings("^");
    }

    public void operatorButtonOnClick(View view) {
        String operator = ((TextView) view).getText().toString();
        updateWorkings(operator);
    }

    public void numberButtonOnClick(View view) {
        String number = ((TextView) view).getText().toString();
        updateWorkings(number);
    }

    public void decimalButtonOnClick(View view) {
        updateWorkings(".");
    }
}
