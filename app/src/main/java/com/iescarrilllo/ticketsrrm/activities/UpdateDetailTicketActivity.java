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
import com.iescarrilllo.ticketsrrm.models.DetailTicket;
import com.iescarrilllo.ticketsrrm.models.Ticket;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDetailTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_detail_ticket);

        // Obtenemos el objeto DetailTicket de la intención
        DetailTicket detailTicket = (DetailTicket) getIntent().getSerializableExtra("detailTicket");

        // Obtenemos referencias a las vistas en el diseño
        EditText etDescription = findViewById(R.id.etDescription);
        EditText etAmount = findViewById(R.id.etAmount);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        ImageView ivBack = findViewById(R.id.ivBack);

        // Inicializamos el servicio de la API de GoldenRace
        GoldenRaceApiService apiService = GoldenRaceApiClient.getClient().create(GoldenRaceApiService.class);

        // Configuramos los valores iniciales de los EditText con los datos actuales del DetailTicket
        etDescription.setText(detailTicket.getDescription());
        etAmount.setText(String.valueOf(detailTicket.getAmount()));

        // Configuramos el listener del botón de actualización (update)
        btnUpdate.setOnClickListener(v -> {
            if (etDescription.getText().toString().isEmpty()) {
                Toast.makeText(UpdateDetailTicketActivity.this, "Please type description", Toast.LENGTH_SHORT).show();
                etDescription.setError("Description is required");
                etDescription.requestFocus();
            } else if (etAmount.getText().toString().isEmpty()) {
                Toast.makeText(UpdateDetailTicketActivity.this, "Please type amount", Toast.LENGTH_SHORT).show();
                etAmount.setError("Amount is required");
                etAmount.requestFocus();
            } else if (etDescription.getText().toString().length() < 5) {
                Toast.makeText(UpdateDetailTicketActivity.this, "Please type a longer description (at least 5 characters)", Toast.LENGTH_SHORT).show();
                etDescription.setError("Description must have at least 5 characters");
                etDescription.requestFocus();
            } else {

                // Actualizar los valores del DetailTicket con los nuevos datos
                detailTicket.setDescription(etDescription.getText().toString());
                detailTicket.setAmount(Double.parseDouble(etAmount.getText().toString()));

                // Realizamos la llamada a la API para actualizar el DetailTicket
                Call<DetailTicket> updateDetailTicket = apiService.updateDetailTicket(detailTicket.getId(), detailTicket);

                updateDetailTicket.enqueue(new Callback<DetailTicket>() {
                    @Override
                    public void onResponse(Call<DetailTicket> call, Response<DetailTicket> response) {
                        if (response.isSuccessful()) { // Si la respuesta es exitosa
                            Toast.makeText(UpdateDetailTicketActivity.this, "Detail ticket updated", Toast.LENGTH_SHORT).show();
                            Ticket ticketAux = detailTicket.getTicket();
                            Intent backDetailTicketIntent = new Intent(getApplicationContext(), DetailTicketActivity.class);
                            backDetailTicketIntent.putExtra("ticketSelected", ticketAux);
                            backDetailTicketIntent.putExtra("updateTotal", true);
                            startActivity(backDetailTicketIntent);
                            finish();
                        } else {
                            Toast.makeText(UpdateDetailTicketActivity.this, "Error to update Detail Ticket", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailTicket> call, Throwable t) {
                        Toast.makeText(UpdateDetailTicketActivity.this, "Request rejected", Toast.LENGTH_SHORT).show();
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