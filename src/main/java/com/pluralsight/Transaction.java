package com.pluralsight;

import java.time.LocalDateTime;

public class Transaction
{
    private LocalDateTime date;
    private LocalDateTime time;
    private String description;
    private String vendor;
    private double amount;

    public Transaction(LocalDateTime date, LocalDateTime time, String description, String vendor, double amount)
    {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }
}
