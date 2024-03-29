package dk.dtu.company;

import dk.dtu.host.GlobalClock;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.jspace.ActualField;
import org.jspace.Space;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

/**
 * Company whose fundamentals are based off stochastic processes or directly probability distributions
 */
public class StochasticCompany extends Company {

    public StochasticCompany(String companyName, String companyTicker, LocalDateTime ipoDateTime, Space fundamentalsSpace) {
        super(companyName, companyTicker, ipoDateTime, fundamentalsSpace);


        //A normal distribution with a 95% C.I between [-6,6]
        NormalDistribution normalDistribution = new NormalDistribution(0, 3);
        int deltaMonth = (int) normalDistribution.sample();
        if (deltaMonth < 0) {
            //System.out.println("Changing ipoDateTime from " + ipoDateTime + " to " + ipoDateTime.minusMonths(deltaMonth));
            ipoDateTime = ipoDateTime.minusMonths(deltaMonth);
            //System.out.println("Changed ipoDateTime to " + ipoDateTime);
        } else {
            //System.out.println("Changing ipoDateTime from " + ipoDateTime + " to " + ipoDateTime.plusMonths(deltaMonth));
            ipoDateTime = ipoDateTime.plusMonths(deltaMonth);
            //System.out.println("Changed ipoDateTime to " + ipoDateTime);
        }

    }

    @Override
    public void updateFundamentalData(LocalDateTime ingameDate) throws InterruptedException {
        try {
            //If we've already published fundamentals for this date, then we just need to update
            if (isPubliclyTraded) {
                NormalDistribution growthDetermination = new NormalDistribution(0.2, 0.2);
                List<Object[]> previousFundamentals = getFundamentalsFromSpace(this.companyTicker);
                if (previousFundamentals.size() == 0) {
                    //System.out.println("Company " + this.companyTicker + " is not publicly traded yet, so it cannot update its fundamentals");
                    fundamentalsSpace.get(new ActualField("readTicket"));
                    NormalDistribution X = new NormalDistribution(100, 10);
                    putFundamentals(companyTicker, GlobalClock.getIRLDateTimeNow(), GlobalClock.getSimulatedDateTimeNow(), "income statement", "revenue", (float) X.sample());
                } else {
                    float previousRevenue = (float) previousFundamentals.get(0)[5];
                    float revenueGrowth = (float) (previousRevenue * growthDetermination.sample());
                    float newRevenue = revenueGrowth + previousRevenue;
                    //System.out.println("We're updating the fundamentals for " + this.companyTicker + " from " + previousRevenue + " to " + newRevenue + " on " + GlobalCock.getSimulatedDateTimeNow());
                    putFundamentals(companyTicker, GlobalClock.getIRLDateTimeNow(), GlobalClock.getSimulatedDateTimeNow(), "income statement", "revenue", newRevenue);
                }
            } else {
                //System.out.println("Company " + this.companyTicker + " is not publicly traded yet, so it cannot update its fundamentals");
                fundamentalsSpace.get(new ActualField("readTicket"));
                NormalDistribution X = new NormalDistribution(100, 10);
                putFundamentals(companyTicker, GlobalClock.getIRLDateTimeNow(), GlobalClock.getSimulatedDateTimeNow(), "income statement", "revenue", (float) X.sample());
            }
        } catch (
                InterruptedException e) {
            System.out.println("Error in updateFundamentalData");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}