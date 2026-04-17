import java.util.*;

// Observer Interface
interface Observer {
    void update(String message);
}

// Observer
class User implements Observer {
    String name;

    User(String name) {
        this.name = name;
    }

    public void update(String message) {
        System.out.println(name + " received: " + message);
    }
}

// Strategy Pattern
interface NotificationStrategy {
    String send(String msg);
}

class EmailStrategy implements NotificationStrategy {
    public String send(String msg) {
        return "Email: " + msg;
    }
}

class SMSStrategy implements NotificationStrategy {
    public String send(String msg) {
        return "SMS: " + msg;
    }
}

// Decorator Pattern
class UrgentDecorator implements NotificationStrategy {
    NotificationStrategy strategy;

    UrgentDecorator(NotificationStrategy strategy) {
        this.strategy = strategy;
    }

    public String send(String msg) {
        return strategy.send("[URGENT] " + msg);
    }
}

// Factory Pattern
class NotificationFactory {
    public static Observer createUser(String name) {
        return new User(name);
    }

    public static NotificationStrategy createStrategy(int type) {
        if(type == 1) return new EmailStrategy();
        else return new SMSStrategy();
    }
}

// Singleton + Subject
class NotificationManager {
    private static NotificationManager instance;
    private List<Observer> users = new ArrayList<>();

    private NotificationManager() {}

    public static NotificationManager getInstance() {
        if(instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void subscribe(Observer user) {
        users.add(user);
    }

    public void notifyAllUsers(String msg) {
        for(Observer u : users) {
            u.update(msg);
        }
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        NotificationManager manager = NotificationManager.getInstance();

        System.out.print("Enter number of users: ");
        int n = sc.nextInt();
        sc.nextLine();

        for(int i = 0; i < n; i++) {
            String name = sc.nextLine();
            Observer user = NotificationFactory.createUser(name);
            manager.subscribe(user);
        }

        System.out.println("1 Email  2 SMS");
        int type = sc.nextInt();

        NotificationStrategy strategy = NotificationFactory.createStrategy(type);

        System.out.println("Urgent? (1 Yes / 0 No)");
        int urgent = sc.nextInt();
        sc.nextLine();

        if(urgent == 1) {
            strategy = new UrgentDecorator(strategy);
        }

        System.out.print("Enter message: ");
        String msg = sc.nextLine();

        String finalMsg = strategy.send(msg);

        manager.notifyAllUsers(finalMsg);
    }
}