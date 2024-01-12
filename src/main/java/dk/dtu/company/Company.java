package dk.dtu.company;

import java.io.IOException;
import java.util.UUID;
import dk.dtu.Broker;
import dk.dtu.ClientUtil;
import dk.dtu.CompanyBroker;
import dk.dtu.Order;
import jdk.jshell.spi.ExecutionControl;
import org.jspace.RemoteSpace;
import org.jspace.Space;
import org.jspace.SpaceRepository;

import javax.security.auth.login.LoginException;



public abstract class Company implements Runnable{
    protected final String companyId;
    protected final String companyName;
    protected final String companyTicker;
    protected final int ipoYear;

    //Shares outstanding -> Shares in public circulation on the stock exchange.
    private int sharesOutstanding;

    //The total amount of shares at the time this company IPO'ed
    private final int totalNrShares;

    protected final Space fundamentalsSpace;

    public Company(String companyName, String companyTicker,int ipoYear, Space fundamentalsSpace) {

        this.companyId = UUID.randomUUID().toString();
        this.companyName = companyName;
        this.companyTicker = companyTicker;
        this.ipoYear = ipoYear;
        this.totalNrShares = calculateTotalNrShares();
        this.fundamentalsSpace = fundamentalsSpace;

    }

    @Override
    public void run() {

        int ingameDateDummy = -2000;

        try {

            //First things first; we gotta IPO
            if(isTimeToIPO(ipoYear,ingameDateDummy))
            {
                //Calculate fundamentals and push them to fundamentals space
                updateFundamentalData(ingameDateDummy);

                //Then we IPO!!!
                int IPOFloating = this.getIPOSharesFloated();
                float IPOSharePrice = this.calculateIPOPrice();
                this.sharesOutstanding = getIPOSharesFloated();
                Order IPO = makeOrder(IPOFloating, IPOSharePrice);
                sendRequestToCompanyBroker("IPO", IPO);

            }

            //TODO: Create an object that follows the Petri Net I (Benjamin) designed and continuously ensures that the fundamentals data is updated

            //Then we look at the market, I s'pose m'lord?

        } catch (InterruptedException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException(e);
        }
    }


    private void sendRequestToCompanyBroker(String orderType, Order order) throws InterruptedException {
        CompanyBroker companyBroker = new CompanyBroker();
        new Thread(companyBroker).start();
        companyBroker.getRequestSpace().put(orderType, this, order);
/*
        try {
            if (orderType.equals("IPO")) {
                sendIpoRequestToExchange(companyBroker, order);
            } else if (orderType.equals("buy")) {
                sendBuyOrder(companyId, companyBroker, order);
            } else if (orderType.equals("sell")) {
                sendSellOrder(companyId, companyBroker, order);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
 */
    }

    private void sendIpoRequestToExchange(CompanyBroker companyBroker, Order order) throws IOException, InterruptedException {
        String uri = ClientUtil.getHostUri("exchangeRequestSpace");  //TODO den skal have et rigtig room navn
        String uriConnection = ClientUtil.setConnectType(uri,"keep");
        //TODO er keep den rigtige forbindelse her? Og hvad med alle andre steder? STOR TODO
        Space exchangeRequestSpace = new RemoteSpace(uriConnection);
        exchangeRequestSpace.put(order.getOrderId());

    }

    private void sendBuyOrder(String companyId, CompanyBroker companyBroker, Order order) {
        //TODO samme logik som i Trader.java
    }

    private void sendSellOrder(String companyId, CompanyBroker companyBroker, Order order) {
        //TODO samme logik som i Trader.java
    }

    private Order makeOrder(int amount, float price) {
        return new Order(companyId, companyName,companyTicker, amount, price);
    }

    //Total numbers of shares at IPO
    abstract int calculateTotalNrShares();

    //Number of shares floated (offered publicly) at IPO
    abstract int getIPOSharesFloated();

    abstract float calculateIPOPrice();

    //Is it time for IPO?
    abstract boolean isTimeToIPO(int ipoYear, int ingameDate);


    public String getCompanyName() { return companyName; }
    public String getCompanyTicker() { return companyTicker; }
    public String getCompanyId() { return companyId; }

    public int getIpoYear(){ return ipoYear;}

    public abstract void updateFundamentalData(int ingameDate);

    public abstract float getFundamentalData(String financialPost);


    public int getSharesOutstanding() {
        return sharesOutstanding;
    }

    public int getTotalNrShares() {
        return totalNrShares;
    }


}

//TODO: Add a CompanyFundamentals class which can standardize the way we update the fundamentals