package Rideways;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Console application to handle input for the Part1 of the BookingGo challenge.
 *
 * @author Vlad Mihai Vasile
 */
public class RidewaysConsoleApplication
{
    private static final HashMap<String, Integer> carTypeCapacity = new HashMap<String, Integer>();
    private static OkHttpClient okHttpClient = null;
    private static final String DAVE_URL = "https://techtest.rideways.com/dave/?";
    private static final String ERIC_URL = "https://techtest.rideways.com/eric/?";
    private static final String JEFF_URL = "https://techtest.rideways.com/jeff/?";

    public static void main(String[] args)
    {
        if(DisplayProgramDetails(args))
        {
            return;
        }

        // Get the option from the user: Dave or cheapest or null if illegal format.
        String option = HandleArguments(args);
        if(option == null)
        {
            return;
        }

        // We're ready to go so build the OkHttpClient.
        ConfigureHttpClient();

        if(option.equals("Dave"))
        {
            GetResultsFromDave(args[1]);
        }
        else if(option.equals("cheapest"))
        {
            int passengers = 0;
            try
            {
                passengers = Integer.parseInt(args[2]);
            }
            catch (Exception ex)
            {
                System.out.println("Missing or incorrect arguments. Run with --help for details.");
                return;
            }

            PopulateCarHashMap();
            GetCheapestResults(args[1], passengers);
        }
    }

    private static boolean DisplayProgramDetails(String[] args)
    {
        if(args.length == 0 ||
          (args.length == 1 && args[0].equals("--help")))
        {
            System.out.println("Welcome to the Rides Team. You can choose one of the options below:");
            System.out.println("Dave - to get search results in descending order. Latitude and longitude are needed for both pickup and dropoff.");
            System.out.println("e.g.: RidewaysConsoleApplication Dave pickup=51.470020,-0.454295&dropoff=3.410632,-2.157533");

            System.out.println("cheapest - to get search results of the cheapest suppliers. Additional parameter: passenger capacity.");
            System.out.println("e.g.: RidewaysConsoleApplication cheapest pickup=51.470020,-0.454295&dropoff=3.410632,-2.157533 5");
            return true;
        }
        return false;
    }

    private static String HandleArguments(String[] args)
    {
        String option = null;

        if(args.length >= 1 && !args[0].equals("--help"))
        {
            if(args[0].equals("Dave"))
            {
                if(args.length != 2)
                {
                    System.out.println("Missing or incorrect arguments. Run with --help for details.");
                    return option;
                }
                option = "Dave";
            }
            else if(args[0].equals("cheapest"))
            {
                if(args.length != 3)
                {
                    System.out.println("Missing or incorrect arguments. Run with --help for details.");
                    return option;
                }
                option = "cheapest";
            }
            else
            {
                System.out.println("Missing or incorrect arguments. Run with --help for details.");
                return option;
            }
        }

        return option;
    }

