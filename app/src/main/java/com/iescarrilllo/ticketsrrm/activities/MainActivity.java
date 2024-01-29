package com.iescarrilllo.ticketsrrm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

        // Obtenemos referencias a las vistas en el diseño
        ImageView ivResearch = findViewById(R.id.ivResearch);
        Button btnAdd = findViewById(R.id.btnAdd);
        lvTicketList = findViewById(R.id.lvTickets);

        // Inicializamos el servicio de la API de GoldenRace
        apiService = GoldenRaceApiClient.getClient().create(GoldenRaceApiService.class);

        // Configuramos el listener del botón de refrescar (research)
        ivResearch.setOnClickListener(v -> {
            research(apiService);
        });

        // Llamamos al método para que cargue los datos en el listView
        research(apiService);

        // Configuramos el listener de la lista de Tickets
        lvTicketList.setOnItemClickListener((parent, view, position, id) -> {

            // Obtenemos el Ticket seleccionado
            Ticket ticketSelected = (Ticket) parent.getItemAtPosition(position);

            // Creamos el intent para abrir una actividad de detalles del Ticket
            Intent detailTicketViewIntent = new Intent(getApplicationContext(), DetailTicketActivity.class);
            detailTicketViewIntent.putExtra("ticketSelected", ticketSelected);
            startActivity(detailTicketViewIntent);
        });

        // Configuramos el listener del botón de añadir (add)
        btnAdd.setOnClickListener(v -> {

            // Iniciar una nueva actividad para crear un nuevo Ticket
            Intent CreateTicketViewIntent = new Intent(getApplicationContext(), CreateTicketActivity.class);
            startActivity(CreateTicketViewIntent);
        });
    }

    /**
     *  Método para calcular y actualizar el monto total del Ticket
     *  @param apiService apiService que va a utilizar el método
     */
    private void research(GoldenRaceApiService apiService) {
        Call<List<Ticket>> call = apiService.getTickets();

        call.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                List<Ticket> ticketList = new ArrayList<>();

                if (response.isSuccessful()) { // Si la respuesta es exitosa
                    ticketList = response.body();

                    // Configuramos el adaptador para cargar el listado de DetailTickets
                    goldenRaceAdapter = new GoldenRaceAdapter(getApplicationContext(), ticketList);
                    goldenRaceAdapter.notifyDataSetChanged();
                    lvTicketList.setAdapter(goldenRaceAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                // Manejar el caso en que la llamada falle
                Toast.makeText(MainActivity.this, "Request rejected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}