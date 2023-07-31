package EBILL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class ElectricityBillCalculator {
    public static void main(String[] args) {
        try {
            // Establish the connection to the MySQL database (Abstraction)
            String url = "jdbc:mysql://127.0.0.1:3306/EBill";
            String username = "root";
            String password = "Thilo@1069";
            Connection connection = DriverManager.getConnection(url, username, password);
            
            // Initialize Scanner for user input (Encapsulation)
            Scanner scanner = new Scanner(System.in);

            int choice;
            do {
                // Display menu options (Abstraction)
                System.out.println("1. Add a new customer");
                System.out.println("2. Generate bill");
                System.out.println("3. View customer details");
                System.out.println("4. View bill details");
                System.out.println("5. Update payment status");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        // Add a new customer
                        Customer newCustomer = Customer.addNewCustomer(scanner);
                        newCustomer.insertCustomer(connection);
                        break;
                    case 2:
                        // Generate bill
                        Bill.generateBill(connection, scanner);
                        break;
                    case 3:
                        // View customer details
                        Customer.viewCustomerDetails(connection, scanner);
                        break;
                    case 4:
                        // View bill details
                        Bill.viewBillDetails(connection, scanner);
                        break;
                    case 5:
                        // Update payment status
                        Bill.updatePaymentStatus(connection, scanner);
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } while (choice != 6);

            // Close the scanner and database connection (Encapsulation)
            scanner.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}