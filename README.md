# A distributed stock market simulation using tuple spaces

Welcome to our Stock Market Simulation project.
It's a fun and interactive way to get a feel for basic stock trading, learn about coordination in distributed applications, or continue building upon.

## Key features
Real-World Data: Real time financial data from actual companies is wired into the program through API calls.

Banking and Trading: There's a simple banking system in place. You can manage your virtual money, buy and sell stocks, and see how well you can do in the market.

Automated Trading Bots: To spice things up, we've added trading bots. They follow basic trading strategies, so you can see how you fare against them.

Live Updates: The stock prices update in real-time, so there's always something new happening.

Chat with Peers: Jump into public or private chat rooms to discuss strategies or just chat about the market.

### Quick Start
Before running the program: you'll need to edit configuration and add VM options under "modify options", and add the following line:
`--add-opens java.base/java.time=ALL-UNNAMED` (For the clock to work properly)

Set Up: Start by setting up your host server. In the host class you can choose either stochastic or realistic data. Realistic data is using real life data using an API, whereas stochastic are randomized data. Then run the host.

Connect & Play: Log in as a client, connect to the host, and start trading.

Enjoy: Have fun trading, learning, and connecting with others!
