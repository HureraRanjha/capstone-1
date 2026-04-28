package com.pluralsight;

import java.io.*;
import java.sql.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    static Scanner myScanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>();
    public static void main(String[] args)
    {
        extractTransactions();
        loadHomeScreen();
    }
    private static void extractTransactions()
    {
        try
        {
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufReader = new BufferedReader(fileReader);

            String input;
            input = bufReader.readLine();
            while ((input = bufReader.readLine()) != null)
            {
                String[] parsedLine = input.split("\\|");
                LocalDate date = LocalDate.parse(parsedLine[0]);
                LocalTime time = LocalTime.parse(parsedLine[1]);
                String description = parsedLine[2];
                String vendor = parsedLine[3];
                double amount = Double.parseDouble(parsedLine[4]);

                transactions.add(new Transaction(date, time, description, vendor, amount));
            }
        }
        catch(FileNotFoundException e)
        {
            System.err.println("We could not find the file");
        }
        catch(IOException e)
        {
            System.err.println("Error in reading the file");
        }
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
                    ledgerScreen();
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

    private static void ledgerScreen()
    {
        displayEntry();
    }

    private static void displayEntry()
    {
        for (Transaction t: transactions)
        {
            t.displayTransaction();
        }
    }


}
