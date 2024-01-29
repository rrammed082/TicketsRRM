 package com.iescarrilllo.ticketsrrm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iescarrilllo.ticketsrrm.R;
import com.iescarrilllo.ticketsrrm.apiClients.GoldenRaceApiClient;
import com.iescarrilllo.ticketsrrm.apiServices.GoldenRaceApiService;
import com.iescarrilllo.ticketsrrm.models.DetailTicket;
import com.iescarrilllo.ticketsrrm.models.Ticket;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

 public class DetailsTicketsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_tickets);

        // Obtenemos el objeto DetailTicket de la intención
        DetailTicket detailTicket = (DetailTicket) getIntent().getSerializableExtra("detailTicketSelected");

        // Obtenemos referencias a las vistas en el diseño
        TextView tvId = findViewById(R.id.tvId);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvAmount = findViewById(R.id.tvAmount);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        ImageView ivBack = findViewById(R.id.ivBack);

        // Configuramos el contenido de las vistas con la información del DetailTicket
        tvId.setText("ID: " + String.valueOf(detailTicket.getId()));
        tvDescription.setText("Description: " + String.valueOf(detailTicket.getDescription()));
        tvAmount.setText("Amount: " + String.valueOf(detailTicket.getAmount()));

        // Configuramos el listener del botón de eliminación (delete)
        GoldenRaceApiService apiService = GoldenRaceApiClient.getClient().create(GoldenRaceApiService.class);

        // Obtenemos el objeto Ticket asociado al DetailTicket
        Ticket ticketAux = detailTicket.getTicket();


        btnDelete.setOnClickListener(v -> {

            // Realizamos la llamada a la API para eliminar el DetailTicket
            Call<Void> callDelete = apiService.deteleDetailTicket(detailTicket.getId());

            callDelete.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()) { // Si la respuesta es exitosa
                        Log.i("Ticket Deleted", "The Ticked has been removed");
                        Intent backDetailTicketIntent = new Intent(getApplicationContext(), DetailTicketActivity.class);
                        backDetailTicketIntent.putExtra("ticketSelected", ticketAux);
                        backDetailTicketIntent.putExtra("updateTotal", true);
                        startActivity(backDetailTicketIntent);
                        finish();
                    } else {
                        Toast.makeText(DetailsTicketsActivity.this, "Error deteling Detail Ticket", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(DetailsTicketsActivity.this, "Request rejected", Toast.LENGTH_LONG).show();
                }
            });
        });

        // Configuramos el listener del botón de actualización (update)
        btnUpdate.setOnClickListener(v -> {
            Intent updateDetailTicketIntent = new Intent(getApplicationContext(), UpdateDetailTicketActivity.class);
            updateDetailTicketIntent.putExtra("detailTicket", detailTicket);
            startActivity(updateDetailTicketIntent);
            finish();
        });

        // Configuramos el listener del botón de retroceso (back)
        ivBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }
}