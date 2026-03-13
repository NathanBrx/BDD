package fds.hai811i.bdd;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        int mode = intent.getIntExtra("MODE", 0);

        if (savedInstanceState == null) {

            Fragment f;
            if (mode == 1) {
                f = new ConnexionFragment();
            } else {
                f = new RegistrationFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, f)
                    .commit();
        }
    }
}