    private static void ConfigureHttpClient()
    {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS) // as request allow 2s for a response
                .build();
    }

    private static void GetResultsFromDave(String coordinates)
    {
        try
        {
            TreeMap<Integer, String> orderedOptions = new TreeMap<Integer, String>(Collections.reverseOrder());

            // Make an HTTP request to Dave's taxis.
            String result = SyncGet(DAVE_URL + coordinates);
            if(result != null)
            {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray options = jsonObject.getJSONArray("options");

                for(int i = 0; i < options.length(); i++)
                {
                    JSONObject subObject = options.getJSONObject(i);
                    String carType = subObject.getString("car_type");
                    int price = subObject.getInt("price");
                    orderedOptions.put(price, carType);
                }

                // Iterate over the collection and display the results.
                for(Map.Entry<Integer, String> entry : orderedOptions.entrySet())
                {
                    Integer price = entry.getKey();
                    String carType = entry.getValue();
                    System.out.println(carType + " - " + price);
                }
            }
        }
        catch (Exception ex)
        {
            //TODO remove exception from message.
            System.out.println("Exception retrieving details from Dave's API. Please try again." + ex);
        }
    }

    private static HashMap<String, Integer> BuildHashMapFromHttpResult(String json, int passengers)
    {
        HashMap<String, Integer> result = new HashMap<>();

        if(json != null)
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray options = jsonObject.getJSONArray("options");

            for(int i = 0; i < options.length(); i++)
            {
                JSONObject subObject = options.getJSONObject(i);
                String carType = subObject.getString("car_type");
                int price = subObject.getInt("price");

                // Consider only the cars that can fit in all passengers.
                if(carTypeCapacity.get(carType) >= passengers)
                {
                    result.put(carType, price);
                }
            }
        }

        return result;
    }

    private static void PopulateCarHashMap()
    {
        // Populate the hash map with values
        if(carTypeCapacity.isEmpty())
        {
            carTypeCapacity.put("STANDARD", 4);
            carTypeCapacity.put("EXECUTIVE", 4);
            carTypeCapacity.put("LUXURY", 4);
            carTypeCapacity.put("PEOPLE_CARRIER", 6);
            carTypeCapacity.put("LUXURY_PEOPLE_CARRIER", 6);
            carTypeCapacity.put("MINIBUS", 16);
        }
    }

    private static void GetCheapestResults(String coordinates, int passengers)
    {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        AtomicReference<HashMap<String, Integer>> davesTaxis = new AtomicReference<>(new HashMap<>());
        AtomicReference<HashMap<String, Integer>> ericsTaxis = new AtomicReference<>(new HashMap<>());
        AtomicReference<HashMap<String, Integer>> jeffsTaxis = new AtomicReference<>(new HashMap<>());

        // Run the requests in parallel since they do not depend on each other.
        // Similar to C# Task.Run()
        executorService.execute(() ->
        {
            try
            {
                String json = SyncGet(DAVE_URL + coordinates);
                davesTaxis.set(BuildHashMapFromHttpResult(json, passengers));
            }
            catch (Exception e)
            {
                System.out.println("Exception getting taxis from Dave: " + e);
            }
        });
        executorService.execute(() ->
        {
            try
            {
                String json = SyncGet(ERIC_URL + coordinates);
                ericsTaxis.set(BuildHashMapFromHttpResult(json, passengers));
            }
            catch (Exception e)
            {
                System.out.println("Exception getting taxis from Eric: " + e);
            }
        });
        executorService.execute(() ->
        {
            try
            {
                String json = SyncGet(JEFF_URL + coordinates);
                jeffsTaxis.set(BuildHashMapFromHttpResult(json, passengers));
            }
            catch (Exception e)
            {
                System.out.println("Exception getting taxis from Jeff: " + e);
            }
        });

        // Shutdown the executor.
        executorService.shutdown();
        try
        {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex)
        {
            System.out.println("Exception terminating: " + ex);
        }

        HashMap<String, Integer> filteredDave = davesTaxis.get();
        HashMap<String, Integer> filteredEric = ericsTaxis.get();
        HashMap<String, Integer> filteredJeff = jeffsTaxis.get();

        BuildAndPrintResults(filteredDave, filteredEric, filteredJeff);
    }

    private static void BuildAndPrintResults(HashMap<String, Integer> dave, HashMap<String, Integer> eric,
                                             HashMap<String, Integer> jeff)
    {
        // Iterate over the collection of car types
        // Update provider and price based on whether we find the carType in the respective collection
        // And if it is cheaper than the current price.
        for(Map.Entry<String, Integer> entry : carTypeCapacity.entrySet())
        {
            String carType = entry.getKey();
            String currentProvider = "";
            int currentPrice = Integer.MAX_VALUE;

            // Check to see if Dave has the car and at a better price.
            if(dave.containsKey(carType))
            {
                if(currentPrice > dave.get(carType))
                {
                    currentPrice = dave.get(carType);
                    currentProvider = "Dave";
                }
            }

            // Check to see if Eric has the car and at a better price.
            if(eric.containsKey(carType))
            {
                if(currentPrice > eric.get(carType))
                {
                    currentPrice = eric.get(carType);
                    currentProvider = "Eric";
                }
            }

            // Check to see if Jeff has the car and at a better price.
            if(jeff.containsKey(carType))
            {
                if(currentPrice > jeff.get(carType))
                {
                    currentPrice = jeff.get(carType);
                    currentProvider = "Jeff";
                }
            }

            // If we did find a car and a provider we can print it.
            if(!currentProvider.isEmpty())
            {
                System.out.println(carType + " - " + currentProvider + " - " + currentPrice);
            }
        }
    }

    private static String SyncGet(String url) throws Exception
    {
        String result = null;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = okHttpClient.newCall(request).execute())
        {
            if (!response.isSuccessful())
            {
                throw new IOException("Unexpected code " + response);
            }

            result = response.body().string();
        }
        return result;
    }
}
