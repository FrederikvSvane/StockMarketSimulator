package dk.dtu.company;

import dk.dtu.client.ClientUtil;
import dk.dtu.client.Order;
import dk.dtu.client.broker.Broker;
import dk.dtu.host.HostUtil;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;

public class CompanyBroker extends Broker implements Runnable {
    public CompanyBroker() throws IOException {
        super();
    }

    public void run() {
        while (true) {
            try {
                //TODO find en bedre måde at nedarve requestSpace fra Broker.java
                //TODO lyt på strukturen af tuplen på en bedre måde, fordi company ikke nødvendigvis kommer med, hvis det er en salg/købs order
                Object[] request = super.getRequestSpace().get(new FormalField(String.class) /* orderType */, new FormalField(String.class) /* companyId */, new FormalField(String.class) /* companyName */, new FormalField(String.class) /* companyTicker */, new FormalField(Order.class));
                String orderType = request[0].toString();
                String companyID = (String) request[1];
                String companyName = (String) request[2];
                String companyTicker = (String) request[3];
                Order order = (Order) request[4];
                int amount = order.getAmount();
                float price = order.getPrice();

                if (orderType.equals("IPO")) {
                    //String uri = ClientUtil.getHostUri("exchangeRequestSpace");  //TODO den skal have et rigtig room navn
                    int port = HostUtil.getExchangePort();
                    String uriConnection = ClientUtil.getHostUri("exchangeRequestSpace", port, "keep");
                    Space exchangeRequestSpace = new RemoteSpace(uriConnection);
                    System.out.println("IPO request sent to exchange" + amount + " " + price);
                    exchangeRequestSpace.put(order.getOrderId(), orderType, companyID,companyName, companyTicker, amount, price); //TODO måske skal order bare sendes videre?
                    return;
                } else if (orderType.equals("buy")) {
                    //TODO send buy request to exchange
                    return;
                } else if (orderType.equals("sell")) {
                    //TODO send sell request to exchange
                    return;
                }


            } catch (Exception e) {
                throw new RuntimeException("Error in company broker"+e);
            }
        }
    }

    @Override
    public Space getRequestSpace() { return super.getRequestSpace(); }
}
