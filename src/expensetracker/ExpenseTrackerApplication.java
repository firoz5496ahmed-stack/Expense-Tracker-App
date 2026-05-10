package expensetracker;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ExpenseTrackerApplication {
    private JFrame frame;
    private ExpenseTracker tracker;

    public ExpenseTrackerApplication() {
        tracker = new ExpenseTracker();
        loadCategories();
        loadExpenses();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JLabel bkashLabel = new JLabel("bKash Balance: " + tracker.getBkashAccount().getBalance());
        JLabel nagadLabel = new JLabel("nagad Balance: " + tracker.getNagadAccount().getBalance());
        topPanel.add(bkashLabel);
        topPanel.add(nagadLabel);

        // Center Panel
        JPanel centerPanel = new JPanel(new GridLayout(4, 2));
        JButton addExpenseButton = new JButton("Add Expense");
        JButton viewExpensesButton = new JButton("View Expenses");
        JButton manageBkashButton = new JButton("Manage bKash");
        JButton manageNagadButton = new JButton("Manage nagad");

        centerPanel.add(addExpenseButton);
        centerPanel.add(viewExpensesButton);
        centerPanel.add(manageBkashButton);
        centerPanel.add(manageNagadButton);

        // Button Actions
        addExpenseButton.addActionListener(e -> showAddExpenseDialog());
        viewExpensesButton.addActionListener(e -> showExpenses());
        manageBkashButton.addActionListener(e -> showManageAccountDialog(tracker.getBkashAccount(), bkashLabel));
        manageNagadButton.addActionListener(e -> showManageAccountDialog(tracker.getNagadAccount(), nagadLabel));

        // Adding panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void showAddExpenseDialog() {
        JDialog dialog = new JDialog(frame, "Add Expense", true);
        dialog.setLayout(new GridLayout(6, 2));
        dialog.setSize(400, 300);

        JLabel categoryLabel = new JLabel("Category:");
        JComboBox<String> categoryDropdown = new JComboBox<>(tracker.getCategories().toArray(new String[0]));
        JButton addCategoryButton = new JButton("Add Category");

        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();
        JLabel dateLabel = new JLabel("Date (DD-MM-YY):");
        JTextField dateField = new JTextField();

        JButton addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(e -> {
            try {
                String category = (String) categoryDropdown.getSelectedItem();
                String description = descriptionField.getText();
                double amount = Double.parseDouble(amountField.getText());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
                LocalDate date = LocalDate.parse(dateField.getText(), formatter);
                tracker.addExpense(category, description, amount, date);
                saveExpenses();
                JOptionPane.showMessageDialog(dialog, "Expense added!");
                dialog.dispose();
            } catch (NumberFormatException | DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input! Please check your fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addCategoryButton.addActionListener(e -> {
            String newCategory = JOptionPane.showInputDialog(dialog, "Enter new category:");
            if (newCategory != null && !newCategory.trim().isEmpty()) {
                tracker.addCategory(newCategory.trim());
                categoryDropdown.addItem(newCategory.trim());
                saveCategories();
                JOptionPane.showMessageDialog(dialog, "Category added!");
            } else {
                JOptionPane.showMessageDialog(dialog, "Category name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(categoryLabel);
        dialog.add(categoryDropdown);
        dialog.add(new JLabel());
        dialog.add(addCategoryButton);
        dialog.add(descriptionLabel);
        dialog.add(descriptionField);
        dialog.add(amountLabel);
        dialog.add(amountField);
        dialog.add(dateLabel);
        dialog.add(dateField);
        dialog.add(new JLabel());
        dialog.add(addExpenseButton);

        dialog.setVisible(true);
    }

    private void showExpenses() {
        JDialog dialog = new JDialog(frame, "Expenses", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 400);

        JTextArea expenseList = new JTextArea();
        expenseList.setEditable(false);
        StringBuilder expensesText = new StringBuilder("Date       | Category     | Description           | Amount\n");
        expensesText.append("-----------------------------------------------------------\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");

        for (Expense expense : tracker.getExpensesSortedByDate()) {
            expensesText.append(String.format("%-10s | %-12s | %-20s | %10.2f\n",
                    expense.getDate().format(formatter), expense.getCategory(), expense.getDescription(), expense.getAmount()));
        }

        expenseList.setText(expensesText.toString());
        dialog.add(new JScrollPane(expenseList), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showManageAccountDialog(BankAccount account, JLabel balanceLabel) {
        JDialog dialog = new JDialog(frame, "Manage Account", true);
        dialog.setLayout(new GridLayout(4, 2));
        dialog.setSize(300, 200);

        JLabel currentBalanceLabel = new JLabel("Current Balance: " + account.getBalance());
        JTextField amountField = new JTextField();
        JButton addButton = new JButton("Add Money");
        JButton withdrawButton = new JButton("Withdraw Money");

        addButton.addActionListener(e -> {
            try {
                int amount = Integer.parseInt(amountField.getText()); // Parse the integer
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Invalid amount! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                account.addMoney(amount);
                balanceLabel.setText("Current Balance: " + account.getBalance());
                saveExpenses();
                dialog.dispose(); // Close the dialog after successful transaction
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid amount! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        withdrawButton.addActionListener(e -> {
            try {
                int amount = Integer.parseInt(amountField.getText()); // Parse the integer
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Invalid amount! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                account.withdrawMoney(amount);
                balanceLabel.setText("Current Balance: " + account.getBalance());
                saveExpenses();
                dialog.dispose(); // Close the dialog after successful transaction
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid amount! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(currentBalanceLabel);
        dialog.add(new JLabel());  // Empty label for layout
        dialog.add(new JLabel("Amount:"));
        dialog.add(amountField);
        dialog.add(addButton);
        dialog.add(withdrawButton);

        dialog.setVisible(true);
    }


    private void loadCategories() {
        try {
            Files.lines(Paths.get("categories.txt")).forEach(tracker::addCategory);
        } catch (IOException e) {
            System.out.println("No categories file found, starting with empty categories.");
        }
    }

    private void loadExpenses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("expenses.dat"))) {
            tracker = (ExpenseTracker) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No expense data found, starting with empty expenses.");
        }
    }

    private void saveCategories() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("categories.txt"))) {
            for (String category : tracker.getCategories()) {
                writer.write(category);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveExpenses() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("expenses.dat"))) {
            oos.writeObject(tracker);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseTrackerApplication());
    }
}

