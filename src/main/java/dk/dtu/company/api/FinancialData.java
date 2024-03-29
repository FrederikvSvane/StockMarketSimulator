package dk.dtu.company.api;

public class FinancialData {
    private long revenue;
    private long costOfRevenue;
    private long grossProfit;
    private long cash;
    private long netReceiveable;
    private long totalAssets;
    private long totalDebt;

    // Constructor

    public FinancialData(long cash, long netReceiveable, long totalAssets, long totalDebt) {
        this.cash = cash;
        this.netReceiveable = netReceiveable;
        this.totalAssets = totalAssets;
        this.totalDebt = totalDebt;
    }
    public FinancialData(long revenue, long costOfRevenue, long grossProfit) {
        this.revenue = revenue;
        this.costOfRevenue = costOfRevenue;
        this.grossProfit = grossProfit;
    }

    public FinancialData(long newRevenue, long newCost, long newGrossProfit, long newAssets, long newLiabilities, long newEquity, long newCash) {
        this.revenue = newRevenue;
        this.costOfRevenue = newCost;
        this.grossProfit = newGrossProfit;
        this.totalAssets = newAssets;
        this.totalDebt = newLiabilities;
        this.netReceiveable = newEquity;
        this.cash = newCash;
    }


    // Getters
    public long getRevenue() {
        return revenue;
    }

    public long getCostOfRevenue() {
        return costOfRevenue;
    }

    public long getGrossProfit() {
        return grossProfit;
    }
    public long getCash() {return cash;}
    public long getNetReceiveable() {return netReceiveable;}
    public long getTotalAssets() {return totalAssets;}
    public long getTotalDebt() {return totalDebt;}


    // toString method for easy printing
    @Override
    public String toString() {
        return "Revenue: " + revenue + ", Cost of Revenue: " + costOfRevenue + ", Gross Profit: " + grossProfit;
    }

}
