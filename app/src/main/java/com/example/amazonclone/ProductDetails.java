package com.example.amazonclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.amazonclone.model.AddProdModel;
import com.example.amazonclone.viewholder.RelatedProductsHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {
    Intent intent;
    ImageView productImg;
    TextView productName, productCategory, productPrice, productDesc;
    Button order;
    Toolbar detailsToolbar;

    FirebaseAuth auth;
    String uniqueId, relCategory, name, checkName;
    RecyclerView related_prod_list;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        auth = FirebaseAuth.getInstance();

        productImg = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productCategory = findViewById(R.id.product_category);
        productDesc = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);

        back = findViewById(R.id.product_back);
        order = findViewById(R.id.order);

        related_prod_list = findViewById(R.id.related_prod_list);
        related_prod_list.setLayoutManager(new LinearLayoutManager(ProductDetails.this,
                LinearLayoutManager.HORIZONTAL,true));

        intent = getIntent();
        productCategory.setText(intent.getStringExtra("category"));

        int id = intent.getIntExtra("id", 1);
        uniqueId = intent.getStringExtra("uniqueId");

        relCategory = productCategory.getText().toString();

        if(id==1){
            uniqueId = uniqueId.replaceAll("\n", " ");
            productName.setText(intent.getStringExtra("name").replaceAll("\n", " "));
            productPrice.setText("₹3000");
            productDesc.setText("Style: SportsSeason: All SeasonUpper material: FabricClosure: LacingFunctionality: Slip-resistant, LightweightSole material: Rubber SoleUpper: Low CutToe type: Round ToeHeel type: Flat Heel");
            productImg.setImageResource(R.drawable.shoes1);
        } else if (id==2) {
            uniqueId = uniqueId.replaceAll("\n", " ");
            productName.setText(intent.getStringExtra("name").replaceAll("\n", " "));
            productPrice.setText("₹9500");
            productDesc.setText("Rubber Sole100% recycled polyester lacesFoam midsoleColour shown: Cedar/Brown Basalt/Dark Pony/PollenStyle: DC9402-600Country/Region of Origin: Vietnam");
            productImg.setImageResource(R.drawable.shoes2);
        } else if (id==3) {
            productName.setText(intent.getStringExtra("name").replaceAll("\n", " "));
            productPrice.setText(intent.getStringExtra("pprice"));
            productDesc.setText(intent.getStringExtra("description"));
            String img = intent.getStringExtra("imageName");
            productImg.setImageResource(this.getResources().getIdentifier(img, "drawable", this.getPackageName()));
        }else {
            productName.setText(intent.getStringExtra("addProdName"));
            productPrice.setText(intent.getStringExtra("addProdPrice"));
            productCategory.setText(intent.getStringExtra("addProdCategory"));
            productDesc.setText(intent.getStringExtra("addProdDesc"));
            String imgUri = intent.getStringExtra("img");
            Picasso.get().load(imgUri).into(productImg);
        }

        back.bringToFront();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetails.this, HomeActivity.class));
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingToCartList();
            }
        });

        onStart();
    }

    private void addingToCartList(){

        String saveCurrentDate, saveCurrentTime;
        Calendar calendar =Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String , Object> cartMap = new HashMap<>();
        cartMap.put("pid", uniqueId);
        cartMap.put("name", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);

        cartListRef.child("User View").child(auth.getCurrentUser().getUid()).child("Products")
                .child(uniqueId).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProductDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                            Intent intentcart = new Intent(ProductDetails.this, HomeActivity.class);
                            intentcart.putExtra("cartadd", "yes");
                            startActivity(intentcart);
                        }
                    }
                });
    }

    protected void onStart(){
        super.onStart();

        final DatabaseReference prodListRef = FirebaseDatabase.getInstance().getReference().child("View All")
                .child("User View").child("Products");

        FirebaseRecyclerOptions<AddProdModel> options = new FirebaseRecyclerOptions.Builder<AddProdModel>()
                .setQuery(prodListRef.orderByChild("category").startAt(relCategory), AddProdModel.class).build();

        FirebaseRecyclerAdapter<AddProdModel, RelatedProductsHolder> adapter =
                new FirebaseRecyclerAdapter<AddProdModel, RelatedProductsHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RelatedProductsHolder holder, int position, @NonNull AddProdModel model) {
                        name = model.getName();
                        String price = model.getPrice();
                        String imgUri = model.getImg();

                        holder.relatedProdName.setText(name);
                        holder.relatedProdPrice.setText(price);
                        Picasso.get().load(imgUri).into(holder.relatedProdImg);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProductDetails.this, ProductDetails.class);
                                intent.putExtra("id", 4);
                                intent.putExtra("uniqueId", name);
                                intent.putExtra("addProdName", name);
                                intent.putExtra("addProdPrice", price);
                                intent.putExtra("addProdDesc", model.getDescription());
                                intent.putExtra("addProdCategory", model.getCategory());
                                intent.putExtra("img", imgUri);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public RelatedProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_products_adapter, parent, false);
                        RelatedProductsHolder holder = new RelatedProductsHolder(view);
                        return holder;
                    }
                };

        related_prod_list.setAdapter(adapter);
        adapter.startListening();
    }

}