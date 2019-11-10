package Rideways;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Testing functionality from the console application.
 *
 * @author Vlad Mihai Vasile
 */

public class RidewaysConsoleApplicationTest
{
    @Test
    public void testProgramDetailsNullArgs()
    {
        String[] args = new String[0];
        boolean methodResult = RidewaysConsoleApplication.DisplayProgramDetails(args);
        assertTrue(methodResult);
    }
    
    @Test
    public void testProgramDetailsHelpArgs()
    {
        String[] args = new String[] { "--help" };
        boolean methodResult = RidewaysConsoleApplication.DisplayProgramDetails(args);
        assertTrue(methodResult);
    }
    
    // Null cases for arguments handling
    @Test
    public void testHandleArgumentsGibberish()
    {
        String[] args = new String[] { "--help", "nonsense" };
        String result = RidewaysConsoleApplication.HandleArguments(args);
        assertNull(result);
    }
    
    @Test
    public void testHandleArgumentsDaveNull()
    {
        String[] args = new String[] { "Dave" };
        String result = RidewaysConsoleApplication.HandleArguments(args);
        assertNull(result);
    }
    
    @Test
    public void testHandleArgumentsCheapestNull()
    {
        String[] args = new String[] { "cheapest" };
        String result = RidewaysConsoleApplication.HandleArguments(args);
        assertNull(result);
    }
    
    // Valid cases for arguments handling
    @Test
    public void testHandleArgumentsDaveCoordinates()
    {
        String[] args = new String[] { "Dave", "coordinates" };
        String result = RidewaysConsoleApplication.HandleArguments(args);
        assertEquals("Dave", result);
    }
    
    @Test
    public void testHandleArgumentsCheapestCoordinatesPassengers()
    {
        String[] args = new String[] {"cheapest", "coordinates", "passengers" };
        String result = RidewaysConsoleApplication.HandleArguments(args);
        assertEquals("cheapest", result);
    }

    @Test
    public void testGetResultsFromDave()
    {
        String coordinates = "pickup=51.470020,-0.454295&dropoff=3.410632,-2.157533";
        ArrayList<Car> result = RidewaysConsoleApplication.GetResultsFromDave(coordinates);
        assertNotNull(result);
    }

    @Test
    public void testGetCheapestResults()
    {
        String coordinates = "pickup=51.470020,-0.454295&dropoff=3.410632,-2.157533";
        int passengers = 5;
        ArrayList<Car> result = RidewaysConsoleApplication.GetCheapestResults(coordinates, passengers);
        assertNotNull(result);
    }
}
