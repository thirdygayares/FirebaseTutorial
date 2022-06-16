package com.example.firebasetutorial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String PRODUCTNAME = "productname";
    private static final String PRODUCTPRICE = "productprice";
    private static final String PRODUCTSTOCKS = "productstocks";

    EditText name,price,stocks;
    Button add,load, update, btn_delete_stocks, btn_delete_all;
    TextView output;

    //database
    private FirebaseFirestore Database = FirebaseFirestore.getInstance();
    private DocumentReference db = Database.document("ProductDb/Products");

    private void initXml() {
        name = findViewById(R.id.txt_name);
        price = findViewById(R.id.txt_price);
        stocks = findViewById(R.id.txt_stocks);
        add = findViewById(R.id.btn_addproduct);
        load = findViewById(R.id.btn_load);
        output = findViewById(R.id.output);
        update = findViewById(R.id.btn_update);
        btn_delete_stocks = findViewById(R.id.btn_delete_stocks);
        btn_delete_all = findViewById(R.id.btn_delete_all);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initXml();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        btn_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
            }
        });

        btn_delete_stocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStocks();
            }
        });

    }

    private void deleteStocks() {
        db.update("stocks", FieldValue.delete());
    }

    private void deleteAll() {
        db.delete();
    }

    private void update() {
        String ProductStocks = stocks.getText().toString();

        db.update("stocks", ProductStocks);
    }

    private void load() {
        db.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                                Product product = documentSnapshot.toObject(Product.class);

                                String productName = product.getName();
                                String productPrice = product.getPrice();
                                String productStocks = product.getStocks();

                                output.setText("Product Name: " + productName + "\n Price: " + productPrice + "\n Stocks: " + productStocks );

                        }else{
                            Toast.makeText(MainActivity.this, "Products Does't exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
    }

    private void add() {
        String ProductName = name.getText().toString();
        String ProductPrice = price.getText().toString();
        String ProductStocks = stocks.getText().toString();

        Product product = new Product(ProductName,ProductPrice,ProductStocks);

        db.set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this, "Products saved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        db.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(e != null){
                    Toast.makeText(MainActivity.this, "Error While Loading data", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    return;
                }

            if(documentSnapshot.exists()){
                Product product = documentSnapshot.toObject(Product.class);

                String productName = product.getName();
                String productPrice = product.getPrice();
                String productStocks = product.getStocks();


                output.setText("Product Name: " + productName + "\n Price: " + productPrice + "\n Stocks: " + productStocks );
            }else{
                output.setText("");
            }

            }
        });
    }
}