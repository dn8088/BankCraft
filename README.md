# BudgetCraft

A personal-finance CLI that imports expenses from CSV and turns them into a useful spending report. It uses Java streams for aggregation and flags categories that exceed a monthly limit.

## Run

```bash
javac -d out src/main/java/com/portfolio/budgetcraft/BudgetCraft.java
java -cp out com.portfolio.budgetcraft.BudgetCraft expenses.csv 5000
```

CSV columns: `date,category,amount,note` — for example `2026-09-01,Food,280,Lunch`.
