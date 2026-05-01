# 💰Money Bag Inc Account App 💰

## Description
This is a simple CLI app that allows users to add transactions, searching deposits and payments, and custom searching.
Each transaction is saved to transactions.csv and the filters are outputted to the CLI

## Running the Code
Ideally run the Main.java class in IntelliJ 

## The Code I'm Most Proud of
I am proud of the filterByDates() function as that was the funnest function to think about how to implement. 
I first checked if the user inputted a string for both the start and end date and when they did not I would 
not assign a value and let the default value stay null. 

I also liked how i broke up the code so that while going through each transaction in the for each loop the 
if statement is easy to read and follow

```java
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
```

## My Personal Challenges
Just giving myself enough time to clearly plan out what date types I am going to use as well as how I am going to structure this program to uphold the spirit of becoming an efficient (aka lazy) programmer. 

DNR: Do Not Repeat 

I was trying to fulfill this mantra by making sure that a lot of the code is in smaller methods to increase reusability

For the function previousMonth() I thought of the edge case that the previous month would loop to a previous year, I had originally implemented my solution and talked to my instructor. He noticed a bug where I had not fully addressed the problem. After this feedback I went back and came up with a satisfactory solution.

## Next Time...
I would love to add more color to my program and a little more personality. 

I heard a suggestion from another student to add a log-in and password so unauthorized users cannot edit the transactions.csv file.