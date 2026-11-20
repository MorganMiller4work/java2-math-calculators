package edu.kirkwood.controller.morgan;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.kirkwood.controller.morgan.DistanceCalculator;
import edu.kirkwood.model.morgan.Coordinate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class DistanceCalculatorTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream output;
    

    @BeforeEach
    void captureOutput() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private String run(String typedInput){
        System.setIn(new ByteArrayInputStream(typedInput.getBytes()));
        edu.kirkwood.view.UserInput.resetScanner();
        DistanceCalculator.start();
        return output.toString();
    }

    @Test 
    void londonToParis_inMiles_showsMilesResult()
    {
        // coordinate 1, coordinate 2, unit, quit, then Enter for the pause
        String out = run("51.5074, -0.1278\n48.8566, 2.3522\nmiles\nq\n\n");
        assertTrue(out.contains("The distance between the two coordinates is:"));
        assertTrue(out.contains("miles"));
    }

    @Test 
    void londonToParis_inMiles_showsKmResult()
    {
        String out = run("51.5074, -0.1278\n48.8566, 2.3522\nkilometers\nq\n\n")    ;
        assertTrue(out.contains("km"));
    }

    @Test
    void badCoordinate_showsErrorThenAcceptsValidOne() {
        String out = run("91, 0\nabc, def\n10, 20\n30, 40\nkm\nq\n\n");
        assertTrue(out.contains("Latitude must be -90 to 90"));
        assertTrue(out.contains("Those aren't valid numbers"));
        assertTrue(out.contains("The distance between the two coordinates is:"));
    }

    @Test
    void quitImmediately_showsNoDistance() {
        String out = run("  q\n\n");
        assertFalse(out.contains("The distance between"));
    }

    @Test
    void oneDegreeOnEquator_inKilometers_showsExactValue() {
        String out = run("0, 0\n0, 1\nkilometers\nq\n\n");
        assertTrue(out.contains("111.19 km"));
    }

    @Test
    void oneDegreeOnEquator_inMiles_showsExactValue() {
        String out = run("0, 0\n0, 1\nmiles\nq\n\n");
        assertTrue(out.contains("69.09 miles"));
    }   

    @Test
    void unrecognizedUnit_showsErrorThenAcceptsValidUnit() {
    // Arrange
    String typedInput = "0, 0\n0, 1\nbanana\nkm\nq\n\n";

    // Act
    String out = run(typedInput);

    // Assert
    assertTrue(out.contains("Please enter km or miles."));
    assertTrue(out.contains("111.19 km"));
}

    @Test
    void twoCalculationsInARow_showsTwoResults() {
        // first pair in km, second pair in miles, then quit
        String out = run("0, 0\n0, 1\nkm\n0, 0\n0, 90\nmiles\nq\n\n");
        String marker = "The distance between the two coordinates is:";
        int count = out.split(marker, -1).length - 1;
        assertEquals(2, count);
    }   

}
