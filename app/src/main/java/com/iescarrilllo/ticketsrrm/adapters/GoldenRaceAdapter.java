package com.iescarrilllo.ticketsrrm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iescarrilllo.ticketsrrm.R;
import com.iescarrilllo.ticketsrrm.models.Ticket;

import java.util.List;

public class GoldenRaceAdapter extends ArrayAdapter<Ticket> {

    public GoldenRaceAdapter(Context context, List<Ticket> TicketList) {
        super(context, 0, TicketList);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        // Obtiene la instancia de motocicleta en la posición dada.
        Ticket ticket = getItem(position);

        // Verifica si la vista ya ha sido creada, de lo contrario, infla el diseño personalizado.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ticket, parent , false);
        }

        // Obtiene las referencias a los TextViews en el diseño personalizado.
        TextView tvVersion = convertView.findViewById(R.id.tvVersion);
        TextView tvCreationDate = convertView.findViewById(R.id.tvCreationDate);
        TextView tvTotalAmount = convertView.findViewById(R.id.tvTotalAmount);
        TextView tvId = convertView.findViewById(R.id.tvId);

        // Asigna los valores de la motocicleta a los TextViews.
        tvVersion.setText(ticket.getVersion().toString());
        tvCreationDate.setText(ticket.getCreationDate());
        tvTotalAmount.setText(ticket.getTotalAmount().toString());
        tvId.setText(ticket.getId().toString());

        // Devuelve la vista actualizada.
        return convertView;
    }
}
