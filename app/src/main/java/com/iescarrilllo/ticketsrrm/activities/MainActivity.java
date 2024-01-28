package com.iescarrilllo.ticketsrrm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.iescarrilllo.ticketsrrm.R;
import com.iescarrilllo.ticketsrrm.adapters.GoldenRaceAdapter;
import com.iescarrilllo.ticketsrrm.apiClients.GoldenRaceApiClient;
import com.iescarrilllo.ticketsrrm.apiServices.GoldenRaceApiService;
import com.iescarrilllo.ticketsrrm.models.Ticket;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    GoldenRaceAdapter goldenRaceAdapter;
    GoldenRaceApiService apiService;
    ListView lvTicketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ivResearch = findViewById(R.id.ivResearch);
        Button btnAdd = findViewById(R.id.btnAdd);
        lvTicketList = findViewById(R.id.lvTickets);

        apiService = GoldenRaceApiClient.getClient().create(GoldenRaceApiService.class);

        ivResearch.setOnClickListener(v -> {
            research(apiService);
        });

        research(apiService);



        lvTicketList.setOnItemClickListener((parent, view, position, id) -> {

            // Obtenemos el Ticket seleccionado
            Ticket ticketSelected = (Ticket) parent.getItemAtPosition(position);

            // Creamos el intent para abrir una actividad de detalles del Ticket
            Intent detailTicketViewIntent = new Intent(getApplicationContext(), DetailTicketActivity.class);
            detailTicketViewIntent.putExtra("ticketSelected", ticketSelected);
            startActivity(detailTicketViewIntent);
        });

        btnAdd.setOnClickListener(v -> {
            Intent CreateTicketViewIntent = new Intent(getApplicationContext(), CreateTicketActivity.class);
            startActivity(CreateTicketViewIntent);
        });
    }

    private void research(GoldenRaceApiService apiService) {
        Call<List<Ticket>> call = apiService.getTickets();

        call.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                List<Ticket> ticketList = new ArrayList<>();

                if (response.isSuccessful()) {
                    ticketList = response.body();


                    goldenRaceAdapter = new GoldenRaceAdapter(getApplicationContext(), ticketList);
                    goldenRaceAdapter.notifyDataSetChanged();
                    lvTicketList.setAdapter(goldenRaceAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                // Manejar el caso en que la llamada falle
                Log.e("Error", "Request rejected");
            }
        });
    }
}