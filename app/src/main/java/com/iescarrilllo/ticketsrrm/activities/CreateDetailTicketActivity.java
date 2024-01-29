package com.iescarrilllo.ticketsrrm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iescarrilllo.ticketsrrm.R;
import com.iescarrilllo.ticketsrrm.apiClients.GoldenRaceApiClient;
import com.iescarrilllo.ticketsrrm.apiServices.GoldenRaceApiService;
import com.iescarrilllo.ticketsrrm.models.DetailTicket;
import com.iescarrilllo.ticketsrrm.models.Ticket;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDetailTicketActivity extends AppCompatActivity {
    GoldenRaceApiService apiService;
    DetailTicket detailTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_detail_ticket);

        // Obtenemos el objeto Ticket de la intención
        Ticket ticket = (Ticket) getIntent().getSerializableExtra("ticket");

        // Obtenemos referencias a las vistas en el diseño
        EditText etDescription = findViewById(R.id.etDescription);
        EditText etAmount = findViewById(R.id.etAmount);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        ImageView ivBack = findViewById(R.id.ivBack);

        // Inicializamos el servicio de la API de GoldenRace
        apiService = GoldenRaceApiClient.getClient().create(GoldenRaceApiService.class);

        // Configuramos el listener del botón de envío (submit)
        btnSubmit.setOnClickListener(v -> {
            // Validamos los campos vacíos
            if (etDescription.getText().toString().isEmpty()) {
                Toast.makeText(CreateDetailTicketActivity.this, "Please type the description", Toast.LENGTH_SHORT).show();
                etDescription.setError("Description is required");
                etDescription.requestFocus();
            } else if (etAmount.getText().toString().isEmpty()) {
                Toast.makeText(CreateDetailTicketActivity.this, "Please type the amount", Toast.LENGTH_SHORT).show();
                etAmount.setError("Amount is required");
                etAmount.requestFocus();
            } else if (etDescription.getText().toString().length() < 5) {
                Toast.makeText(CreateDetailTicketActivity.this, "Please type a longer description (at least 5 characters)", Toast.LENGTH_SHORT).show();
                etDescription.setError("Description must have at least 5 characters");
                etDescription.requestFocus();
            } else {

                // Creamos un objeto DetailTicket con los datos proporcionados por el usuario
                detailTicket = new DetailTicket();
                detailTicket.setId(0);
                detailTicket.setDescription(String.valueOf(etDescription.getText()));
                detailTicket.setAmount(Double.parseDouble(etAmount.getText().toString()));
                detailTicket.setTicket(ticket);

                // Realizamos la llamada a la API para crear un nuevo DetailTicket
                Call<DetailTicket> call = apiService.postDetailTicket(detailTicket);

                call.enqueue(new Callback<DetailTicket>() {
                    @Override
                    public void onResponse(Call<DetailTicket> call, Response<DetailTicket> response) {
                        if (response.isSuccessful()) { // Si la respuesta exitosa
                            Toast.makeText(CreateDetailTicketActivity.this, "Successfull detail ticket created", Toast.LENGTH_LONG).show();

                            // Creamos el intent y para volver al DetailTicket y pasamos el ticket borrado y si ha sido borrado
                            Intent backDetailTicketIntent = new Intent(getApplicationContext(), DetailTicketActivity.class);
                            backDetailTicketIntent.putExtra("ticketSelected", ticket);
                            backDetailTicketIntent.putExtra("updateTotal", true);
                            startActivity(backDetailTicketIntent);
                            finish();
                        } else { //En caso de error devuelve la respuesta
                            Toast.makeText(CreateDetailTicketActivity.this, "Error to upload Detail Ticket", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailTicket> call, Throwable t) {
                        // Manejamos en el caso en que la llamada falle
                        Log.e("Error", "Request rejected", t);
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