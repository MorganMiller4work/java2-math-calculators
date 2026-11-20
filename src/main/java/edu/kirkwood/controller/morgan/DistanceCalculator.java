package edu.kirkwood.controller.morgan;

import static edu.kirkwood.view.Messages.*;
import static edu.kirkwood.view.UIUtility.*;
import static edu.kirkwood.view.UserInput.*;

import edu.kirkwood.view.*;
import edu.kirkwood.model.morgan.*;

/**
 * 
 * Controller that reads two coordinates from the user and reports the distance
 */
public class DistanceCalculator {
    
    /*Prevents instantiation */
    private DistanceCalculator(){}

    /**
     * Starts the distance calculator.
     */
    public static void start() {
        distanceGreet();
        UnitConverter converter = new UnitConverter();
        while (true){
            Coordinate c1 = readCoordinate("Enter the first coordinate as latitude, longitude (or 'q' to quit):");
            if (c1 == null) {
                break;
            }
            Coordinate c2 = readCoordinate("Enter the second coordinate as latitude, longitude (or 'q' to quit): ");
            if (c2 == null) {
                break;
            }

            // Unit selected? Calculation is in km and will be converrted to miles only if needed
                    DistanceUnit unit = null;
        while (unit == null) {
            String unitChoice = getString("Show distance in kilometers or miles? ").trim().toLowerCase();
            if (unitChoice.equals("km") || unitChoice.equals("kilometers")|| unitChoice.equals("k")) {
                unit = DistanceUnit.KILOMETERS;
            } else if (unitChoice.equals("miles") || unitChoice.equals("mi") || unitChoice.equals("m")) {
                unit = DistanceUnit.MILES;
            } else {
                displayError("Please enter km or miles.");
            }
        }
            double km = Coordinate.computeDistance(c1, c2);
            double result = converter.convert(km, unit);
            String label = (unit == DistanceUnit.MILES) ? "miles" : "km";

              displayMessage(String.format("The distance between the two coordinates is: %.2f %s", result, label));        
        }
       
        pressEnterToContinue();   
        distanceGoodbye();
        
    }

    /**
     * Prompts until the user enters a valid coordinate or quits.
     * @param prompt the text shown to the user
     * @return a valid {Coordinate}, or {null} if the user quits
     */
    private static Coordinate readCoordinate(String prompt){
        while (true) {
            String value = getString(prompt).trim();
            if(value.equalsIgnoreCase("q")|| value.equalsIgnoreCase("quit"))
            {
                return null;
            }
            String[] parts = value.split(",");
            if (parts.length != 2) {
                displayError("Please enter two numbers separated by a comma, e.g. 41.9, -91.67");
                continue;
            }
            try{
                double lat = Double.parseDouble(parts[0].trim());
                double lon = Double.parseDouble(parts[1].trim());
                Coordinate coordinate = new Coordinate(lat, lon);
                if(coordinate.isValid())
                {
                    return coordinate;
                }
                displayError("Latitude must be -90 to 90 and longitude must be -180 to 180.");
            }catch(NumberFormatException e){
                displayError("Those aren't valid numbers. Try again.");
            }
        }
    }

}