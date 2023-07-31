package EBILL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Customer {
    // Class variables (attributes)

    private int customer_num;
    private String name;
    private long phone;
    private String address;
    private long aadhar_number;

    // Constructor and getter/setter methods (Encapsulation)
    // Getters and setters are used to encapsulate access to private class variables.

    public int getCustomerNum() {
        return customer_num;
    }

    public void setCustomerNum(int customerNum) {
        this.customer_num = customer_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getAadharNumber() {
        return aadhar_number;
    }

    public void setAadharNumber(long aadharNumber) {
        this.aadhar_number = aadharNumber;
    }
    // Method to insert a new customer into the database (Abstraction)

    public void insertCustomer(Connection connection) throws SQLException {
        // Check if the Aadhar number already exists in the database
        if (isAadharNumberExists(connection, aadhar_number)) {
            System.out.println("Aadhar number already exists. Unable to add the customer.");
            return;
        }

        String insertCustomerQuery = "INSERT INTO customer_new (customer_num, name, phone, address, aadhar_number) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertCustomerQuery)) {
            // Manually generate a unique customer number and set it in the query (Abstraction)
            long uniqueCustomerNum = generateUniqueCustomerNumber();
            preparedStatement.setLong(1, uniqueCustomerNum);
            preparedStatement.setString(2, name);
            preparedStatement.setLong(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setLong(5, aadhar_number);
            preparedStatement.executeUpdate();
            System.out.println("New customer added successfully!");
            System.out.println("*************************************");
        }
    }
    private boolean isAadharNumberExists(Connection connection, long aadharNumber) throws SQLException {
        String checkAadharQuery = "SELECT COUNT(*) AS count FROM customer_new WHERE aadhar_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkAadharQuery)) {
            preparedStatement.setLong(1, aadharNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0; // If count is greater than 0, Aadhar number exists; otherwise, it does not exist
                }
            }
        }
        return false; // In case of any error, assume the Aadhar number doesn't exist
    }
    // Private method to generate a unique customer number (Abstraction)


    private long generateUniqueCustomerNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

    // Method to create a new customer object from user input (Encapsulation)
    public static Customer addNewCustomer(Scanner scanner) {
        Customer newCustomer = new Customer();

        System.out.print("Enter customer name: ");
        newCustomer.setName(scanner.next());

        System.out.print("Enter phone number: ");
        String phoneNumberString = scanner.next();
        long phoneNumber = Long.parseLong(phoneNumberString);
        newCustomer.setPhone(phoneNumber);

        System.out.print("Enter address: ");
        // Read the entire line as the address
        scanner.nextLine(); // Consume the newline character left by previous scanner.next()
        String address = scanner.nextLine();
        newCustomer.setAddress(address);

        System.out.print("Enter Aadhar number: ");
        newCustomer.setAadharNumber(scanner.nextLong());

        return newCustomer;
    }



    // Method to view customer details (Abstraction)
    public static void viewCustomerDetails(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter customer ID: ");
        long customerId = scanner.nextLong();

        String selectCustomerQuery = "SELECT * FROM customer_new WHERE customer_num = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectCustomerQuery)) {
            preparedStatement.setLong(1, customerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Display customer details (Abstraction)

                    System.out.println("Customer Details:");
                    System.out.println("Customer ID: " + resultSet.getLong("customer_num"));
                    System.out.println("Name: " + resultSet.getString("name"));
                    System.out.println("Phone: " + resultSet.getLong("phone"));
                    System.out.println("Address: " + resultSet.getString("address"));
                    System.out.println("Aadhar Number: " + resultSet.getLong("aadhar_number"));
                } else {
                    System.out.println("Customer with ID " + customerId + " not found.");
                }
            }
        }
    }
    // Other methods for updating and retrieving customer records from the database (Abstraction)

    // These methods are used to interact with the database and perform CRUD operations on customer data.
}