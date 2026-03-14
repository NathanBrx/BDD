package fds.hai811i.bdd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private String userLogin;
    private String selectedDateStr;

    private TextView tvSelectedDate, tvEmptyPlanning, tvPlanningContent;
    private ScrollView scrollPlanning;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        db = new DatabaseHelper(this);
        userLogin = getIntent().getStringExtra("USER_LOGIN");

        CalendarView calendarView = findViewById(R.id.calendarView);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        tvEmptyPlanning = findViewById(R.id.tv_empty_planning);
        tvPlanningContent = findViewById(R.id.tv_planning_content);
        scrollPlanning = findViewById(R.id.scroll_planning);
        fabAdd = findViewById(R.id.fab_add_planning);
        Button btnDeletePlanning = findViewById(R.id.btn_delete_planning);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        selectedDateStr = sdf.format(new Date(calendarView.getDate()));

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDateStr = String.format(Locale.FRANCE, "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            refreshPlanningView();
        });

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarActivity.this, PlanningActivity.class);
            intent.putExtra("USER_LOGIN", userLogin);
            intent.putExtra("SELECTED_DATE", selectedDateStr);
            startActivity(intent);
        });

        btnDeletePlanning.setOnClickListener(v -> {
            new AlertDialog.Builder(CalendarActivity.this)
                    .setTitle("Supprimer le planning")
                    .setMessage("Voulez-vous vraiment supprimer le planning du " + selectedDateStr + " ?")
                    .setPositiveButton("Oui, supprimer", (dialog, which) -> {

                        boolean isDeleted = db.deletePlanningForDate(userLogin, selectedDateStr);

                        if (isDeleted) {
                            Toast.makeText(CalendarActivity.this, "Planning supprimé", Toast.LENGTH_SHORT).show();
                            refreshPlanningView();
                        } else {
                            Toast.makeText(CalendarActivity.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPlanningView();
    }

    private void refreshPlanningView() {
        tvSelectedDate.setText(String.format("Planning du %s", selectedDateStr));

        String[] creneaux = db.getPlanningForDate(userLogin, selectedDateStr);

        if (creneaux != null) {
            // si planning existe, afficher la scrollview avec celui-ci
            scrollPlanning.setVisibility(View.VISIBLE);
            tvEmptyPlanning.setVisibility(View.GONE);
            fabAdd.setVisibility(View.GONE);

            String content = "• 08h-10h : " + (creneaux[0].isEmpty() ? "Libre" : creneaux[0]) + "\n\n" +
                    "• 10h-12h : " + (creneaux[1].isEmpty() ? "Libre" : creneaux[1]) + "\n\n" +
                    "• 14h-16h : " + (creneaux[2].isEmpty() ? "Libre" : creneaux[2]) + "\n\n" +
                    "• 16h-18h : " + (creneaux[3].isEmpty() ? "Libre" : creneaux[3]);
            tvPlanningContent.setText(content);
        } else {
            // si pas de planning, ne pas afficher la scrollview mais le message "pas de planning"
            scrollPlanning.setVisibility(View.GONE);
            tvEmptyPlanning.setVisibility(View.VISIBLE);
            fabAdd.setVisibility(View.VISIBLE);
        }
    }
}