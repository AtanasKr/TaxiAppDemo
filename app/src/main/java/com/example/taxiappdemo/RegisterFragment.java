package com.example.taxiappdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterFragment extends Fragment {
    View holder;
    Uri selectedImageURI;
    ImageView registerImage;
    String userId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        holder =view;
        return inflater.inflate(R.layout.fragment_register,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Spinner spinner = view.findViewById(R.id.spinner);
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Client");
        spinnerArray.add("Driver");
        int RESULT_LOAD_IMAGE = 123;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,spinnerArray);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        registerImage = view.findViewById(R.id.registerImage);
        EditText registerEmail = view.findViewById(R.id.registerEmail);
        EditText registerPassword = view.findViewById(R.id.registerPassword);
        EditText registerName = view.findViewById(R.id.registerName);
        ImageButton registerBtn = view.findViewById(R.id.submitRegister);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        registerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = registerEmail.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();
                String name = registerName.getText().toString().trim();

                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)||password.length()<6||email.length()<6){
                    Toast.makeText(getContext(),"Invalid input",Toast.LENGTH_LONG);
                }else {
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                userId= firebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);
                                Map<String,Object> user = new HashMap<>();
                                user.put("fName",name);
                                user.put("role",spinner.getSelectedItem().toString());
                                user.put("profileImage",selectedImageURI.toString());
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("data","Data Saved!");
                                    }
                                });
                                Toast.makeText(getContext(),"User Created!",Toast.LENGTH_LONG);
                                Fragment orderFragment = new OrderFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, orderFragment ); // give your fragment container id in first parameter
                                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                                transaction.commit();

                            }else {
                                Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 123) {
                selectedImageURI = data.getData();
                Picasso.get().load(selectedImageURI).rotate(90).into(registerImage);
            }

        }
    }
}
