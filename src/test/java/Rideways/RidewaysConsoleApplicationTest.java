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
    public void testProgramDetails()
    {
        String[] args = new String[0];
        boolean methodResult = RidewaysConsoleApplication.DisplayProgramDetails(args);
        assertTrue(methodResult);

        args = new String[] { "--help" };
        methodResult = RidewaysConsoleApplication.DisplayProgramDetails(args);
        assertTrue(methodResult);
    }

    @Test
    public void testHandleArguments()
    {
        // Null cases first.
        String[] args = new String[] { "--help", "nonsense" };
        String result = RidewaysConsoleApplication.HandleArguments(args);
        assertNull(result);

        args = new String[] { "Dave" };
        result = RidewaysConsoleApplication.HandleArguments(args);
        assertNull(result);

        args = new String[] { "cheapest" };
        result = RidewaysConsoleApplication.HandleArguments(args);
        assertNull(result);

        // Valid cases now.
        args = new String[] { "Dave", "coordinates" };
        result = RidewaysConsoleApplication.HandleArguments(args);
        assertEquals("Dave", result);

        args = new String[] {"cheapest", "coordinates", "passengers" };
        result = RidewaysConsoleApplication.HandleArguments(args);
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
