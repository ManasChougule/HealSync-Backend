package com.java.healSync.entities;

public class DayLoadDTO {
    private int total;
    private int confirmed;
    private int cancelled;
    private int pending;

    public DayLoadDTO() {}

    public DayLoadDTO(int total, int confirmed, int cancelled, int pending) {
        this.total = total;
        this.confirmed = confirmed;
        this.cancelled = cancelled;
        this.pending = pending;
    }

    public int getTotal() {
        return total;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public int getCancelled() {
        return cancelled;
    }

    public int getPending() {
        return pending;
    }

    public void incrementStatus(String status) {
        total++;
        switch (status.toUpperCase()) {
            case "CONFIRMED": confirmed++; break;
            case "CANCELLED": cancelled++; break;
            case "PENDING": pending++; break;
        }
    }
}
