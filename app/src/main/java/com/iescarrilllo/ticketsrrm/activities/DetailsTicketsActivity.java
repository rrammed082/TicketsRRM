 package com.iescarrilllo.ticketsrrm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iescarrilllo.ticketsrrm.R;
import com.iescarrilllo.ticketsrrm.apiClients.GoldenRaceApiClient;
import com.iescarrilllo.ticketsrrm.apiServices.GoldenRaceApiService;
import com.iescarrilllo.ticketsrrm.models.DetailTicket;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

 public class DetailsTicketsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_tickets);

        DetailTicket detailTicket = (DetailTicket) getIntent().getSerializableExtra("detailTicketSelected");

        TextView tvId = findViewById(R.id.tvId);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvAmount = findViewById(R.id.tvAmount);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        ImageView ivBack = findViewById(R.id.ivBack);

        tvId.setText("ID: " + String.valueOf(detailTicket.getId()));
        tvDescription.setText("Description: " + String.valueOf(detailTicket.getDescription()));
        tvAmount.setText("Amount: " + String.valueOf(detailTicket.getAmount()));

        GoldenRaceApiService apiService = GoldenRaceApiClient.getClient().create(GoldenRaceApiService.class);


        btnDelete.setOnClickListener(v -> {
            Call<Void> callDelete = apiService.deteleDetailTicket(detailTicket.getId());

            callDelete.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()) {
                        Log.i("Ticket Deleted", "The Ticked has been removed");
                        onBackPressed();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("Error", "Request rejected");
                }
            });
        });

        btnUpdate.setOnClickListener(v -> {
            Intent updateDetailTicketIntent = new Intent(getApplicationContext(), UpdateDetailTicketActivity.class);
            updateDetailTicketIntent.putExtra("detailTicket", detailTicket);
            startActivity(updateDetailTicketIntent);
        });

        ivBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }
}