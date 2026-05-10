package expensetracker;

import java.io.*;
import java.time.LocalDate;

class Expense implements Serializable {
    private String category;
    private String description;
    private double amount;
    private LocalDate date;

    public Expense(String category, String description, double amount, LocalDate date) {
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-20s %-10.2f %s", category, description, amount, date);
    }
}
