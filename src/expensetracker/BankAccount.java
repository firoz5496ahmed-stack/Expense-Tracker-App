package expensetracker;

import javax.swing.*;
import java.io.Serializable;

public class BankAccount implements Serializable {
    private double balance;

    public double getBalance() {
        return balance;
    }

    public void addMoney(double amount) {
        balance += amount;
    }

    public void withdrawMoney(double amount) {
        if (balance >= amount) {
            balance -= amount;
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient balance!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
