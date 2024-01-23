package com.iescarrilllo.ticketsrrm.apiClients;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoldenRaceApiClient {
    // URL base para la API de Pokémon
    private static final String BASE_URL = "https://goldenraceapi.onrender.com/golden-race/";

    // Instancia única de Retrofit para la clase PokemonApiClient
    private static Retrofit retrofit = null;

    // Método estático para obtener la instancia de Retrofit
    public static Retrofit getClient() {

        // Verificar si la instancia de Retrofit no ha sido creada
        if (retrofit == null) {

            // Crear una nueva instancia de Retrofit con la URL base y un convertidor Gson
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                    .build();
        }

        // Devolver la instancia de Retrofit (puede ser la existente o la recién creada)
        return retrofit;
    }
}
