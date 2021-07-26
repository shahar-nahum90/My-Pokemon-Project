package com.example.myapppokimon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private Button button;

    private static final int SIGN_CODE=1;
    public static final String ClassName=MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.btn_battel);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_CODE);


        } else {
            toastWithDetails(true);
        }
    }
    private void toastWithDetails(boolean success){
        if (success) {
            String userDetails = "Hi, your display name is: " + FirebaseAuth.getInstance().getCurrentUser()
                    + " " + FirebaseAuth.getInstance().getCurrentUser().getUid();
            Toast.makeText(this, userDetails, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Sign in failed", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.sign_out_menu){
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this,"SIGN OUT",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    });
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==SIGN_CODE){
            if(requestCode==RESULT_OK){
                toastWithDetails(true);
            }
            else{
                toastWithDetails(false);
                finish();
            }
        }
    }

    public void Battel(View view) throws IOException, ClassNotFoundException {
        Socket socket =new Socket("127.0.0.1",8010);
        System.out.println("client: Created Socket");

        ObjectOutputStream toServer=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream fromServer=new ObjectInputStream(socket.getInputStream());

        EditText poke1   = (EditText)findViewById(R.id.pok1);
        String pokemon1      =  poke1.getText().toString();

        EditText poke2   = (EditText)findViewById(R.id.pok2);
        String pokemon2      =  poke2.getText().toString();

        toServer.writeObject(pokemon1);
        toServer.writeObject(pokemon2);
       String Won = (String) fromServer.readObject();


        Toast.makeText(MainActivity.this,Won,Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this,Activity2.class);
        Intent intent = new Intent(getApplicationContext(),Activity2.class);
        intent.putExtra("message_key", Won);
       startActivity(intent);
    }
}