package Rideways;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starting point of the API.
 *
 * @author Vlad Mihai Vasile
 */

@SpringBootApplication
@EnableAutoConfiguration
public class RidewaysAppApi
{
    public static void main(String[] args)
    {
        SpringApplication.run(RidewaysAppApi.class, args);
    }
}