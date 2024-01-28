package com.iescarrilllo.ticketsrrm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iescarrilllo.ticketsrrm.R;
import com.iescarrilllo.ticketsrrm.adapters.GoldenRaceAdapter;
import com.iescarrilllo.ticketsrrm.adapters.GoldenRaceAdapterApi2;
import com.iescarrilllo.ticketsrrm.apiClients.GoldenRaceApiClient;
import com.iescarrilllo.ticketsrrm.apiServices.GoldenRaceApiService;
import com.iescarrilllo.ticketsrrm.models.DetailTicket;
import com.iescarrilllo.ticketsrrm.models.Ticket;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTicketActivity extends AppCompatActivity {
    Ticket ticket;
    TextView tvTotalAmount;
    ListView lvDetailsTicketList;
    GoldenRaceAdapterApi2 goldenRaceAdapterApi2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ticket);

        ticket = (Ticket) getIntent().getSerializableExtra("ticketSelected");

        // Obtiene las referencias a los TextViews en el diseño personalizado.
        TextView tvCreationDate = findViewById(R.id.tvCreationDate);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        TextView tvId = findViewById(R.id.tvId);
        lvDetailsTicketList = findViewById(R.id.lvDetailsTicketList);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView ivResearch = findViewById(R.id.ivResearch);



        // Instanciamos el Services
        GoldenRaceApiService apiService = GoldenRaceApiClient.getClient().create(GoldenRaceApiService.class);

        // Asigna los valores de la motocicleta a los TextViews.
        tvId.setText("ID: " + ticket.getId().toString());
        tvCreationDate.setText("Creation Date: " + ticket.getCreationDate());
        tvTotalAmount.setText("Total Amount: " + String.valueOf(ticket.getTotalAmount()));

        research(apiService);

        ivResearch.setOnClickListener(v -> {
            research(apiService);
        });

        lvDetailsTicketList.setOnItemClickListener((parent, view, position, id) -> {
            // Obtenemos el Ticket seleccionado
            DetailTicket detailTicketSelect = (DetailTicket) parent.getItemAtPosition(position);

            // Creamos el intent para abrir una actividad de detalles del Ticket
            Intent detailsTicketsViewIntent = new Intent(getApplicationContext(), DetailsTicketsActivity.class);
            detailTicketSelect.setTicket(ticket);
            detailsTicketsViewIntent.putExtra("detailTicketSelected", detailTicketSelect);
            startActivity(detailsTicketsViewIntent);
        });

        btnAdd.setOnClickListener(v -> {
            Intent createDetailTicketIntent = new Intent(getApplicationContext(), CreateDetailTicketActivity.class);
            createDetailTicketIntent.putExtra("ticket", ticket);
            startActivity(createDetailTicketIntent);
        });

        btnDelete.setOnClickListener(v -> {
            Call<Void> callDelete = apiService.deleteTicket(ticket.getId());

            callDelete.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()) {
                        Log.i("Ticket Deleted", "The Ticked has been removed");
                        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
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
            Intent updateTicketIntent = new Intent(getApplicationContext(), UpdateTicketActivity.class);
            updateTicketIntent.putExtra("ticket", ticket);
            startActivity(updateTicketIntent);
        });

        ivBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    private void calculateTotalAmount(GoldenRaceApiService apiService, List<DetailTicket> detailTicketList){
        Double totalValue = 0.0;

        for (DetailTicket d : detailTicketList) {
            totalValue += d.getAmount();
        }

        ticket.setTotalAmount(totalValue);

        Call<Ticket> callUpdateTicket = apiService.updateTicket(ticket.getId(), ticket);

        callUpdateTicket.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                if(response.isSuccessful()){
                    tvTotalAmount.setText("Total Amount: " + ticket.getTotalAmount());
                }
                else {
                    Toast.makeText(DetailTicketActivity.this, "Error updating ticket total amount", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                Log.e("Error", "Request rejected", t);
            }
        });
    }

    private void research(GoldenRaceApiService apiService) {
        Call<List<DetailTicket>> call = apiService.getDetailsTicket(ticket.getId());

        call.enqueue(new Callback<List<DetailTicket>>() {
            @Override
            public void onResponse(Call<List<DetailTicket>> call, Response<List<DetailTicket>> response) {
                List<DetailTicket> detailTicketsList = new ArrayList<>();

                if(response.isSuccessful()) {
                    detailTicketsList = response.body();

                    calculateTotalAmount(apiService, detailTicketsList);

                    goldenRaceAdapterApi2 = new GoldenRaceAdapterApi2(getApplicationContext(), detailTicketsList);
                    lvDetailsTicketList.setAdapter(goldenRaceAdapterApi2);
                }
            }

            @Override
            public void onFailure(Call<List<DetailTicket>> call, Throwable t) {
                // Manejar el caso en que la llamada falle
                Log.e("Error", "Request rejected");
            }
        });
    }
}

