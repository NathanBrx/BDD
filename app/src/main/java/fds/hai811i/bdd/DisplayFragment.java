package fds.hai811i.bdd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DisplayFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display, container, false);

        TextView tvSummary = view.findViewById(R.id.tv_summary);

        // récuperation des données
        if (getArguments() != null) {
            String summary = getArguments().getString("SUMMARY_DATA");
            tvSummary.setText(summary);
        }

        return view;
    }
}