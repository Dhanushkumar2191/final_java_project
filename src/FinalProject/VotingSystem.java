package FinalProject;



import java.util.*;

class User {
    String username;
    String password;
    boolean voted;

    User(String username, String password) {
        this.username = username;
        this.password = password;
        this.voted = false;
    }
}

class Candidate {
    int id;
    String name;
    int votes;

    Candidate(int id, String name) {
        this.id = id;
        this.name = name;
        this.votes = 0;
    }
}

public class VotingSystem {

    static Scanner sc = new Scanner(System.in);

    static HashMap<String, User> users = new HashMap<>();
    static HashMap<String, Boolean> userVoted = new HashMap<>();
    static HashMap<Integer, Candidate> candidates = new HashMap<>();
    static HashMap<String, Integer> candidateVotes = new HashMap<>();

    public static void main(String[] args) {

        candidates.put(1, new Candidate(1, "Arun"));
        candidates.put(2, new Candidate(2, "Bala"));
        candidates.put(3, new Candidate(3, "Kumar"));
        candidates.put(4, new Candidate(4, "Dinesh"));

        while (true) {

            System.out.println("\n===== ONLINE VOTING SYSTEM =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Candidates");
            System.out.println("4. Total Votes");
            System.out.println("5. Display Results");
            System.out.println("6. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {

                    case 1:
                        register();
                        break;

                    case 2:
                        login();
                        break;

                    case 3:
                        showCandidates();
                        break;

                    case 4:
                        showTotalVotes();
                        break;

                    case 5:
                        displayResults();
                        break;

                    case 6:
                        System.out.println("Thank You!");
                        return;

                    default:
                        System.out.println("Invalid Choice!");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Registration
    static void register() {

        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username Already Exists!");
            return;
        }

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        User user = new User(username, password);

        users.put(username, user);
        userVoted.put(username, false);

        System.out.println("Registration Successful!");
    }

    // Login
    static void login() {

        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        User user = users.get(username);

        if (user != null && user.password.equals(password)) {

            System.out.println("Login Successful!");
            studentMenu(user);

        } else {
            System.out.println("Invalid Username or Password!");
        }
    }

    // Student Menu
    static void studentMenu(User user) {

        while (true) {

            System.out.println("\n===== STUDENT MENU =====");
            System.out.println("1. View Candidates");
            System.out.println("2. Cast Vote");
            System.out.println("3. Logout");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    showCandidates();
                    break;

                case 2:
                    castVote(user);
                    break;

                case 3:
                    System.out.println("Logout Successful!");
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    // Candidate List
    static void showCandidates() {

        System.out.println("\n===== CANDIDATES =====");

        for (Candidate c : candidates.values()) {
            System.out.println(c.id + ". " + c.name);
        }
    }

    // Vote Casting
    static void castVote(User user) {

        try {

            if (userVoted.get(user.username)) {
                throw new Exception("You Have Already Voted!");
            }

            showCandidates();

            System.out.print("Enter Candidate ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            Candidate c = candidates.get(id);

            if (c == null) {
                throw new Exception("Invalid Candidate ID!");
            }

            c.votes++;

            candidateVotes.put(
                c.name,
                candidateVotes.getOrDefault(c.name, 0) + 1
            );

            user.voted = true;
            userVoted.put(user.username, true);

            System.out.println("Vote Cast Successfully!");

        } catch (Exception e) {
            System.out.println("Voting Error: " + e.getMessage());
        }
    }

    // Total Votes
    static void showTotalVotes() {

        int total = 0;

        System.out.println("\n===== TOTAL VOTES =====");

        for (Candidate c : candidates.values()) {

            System.out.println(c.name + " : " + c.votes);

            total += c.votes;
        }

        System.out.println("Total Votes Cast: " + total);
    }

    // Winner and Runner-Up
    static void displayResults() {

        List<Candidate> list =
            new ArrayList<>(candidates.values());

        list.sort((a, b) ->
            Integer.compare(b.votes, a.votes)
        );

        if (list.isEmpty() || list.get(0).votes == 0) {
            System.out.println("No Votes Cast Yet!");
            return;
        }

        int maxVotes = list.get(0).votes;

        System.out.println("\n===== ELECTION RESULTS =====");

        System.out.println("WINNER:");

        for (Candidate c : list) {
            if (c.votes == maxVotes) {
                System.out.println(c.name + " - " + c.votes + " votes");
            }
        }

        int runnerUpVotes = -1;

        for (Candidate c : list) {
            if (c.votes < maxVotes) {
                runnerUpVotes = c.votes;
                break;
            }
        }

        if (runnerUpVotes == -1) {
            System.out.println("No Runner-Up Available!");
        } else {
            System.out.println("\nRUNNER-UP:");

            for (Candidate c : list) {
                if (c.votes == runnerUpVotes) {
                    System.out.println(c.name + " - " + c.votes + " votes");
                }
            }
        }
    }
}
