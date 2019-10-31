package Rideways.controller;

import Rideways.RidewaysAppController;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

/**
 * Testing the API results for the cars from Dave's case and the cheapest case.
 *
 * @author Vlad Mihai Vasile
 */

public class RidewaysAppControllerTest
{
    @Test
    public void testDaveApi()
    {
        String pickup = "51.470020,-0.454295";
        String dropoff = "3.410632,-2.157533";
        RidewaysAppController controller = new RidewaysAppController();
        ResponseEntity result = controller.daveApi(pickup, dropoff);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testCheapestApi()
    {

        String pickup = "51.470020,-0.454295";
        String dropoff = "3.410632,-2.157533";
        String passengers = "5";
        RidewaysAppController controller = new RidewaysAppController();
        ResponseEntity result = controller.cheapestApi(pickup, dropoff, passengers);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
