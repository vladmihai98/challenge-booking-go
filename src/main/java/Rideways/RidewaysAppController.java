package Rideways;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Controller for the API in the part2 of the BookingGo Challenge.
 * Returns the list of cars available from Dave in descending order or
 * The list of cars available from the cheapest supplier.
 *
 * @author Vlad Mihai Vasile
 */

@RestController
@RequestMapping("/api")
public class RidewaysAppController
{
    // http://localhost:8080/api/dave/?pickup=51.470020,-0.454295&dropoff=3.410632,-2.157533
    @RequestMapping(value = "/dave", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> daveApi(@RequestParam("pickup") String pickup, @RequestParam("dropoff") String dropoff)
    {
        ResponseEntity requestResult = null;
        String coordinates = "pickup=" + pickup + "&dropoff=" + dropoff;

        try
        {
            ArrayList<Car> results = RidewaysConsoleApplication.GetResultsFromDave(coordinates);

            if(results.isEmpty())
            {
                requestResult = new ResponseEntity("\"No cars could be found. Please try again later.\"", HttpStatus.OK);
            }
            else
            {
                JSONArray jsonArray = new JSONArray();
                results.forEach(car ->
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("car_type", car.getType());
                    jsonObject.put("price", car.getPrice());
                    jsonArray.put(jsonObject);
                });

                requestResult = new ResponseEntity(jsonArray.toString(), HttpStatus.OK);
            }
        }
        catch (JSONException ex)
        {
            requestResult = new ResponseEntity(ex.toString(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex)
        {
            requestResult = new ResponseEntity(ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return requestResult;
    }

    // http://localhost:8080/api/cheapest/?pickup=51.470020,-0.454295&dropoff=3.410632,-2.157533&passengers=5
    @RequestMapping(value = "/cheapest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> cheapestApi(@RequestParam("pickup") String pickup, @RequestParam("dropoff") String dropoff,
                                         @RequestParam("passengers") String passengers)
    {
        ResponseEntity requestResult = null;
        String coordinates = "pickup=" + pickup + "&dropoff=" + dropoff;
        int noOfPassengers = Integer.parseInt(passengers);

        try
        {
            ArrayList<Car> results = RidewaysConsoleApplication.GetCheapestResults(coordinates, noOfPassengers);

            if(results.isEmpty())
            {
                requestResult = new ResponseEntity("\"No cars could be found. Please try again later.\"", HttpStatus.OK);
            }
            else
            {
                JSONArray jsonArray = new JSONArray();
                results.forEach(car ->
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("car_type", car.getType());
                    jsonObject.put("supplied_id", car.getSupplier());
                    jsonObject.put("price", car.getPrice());
                    jsonArray.put(jsonObject);
                });

                requestResult = new ResponseEntity(jsonArray.toString(), HttpStatus.OK);
            }
        }
        catch (JSONException ex)
        {
            requestResult = new ResponseEntity(ex.toString(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex)
        {
            requestResult = new ResponseEntity(ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return requestResult;
    }
}