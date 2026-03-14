package fds.hai811i.bdd;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PlanningSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_summary);

        TextView tvResult = findViewById(R.id.tv_planning_result);
        Button back = findViewById(R.id.btn_back);

        String summary = getIntent().getStringExtra("PLANNING_SUMMARY");

        if (summary != null) {
            tvResult.setText(summary);
        }

        back.setOnClickListener(v -> finish());
    }
}