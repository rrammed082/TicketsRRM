package com.iescarrilllo.ticketsrrm.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Ticket implements Serializable {

    @SerializedName("creationDate")
    @Expose
    private String creationDate;
    @SerializedName ("totalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName ("detailTickets")
    @Expose
    private List<DetailTicket> detailTickets;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Ticket() {
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<DetailTicket> getDetailTickets() {
        return detailTickets;
    }

    public void setDetailTickets(List<DetailTicket> detailTickets) {
        this.detailTickets = detailTickets;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "creationDate='" + creationDate + '\'' +
                ", totalAmount=" + totalAmount +
                ", detailTickets=" + detailTickets +
                ", id=" + id +
                '}';
    }
}

