package dk.dtu.client.datafetcher;

import dk.dtu.company.Company;
import dk.dtu.company.StochasticCompany;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

import java.util.List;
import java.util.Queue;

public class PriceGraphDataFetcher extends DataFetcher implements Runnable {

    public PriceGraphDataFetcher(Space traderDataSpace) {
        super(traderDataSpace, 10000, "companiesAndPriceHistorySpace");
    }

    public void run() {
        // Tilkobl til Host data space
        try {
            connectToDataSpace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        while (true) {
            // Hent data fra Host data space
            List<Object[]> companyData;
            try {
                // Få data fra companyDataSpace
                companyData = QueryAllCompanies();
                // Vent på der er ticket fra traderDataSpace og tag den
                traderDataSpace.get(new ActualField("ticket"));
                // Fjern alle data fra traderDataSpace
                traderDataSpace.getAll(new FormalField(String.class), new FormalField(String.class), new FormalField(Float.class));
                updateCompanyData(companyData);
                // Indsæt ticket i traderDataSpace
                traderDataSpace.put("ticket");

                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    void updateCompanyData(List<Object[]> companyData) throws InterruptedException {
        for (Object[] companyList : companyData) {
            // extract data from company
            String companyName = (String) companyList[1];
            String companyTicker = (String) companyList[2];
            // Queue<Object[]> priceHistory = (Queue<Object[]>) companyList[3];
            float price = (float) companyList[3];
            // add price history to traderDataSpace
            traderDataSpace.put(companyName, companyTicker, price);
        }
    }
}