package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main
{
    // Initialized Field Variables Scanner, ArrayList with Transaction Objects, LocalDate
    static Scanner myScanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>();
    static LocalDate localDate;

    public static void main(String[] args)
    {
        loadTransactions();     //Populate transactions ArrayList
        displayHomeScreen();    //Entry Point
    }

    /**
     * Loads transactions CSV into the ArrayList of transaction objects
     * Then reverses the ArrayList
     */
    private static void loadTransactions()
    {
        try
        {
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufReader = new BufferedReader(fileReader);

            String input;
            input = bufReader.readLine();   //Skips the header
            while ((input = bufReader.readLine()) != null)
            {
                String[] parsedLine = input.split("\\|");       //String Array contai   ning splitted elements
                LocalDate date = LocalDate.parse(parsedLine[0]);
                LocalTime time = LocalTime.parse(parsedLine[1]);
                String description = parsedLine[2];
                String vendor = parsedLine[3];
                double amount = Double.parseDouble(parsedLine[4]);

                transactions.add(new Transaction(date, time, description, vendor, amount)); //Created Transaction object and stored in ArrayList
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
        Collections.reverse(transactions);   //Reverse ArrayList
    }

    /***
     *  Entrypoint into the Home Screen and leads to other screens
     */
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
            /*
             *  4 Cases: D = Deposit  => makeTransaction field = "deposit"
             *           P = Payment  => makeTransaction field = "payment"
             *           L = Ledger   => ledgerScreen
             *           X = Exit     => break
             */
            switch (selection)
            {
                case "D":
                    System.out.println("Deposit");
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
                    repeat = false;          // Break out of while loop
                    break;
            }
        }
    }

    /**
     * Make Transactions gets date, time, description, vendor, amount from user
     * Creates transaction object adds to ArrayList as well as appends to
     * transaction.csv
     * @param type
     */
    private static void makeTransaction(String type)
    {
        System.out.printf("What is the date of this %s (yyyy-MM-dd): ", type);
        String dateInput = myScanner.nextLine();                      //Get User dateInput
        LocalDate localDate = LocalDate.parse(dateInput);             //Convert String to LocalDate object

        System.out.printf("What is the time of this %s (hh:mm:ss): ", type);
        String timeInput = myScanner.nextLine();                       //Get User timeInput

        LocalTime localTime = LocalTime.parse(timeInput);              //Convert String to LocalTime object

        System.out.printf("What is the description of the %s: ", type);
        String description = myScanner.nextLine();                     //Get User Description

        System.out.println("Who is the vendor: ");
        String vendor = myScanner.nextLine();                          //Get User Vendor

        System.out.println("What is the amount: ");
        double amount = Double.parseDouble(myScanner.nextLine());      //Get amount from user and parse String as Double
        if (type.equals("payment")) amount *= -1;       //If it is a payment then multiply -1

        try
        {
            FileWriter writer = new FileWriter("src/main/resources/transactions.csv", true); //Appending is allowed
            Transaction transaction = new Transaction(localDate, localTime, description, vendor, amount);
            transactions.add(0, transaction);    //appending transactions at the beginning of the arrayList

            String stringTransaction = transaction.displayTransaction();
            writer.append(stringTransaction);          //Append transactions to the transactions.csvw
            writer.close();
        } catch (IOException e)
        {
            System.err.println("Couldnt write to file tranactions.csv");
        }
    }

    /**
     *
     */
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
            /*
             *  5 Cases: A = All entries      => displayTransactions field = "all"
             *           D = Display Deposit  => displayTransactions field = "deposit"
             *           P = Display Payment  => displayTransactions field = "payment"
             *           R = Display Report   => displayReportsScreen()
             *           H = Go Home          => break
             */
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
                    repeat = false;    // Break out of while loop
                    break;
            }
        }
    }

    /**
     * dispaly transactions based on the type
     * All : Every transaction in the CSV file
     * Deposit: Only Transactions with a positive amount
     * Payment: Only Transactions with a negative amount
     * @param type
     */
    private static void displayTransactions(String type)
    {
        for (Transaction t: transactions)
        {
            if (type.equals("all")) System.out.println(t.displayTransaction());
            else if (type.equals("deposit") && t.getAmount() > 0) System.out.println(t.displayTransaction());
            else if (type.equals("payment") && t.getAmount() < 0) System.out.println(t.displayTransaction());
        }
    }

    /**
     * Display A Reports Screen with transactions from
     * The start of the month to the current date
     * The start of the previous month to the end of the previous month
     * The Start of the Year to the current date
     * The start of the previous year to the end of the previous year
     * BONUS: Custom Search
     */
    private static void displayReportsScreen()
    {
        localDate = LocalDate.now();  //Get local search
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

            /*
             *  7 Cases: 1 = Month to Date       => monthToDate()
             *           2 = Previous Month      => previousMonth()
             *           3 = Year to Date        => yearToDate()
             *           4 = Previous Year       => previousYear()
             *           5 = Search by Vendor    => searchByVendor()
             *           6 = Bonus Custom Filter => applyCustomFilter() and print out list of transactions
             *           0 = Go back to Ledger   => break
             */
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
                    ArrayList<Transaction> filteredTransactions = applyCustomFilters(transactions); //return ArrayList of filtered Transaction objects
                    for (Transaction t: filteredTransactions)
                    {
                        System.out.println(t.displayTransaction());   //Print out transactions after custom filter
                    }
                    break;
                case "0":
                    repeat = false;  //Break out of while loop, Go back to Ledger
                    break;
            }
        }
    }

    /**
     * If the current month and year are the same as the current transaction as well as
     * The transaction day is before the current day then output that transaction
     */
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

    /**
     * Displays the transactions previous month even if the previous month goes to the prior year
     */
    private static void previousMonth()
    {
        //initialize Year and Month to keep track of edge case
        int year;
        int month;
        //If the month is January then set month to december and year to the previous year
        if(localDate.getMonthValue() == 1)
        {
            year = localDate.getYear() - 1 ;
            month = 12;
        }
        else  //Otherwise keep the same year and decrement the month value
        {
            year = localDate.getYear();
            month = localDate.getMonthValue() - 1;
        }


        for(Transaction t: transactions)
        {
            //If the month equals the previous month and the year equals the correct year display transactions
            if(t.getDate().getMonthValue() == month && t.getDate().getYear() == year)
            {
                System.out.println(t.displayTransaction());
            }
        }
    }

    /**
     * Displays the transactions starting of the year to current date
     */
    private static void yearToDate()
    {
        for(Transaction t: transactions)
        {
            if(t.getDate().getMonthValue() <= localDate.getMonthValue() && t.getDate().getDayOfMonth() <= localDate.getDayOfMonth() && t.getDate().getYear() == localDate.getYear())
            {
                System.out.println(t.displayTransaction()); //Display all transactions before the current date
            }
        }
    }

    /**
     * Displays all transactions from the previous year
     */
    private static void previousYear()
    {
        for(Transaction t: transactions)
        {
            if(t.getDate().getYear() == localDate.getYear() - 1) //Current Year - 1
            {
                System.out.println(t.displayTransaction());
            }
        }
    }

    /**
     * Displays Vendors equals User specified string
     */
    private static void searchByVendor()
    {
        System.out.println("What vendor are you searching for? ");
        String vendorName = myScanner.nextLine();
        for(Transaction t: transactions)
        {
            if(t.getVendor().equals(vendorName))   //If the transactions vendor equals the specificed user vendor name display transactions
            {
                System.out.println(t.displayTransaction());
            }
        }
    }

    /**
     * Runs filterByDates, description, vendor and amount. User can make the filters empty to return the original list
     * @param transactions
     * @return
     */
    public static ArrayList<Transaction> applyCustomFilters(ArrayList<Transaction> transactions)
    {
        ArrayList<Transaction> results = filterByDates(transactions); //Stores filtered dates into results variable
        results = filterByDescription(results);                       //Stores filtered description into results variable
        results = filterByVendor(results);                            //Stores filtered vendor into results variable
        results = filterByAmount(results);                            //Stores filtered amount into results variable

        return  results;                                              //Returns a ArrayList of the filtered result
    }

    /**
     * Filter By Dates chooses a start and end date and stores them as a LocalDate to compare with transactions date.
     * Allows either or both user inputs to be empty.
     * @param transactions
     * @return
     */
    private static ArrayList<Transaction> filterByDates(ArrayList<Transaction> transactions)
    {
        ArrayList<Transaction> transactionsByDate = new ArrayList<>();     //Create new ArrayList

        System.out.println("What is the start date: ");
        String startDate = myScanner.nextLine();

        System.out.println("What is the end date: ");
        String endDate = myScanner.nextLine();

        LocalDate startSelection = null;
        LocalDate endSelection = null;
        if (!startDate.isEmpty()) {startSelection = LocalDate.parse(startDate);} //If user inputted start date then parse it to become a LocalDate
        if(!endDate.isEmpty()) { endSelection = LocalDate.parse(endDate);}       //If user inputted end date then parse it to become a LocalDate

        for (Transaction t: transactions)
        {
            boolean afterStart = (startSelection == null || t.getDate().isAfter(startSelection));  //Check if the transaction happened after the start date
            boolean afterEnd = (endSelection == null || t.getDate().isBefore(endSelection));       //Check if the transaction happened before the end date
            if(afterStart && afterEnd) // Both conditions have to be fulfilled before the transactions are appended to the ArrayList
            {
                transactionsByDate.add(t);
                //System.out.println(t.displayTransaction());
            }
        }
        return transactionsByDate;

    }

    /**
     * Filter By Description, if the user input contained the transactions description based on
     * the transactions ArrayList, add the element to a new ArrayList
     * @param transactions
     * @return
     */
    private static ArrayList<Transaction> filterByDescription(ArrayList<Transaction> transactions)
    {
        ArrayList<Transaction> transactionsByDescription = new ArrayList<>(); //Create new ArrayList

        System.out.println("What is the description: ");
        String description = myScanner.nextLine();

        for (Transaction t: transactions)
        {
            if(t.getDescription().contains(description))         //If the transaction contains the description append to new ArrayList
            {
                transactionsByDescription.add(t);
            }
        }
        return transactionsByDescription;
    }

    /**
     * Filter By Vendor if the user input contained the transactions vendor based on
     * the transactions ArrayList, add the element to a new ArrayList
     * @param transactions
     * @return
     */

    private static ArrayList<Transaction> filterByVendor(ArrayList<Transaction> transactions)
    {
        ArrayList<Transaction> transactionsByVendor = new ArrayList<>(); //Create new ArrayList

        System.out.println("Who is the vendor: ");
        String vendor = myScanner.nextLine();

        for (Transaction t: transactions)
        {
            if(t.getVendor().contains(vendor))       //If the transaction contains the vendor append to new ArrayList
            {
                transactionsByVendor.add(t);
            }
        }
        return transactionsByVendor;
    }

    /**
     * Filter By Amount checks the minimum value and maximum value that the user inputs. Either field or both could be empty.
     * Returns an ArrayList between the lower and upper bounded values of the Transactions
     * @param transactions
     * @return
     */
    private static ArrayList<Transaction> filterByAmount(ArrayList<Transaction> transactions)
    {
        ArrayList<Transaction> transactionsByAmount = new ArrayList<>();  //Create new array

        double minAmount = -Double.MAX_VALUE;       //Initialized maximum negative value
        double maxAmount = Double.MAX_VALUE;        //Initialized maximum postivie value

        System.out.println("What is the minimum amount: ");
        String minimumAmount = myScanner.nextLine();
        if (!minimumAmount.isEmpty()) minAmount = Double.parseDouble(minimumAmount);    // If the user added a min convert into double

        System.out.println("What is the maximum amount: ");
        String maximumAmount = myScanner.nextLine();
        if (!maximumAmount.isEmpty()) maxAmount = Double.parseDouble(maximumAmount);    // If the user added a max convert into double

        for (Transaction t: transactions)
        {
            if(t.getAmount() >= minAmount && t.getAmount() <= maxAmount)       //Compare the bounds of max and min to the transaction amount
            {
                transactionsByAmount.add(t);      //Append to the new ArrayList
            }
        }
        return transactionsByAmount;
    }
}
