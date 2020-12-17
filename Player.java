package zetaomicron;

public class Player {
    private int playerNum;
    private String name;
    private int roundBalance;
    private int totalBalance;
    
    public Player(String n, int num) {
        name = n;
        playerNum = num;
        roundBalance = 0;
        totalBalance = 0;
    }
    
    public Player() {
    }
    
    public void clearRoundBalance() {
        roundBalance = 0;
    }
    
    public void addRoundBalance(int amount) {
        roundBalance += amount;
    }
    
    public void addBalance(int amount) {
        totalBalance += amount;
    }
    
    public String getName() {
        return name;
    }
    
    public int getNum() {
        return playerNum;
    }
    
    public int getRoundBalance() {
        return roundBalance;
    }
    
    public int getTotalBalance() {
        return totalBalance;
    }
}
