// Name: Shivam Patel 
// Student ID: 501168896

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

import java.io.FileNotFoundException;

public class TMUberRegistered
{
    // These variables are used to generate user account and driver ids
    private static int firstUserAccountID = 900;
    private static int firstDriverId = 700;

    // Generate a new user account id
    public static String generateUserAccountId(ArrayList<User> current)
    {
        return "" + firstUserAccountID + current.size();
    }

    // Generate a new driver id
    public static String generateDriverId(ArrayList<Driver> current)
    {
        return "" + firstDriverId + current.size();
    }

    // Database of Preregistered users
    // In Assignment 2 these will be loaded from a file
    // The test scripts and test outputs included with the skeleton code use these
    // users and drivers below. You may want to work with these to test your code (i.e. check your output with the
    // sample output provided). 
    public static ArrayList<User> loadPreregisteredUsers(String fileName) throws FileNotFoundException
    {
        ArrayList<User> users = new ArrayList<User>();
        File to_read_file = new File(fileName); // constructing the file object based on file name 
        Scanner in = new Scanner(to_read_file); // scanner to read newly constructed file object 
        
        while (in.hasNext()) {
            String user_id = generateUserAccountId(users);
            String user_name = in.nextLine();
            String user_address = in.nextLine();
            double user_wallet = Double.parseDouble(in.nextLine());
            User new_user = new User(user_id,user_name,user_address,user_wallet);
            users.add(new_user);
        }
        in.close(); // close scanner to prevent resource leak 
        return users;
    }

    // Database of Preregistered users
    // In Assignment 2 these will be loaded from a file
    public static ArrayList<Driver> loadPreregisteredDrivers(String fileName) throws FileNotFoundException
    {
        ArrayList<Driver> drivers = new ArrayList<Driver>();
        File to_read_file = new File(fileName); // constructing file object based on file name 
        Scanner in = new Scanner(to_read_file); // scanner to read newly constructed file object 

        while (in.hasNext()) {
            String driver_id = generateDriverId(drivers);
            String driver_name = in.nextLine();
            String driver_car_model = in.nextLine();
            String driver_license_plate = in.nextLine();
            String driver_address = in.nextLine();
            int driver_zone = CityMap.getCityZone(driver_address); // calculate zone based on address 
            Driver new_driver = new Driver(driver_id,driver_name,driver_car_model,driver_license_plate,driver_address,driver_zone);
            drivers.add(new_driver);
        }
        in.close(); // close scanner to prevent resource leak 
        return drivers;
    }
}
