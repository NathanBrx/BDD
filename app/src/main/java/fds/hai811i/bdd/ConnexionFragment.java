package fds.hai811i.bdd;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class ConnexionFragment extends Fragment {
    DatabaseHelper db;
    EditText etLogin;
    EditText etPassword;
    List<EditText> etList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connexion, container, false);

        db = new DatabaseHelper(getContext());

        etLogin = view.findViewById(R.id.et_login);
        etPassword = view.findViewById(R.id.et_password);

        etList = List.of(etLogin, etPassword);

        Button submit = view.findViewById(R.id.btn_submit);
        submit.setOnClickListener(v -> {
            String login = etLogin.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isValidUser = db.checkUserAndPass(login, password);

            if (isValidUser) {
                Toast.makeText(getContext(), "Bienvenue !", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), PlanningActivity.class);
                intent.putExtra("USER_LOGIN", login);
                startActivity(intent);

            } else {
                Toast.makeText(getContext(), "Login ou mot de passe incorrect", Toast.LENGTH_LONG).show();

                etPassword.setText("");
                etPassword.requestFocus();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        clearFields();
    }

    private void clearFields() {
        if (etList != null) {
            for (EditText e : etList) {
                e.setText("");
            }
        }
    }
}
