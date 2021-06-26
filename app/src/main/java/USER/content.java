package USER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class content extends AppCompatActivity {
    TextView name,beds,oxygen,time,date,success;
    Button book,book1;
    String pa;
    String hospital;
    Integer i;
    String city;
    String patient;
    DocumentReference ref;
    CardView map;

    String names,age,phone,address,email;
    String hospita;
    String hospaddress;
    DatabaseReference harsh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        name=findViewById(R.id.name);
        beds=findViewById(R.id.bed);
        oxygen=findViewById(R.id.oxygen);
        time=findViewById(R.id.todaytime);
        date=findViewById(R.id.todaydate);
        success=findViewById(R.id.stark);
        book=findViewById(R.id.book);
        book1 =  findViewById(R.id.oxybook);
        city=getIntent().getStringExtra("city");
        hospita=getIntent().getStringExtra("hosp");
        Date currentTime = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm.ss aa");
        String output = dateFormat.format(currentTime);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        success.setText(hospita);
        time.setText(output);
        date.setText(formattedDate);
        map=findViewById(R.id.roll);
        ref= FirebaseFirestore.getInstance().collection(city).document(hospita);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    name.setText(documentSnapshot.getString("Address of My Hospital") );
                    hospaddress=documentSnapshot.getString("Address of My Hospital");
                    //+" NORMAL BEDS/n "+documentSnapshot.getString("Total no of Normal Beds")+"OXYGEN CYLINDER \n   "+documentSnapshot.getString("Total no of Oxygen Beds")
                    String normalbed=documentSnapshot.getString("Total no of Normal Beds");
                    int score=Integer.parseInt(normalbed);
                    if (score==0){
                        book.setEnabled(false);
                    }

                    String oxygenbed=documentSnapshot.getString("Total no of Oxygen Beds");
                    int oxyscore=Integer.parseInt(oxygenbed);
                    if (oxyscore==0){
                        book1.setEnabled(false);
                    }
                    ValueAnimator valueAnimator=ValueAnimator.ofInt(0,score);
                    ValueAnimator valueAnimator1=ValueAnimator.ofInt(0,oxyscore);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            beds.setText(valueAnimator.getAnimatedValue().toString());
                        }
                    });
                    valueAnimator.start();
                    valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            oxygen.setText(valueAnimator1.getAnimatedValue().toString());
                        }
                    });
                    valueAnimator1.start();
                    hospital=documentSnapshot.getString("Gmail of Hospital");
                    DocumentReference ref1=FirebaseFirestore.getInstance().collection("user").document(patient);

                    ref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            names=documentSnapshot.getString("name");
                            age=documentSnapshot.getString("age");
                            email=documentSnapshot.getString("email");
                            phone=documentSnapshot.getString("phone");
                            address=documentSnapshot.getString("address");

                        }
                    });
                }
            }
        });
        patient=getIntent().getStringExtra("gmail");
         pa=patient;
        String [] java =patient.split("(?=@)");
        String java1=java[0];
        java.util.Map<String, Object> data = new HashMap<>();

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                harsh=FirebaseDatabase.getInstance().getReference().child("BOOKING HISTORY").child(java1);
                String id = harsh.push().getKey();
                data.put("hospitalname",hospita);
                data.put("address",hospaddress);
                data.put("time",output);
                data.put("date",formattedDate);
                data.put("type","NORMAL BED");
                harsh.child(id).setValue(data);
                senEmail();
                Toast.makeText(content.this, "Confirmation E-Mail has been sent", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),ConfirmationActivity.class);
                intent.putExtra("city",city);
                intent.putExtra("hospital",hospita);
                intent.putExtra("email",hospital);
                intent.putExtra("time",output);
                intent.putExtra("date",formattedDate);
                intent.putExtra("address",hospaddress);
                intent.putExtra("username",names);
                intent.putExtra("userage",age);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();


            }
        });
        book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                harsh=FirebaseDatabase.getInstance().getReference().child("BOOKING HISTORY").child(java1);
                String id = harsh.push().getKey();
                data.put("hospitalname",hospita);
                data.put("address",hospaddress);
                data.put("time",output);
                data.put("date",formattedDate);
                data.put("type","OXYGEN BED");
                harsh.child(id).setValue(data);
                senEmail();
                Toast.makeText(content.this, "Confirmation E-Mail has been sent", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),confirmation2.class);
                intent.putExtra("city",city);
                intent.putExtra("hospital",hospita);
                intent.putExtra("email",hospital);
                intent.putExtra("time",output);
                intent.putExtra("date",formattedDate);
                intent.putExtra("address",hospaddress);
                intent.putExtra("age",age);
                intent.putExtra("name",names);

                startActivity(intent);
                finish();


            }
        });


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 =new Intent(getApplicationContext(), Map.class);
                intent3.putExtra("address1",hospaddress);
                startActivity(intent3);
            }
        });

    }




    private void senEmail() {

        String mEmail =hospital;
        String  mSubject = "Booking Notification";
        String mMessage = "A seat has been booked in your hospital \n"+"Name:"+names+"\nAge:"+age+"\nPhone Number:"+phone+"\nEmail:"+email+"\nAddress:"+address;


        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);

        javaMailAPI.execute();

        String mEmail1 = pa;
        mSubject = "Booking request";
        mMessage = "A seat has been booked in "+hospita.toUpperCase()+" HOSPITAL\n"+"Address:"+hospaddress+"\n Show this email in the hospital in order to claim your seats";

        JavaMailAPI javaMailAPI1 = new JavaMailAPI(this, mEmail1, mSubject, mMessage);

        javaMailAPI1.execute();
    }
}