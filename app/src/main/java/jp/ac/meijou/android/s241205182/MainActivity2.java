package jp.ac.meijou.android.s241205182;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Optional;

import jp.ac.meijou.android.s241205182.databinding.ActivityMain2Binding;
import jp.ac.meijou.android.s241205182.databinding.ActivityMainBinding;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //明示的
        binding.button2.setOnClickListener(view -> {
            var intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        //暗黙的
         binding.button3.setOnClickListener(view -> {
            var intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://ktsgsg.github.io/ie2025_19/"));
            startActivity(intent);
         });
         //送信ボタン
        binding.submitButton.setOnClickListener(view -> {
            var text = binding.submitText.getText().toString();
            var intent = new Intent(this, MainActivity4.class);
            intent.putExtra("text", text);
            startActivity(intent);
        });
        binding.button5.setOnClickListener(view -> {
            var intent = new Intent(this, MainActivity4.class);
            getActivityResult.launch(intent);
        });
    }
    private final ActivityResultLauncher<Intent> getActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                switch (result.getResultCode()) {
                    case RESULT_OK -> {
                        Optional.ofNullable(result.getData())
                                .map(data -> data.getStringExtra("ret"))
                                .map(text -> "Result: " + text)
                                .ifPresent(text -> binding.returnResult.setText(text));
                    }
                    case RESULT_CANCELED -> {
                        binding.returnResult.setText("Result: Canceled");
                    }
                    default -> {
                        binding.returnResult.setText("Result: Unknown(" + result.getResultCode() + ")");
                    }
                }
            }
    );

}