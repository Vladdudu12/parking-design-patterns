package ro.ase.parking.observer;

public class UserNotificationObserver implements Observer {
    private String email;

    public UserNotificationObserver(String email) {
        this.email = email;
    }

    public void update(String message) {
        System.out.println("Email to " + email + ": " + message);
    }
}

