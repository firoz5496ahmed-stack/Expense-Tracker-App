package expensetracker;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExpenseTracker implements Serializable {
    private List<String> categories;
    private List<Expense> expenses;
    private BankAccount bkashAccount;
    private BankAccount nagadAccount;

    public ExpenseTracker() {
        categories = new ArrayList<>();
        expenses = new ArrayList<>();
        bkashAccount = new BankAccount();
        nagadAccount = new BankAccount();
    }

    public List<String> getCategories() {
        return categories;
    }

    public void addCategory(String category) {
        categories.add(category);
    }

    public void addExpense(String category, String description, double amount, LocalDate date) {
        expenses.add(new Expense(category, description, amount, date));
    }

    public List<Expense> getExpensesSortedByDate() {
        expenses.sort(Comparator.comparing(Expense::getDate));
        return expenses;
    }

    public BankAccount getBkashAccount() {
        return bkashAccount;
    }

    public BankAccount getNagadAccount() {
        return nagadAccount;
    }
}
