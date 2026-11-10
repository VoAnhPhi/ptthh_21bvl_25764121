package lab6.bai07;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GeometryServiceImpl extends UnicastRemoteObject implements GeometryService {
    public GeometryServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public double rectangleArea(double width, double height) throws RemoteException {
        requirePositiveFinite(width, "Chieu rong");
        requirePositiveFinite(height, "Chieu cao");
        return width * height;
    }

    @Override
    public double circleArea(double radius) throws RemoteException {
        requirePositiveFinite(radius, "Ban kinh");
        return Math.PI * radius * radius;
    }

    @Override
    public double triangleArea(double base, double height) throws RemoteException {
        requirePositiveFinite(base, "Canh day");
        requirePositiveFinite(height, "Chieu cao");
        return base * height / 2.0;
    }

    private static void requirePositiveFinite(double value, String label) {
        if (!Double.isFinite(value) || value <= 0) {
            throw new IllegalArgumentException(label + " phai la so duong hop le.");
        }
    }
}
