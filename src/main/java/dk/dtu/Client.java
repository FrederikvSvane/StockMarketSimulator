package dk.dtu;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    int amountOfTraders = 1;
     static String lobbyToTraderName = "lobbyToTrader";
     static String traderToLobbyName = "traderToLobby";

    public static void main(String[] args) throws IOException {
        Trader trader = new Trader("localhost", 32991);
        new Thread(trader).start();
    }
}
