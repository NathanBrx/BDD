package fds.hai811i.bdd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DBDisplayFragment extends Fragment {

    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dbdisplay, container, false);

        TextView content = view.findViewById(R.id.db_content);
        Button btnClearDb = view.findViewById(R.id.btn_clear_db);

        db = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            String summary = getArguments().getString("DATA");
            content.setText(summary);
        }

        btnClearDb.setOnClickListener(v -> {
            db.deleteAll();

            content.setText("Aucune entrée dans la base de données");

            Toast.makeText(getContext(), "Base de données vidée avec succès !", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
