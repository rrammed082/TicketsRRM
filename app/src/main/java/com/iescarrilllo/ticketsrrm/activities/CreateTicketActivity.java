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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);

        Button btnSubmit = findViewById(R.id.btnSubmit);
        ImageView ivBack = findViewById(R.id.ivBack);
        EditText etTotalAmount = findViewById(R.id.etTotalAmount);

        GoldenRaceApiService apiService = GoldenRaceApiClient.getClient().create(GoldenRaceApiService.class);

        Ticket ticket = new Ticket();

        // Obtenemos la fecha y hora actual
        LocalDateTime now = LocalDateTime.now();

        // Define el patrón de formato
        String patron = "dd/MM/yyyy HH:mm:ss";

        // Crea un objeto DateTimeFormatter con el patrón
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patron);

        // Formatea la fecha y hora actual usando el patrón
        String fechaFormateada = now.format(formatter);

        btnSubmit.setOnClickListener(v -> {

            if (etTotalAmount.getText().toString().isEmpty()) {
                Toast.makeText(CreateTicketActivity.this, "Please enter the total amount", Toast.LENGTH_SHORT).show();
                etTotalAmount.setError("Total amount is required");
                etTotalAmount.requestFocus();
            } else {
                // Seteo de elementos del Ticket
                ticket.setId(0);
                ticket.setCreationDate(fechaFormateada);
                ticket.setTotalAmount(Double.parseDouble(etTotalAmount.getText().toString()));

                Call<Ticket> call = apiService.postTicket(ticket);
                call.enqueue(new Callback<Ticket>() {
                    @Override
                    public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                        if (response.isSuccessful()) {
                            Ticket createdTicket = response.body();
                            Toast.makeText(CreateTicketActivity.this, "Successfull ticket created", Toast.LENGTH_LONG).show();
                            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                            onBackPressed();
                            finish();
                        } else {
                            Toast.makeText(CreateTicketActivity.this, "Error to upload Ticket", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Ticket> call, Throwable t) {
                        // Manejar el caso en que la llamada falle
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