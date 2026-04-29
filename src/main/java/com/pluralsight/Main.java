package com.pluralsight;

import java.io.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main
{
    static Scanner myScanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>();
    static LocalDate localDate;

    public static void main(String[] args)
    {
        loadTransactions();
        displayHomeScreen();
    }
    private static void loadTransactions()
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
        Collections.reverse(transactions);
    }
    private static void displayHomeScreen()
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
            Transaction transaction = new Transaction(localDate, localTime, description, vendor, amount);
            transactions.add(0, transaction);

            String stringTransaction = transaction.displayTransaction();
            writer.append(stringTransaction);
            writer.close();
        } catch (IOException e)
        {
            System.err.println("Couldnt write to file tranactions.csv");
        }
    }

    private static void ledgerScreen()
    {
        boolean repeat = true;
        while(repeat)
        {
            System.out.println("""
                    Choose an action:
                    (A) All Entries
                    (D) Deposits 
                    (P) Payments
                    (R) Reports
                    (H) Home
                    """);
            String selection = myScanner.nextLine();
            switch(selection)
            {
                case "A":
                    displayTransactions("all");
                    break;
                case "D":
                    displayTransactions("deposit");
                    break;
                case "P":
                    displayTransactions("payment");
                    break;
                case "R":
                    displayReportsScreen();
                    break;
                case "H":
                    repeat = false;
                    break;
            }
        }
    }

    private static void displayTransactions(String type)
    {
        for (Transaction t: transactions)
        {
            if (type.equals("all")) System.out.println(t.displayTransaction());
            else if (type.equals("deposit") && t.getAmount() > 0) System.out.println(t.displayTransaction());
            else if (type.equals("payment") && t.getAmount() < 0) System.out.println(t.displayTransaction());
        }
    }

    private static void displayReportsScreen()
    {
        localDate = LocalDate.now();
        boolean repeat = true;
        while(repeat)
        {
            System.out.println("""
                    Choose an action:
                    (1) Month to date
                    (2) Previous Month 
                    (3) Year to Date
                    (4) Previous Year
                    (5) Search by Vendor
                    (6) Custom Search
                    (0) Back to ledger
                    """);
            // Month to date start of the month
            //Previous month start to the end
            // Start of the year to now
            //Previous year

            String selection = myScanner.nextLine();
            switch(selection)
            {
                case "1":
                    monthToDate();
                    break;
                case "2":
                    previousMonth();
                    break;
                case "3":
                    yearToDate();
                    break;
                case "4":
                    previousYear();
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "6":
                    ArrayList<Transaction> filteredTransactions = applyCustomFilters(transactions);
                    for (Transaction t: filteredTransactions)
                    {
                        System.out.println(t.displayTransaction());
                    }
                    break;
                case "0":
                    repeat = false;
                    break;
            }
        }
    }

    private static void monthToDate()
    {
        for(Transaction t: transactions)
        {
            if(t.getDate().getMonthValue() == localDate.getMonthValue() && t.getDate().getYear() == localDate.getYear() && t.getDate().getDayOfMonth() <= localDate.getDayOfMonth())
            {
                System.out.println(t.displayTransaction());
            }
        }
    }

    private static void previousMonth()
    {
        int year;
        int month;
        if(localDate.getMonthValue() == 1)
        {
            year = localDate.getYear() - 1 ;
            month = 12;
        }
        else
        {
            year = localDate.getYear();
            month = localDate.getMonthValue() - 1;
        }


        for(Transaction t: transactions)
        {
            if(t.getDate().getMonthValue() == month && t.getDate().getYear() == year)
            {
                System.out.println(t.displayTransaction());
            }
        }
    }

    private static void yearToDate()
    {
        for(Transaction t: transactions)
        {
            if(t.getDate().getMonthValue() <= localDate.getMonthValue() && t.getDate().getDayOfMonth() <= localDate.getDayOfMonth() && t.getDate().getYear() == localDate.getYear())
            {
                System.out.println(t.displayTransaction());
            }
        }
    }

    private static void previousYear()
    {
        for(Transaction t: transactions)
        {
            if(t.getDate().getYear() == localDate.getYear() - 1)
            {
                System.out.println(t.displayTransaction());
            }
        }
    }

    private static void searchByVendor()
    {
        System.out.println("What vendor are you searching for? ");
        String vendorName = myScanner.nextLine();
        for(Transaction t: transactions)
        {
            if(t.getVendor().equals(vendorName))
            {
                System.out.println(t.displayTransaction());
            }
        }
    }

    public static ArrayList<Transaction> applyCustomFilters(ArrayList<Transaction> transactions)
    {
        ArrayList<Transaction> results = filterByDates(transactions);
        results = filterByDescription(results);
        results = filterByVendor(results);
        results = filterByAmount(results);

        return  results;
    }

    private static ArrayList<Transaction> filterByDates(ArrayList<Transaction> transactions)
    {
        ArrayList<Transaction> transactionsByDate = new ArrayList<>();

        System.out.println("What is the start date: ");
        String startDate = myScanner.nextLine();

        System.out.println("What is the end date: ");
        String endDate = myScanner.nextLine();

        LocalDate startSelection = null;
        LocalDate endSelection = null;
        if (!startDate.isEmpty()) {startSelection = LocalDate.parse(startDate);}
        if(!endDate.isEmpty()) { endSelection = LocalDate.parse(endDate);}

        for (Transaction t: transactions)
        {
            boolean afterStart = (startSelection == null || t.getDate().isAfter(startSelection));
            boolean afterEnd = (endSelection == null || t.getDate().isBefore(endSelection));
            if(afterStart && afterEnd)
            {
                transactionsByDate.add(t);
                //System.out.println(t.displayTransaction());
            }
        }
        return transactionsByDate;

    }

    private static ArrayList<Transaction> filterByDescription(ArrayList<Transaction> transactions)
    {
        ArrayList<Transaction> transactionsByDescription = new ArrayList<>();

        System.out.println("What is the description: ");
        String description = myScanner.nextLine();

        for (Transaction t: transactions)
        {
            if(t.getDescription().contains(description))
            {
                transactionsByDescription.add(t);
            }
        }
        return transactionsByDescription;
    }

    private static ArrayList<Transaction> filterByVendor(ArrayList<Transaction> transactions)
    {
        ArrayList<Transaction> transactionsByVendor = new ArrayList<>();

        System.out.println("Who is the vendor: ");
        String vendor = myScanner.nextLine();

        for (Transaction t: transactions)
        {
            if(t.getVendor().contains(vendor))
            {
                transactionsByVendor.add(t);
            }
        }
        return transactionsByVendor;
    }

    private static ArrayList<Transaction> filterByAmount(ArrayList<Transaction> transactions)
    {
        ArrayList<Transaction> transactionsByAmount = new ArrayList<>();

        double minAmount = -Double.MAX_VALUE;
        double maxAmount = Double.MAX_VALUE;

        System.out.println("What is the minimum amount: ");
        String minimumAmount = myScanner.nextLine();
        if (!minimumAmount.isEmpty()) minAmount = Double.parseDouble(minimumAmount);

        System.out.println("What is the maximum amount: ");
        String maximumAmount = myScanner.nextLine();
        if (!maximumAmount.isEmpty()) maxAmount = Double.parseDouble(maximumAmount);

        for (Transaction t: transactions)
        {
            if(t.getAmount() >= minAmount && t.getAmount() <= maxAmount)
            {
                transactionsByAmount.add(t);
            }
        }
        return transactionsByAmount;
    }
}
