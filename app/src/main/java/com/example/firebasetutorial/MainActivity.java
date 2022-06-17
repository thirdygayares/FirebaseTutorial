package com.example.firebasetutorial;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";



    EditText name, price, stock;
    Button add, load;
    TextView output;

    //database
    private FirebaseFirestore Database = FirebaseFirestore.getInstance();
    private CollectionReference productRef = Database.collection("ProductDb");
    private DocumentReference db = Database.document("ProductDb/Products");
    private DocumentSnapshot lastResult;


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
        Query query;
        if (lastResult == null){
            query = productRef.orderBy("stocks").limit(3);

        }else{
            query = productRef.orderBy("stocks")
                    .startAfter(lastResult)
                    .limit(3);
        }

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                        {
                            Product product = documentSnapshot.toObject(Product.class);
                            product.setId(documentSnapshot.getId());

                            String id = product.getId();
                            String name = product.getName();
                            String price = product.getPrice();
                            int stocks = product.getStocks();

                            data += "\n\nId: " + id + "\nName: " + name + "\nPrice: " + price + "\nstocks: " + stocks ;

                        }
                        if(queryDocumentSnapshots.size() > 0) {
                            data += "\n_____________\n\n";
                            output.append(data);
                            lastResult = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() -1 );

                        }

                    }
                });

    }

    private void add() {
        String ProductName = name.getText().toString();
        String ProductPrice = price.getText().toString();

        if (stock.length() == 0) {
            stock.setText("0");
        }

        int ProductStocks = Integer.parseInt(stock.getText().toString());
        Product product = new Product(ProductName, ProductPrice, ProductStocks);
        productRef.add(product);

    }

    @Override
    protected void onStart() {
        super.onStart();
        productRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
               if(error != null){
                   return;
               }

               for (DocumentChange dc: queryDocumentSnapshots.getDocumentChanges()){
                   DocumentSnapshot documentSnapshot = dc.getDocument();
                   String id = documentSnapshot.getId();

                   int oldIndex = dc.getOldIndex();
                   int newIndex = dc.getNewIndex();

                   switch (dc.getType()){
                       case ADDED:
                           output.append("\n Added: " + id +
                                   "\nold Index: " + oldIndex + "\nNew Index: " + newIndex + "\n");
                           break;

                       case MODIFIED:
                           output.append("\n MODIFIED: " + id +
                                   "\nold Index: " + oldIndex + "\nNew Index: " + newIndex + "\n");
                           break;


                       case REMOVED:
                           output.append("\n REMOVED: " + id +
                                   "\nold Index: " + oldIndex + "\nNew Index: " + newIndex + "\n");
                           break;


                   }

               }

            }
        });
    }
}