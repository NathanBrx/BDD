package fds.hai811i.bdd;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class RegistrationFragment extends Fragment {

    DatabaseHelper db;
    EditText etLogin;
    EditText etPassword;
    EditText etNom;
    EditText etPrenom;
    EditText etDob;
    EditText etPhone;
    EditText etEmail;
    CheckBox cbSport;
    CheckBox cbMusique;
    CheckBox cbLecture;
    List<EditText> etList;
    List<CheckBox> cbList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        db = new DatabaseHelper(getContext());

        etLogin = view.findViewById(R.id.et_login);
        etPassword = view.findViewById(R.id.et_password);
        etNom = view.findViewById(R.id.et_nom);
        etPrenom = view.findViewById(R.id.et_prenom);
        etDob = view.findViewById(R.id.et_dob);
        etPhone = view.findViewById(R.id.et_phone);
        etEmail = view.findViewById(R.id.et_email);

        cbSport = view.findViewById(R.id.cb_sport);
        cbMusique = view.findViewById(R.id.cb_musique);
        cbLecture = view.findViewById(R.id.cb_lecture);

        etList = Arrays.asList(etLogin, etPassword, etNom, etPrenom, etDob, etPhone, etEmail);
        cbList = Arrays.asList(cbSport, cbMusique, cbLecture);

        Button btnClear = view.findViewById(R.id.btn_clear);
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        Button btnDbDisplay = view.findViewById(R.id.btn_db_display);

        btnClear.setOnClickListener(v -> clearFields());

        btnDbDisplay.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();

            Cursor cursor = db.getAll();
            if (cursor.getCount() == 0) {
                sb.append("Aucune entrée dans la base de données");
            } else {
                while (cursor.moveToNext()) {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        String nomColonne = cursor.getColumnName(i);
                        String valeur = cursor.getString(i);
                        sb.append(nomColonne).append(" : ").append(valeur).append("\n");
                    }
                    sb.append("-----------------------------\n");
                }
            }
            cursor.close();

            DBDisplayFragment displayFragment = new DBDisplayFragment();
            Bundle args = new Bundle();
            args.putString("DATA", sb.toString());
            displayFragment.setArguments(args);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, displayFragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnSubmit.setOnClickListener(v -> {
            String login = etLogin.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String nom = etNom.getText().toString().trim();
            String prenom = etPrenom.getText().toString().trim();
            String dob = etDob.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            // required fields not empty
            if (login.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(getContext(), "Veuillez remplir les champs obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            // login max 10 char
            if (!login.matches("^[a-zA-Z].{0,9}$")) {
                etLogin.setError("Doit commencer par une lettre et max 10 caractères");
                etLogin.requestFocus();
                return;
            }

            // login does not exist
            if (db.checkUser(login)) {
                etLogin.setError("Ce login est déjà utilisé, choisissez-en un autre");
                etLogin.requestFocus();
                return;
            }

            // passwors > 6 char
            if (password.length() < 6) {
                etPassword.setError("Le mot de passe doit contenir au moins 6 caractères");
                etPassword.requestFocus();
                return;
            }

            // email format ok
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Veuillez entrer une adresse email valide");
                etEmail.requestFocus();
                return;
            }

            // phone format ok
            if (!Patterns.PHONE.matcher(phone).matches()) {
                etPhone.setError("Le numéro de téléphone n'est pas valide");
                etPhone.requestFocus();
                return;
            }

            StringBuilder interests = new StringBuilder();
            if (cbSport.isChecked()) interests.append("Sport ");
            if (cbMusique.isChecked()) interests.append("Musique ");
            if (cbLecture.isChecked()) interests.append("Lecture ");

            boolean isInserted = db.insertUser(login, password, nom, prenom, dob, phone, email, interests.toString());

            if (isInserted) {
                Toast.makeText(getContext(), "Inscription réussie !", Toast.LENGTH_SHORT).show();

                String summary = "Login : " + login + "\n" +
                        "Nom : " + nom + "\n" +
                        "Prénom : " + prenom + "\n" +
                        "Date de naissance : " + dob + "\n" +
                        "Téléphone : " + phone + "\n" +
                        "Email : " + email + "\n" +
                        "Centres d'intérêt : " + (interests.length() > 0 ? interests.toString() : "Aucun");

                DisplayFragment displayFragment = new DisplayFragment();
                Bundle args = new Bundle();
                args.putString("SUMMARY_DATA", summary);
                displayFragment.setArguments(args);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, displayFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "Erreur critique lors de la sauvegarde", Toast.LENGTH_SHORT).show();
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
        if (cbList != null) {
            for (CheckBox cb : cbList) {
                cb.setChecked(false);
            }
        }
    }
}