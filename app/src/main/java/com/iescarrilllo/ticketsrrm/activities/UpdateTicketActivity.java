package com.iescarrilllo.ticketsrrm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iescarrilllo.ticketsrrm.R;
import com.iescarrilllo.ticketsrrm.apiClients.GoldenRaceApiClient;
import com.iescarrilllo.ticketsrrm.apiServices.GoldenRaceApiService;
import com.iescarrilllo.ticketsrrm.models.Ticket;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ticket);

        Ticket ticket = (Ticket) getIntent().getSerializableExtra("ticket");

        EditText etTotalAmount = findViewById(R.id.etTotalAmount);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        ImageView ivBack = findViewById(R.id.ivBack);

        etTotalAmount.setText(String.valueOf(ticket.getTotalAmount()));

        GoldenRaceApiService apiService = GoldenRaceApiClient.getClient().create(GoldenRaceApiService.class);

        btnUpdate.setOnClickListener(v -> {
            if (etTotalAmount.getText().toString().isEmpty()) {
                Toast.makeText(UpdateTicketActivity.this, "Please type total amount", Toast.LENGTH_SHORT).show();
                etTotalAmount.setError("Total amount is required");
                etTotalAmount.requestFocus();
            } else {
                ticket.setTotalAmount(Double.parseDouble(etTotalAmount.getText().toString()));

                Call<Ticket> updateTicket = apiService.updateTicket(ticket.getId(), ticket);

                updateTicket.enqueue(new Callback<Ticket>() {
                    @Override
                    public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(UpdateTicketActivity.this, "Ticket updated", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            finish();
                        } else {
                            Toast.makeText(UpdateTicketActivity.this, "Error to update Ticket", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Ticket> call, Throwable t) {
                        Log.e("Error", "Request rejected", t);
                    }
                });
            }
        });

        ivBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }
}