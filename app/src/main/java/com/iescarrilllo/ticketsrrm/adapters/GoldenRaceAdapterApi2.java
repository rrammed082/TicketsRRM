package com.iescarrilllo.ticketsrrm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iescarrilllo.ticketsrrm.R;
import com.iescarrilllo.ticketsrrm.models.DetailTicket;
import com.iescarrilllo.ticketsrrm.models.Ticket;

import org.w3c.dom.Text;

import java.util.List;

public class GoldenRaceAdapterApi2 extends ArrayAdapter<DetailTicket> {

    public GoldenRaceAdapterApi2(Context context, List<DetailTicket> detailsTicketList) {
        super(context, 0, detailsTicketList);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        // Obtiene la instancia de detailTicket en la posición dada.
        DetailTicket detailTicket = getItem(position);

        // Verifica si la vista ya ha sido creada, de lo contrario, infla el diseño personalizado.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_detail_ticket, parent , false);
        }

        // Obtiene las referencias a los TextViews en el diseño personalizado.
        TextView tvId = convertView.findViewById(R.id.tvId);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);


        // Asigna los valores del detailTicket a los TextViews.
        tvId.setText("ID: " + String.valueOf(detailTicket.getId()));
        tvDescription.setText("Description: " + String.valueOf(detailTicket.getDescription()));
        tvAmount.setText("Amount: " + String.valueOf(detailTicket.getAmount()));


        // Devuelve la vista actualizada.
        return convertView;
    }
}