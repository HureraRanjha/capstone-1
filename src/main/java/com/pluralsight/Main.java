package com.pluralsight;

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
                    System.out.println("Deposit");
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
}
