package com.pluralsight;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main
{
    static Scanner myScanner = new Scanner(System.in);

    public static void main(String[] args)
    {
        loadHomeScreen();
    }

    private static void loadHomeScreen()
    {
        boolean repeat = true;
        while(repeat)
        {
            System.out.println("""
                    Choose an action:
                    (D) Deposit
                    (P) Payment
                    (L) Ledger
                    (X) Exit
                    """);
            String selection = myScanner.nextLine();
            switch (selection)
            {
                case "D":
                    System.out.println("                 Deposit");
                    makeTransaction("deposit");
                    break;
                case "P":
                    System.out.println("Payment");
                    makeTransaction("payment");
                    break;
                case "L":
                    System.out.println("Ledger");
                    break;
                case "X":
                    System.out.println("Exit");
                    repeat = false;
                    break;
            }
        }
    }

    private static void makeTransaction(String type)
    {
        System.out.printf("What is the date of this %s (yyyy-MM-dd): ", type);
        String dateInput = myScanner.nextLine();
        LocalDate localDate = LocalDate.parse(dateInput);

        System.out.printf("What is the time of this %s (hh:mm:ss): ", type);
        String timeInput = myScanner.nextLine();

        LocalTime localTime = LocalTime.parse(timeInput);

        System.out.printf("What is the description of the %s: ", type);
        String description = myScanner.nextLine();

        System.out.println("Who is the vendor: ");
        String vendor = myScanner.nextLine();

        System.out.println("What is the amount: ");
        double amount = Double.parseDouble(myScanner.nextLine());
        if (type.equals("payment")) amount *= -1;

        try
        {
            FileWriter writer = new FileWriter("src/main/resources/transactions.csv", true);
            String transaction = new Transaction(localDate, localTime, description, vendor, amount).displayTransaction();
            writer.append(transaction);
            writer.close();
        } catch (IOException e)
        {
            System.err.println("Couldnt write to file tranactions.csv");
        }
    }
}
