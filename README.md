# Expense Tracker App

A simple Java Swing desktop application for tracking personal expenses and managing digital wallet balances.

## Features

- Add expenses with category, description, amount, and date
- View expenses sorted by date
- Add custom expense categories
- Manage bKash and nagad account balances
- Save categories and expense data locally

## Project Structure

```text
src/expensetracker/
├── BankAccount.java
├── Expense.java
├── ExpenseTracker.java
└── ExpenseTrackerApplication.java
```

## How to Run

Compile the Java files:

```bash
javac -d out src/expensetracker/*.java
```

Run the application:

```bash
java -cp out expensetracker.ExpenseTrackerApplication
```

## Data Files

- `categories.txt` stores expense categories
- `expenses.dat` stores saved expense and account data

## Technologies Used

- Java
- Java Swing
- File serialization
