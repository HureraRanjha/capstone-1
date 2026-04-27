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
                    addDeposit();
                    break;
                case "P":
                    System.out.println("Payment");
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

    private static void addDeposit()
    {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

        System.out.println("What is the description of the deposit: ");
        String description = myScanner.nextLine();

        System.out.println("Who is the vendor");
        String vendor = myScanner.nextLine();

        System.out.println("What is the amount");
        double amount = Double.parseDouble(myScanner.nextLine());
        try
        {
            FileWriter writer = new FileWriter("src/main/resources/transactions.csv", true);
            String deposit = new Transaction(date, time, description, vendor, amount).displayTransaction();
            writer.append(deposit);
            writer.close();
        } catch (IOException e)
        {
            System.err.println("Couldnt write to file tranactions.csv");
        }
    }
}
