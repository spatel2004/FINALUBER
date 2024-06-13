// Name: Shivam Patel 
// Student ID: 501168896

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

// Simulation of a Simple Command-line based Uber App 

// This system supports "ride sharing" service and a delivery service

public class TMUberUI
{
  public static void main(String[] args)
  {
    // Create the System Manager - the main system code is in here 
    TMUberSystemManager tmuber = new TMUberSystemManager();
    Scanner scanner = new Scanner(System.in);
    System.out.print(">");

    // Process keyboard actions
    while (scanner.hasNextLine())
    {
      String action = scanner.nextLine();
      // Continue with next command(empty )
      if (action == null || action.equals("")) 
      {
        System.out.print("\n>");
        continue;
      }
      // Quit the App
      else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
        return;
      // Print all the registered drivers
      else if (action.equalsIgnoreCase("DRIVERS"))
      {
        tmuber.listAllDrivers(); 
        System.out.println();
      }
      // Print all the registered users
      else if (action.equalsIgnoreCase("USERS")) 
      {
        tmuber.listAllUsers(); 
        System.out.println();
      }
      // Print all current ride requests or delivery requests
      else if (action.equalsIgnoreCase("REQUESTS")) 
      {
        tmuber.listAllServiceRequests(); 
      }
      // Register a new driver
      else if (action.equalsIgnoreCase("REGDRIVER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String carModel = "";
        System.out.print("Car Model: ");
        if (scanner.hasNextLine())
        {
          carModel = scanner.nextLine();
        }
        String license = "";
        System.out.print("Car License: ");
        if (scanner.hasNextLine())
        {
          license = scanner.nextLine();
        }
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine()) {
          address = scanner.nextLine();
        }
        try{
          tmuber.registerNewDriver(name, carModel, license, address, CityMap.getCityZone(address));
          System.out.printf("Driver: %-15s Car Model: %-15s License Plate: %-10s", name, carModel, license);
        }
        catch (IllegalInputException exception) {
          System.out.println(exception.getMessage());
        }
        catch (DriverExistsException exception) {
          System.out.println(exception.getMessage());
        }
      }

      // Register a new user
      else if (action.equalsIgnoreCase("REGUSER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        double wallet = 0.0;
        System.out.print("Wallet: ");
        try {
          wallet = scanner.nextDouble();
        }
        // Catches error if wallet is not valid
        catch (InputMismatchException exception) {
          scanner.nextLine();
        }
        scanner.nextLine();
        try{
          tmuber.registerNewUser(name, address, wallet);
          System.out.printf("User: %-15s Address: %-15s Wallet: %2.2f", name, address, wallet);
          System.out.println();
        }
        // Catches error if incorrect input types 
        catch (InputMismatchException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches argument values 
        catch (IllegalInputException exception) {
          System.out.println(exception.getMessage());
          scanner.nextLine();
        }
        // Catches error if user already exists 
        catch (UserExistsException exception) {
          System.out.println(exception.getMessage());
        }
      }

      // Request a ride
      else if (action.equalsIgnoreCase("REQRIDE")) 
      {
        String account = "";
        System.out.print("User Account Id: ");
        if (scanner.hasNextLine())
        {
          account = scanner.nextLine();
        }
        String from = "";
        System.out.print("From Address: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To Address: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        try {
          User user = tmuber.getUser(account);
          tmuber.requestRide(account, from, to);
          System.out.printf("\nRIDE for: %-15s From: %-15s To: %-15s", user.getName(), from, to);
          System.out.println();
        }
        // Catches illegal argument values 
        catch (IllegalInputException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if user account is not found 
        catch (UserAccountNotFoundException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if travel distances between addresses is not enough 
        catch (InsufficientTravelDistanceException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if user has insufficient funds 
        catch (InsufficientFundsException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if user already has ride request pending 
        catch (ExistingRideRequestException exception) {
          System.out.println(exception.getMessage());
        }
      }

      // Request a food delivery
      else if (action.equalsIgnoreCase("REQDLVY")) 
      {
        String account = "";
        System.out.print("User Account Id: ");
        if (scanner.hasNextLine())
        {
          account = scanner.nextLine();
        }
        String from = "";
        System.out.print("From Address: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To Address: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        String restaurant = "";
        System.out.print("Restaurant: ");
        if (scanner.hasNextLine())
        {
          restaurant = scanner.nextLine();
        }
        String foodOrder = "";
        System.out.print("Food Order #: ");
        if (scanner.hasNextLine())
        {
          foodOrder = scanner.nextLine();
        }
        try {
          User user = tmuber.getUser(account);
          tmuber.requestDelivery(account, from, to, restaurant, foodOrder);
          System.out.printf("\nDELIVERY for: %-15s From: %-15s To: %-15s", user.getName(), from, to);
        }  
        // Catches illegal argument values 
        catch (IllegalInputException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if user not found 
        catch (UserAccountNotFoundException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if distance between addresses is insufficient 
        catch (InsufficientTravelDistanceException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if user has insufficent funds for delivery 
        catch (InsufficientFundsException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if user already has delivery request pending 
        catch (ExistingDeliveryRequestException exception) {
          System.out.println(exception.getMessage());
        }
      }

      // Sort users by name
      else if (action.equalsIgnoreCase("SORTBYNAME")) 
      {
        tmuber.sortByUserName();
      }
      // Sort users by their wallet 
      else if (action.equalsIgnoreCase("SORTBYWALLET")) 
      {
        tmuber.sortByWallet();
      }
      // Sort current service requests (ride or delivery) by distance
      else if (action.equalsIgnoreCase("SORTBYDIST")) 
      {
        tmuber.sortByDistance();
      }
      // Cancel a current service (ride or delivery) request
      else if (action.equalsIgnoreCase("CANCELREQ")) 
      {
        try {
          System.out.print("Zone: ");
          int zone = scanner.nextInt();
          System.out.print("Request #: ");
          int request = scanner.nextInt();
          scanner.nextLine();
          tmuber.cancelServiceRequest(request,zone);
        }
        // Catches error if incorrect input types 
        catch (InputMismatchException exception) {
          System.out.println("InputMismatchException");
          scanner.nextLine();
        }
        // Catches illegal argument values  
        catch (IllegalInputException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error in request number 
        catch (InvalidRequestNumberException exception) {
          System.out.println(exception.getMessage());
        }
      }

      // Get the Current Total Revenues
      else if (action.equalsIgnoreCase("REVENUES")) 
      {
        System.out.println("Total Revenue: " + tmuber.totalRevenue);
      }

      // Unit Test of Valid City Address 
      else if (action.equalsIgnoreCase("ADDR")) 
      {
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        System.out.print(address);
        if (CityMap.validAddress(address))
          System.out.println("\nValid Address"); 
        else
          System.out.println("\nBad Address"); 
      }

      // Unit Test of CityMap Distance Method
      else if (action.equalsIgnoreCase("DIST")) 
      {
        String from = "";
        System.out.print("From: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        System.out.print("\nFrom: " + from + " To: " + to);
        System.out.println("\nDistance: " + CityMap.getDistance(from, to) + " City Blocks");
      }

      // Load Users from File 
      else if (action.equalsIgnoreCase("LOADUSERS")) {
        System.out.print("Users File: ");
        String file_name = scanner.nextLine();
        try {
          ArrayList<User> users_from_file = TMUberRegistered.loadPreregisteredUsers(file_name);
          tmuber.setUsers(users_from_file);
          System.out.println("Users Loaded!");
        }
        // Catches error if file is not found 
        catch (FileNotFoundException exception) {
          System.out.println(file_name + " Not Found ");
        }
      }

      // Load Drivers from a File 
      else if (action.equalsIgnoreCase("LOADDRIVERS")) {
        System.out.print("Drivers File: ");
        String file_name = scanner.nextLine();
        try {
          ArrayList<Driver> drivers_from_file = TMUberRegistered.loadPreregisteredDrivers(file_name);
          tmuber.setDrivers(drivers_from_file);
          System.out.println("Drivers Loaded");
        }
        // Catches error if file is not found 
        catch (FileNotFoundException exception) {
          System.out.println(file_name + " Not Found");
        }
      }

      // Initiates pickup action  
      else if(action.equalsIgnoreCase("pickup")) {
        System.out.print("Driver Id: ");
        String driver_id = scanner.nextLine();
        try {
          tmuber.pickup(driver_id);
        }
        // Catches illegal argument values 
        catch (IllegalInputException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if driver is not found 
        catch (DriverNotFoundException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if zone is invalid 
        catch (InvalidZoneException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if request number is invalid 
        catch (NoServiceRequestFoundException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if no available driver is found 
        catch (NoAvailableDriverException exception) {
          System.out.println(exception.getMessage());
        }
      }

      // Drops off a user
      else if (action.equalsIgnoreCase("dropoff")) {
        System.out.print("Driver Id: ");
        String driver_id = scanner.nextLine();
        try {
          tmuber.dropOff(driver_id);
        }
        // Catches illegal argument values 
        catch (IllegalInputException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if driver is not found 
        catch (DriverNotFoundException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if no available driver is found 
        catch (NoAvailableDriverException exception) {
          System.out.println(exception.getMessage());
        }
      }

      // Drives to a certain location 
      else if (action.equalsIgnoreCase("driveto")) {
        System.out.print("Driver Id: ");
        String driver_id = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        try {
          tmuber.driveTo(driver_id,address);
        }
        // Catches illegal argument values 
        catch (IllegalInputException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if driver not found 
        catch (DriverNotFoundException exception) {
          System.out.println(exception.getMessage());
        }
        // Catches error if no available driver is found 
        catch (NoAvailableDriverException exception) {
          System.out.println(exception.getMessage());
        }
      }

      System.out.print("\n>");
    }
  }
}
