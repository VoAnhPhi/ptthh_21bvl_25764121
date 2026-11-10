package lab6.bai06;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BankServiceImpl extends UnicastRemoteObject implements BankService {
    private double balance = 1_000_000.0;

    public BankServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized double getBalance() throws RemoteException {
        return balance;
    }

    @Override
    public synchronized void deposit(double amount) throws RemoteException {
        requirePositiveAmount(amount);
        if (!Double.isFinite(balance + amount)) {
            throw new IllegalArgumentException("So du vuot qua gioi han.");
        }
        balance += amount;
    }

    @Override
    public synchronized boolean withdraw(double amount) throws RemoteException {
        if (!isPositiveAmount(amount) || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    private static void requirePositiveAmount(double amount) {
        if (!isPositiveAmount(amount)) {
            throw new IllegalArgumentException("So tien phai la so duong hop le.");
        }
    }

    private static boolean isPositiveAmount(double amount) {
        return Double.isFinite(amount) && amount > 0;
    }
}
