package fds.hai811i.bdd;

import android.database.Cursor;
import android.os.Bundle;
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

        etList = List.of(etLogin, etPassword, etNom, etPrenom, etDob, etPhone, etEmail);
        cbList = List.of(cbSport, cbMusique, cbLecture);

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
            // récupération des données
            String login = etLogin.getText().toString();
            String password = etPassword.getText().toString();
            String nom = etNom.getText().toString();
            String prenom = etPrenom.getText().toString();
            String dob = etDob.getText().toString();
            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();

            StringBuilder interests = new StringBuilder();
            if (cbSport.isChecked()) interests.append("Sport ");
            if (cbMusique.isChecked()) interests.append("Musique ");
            if (cbLecture.isChecked()) interests.append("Lecture ");

            // save dans la db
            boolean isInserted = db.insertData(login, password, nom, prenom, dob, phone, email, interests.toString());

            if (isInserted) {
                Toast.makeText(getContext(), "Données sauvegardées en BDD !", Toast.LENGTH_SHORT).show();

                // données à envoyer au fragment 2
                String summary = "Login : " + login + "\n" +
                        "Nom : " + nom + "\n" +
                        "Prénom : " + prenom + "\n" +
                        "Date de naissance : " + dob + "\n" +
                        "Téléphone : " + phone + "\n" +
                        "Email : " + email + "\n" +
                        "Centres d'intérêt : " + (interests.length() > 0 ? interests.toString() : "Aucun");

                // lancement fragment 2 avec la synthèse
                DisplayFragment displayFragment = new DisplayFragment();
                Bundle args = new Bundle();
                args.putString("SUMMARY_DATA", summary);
                displayFragment.setArguments(args);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, displayFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "Erreur lors de la sauvegarde", Toast.LENGTH_SHORT).show();
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
        for (EditText e : etList) {
            e.setText("");
        }
        for (CheckBox cb : cbList) {
            if (cb.isChecked()) {
                cb.toggle();
            }
        }
    }
}