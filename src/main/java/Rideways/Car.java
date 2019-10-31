package Rideways;

/**
 * Object to hold type, supplier and price.
 *
 * @author Vlad Mihai Vasile
 */
public class Car implements Comparable<Car>
{
    // TODO change to enum
    private String type;
    private String supplier;
    private int price;

    public Car()
    {
        this("", "", 0);
    }

    public Car(String type, int price)
    {
        this(type, "", price);
    }

    public Car(String type, String supplier, int price)
    {
        this.type = type;
        this.supplier = supplier;
        this.price = price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public int getPrice()
    {
        return price;
    }

    public void setSupplier(String supplier)
    {
        this.supplier = supplier;
    }

    public String getSupplier()
    {
        return supplier;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return type + " - " + price;
    }

    public String toStringSupplier()
    {
        return type + " - " + supplier + " - " + price;
    }

    @Override
    public int compareTo(Car o)
    {
        return Integer.compare(this.price, o.price);
    }
}
