package com.example.taxiappdemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText userEmail = view.findViewById(R.id.inputEmail);
        EditText userPassword = view.findViewById(R.id.inputPassword);
        ImageButton btnLogin = view.findViewById(R.id.submitLogin);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(userEmail.getText().toString().trim())||TextUtils.isEmpty(userPassword.getText().toString().trim())||userEmail.getText().toString().trim().length()<6||userPassword.getText().toString().trim().length()<6){
                    Toast.makeText(getContext(),"Invalid Input!",Toast.LENGTH_LONG);
                }else {
                    firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString().trim(),userPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                getActivity().recreate();
                                Toast.makeText(getContext(),"Success!",Toast.LENGTH_LONG);
                                Fragment orderFragment = new OrderFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, orderFragment ); // give your fragment container id in first parameter
                                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                                transaction.commit();
                            }else {
                                Toast.makeText(getContext(),"Wrong Email or Password!",Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
            }
        });
    }
}
