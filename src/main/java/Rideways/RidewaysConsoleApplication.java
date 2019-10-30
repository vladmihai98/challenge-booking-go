package Rideways;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RidewaysConsoleApplication
{
    private static final HashMap<String, Integer> carTypeCapacity = new HashMap<String, Integer>();
    private static final OkHttpClient okHttpClient = new OkHttpClient();
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

        //ConfigureHttpClient();

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

        System.out.println("eof");
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
        //okHttpClient = new OkHttpClient.Builder()
                //.connectTimeout(10, TimeUnit.SECONDS)
                //.writeTimeout(10, TimeUnit.SECONDS)
                //.readTimeout(2, TimeUnit.SECONDS)
                //.build();
    }

    private static void GetResultsFromDave(String coordinates)
    {
        try
        {
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
                    System.out.println(carType + " - " + price);
                }

                System.out.println("Finished results dave");
            }
        }
        catch (Exception ex)
        {
            //TODO
            System.out.println("bummer" + ex);
        }
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

    }

    public static String SyncGet(String url) throws Exception
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
        System.out.println("Finished request");
        return result;
    }
}
