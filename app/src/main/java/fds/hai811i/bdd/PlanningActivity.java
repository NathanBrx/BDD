package fds.hai811i.bdd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PlanningActivity extends AppCompatActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);

        db = new DatabaseHelper(this);

        String userLogin = getIntent().getStringExtra("USER_LOGIN");
        String selectedDate = getIntent().getStringExtra("SELECTED_DATE");

        EditText etC1 = findViewById(R.id.et_creneau1);
        EditText etC2 = findViewById(R.id.et_creneau2);
        EditText etC3 = findViewById(R.id.et_creneau3);
        EditText etC4 = findViewById(R.id.et_creneau4);
        Button btnValidate = findViewById(R.id.btn_validate_planning);

        btnValidate.setOnClickListener(v -> {
            String c1 = etC1.getText().toString().trim();
            String c2 = etC2.getText().toString().trim();
            String c3 = etC3.getText().toString().trim();
            String c4 = etC4.getText().toString().trim();

            boolean isInserted = db.insertPlanning(userLogin, selectedDate, c1, c2, c3, c4);

            if (isInserted) {
                Toast.makeText(PlanningActivity.this, "Planning sauvegardé !", Toast.LENGTH_SHORT).show();

                String summary = "Planning pour le : " + selectedDate + "\n\n" +
                        "08h-10h : " + (c1.isEmpty() ? "Libre" : c1) + "\n" +
                        "10h-12h : " + (c2.isEmpty() ? "Libre" : c2) + "\n" +
                        "14h-16h : " + (c3.isEmpty() ? "Libre" : c3) + "\n" +
                        "16h-18h : " + (c4.isEmpty() ? "Libre" : c4);

                Intent intent = new Intent(PlanningActivity.this, PlanningSummaryActivity.class);
                intent.putExtra("PLANNING_SUMMARY", summary);
                startActivity(intent);

                finish();
            } else {
                Toast.makeText(PlanningActivity.this, "Erreur de sauvegarde", Toast.LENGTH_SHORT).show();
            }
        });
    }
}