package com.portfolio.budgetcraft;

import java.io.IOException;
import java.nio.file.*;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/** CSV expense analyzer: an intentionally dependency-free portfolio project. */
public class BudgetCraft {
    record Expense(YearMonth month, String category, double amount, String note) {}
    public static void main(String[] args) throws IOException {
        if (args.length < 2) { System.out.println("Usage: BudgetCraft <expenses.csv> <monthly-budget>"); return; }
        double limit = Double.parseDouble(args[1]);
        List<Expense> expenses = Files.readAllLines(Path.of(args[0])).stream().skip(1).filter(s -> !s.isBlank()).map(BudgetCraft::parse).toList();
        double total = expenses.stream().mapToDouble(Expense::amount).sum();
        System.out.printf("%nBudgetCraft report%nTotal spending: ₹%,.2f%nBudget: ₹%,.2f%nRemaining: ₹%,.2f%n%n", total, limit, limit-total);
        Map<String, Double> byCategory = expenses.stream().collect(Collectors.groupingBy(Expense::category, TreeMap::new, Collectors.summingDouble(Expense::amount)));
        System.out.println("Spending by category:");
        byCategory.forEach((category, amount) -> System.out.printf("%-18s ₹%,10.2f  %5.1f%%%n", category, amount, total == 0 ? 0 : amount / total * 100));
        Map<YearMonth, Double> byMonth = expenses.stream().collect(Collectors.groupingBy(Expense::month, TreeMap::new, Collectors.summingDouble(Expense::amount)));
        System.out.println("\nMonthly trend:");
        byMonth.forEach((month, amount) -> System.out.printf("%s  ₹%,.2f%s%n", month, amount, amount > limit ? "  ⚠ over budget" : ""));
    }
    private static Expense parse(String line) {
        String[] p = line.split(",", 4);
        if (p.length < 4) throw new IllegalArgumentException("Invalid CSV row: " + line);
        return new Expense(YearMonth.parse(p[0].substring(0, 7)), p[1].trim(), Double.parseDouble(p[2]), p[3].trim());
    }
}
