package ro.ase.parking;

import ro.ase.parking.database.Database;
import ro.ase.parking.database.DatabaseHelper;
import ro.ase.parking.model.ParkingSession;
import ro.ase.parking.model.ParkingSpot;
import ro.ase.parking.model.Reservation;
import ro.ase.parking.model.User;
import ro.ase.parking.observer.UserNotificationObserver;
import ro.ase.parking.singleton.ParkingManager;
import ro.ase.parking.specification.SpotAvailableSpecification;
import ro.ase.parking.specification.TimeIntervalSpecification;
import ro.ase.parking.specification.UserOwnsSpotSpecification;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ParkingManager parkingManager = ParkingManager.getInstance();

    private static User loggedUser;

    static void main() throws SQLException {
//        Database.init();
        showLoginMenu();
    }

    // ================= MENUS =================

    private static void showLoginMenu() throws SQLException {
        while (true) {
            System.out.println("\n=== PARKING APP ===");
            System.out.println("1. Login");
            System.out.println("0. Exit");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                login();
            } else if (option == 0) {
                System.exit(0);
            }
        }
    }

    private static void showMainMenu() throws SQLException {
        while (loggedUser != null) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Vezi toate locurile");
            System.out.println("2. Vezi detalii loc");
            System.out.println("3. Rezerva loc");
            System.out.println("4. Ocupa loc rezervat");
            System.out.println("5. Elibereaza loc");
            System.out.println("6. Logout");
            System.out.println("0. Exit");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> showAllSpots();
                case 2 -> showSpotDetails();
                case 3 -> reserveSpot();
                case 4 -> occupySpot();
                case 5 -> releaseSpot();
                case 6 -> {
                    loggedUser = null;
                    System.out.println("Logout realizat.");
                }
                case 0 -> System.exit(0);
            }
        }
    }

    // ================= ACTIONS =================

    private static void login() throws SQLException {
        System.out.println("Introdu ID utilizator:");
        long id = scanner.nextLong();
        scanner.nextLine();

        loggedUser = DatabaseHelper.findUserById(id);

        if (loggedUser != null) {
            System.out.println("Login reusit: " + loggedUser.getName());
            parkingManager.addObserver(new UserNotificationObserver(loggedUser.getName()));
            showMainMenu();
        } else {
            System.out.println("Utilizator inexistent");
        }
    }

    private static void showAllSpots() throws SQLException {
        System.out.println("\n--- LOCURI DE PARCARE ---");
        List<ParkingSpot> spots = DatabaseHelper.getAllSpots();
        spots.forEach(spot ->
                System.out.println("ID: " + spot.getId() +
                                   " | Status: " + spot.getStatus() +
                                   " | Pret/oră: " + spot.getPricePerHour())
        );
    }

    private static void showSpotDetails() throws SQLException {
        ParkingSpot spot = selectSpot();
        if (spot == null) return;

        System.out.println("\nDetalii loc:");
        System.out.println("Pret/oră: " + spot.getPricePerHour());
        System.out.println("Status: " + spot.getStatus());
        if (spot.getReservation() != null) {
            System.out.println("Rezervat de: " + spot.getReservation().getUser().getName());
            System.out.println("Pana la: " + spot.getReservation().getEnd());
        }
        if (spot.getSession() != null) {
            System.out.println("In sesiune curenta");
        }
    }

    private static void reserveSpot() throws SQLException {
        ParkingSpot spot = selectSpot();
        if (spot == null) return;

        Reservation reservation = new Reservation(
                loggedUser,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2)
        );

        SpotAvailableSpecification availableSpec = new SpotAvailableSpecification();
        TimeIntervalSpecification timeSpec = new TimeIntervalSpecification();

        if (!availableSpec.isSatisfiedBy(spot)) {
            System.out.println("Locul nu este disponibil pentru rezervare!");
            return;
        }

        if (!timeSpec.isSatisfiedBy(reservation)) {
            System.out.println("Intervalul de rezervare nu este valid!");
            return;
        }

        parkingManager.reserveSpot(spot, reservation);
        DatabaseHelper.insertReservation(reservation, spot);
        DatabaseHelper.updateSpot(spot);
        System.out.println("Loc rezervat cu succes!");
    }


    private static void occupySpot() throws SQLException {
        ParkingSpot spot = selectSpot();
        if (spot == null) return;

        UserOwnsSpotSpecification userSpec = new UserOwnsSpotSpecification(loggedUser);

        if (!userSpec.isSatisfiedBy(spot)) {
            System.out.println("Nu poti ocupa acest loc: nu esti utilizatorul care l-a rezervat!");
            return;
        }

        try {
            parkingManager.occupySpot(spot);
            DatabaseHelper.updateSpot(spot);
            System.out.println("Loc ocupat!");
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }


    private static void releaseSpot() throws SQLException {
        ParkingSpot spot = selectSpot();
        if (spot == null) return;

        UserOwnsSpotSpecification userSpec = new UserOwnsSpotSpecification(loggedUser);

        if (!userSpec.isSatisfiedBy(spot)) {
            System.out.println("Nu poti elibera acest loc: nu esti utilizatorul care il ocupa!");
            return;
        }

        try {
            ParkingSession session = spot.getSession();
            DatabaseHelper.insertSession(spot, session);

            Double totalCost = parkingManager.releaseSpot(spot);
            DatabaseHelper.updateSpot(spot);


            System.out.println("Loc eliberat si platit! S-a platit: " + totalCost);
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }


    private static ParkingSpot selectSpot() throws SQLException {
        System.out.println("Introdu ID loc:");
        long id = scanner.nextLong();
        scanner.nextLine();
        ParkingSpot spot = DatabaseHelper.findSpotById(id);
        if (spot == null) System.out.println("Loc inexistent!");
        return spot;
    }
}
