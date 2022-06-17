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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String PRODUCTNAME = "productname";
    private static final String PRODUCTPRICE = "productprice";
    private static final String PRODUCTSTOCKS = "productstocks";

    EditText name,price,stock;
    Button add,load;
    TextView output;

    //database
    private FirebaseFirestore Database = FirebaseFirestore.getInstance();
    private CollectionReference productRef = Database.collection("ProductDb");
    private DocumentReference db = Database.document("ProductDb/Products");

    private void initXml() {
        name = findViewById(R.id.txt_name);
        price = findViewById(R.id.txt_price);
        stock = findViewById(R.id.txt_stocks);
        add = findViewById(R.id.btn_addproduct);
        load = findViewById(R.id.btn_load);
        output = findViewById(R.id.output);
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



    }



    private void load() {
       productRef
               .whereLessThanOrEqualTo("stocks",5)
               .orderBy("stocks", Query.Direction.DESCENDING)
               .limit(3)
               .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               String data = "";

               for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                   Product product = documentSnapshot.toObject(Product.class);
                   product.setId(documentSnapshot.getId());



                   String documentId = product.getId();
                   String name = product.getName();
                   String price = product.getPrice();
                   int stocks = product.getStocks();

                   data += "\nDocumentId: " + documentId + "\nName: " + name + "\nPrice: " + price + " \nStocks: " + stocks + "\n";

               }

               output.setText(data);

           }
       });
    }

    private void add() {
        String ProductName = name.getText().toString();
        String ProductPrice = price.getText().toString();

        if(stock.length() == 0){
            stock.setText("0");
        }

        int ProductStocks = Integer.parseInt(stock.getText().toString());
        Product product = new Product(ProductName,ProductPrice,ProductStocks);
        productRef.add(product);

    }

    @Override
    protected void onStart() {
        super.onStart();

        productRef.addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    return;
                }

                String data = "";
                for(QueryDocumentSnapshot documentSnapshot : querySnapshot){
                    Product product = documentSnapshot.toObject(Product.class);
                    product.setId(documentSnapshot.getId());

                    String documentId = product.getId();
                    String name = product.getName();
                    String price = product.getPrice();
                    int stocks = product.getStocks();


                    data += "\n Document Id: " + documentId +  "\nName: " + name + "\nPrice: " + price + " \nStocks: " + stocks + "\n";
                }

                    output.setText(data);
            }
        });

    }
}