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
    Boolean updateTotal;
    TextView tvTotalAmount;
    ListView lvDetailsTicketList;
    GoldenRaceAdapterApi2 goldenRaceAdapterApi2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ticket);

        // Obtenemos el objeto Ticket de la intención
        ticket = (Ticket) getIntent().getSerializableExtra("ticketSelected");
        updateTotal = getIntent().getBooleanExtra("updateTotal", false);

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

        // Asignamos los valores de la motocicleta a los TextViews.
        tvId.setText("ID: " + ticket.getId().toString());
        tvCreationDate.setText("Creation Date: " + ticket.getCreationDate());
        tvTotalAmount.setText("Total Amount: " + String.valueOf(ticket.getTotalAmount()));

        // Llamamos al método para cargar la listview
        research(apiService);

        // Configuramos el listener del botón de investigación (research)
        ivResearch.setOnClickListener(v -> {
            research(apiService);
        });

        // Configurar el listener de la lista de detalles del Ticket
        lvDetailsTicketList.setOnItemClickListener((parent, view, position, id) -> {
            // Obtenemos el Ticket seleccionado
            DetailTicket detailTicketSelect = (DetailTicket) parent.getItemAtPosition(position);

            // Creamos el intent para abrir una actividad de detalles del Ticket
            Intent detailsTicketsViewIntent = new Intent(getApplicationContext(), DetailsTicketsActivity.class);
            detailTicketSelect.setTicket(ticket);
            detailsTicketsViewIntent.putExtra("detailTicketSelected", detailTicketSelect);
            startActivity(detailsTicketsViewIntent);
        });

        // Configuramos el listener del botón de añadir (add)
        btnAdd.setOnClickListener(v -> {
            Intent createDetailTicketIntent = new Intent(getApplicationContext(), CreateDetailTicketActivity.class);
            createDetailTicketIntent.putExtra("ticket", ticket);
            startActivity(createDetailTicketIntent);
            finish();
        });

        // Configuramos el listener del botón de eliminar (delete)
        btnDelete.setOnClickListener(v -> {
            // Realizamos la llamada a la API para eliminar el Ticket
            Call<Void> callDelete = apiService.deleteTicket(ticket.getId());

            callDelete.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) { // Si la respuesta es exitosa
                        Toast.makeText(DetailTicketActivity.this, "The Ticked has been removed", Toast.LENGTH_LONG).show();
                        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivityIntent);
                        finish();
                    } else {
                        Toast.makeText(DetailTicketActivity.this, "Error deteling Ticket", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(DetailTicketActivity.this, "Request rejected", Toast.LENGTH_LONG).show();
                }
            });
        });

        // Configuramos el listener del botón de actualizar (update)
        btnUpdate.setOnClickListener(v -> {

            // Iniciamos una nueva actividad para actualizar el Ticket
            Intent updateTicketIntent = new Intent(getApplicationContext(), UpdateTicketActivity.class);
            updateTicketIntent.putExtra("ticket", ticket);
            startActivity(updateTicketIntent);
        });

        // Configuramos el listener del botón de retroceso (back)
        ivBack.setOnClickListener(v -> {
            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivityIntent);
        });
    }

    /**
     *  Método para calcular y actualizar el monto total del Ticket
     *  @param apiService apiService que va a utilizar el método
     *  @param detailTicketList lista de detalles de la clase que va a utilizarse para actualizar el TotalAmount
     */
    private void calculateTotalAmount(GoldenRaceApiService apiService, List<DetailTicket> detailTicketList) {
        Double totalValue = 0.0;

        // Recorremos la lista para ir almacenando el Amount de cada elemento
        for (DetailTicket d : detailTicketList) {
            totalValue += d.getAmount();
        }

        // Seteamos el valor del bucle en el objeto Ticket
        ticket.setTotalAmount(totalValue);

        // Realizar la llamada a la API para actualizar el Ticket con el nuevo totalAmount
        Call<Ticket> callUpdateTicket = apiService.updateTicket(ticket.getId(), ticket);

        callUpdateTicket.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                if (response.isSuccessful()) { // Si la respuesta es exitosa
                    tvTotalAmount.setText("Total Amount: " + ticket.getTotalAmount());
                    Toast.makeText(DetailTicketActivity.this, "TotalAmount updated", Toast.LENGTH_SHORT).show();
                } else { // Manejamos en el caso de error en la respuesta de la API
                    Toast.makeText(DetailTicketActivity.this, "Error updating ticket total amount", Toast.LENGTH_SHORT).show();
                }
            }

            // Manejamos el caso en que la llamada falle
            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                Toast.makeText(DetailTicketActivity.this, "Request rejected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *  Método para obtener y mostrar los detalles del Ticket desde la API
     *  @param apiService
     */
    private void research(GoldenRaceApiService apiService) {

        // Realizar la llamada a la API para obtener los DetailTickets del Ticket actual
        Call<List<DetailTicket>> call = apiService.getDetailsTicket(ticket.getId());

        call.enqueue(new Callback<List<DetailTicket>>() {
            @Override
            public void onResponse(Call<List<DetailTicket>> call, Response<List<DetailTicket>> response) {
                List<DetailTicket> detailTicketsList = new ArrayList<>();

                if (response.isSuccessful()) { // Si la respuesta es exitosa
                    detailTicketsList = response.body();

                    if (updateTotal) { // Si la variable recibida por el Intent es true se vuelve a calcular el ToutalAmount
                        // Llamamos al método calculateTotalAmount para verificar si hay algún cambio
                        calculateTotalAmount(apiService, detailTicketsList);
                    }

                    // Configuramos el adaptador para la lista de DetailTickets
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

