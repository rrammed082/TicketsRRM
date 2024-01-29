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

        // Obtenemos el objeto Ticket de la intención
        Ticket ticket = (Ticket) getIntent().getSerializableExtra("ticket");

        // Obtenemos referencias a las vistas en el diseño
        EditText etTotalAmount = findViewById(R.id.etTotalAmount);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        ImageView ivBack = findViewById(R.id.ivBack);

        // Configuramos el valor inicial del EditText con el totalAmount actual del Ticket
        etTotalAmount.setText(String.valueOf(ticket.getTotalAmount()));

        // Inicializamos el servicio de la API de GoldenRace
        GoldenRaceApiService apiService = GoldenRaceApiClient.getClient().create(GoldenRaceApiService.class);

        // Configuramos el listener del botón de actualización (update)
        btnUpdate.setOnClickListener(v -> {
            if (etTotalAmount.getText().toString().isEmpty()) {
                Toast.makeText(UpdateTicketActivity.this, "Please type total amount", Toast.LENGTH_SHORT).show();
                etTotalAmount.setError("Total amount is required");
                etTotalAmount.requestFocus();
            } else {
                // Actualizamos el valor del totalAmount del Ticket con el nuevo valor ingresado
                ticket.setTotalAmount(Double.parseDouble(etTotalAmount.getText().toString()));

                // Realizamos la llamada a la API para actualizar el Ticket
                Call<Ticket> updateTicket = apiService.updateTicket(ticket.getId(), ticket);

                updateTicket.enqueue(new Callback<Ticket>() {
                    @Override
                    public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                        if (response.isSuccessful()) { // Si la respuesta es exitosa
                            Toast.makeText(UpdateTicketActivity.this, "Ticket updated", Toast.LENGTH_SHORT).show();
                            Intent backDetailTicketIntent = new Intent(getApplicationContext(), DetailTicketActivity.class);
                            backDetailTicketIntent.putExtra("ticketSelected", ticket);
                            startActivity(backDetailTicketIntent);
                            finish();
                        } else {
                            // Manejar el caso de error en la respuesta de la API
                            Toast.makeText(UpdateTicketActivity.this, "Error to update Ticket", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Ticket> call, Throwable t) {
                        // Manejamos el caso en que la llamada falle
                        Toast.makeText(UpdateTicketActivity.this, "Request rejected", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Configuramos el listener del botón de retroceso (back)
        ivBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }
}