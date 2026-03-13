package fds.hai811i.bdd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newUser = findViewById(R.id.btn_new_user);
        newUser.setOnClickListener(view -> goTo(0));

        Button connexion = findViewById(R.id.btn_connexion);
        connexion.setOnClickListener(view -> goTo(1));
    }

    private void goTo(int mode) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("MODE", mode); // mode : 0 = new user | 1 = connexion
        startActivity(intent);
    }
}
