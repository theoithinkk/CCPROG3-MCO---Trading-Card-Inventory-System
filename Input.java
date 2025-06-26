package com.tciss;
import java.util.*;
public class Input {
	private Scanner scanner;
	public Input(Scanner sc) {
		this.scanner = sc;
	}
	
	public String ask(String prompt) {
		System.out.println(prompt);
		return scanner.nextLine().trim();
	}
	
	public String askLowerTrimmed(String prompt) {
	    System.out.print(prompt);
	    return scanner.nextLine().trim().toLowerCase();
	}
	
	public void hitEnter() {
		System.out.print("Press enter to continue..");
		scanner.nextLine();
	}
	
	public double askDouble(String prompt) {
	    double value = 0;
	    boolean valid = false;

	    while (!valid) {
	        System.out.print(prompt);
	        try {
	            value = Double.parseDouble(scanner.nextLine().trim());
	            valid = true;
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid number. Try again.");
	        }
	    }

	    return value;
	}
	
	
	/**
     * Gets integer input from user with validation
     *
     * @param prompt the prompt to display
     * @return the integer input
     */
	
	public int getIntInput(String prompt) {
        int input = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine().trim());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }

        return input;
    }

}
