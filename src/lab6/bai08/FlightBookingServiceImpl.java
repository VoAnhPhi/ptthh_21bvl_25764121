package lab6.bai08;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FlightBookingServiceImpl extends UnicastRemoteObject implements FlightBookingService {
    private final Map<String, Flight> flights = new HashMap<>();

    public FlightBookingServiceImpl() throws RemoteException {
        super();
        flights.put("VN101", new Flight(180));
        flights.put("VN202", new Flight(120));
        flights.put("VN303", new Flight(90));
    }

    @Override
    public synchronized boolean bookTicket(String flightNumber, int seats) throws RemoteException {
        Flight flight = findFlight(flightNumber);
        if (flight == null || seats <= 0 || seats > flight.availableSeats()) {
            return false;
        }
        flight.bookedSeats += seats;
        return true;
    }

    @Override
    public synchronized int availableSeats(String flightNumber) throws RemoteException {
        Flight flight = findFlight(flightNumber);
        return flight == null ? -1 : flight.availableSeats();
    }

    @Override
    public synchronized boolean cancelBooking(String flightNumber, int seats) throws RemoteException {
        Flight flight = findFlight(flightNumber);
        if (flight == null || seats <= 0 || seats > flight.bookedSeats) {
            return false;
        }
        flight.bookedSeats -= seats;
        return true;
    }

    private Flight findFlight(String flightNumber) {
        if (flightNumber == null || flightNumber.isBlank()) {
            return null;
        }
        return flights.get(flightNumber.trim().toUpperCase(Locale.ROOT));
    }

    private static class Flight {
        private final int capacity;
        private int bookedSeats;

        private Flight(int capacity) {
            this.capacity = capacity;
        }

        private int availableSeats() {
            return capacity - bookedSeats;
        }
    }
}
