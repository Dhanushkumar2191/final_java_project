package FinalProject;

	import java.util.ArrayList;
	import java.util.Scanner;

	public class MovieTicketBooking {

	    // ---------- Theatre layout: true = booked, false = available ----------
	    static final int ROWS = 5;
	    static final int COLS = 8;
	    static boolean[][] seats = new boolean[ROWS][COLS];

	    // ---------- Pricing ----------
	    static final double BASE_PRICE = 150.0;
	    static final double WEEKEND_SURCHARGE = 0.20; // +20% on Sat/Sun

	    // ---------- Movie rating ----------
	    static double ratingSum = 0;
	    static int ratingCount = 0;

	    // ---------- Bookings ----------
	    static class Booking {
	        int id;
	        ArrayList<int[]> seatList = new ArrayList<>();
	        double amountPaid;
	    }

	    static ArrayList<Booking> bookings = new ArrayList<>();
	    static int nextBookingId = 1001;

	    static Scanner sc = new Scanner(System.in);

	    // ===================== Module 1: Show available seats =====================
	    static void showSeats() {
	    	
	    	
	        System.out.println("\n            S C R E E N");
	        System.out.print("     ");
	        
	        
	        for (int c = 0; c < COLS; c++)
	        	System.out.print((c + 1) + "   ");
	        System.out.println();

	        for (int r = 0; r < ROWS; r++) {
	            System.out.print("  " + (char) ('A' + r) + "  ");
	            for (int c = 0; c < COLS; c++) {
	                System.out.print(seats[r][c] ? "[X] " : "[ ] ");
	            }
	            System.out.println();
	        }
	        System.out.println("\n[ ] Available   [X] Booked");
	    }

	    // ===================== Module 5: Weekend pricing =====================
	    static double getTicketPrice(int day) { // 1 = Mon ... 7 = Sun
	        boolean weekend = (day == 6 || day == 7);
	        return weekend ? BASE_PRICE * (1 + WEEKEND_SURCHARGE) : BASE_PRICE;
	    }

	    // ===================== Module 4: Discount coupon =====================
	    static double getDiscountPercent(String code) {
	        switch (code.toUpperCase()) {
	            case "SAVE10":  return 10;
	            case "MOVIE20": return 20;
	            case "FIRST50": return 50;
	            default:        return 0;
	        }
	    }

	    // ===================== Module 2: Book ticket =====================
	    static void bookTicket() {
	        System.out.print("Show day (1=Mon ... 7=Sun): ");
	        int day = sc.nextInt();
	        if (day < 1 || day > 7) {
	            System.out.println("Invalid day.");
	            return;
	        }
	        double price = getTicketPrice(day);
	        System.out.printf("Price per ticket: Rs. %.2f%s%n", price,
	                (day >= 6) ? " (weekend pricing)" : "");

	        System.out.print("How many seats? ");
	        int count = sc.nextInt();
	        if (count <= 0 || count > ROWS * COLS) {
	            System.out.println("Invalid seat count.");
	            return;
	        }

	        Booking b = new Booking();

	        for (int i = 1; i <= count; i++) {
	            while (true) {
	                System.out.print("Seat " + i + " (e.g. B4): ");
	                String input = sc.next().toUpperCase();

	                if (input.length() < 2) {
	                    System.out.println("Invalid format.");
	                    continue;
	                }
	                int r = input.charAt(0) - 'A';
	                int c;
	                try {
	                    c = Integer.parseInt(input.substring(1)) - 1;
	                } catch (NumberFormatException e) {
	                    System.out.println("Invalid format.");
	                    continue;
	                }

	                if (r < 0 || r >= ROWS || c < 0 || c >= COLS) {
	                    System.out.println("Seat does not exist.");
	                } else if (seats[r][c]) {
	                    System.out.println("Seat already booked. Choose another.");
	                } else {
	                    seats[r][c] = true; // reserve immediately so duplicates are caught
	                    b.seatList.add(new int[]{r, c});
	                    break;
	                }
	            }
	        }

	        double total = price * count;

	        System.out.print("Coupon code (or 'NO'): ");
	        String code = sc.next();
	        if (!code.equalsIgnoreCase("NO")) {
	            double percent = getDiscountPercent(code);
	            if (percent > 0) {
	                double discount = total * percent / 100;
	                total -= discount;
	                System.out.printf("Coupon applied: %.0f%% off (-Rs. %.2f)%n", percent, discount);
	            } else {
	                System.out.println("Invalid coupon. No discount applied.");
	            }
	        }

	        b.id = nextBookingId++;
	        b.amountPaid = total;
	        bookings.add(b);

	        System.out.println("\n--- Booking Confirmed ---");
	        System.out.println("Booking ID : " + b.id);
	        System.out.print("Seats      : ");
	        for (int[] s : b.seatList) System.out.print((char) ('A' + s[0]) + "" + (s[1] + 1) + " ");
	        System.out.printf("%nAmount paid: Rs. %.2f%n", total);
	    }

	    // ===================== Module 3: Cancel ticket =====================
	    static void cancelTicket() {
	        System.out.print("Enter Booking ID: ");
	        int id = sc.nextInt();

	        for (int i = 0; i < bookings.size(); i++) {
	            Booking b = bookings.get(i);
	            if (b.id == id) {
	                for (int[] s : b.seatList) seats[s[0]][s[1]] = false;
	                bookings.remove(i);
	                System.out.printf("Booking %d cancelled. Refund: Rs. %.2f%n", id, b.amountPaid);
	                return;
	            }
	        }
	        System.out.println("Booking ID not found.");
	    }

	    // ===================== Module 6: Movie rating =====================
	    static void rateMovie() {
	        System.out.print("Rate the movie (1-5): ");
	        int rating = sc.nextInt();
	        if (rating < 1 || rating > 5) {
	            System.out.println("Rating must be between 1 and 5.");
	            return;
	        }
	        ratingSum += rating;
	        ratingCount++;
	        System.out.println("Thanks for rating!");
	    }

	    static void showRating() {
	        if (ratingCount == 0) {
	            System.out.println("No ratings yet.");
	        } else {
	            System.out.printf("Average rating: %.1f / 5 (%d reviews)%n",
	                    ratingSum / ratingCount, ratingCount);
	        }
	    }

	    // ===================== Main menu =====================
	    public static void main(String[] args) {
	        int choice;
	        do {
	            System.out.println("\n===== MOVIE TICKET BOOKING =====");
	            System.out.println("1. Show available seats");
	            System.out.println("2. Book ticket");
	            System.out.println("3. Cancel ticket");
	            System.out.println("4. Rate movie");
	            System.out.println("5. View movie rating");
	            System.out.println("6. Exit");
	            System.out.print("Choice: ");
	            choice = sc.nextInt();

	            switch (choice) {
	                case 1: showSeats();    break;
	                case 2: showSeats(); bookTicket(); break;
	                case 3: cancelTicket(); break;
	                case 4: rateMovie();    break;
	                case 5: showRating();   break;
	                case 6: System.out.println("Goodbye!"); break;
	                default: System.out.println("Invalid choice.");
	            }
	        } while (choice != 6);
	    }
	}

