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
import java.util.TreeMap;

@RestController
@RequestMapping("/api")
public class RidewaysAppController
{
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

    @RequestMapping(value = "/cheapest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> cheapestApi(@RequestParam("coordinates") String coordinates)
    {


        return null;
    }
}