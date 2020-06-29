package com.cegep.saporiitaliano.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cegep.saporiitaliano.EditPersonalDetails;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.TermsAndCondition;
import com.cegep.saporiitaliano.auth.SignInActivity;

public class SettingsFragment extends Fragment {

    private TextView nameTextView,terms;

    private Button signOutButton,editProfile;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        nameTextView = view.findViewById(R.id.person_name);
        signOutButton = view.findViewById(R.id.sign_out_button);
        terms=view.findViewById(R.id.terms);

        editProfile=view.findViewById(R.id.editProfile);
<<<<<<< HEAD
        if(!SaporiItalianoApplication.user.isAdmin) {

            editProfile.setVisibility(View.VISIBLE);
        }
=======
>>>>>>> 6d5e84cd0edd03cd7cc37bed3414e7bc9229ae9d
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTextView.setText(SaporiItalianoApplication.user.name);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SaporiItalianoApplication.user = null;
                SaporiItalianoApplication.products.clear();
                startActivity(intent);
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), TermsAndCondition.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //SaporiItalianoApplication.user = null;
                //SaporiItalianoApplication.products.clear();
                intent.putExtra("ScreenName", "EDITPROFILE");
                startActivity(intent);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), EditPersonalDetails.class);

                startActivity(intent);
            }
        });
    }
}
