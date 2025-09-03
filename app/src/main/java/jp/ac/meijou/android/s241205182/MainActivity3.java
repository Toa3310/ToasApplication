package jp.ac.meijou.android.s241205182;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import jp.ac.meijou.android.s241205182.databinding.ActivityMain3Binding;

public class MainActivity3 extends AppCompatActivity {

    private ActivityMain3Binding binding;
    private int display;
    private int operand1;
    private int operand2;
    private Operator operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.button0.setOnClickListener(view -> pushOperand(0));
        binding.button1.setOnClickListener(view -> pushOperand(1));
        binding.button2.setOnClickListener(view -> pushOperand(2));
        binding.button3.setOnClickListener(view -> pushOperand(3));
    }
    private void pushOperand(int num) {
        if (operator == null) {
            operand1 = operand1 * 10 + num;
            display = operand1;
        } else {
            operand2 = operand2 * 10 + num;
            display = operand2;
        }
        binding.textResult.setText(String.valueOf(display));
    }
    private enum Operator {
        PLUS(Integer::sum),
        MINUS((a, b) -> a - b),
        MULTIPLY((a, b) -> a * b),
        DIVIDE((a, b) -> a / b);

        public final BiFunction<Integer, Integer, Integer> calc;

        Operator(BiFunction<Integer, Integer, Integer> calc) {
            this.calc = calc;
        }
    }
}