package EBILL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Bill {
    // Class variables (attributes)

    private int bill_no;
    private long customer_id;
    private String month;
    private double units_consumed;
    private double amount;
    private String payment_status;

    // Constructor and getter/setter methods

    // Method to generate a new bill and insert it into the database
    public static void generateBill(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter customer ID: ");
        long customer_id = scanner.nextLong();

        System.out.print("Enter month (e.g., Jan, Feb, ...): ");
        String month = scanner.next();

        // Check if the bill for the given customer and month already exists(Abstraction)
        if (isBillGeneratedForMonth(connection, customer_id, month)) {
            System.out.println("Bill for customer " + customer_id + " in month " + month + " has already been generated.");
            return;
        }

        System.out.print("Enter units consumed: ");
        double units_consumed = scanner.nextDouble();

        // Calculate bill amount using the formula
        double billAmount = calculateBillAmount(units_consumed);

        // Insert new bill record into the database
        String insertBillQuery = "INSERT INTO bill (customer_id, month, units_consumed, amount, payment_status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertBillQuery)) {
            preparedStatement.setLong(1, customer_id);
            preparedStatement.setString(2, month);
            preparedStatement.setDouble(3, units_consumed);
            preparedStatement.setDouble(4, billAmount);
            preparedStatement.setString(5, "Unpaid"); // Initially set payment status as "Unpaid"
            preparedStatement.executeUpdate();
            System.out.println("Bill generated successfully!");
            System.out.println("***************************************");
        }
    }

    // Method to check if a bill for a specific customer and month is already generated
    private static boolean isBillGeneratedForMonth(Connection connection, long customer_id, String month) throws SQLException {
        // SQL query to check if the bill exists in the database (Abstraction)

        String selectBillQuery = "SELECT COUNT(*) AS count FROM bill WHERE customer_id = ? AND month = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectBillQuery)) {
            preparedStatement.setLong(1, customer_id);
            preparedStatement.setString(2, month);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0; // If count is greater than 0, bill exists; otherwise, it does not exist
                }
            }
        }
        return false; // In case of any error, assume the bill doesn't exist
    }

    // Method to calculate the bill amount based on the formula
    private static double calculateBillAmount(double unitsConsumed) {
        // Implement the formula for bill calculation based on units consumed
        // Formula:
        // The first 100 units are free
        // Next 100 units are charged at Rs. 1.5 per unit
        // Next 300 units are charged at Rs. 2 per unit
        // Remaining units are charged at Rs. 3 per unit

        double billAmount = 0.0;
        if (unitsConsumed > 0) {
            if (unitsConsumed <= 100) {
                billAmount = 0.0; // First 100 units are free
            } else if (unitsConsumed <= 200) {
                billAmount = (unitsConsumed - 100) * 1.5; // Rs. 1.5 per unit for next 100 units
            } else if (unitsConsumed <= 500) {
                billAmount = 100 * 1.5 + (unitsConsumed - 200) * 2; // Rs. 1.5 per unit for first 100 units, Rs. 2 per unit for next 300 units
            } else {
                billAmount = 100 * 1.5 + 300 * 2 + (unitsConsumed - 500) * 3; // Rs. 1.5 per unit for first 100 units, Rs. 2 per unit for next 300 units, Rs. 3 per unit for remaining units
            }
        }

        return billAmount;
    }

    // Method to view bill details
    public static void viewBillDetails(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter customer ID: ");
        long customer_id = scanner.nextLong();
        // SQL query to retrieve bill details from the database (Abstraction)

        String selectBillQuery = "SELECT * FROM bill WHERE customer_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectBillQuery)) {
            preparedStatement.setLong(1, customer_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Display bill details (Abstraction)

                    System.out.println("Bill Details:");
                    System.out.println("Bill Number: " + resultSet.getInt("bill_no"));
                    System.out.println("Customer ID: " + resultSet.getLong("customer_id"));
                    System.out.println("Month: " + resultSet.getString("month"));
                    System.out.println("Units Consumed: " + resultSet.getDouble("units_consumed"));
                    System.out.println("Amount: " + resultSet.getDouble("amount"));
                    System.out.println("Payment Status: " + resultSet.getString("payment_status"));
                    System.out.println("*******************************************");
                }
            }
        }
    }

    // Method to update payment status
    public static void updatePaymentStatus(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter bill number: ");
        int bill_no = scanner.nextInt();

        System.out.print("Enter new payment status (Paid/Unpaid): ");
        String payment_status = scanner.next();

        // Validate the payment status input to either "Paid" or "Unpaid"
        if (!payment_status.equalsIgnoreCase("Paid") && !payment_status.equalsIgnoreCase("Unpaid")) {
            System.out.println("Invalid payment status. Please enter 'Paid' or 'Unpaid'.");
            return;
        }

        // Update the payment status in the database
        String updatePaymentQuery = "UPDATE bill SET payment_status = ? WHERE bill_no = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updatePaymentQuery)) {
            preparedStatement.setString(1, payment_status);
            preparedStatement.setInt(2, bill_no);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Payment status updated successfully!");
            } else {
                System.out.println("Bill with number " + bill_no + " not found.");
            }
        }
    }

    // Other methods for retrieving and updating bill records from the database (Abstraction)
}
