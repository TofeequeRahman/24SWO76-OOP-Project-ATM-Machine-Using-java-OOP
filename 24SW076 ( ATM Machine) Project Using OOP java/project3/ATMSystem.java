
import java.util.*;

// Inheritance: Customer inherits from Person
class Person {

    protected String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// Customer inherits from Person
// Composition: Customer HAS an Account
class Customer extends Person {

    private Account account;

    public Customer(String name, double initialBalance) {
        super(name); // calling Person constructor
        this.account = new Account(initialBalance);
    }

    public Account getAccount() {
        return account;
    }
}

// Account class with synchronized methods for thread safety
class Account {

    private double balance;

    public Account(double balance) {
        this.balance = balance;
    }

    public synchronized void deposit(double amount) {
        balance += amount;
        System.out.println(" deposits Money ->>> " + amount);
        displayBalance();
    }

    public synchronized void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println(" << Withdrawn Is >> " + amount);
        } else {
            System.out.println(" Hwre is an Insufficient balance In YOUR Account  !!!!!");
        }
        displayBalance();
    }

    public void displayBalance() {
        System.out.println(" *******Current Balance******* IS of YOu " + balance);
    }
}

// Aggregation: ATM uses Customer, but Customer can exist independently
class ATM {

    public void performTransaction(Customer customer, String type, double amount) {
        switch (type.toLowerCase()) {
            case "deposit":
                customer.getAccount().deposit(amount);
                break;
            case "withdraw":
                customer.getAccount().withdraw(amount);
                break;
            default:
                System.out.println("  Invalid transaction type....");
        }
    }
}

// Threading: Each transaction handled in its own thread


class TransactionThread extends Thread {
    private Customer customer;
    private String type;
    private double amount;

    public TransactionThread(Customer customer, String type, double amount) {
        this.customer = customer;
        this.type = type;
        this.amount = amount;
    }

    @Override
    public void run() {
        System.out.println("Transaction Started: " + type + " for " + customer.getName());
        ATM atm = new ATM();
        atm.performTransaction(customer, type, amount);
        System.out.println(" HERE *Transaction Completed *" + type + " for " + customer.getName());
    }
}

// Main class
public class ATMSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Customer customer = new Customer("*** Tofeeque Rahman ***", 1000);
        ATM atm = new ATM();

        System.out.println("*** YOU can NOW use the an ATM ***");

        boolean exit = false;

        while (!exit) {

            System.out.println("\n  Enter For the Transction YOU wnat !! (deposit/withdraw/exit): ");
            String type = scanner.nextLine();

            if (type.equalsIgnoreCase("exit")) {
                exit = true;
                break;
            }

            System.out.println("!!.....Enter amount......!!: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // consume newline

            TransactionThread transaction = new TransactionThread(customer, type, amount);
            transaction.start();

            try {
                transaction.join();
            } catch (InterruptedException e) {
                System.out.println("....Transaction interrupted...");
            }
        }

        System.out.println("********* Thank you for using the ATM *********");
        System.out.println("********* Have a nice day *********");
        scanner.close();
    }
}
