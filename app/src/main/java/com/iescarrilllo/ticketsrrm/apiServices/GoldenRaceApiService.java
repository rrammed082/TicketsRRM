package com.iescarrilllo.ticketsrrm.apiServices;

import com.iescarrilllo.ticketsrrm.models.DetailTicket;
import com.iescarrilllo.ticketsrrm.models.Ticket;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GoldenRaceApiService {

    // TICKETS

    /**
     * Obtiene una lista de tickets desde el servidor.
     *
     * @return Una llamada de Retrofit que contiene una lista de objetos Ticket.
     */
    @GET("api/tickets/")
    Call<List<Ticket>> getTickets();

    /**
     * Publica un nuevo ticket en el servidor.
     *
     * @param ticket El objeto Ticket que se enviará en el cuerpo de la solicitud, debe ir con el ID a 0.
     * @return Una llamada de Retrofit que contiene el Ticket creado, con el ID autoasignado.
     */
    @POST("api/ticket/")
    Call<Ticket> postTicket(@Body Ticket ticket);

    /**
     * Actualiza un ticket existente en el servidor.
     *
     * @param ticketId El ID del ticket que se actualizará.
     * @param ticket   El objeto Ticket actualizado que se enviará en el cuerpo de la solicitud.
     * @return Una llamada de Retrofit que contiene el Ticket actualizado.
     */
    @PUT("api/ticket/{id}")
    Call<Ticket> updateTicket(@Path("id") int ticketId, @Body Ticket ticket);

    /**
     * Elimina un ticket del servidor.
     *
     * @param ticketId El ID del ticket que se eliminará.
     * @return Una llamada de Retrofit sin cuerpo de respuesta, indicando éxito o fallo.
     */
    @DELETE("api/ticket/{id}")
    Call<Void> deleteTicket(@Path("id") int ticketId);

    // DETAIL TICKETS

    /**
     * Obtiene una lista de detalles de tickets desde el servidor.
     *
     * @return Una llamada de Retrofit que contiene una lista de objetos DetailTicket.
     */
    @GET("api2/detailTickets/{id}")
    Call<List<DetailTicket>> getDetailsTicket(@Path("id") int ticketId);

    /**
     * Realiza una petición DELETE para eliminar un DetailTicket específico del servidor por su ID.
     *
     * @param detailTicketId El ID del DetailTicket que se desea borrar.
     * @return Una llamada de Retrofit sin respuesta (Void).
     */
    @DELETE("api2/detailTicket/{id}")
    Call<Void> deteleDetailTicket(@Path("id") int detailTicketId);

    /**
     * Realiza una petición POST para crear un nuevo DetailTicket en el servidor.
     *
     * @param detailsTicket El objeto DetailTicket que se desea crear.
     * @return Una llamada de Retrofit que contiene el DetailTicket creado.
     */
    @POST("api2/detailTicket/")
    Call<DetailTicket> postDetailTicket(@Body DetailTicket detailsTicket);

    /**
     * Realiza una petición PUT para actualizar un DetailTicket existente en el servidor.
     *
     * @param detailTicketId El ID del DetailTicket que se desea actualizar.
     * @param detailTicket   El objeto DetailTicket con los nuevos datos.
     * @return Una llamada de Retrofit que contiene el DetailTicket actualizado.
     */
    @PUT("api2/detailTicket/{id}")
    Call<DetailTicket> updateDetailTicket(@Path("id") int detailTicketId, @Body DetailTicket detailTicket);
}
