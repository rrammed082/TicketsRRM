package com.iescarrilllo.ticketsrrm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    private GoldenRaceAdapter goldenRaceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ListView lvTicketList;

        GoldenRaceApiService apiService = GoldenRaceApiClient.getClient().create(GoldenRaceApiService.class);
        lvTicketList = findViewById(R.id.lvTickets);

        Call<List<Ticket>> call = apiService.getTickets();

        call.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                List<Ticket> ticketList = new ArrayList<>();

                if(response.isSuccessful()) {
                    ticketList = response.body();

                    goldenRaceAdapter = new GoldenRaceAdapter(getApplicationContext(), ticketList);
                    lvTicketList.setAdapter(goldenRaceAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                // Manejar el caso en que la llamada falle
                Log.e("Error", "No se pudo realizar la petición", t);
            }
        });
    }
}