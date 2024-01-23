package com.iescarrilllo.ticketsrrm.apiServices;

import com.iescarrilllo.ticketsrrm.models.Ticket;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GoldenRaceApiService {
    @GET("api/tickets/")
    Call<List<Ticket>> getTickets();


}