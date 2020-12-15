/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indproject;

/**
 *
 * @author S561561
 */
public class Player {
    private int num;
    private String name;
    private int balance;
    private int roundBalance;

    public Player(String n, int m) {
        name = n;
        num = m;
        balance = 0;
        roundBalance = 0;
    }
    
    public Player() {    
    }
    
    public int getRoundBalance() {
        return roundBalance;
    }

    public int getBalance() {
        return balance;
    }

    public void addRoundBalance(int amount) {
        roundBalance = roundBalance + amount;
    }

    public void addBalance(int amount) {
        balance = balance + amount;
    }

    public String getName() {
        return name;
    }
    
    public int getNum() {
        return num;
    }
}
