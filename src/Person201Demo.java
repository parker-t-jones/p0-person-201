/**
 * Used in Compsci 201 to demonstrate understanding of basic
 * Java classes working together.
 * 
 * @author Owen Astrachan
 * @author YOUR NAME HERE
 * @version 2.0, modified for Fall 2025
 */


public class Person201Demo {
    public static void main(String[] args) {
        Person201 a = new Person201("claire",37.8044,-122.2712,"Vin Rouge");
        Person201 b = new Person201("ricardo",-1.2921, 36.8219,"Elmo's Diner");
        Person201 c = new Person201("julie",40.7081,-73.9571,"Alpaca");

        Person201[] data = {a,b,c};

        for(Person201 p : data) {
            System.out.println(p);
        }

        System.out.printf("names: %s, %s, %s\n",a.name(),b.name(),c.name());

        Person201 p = new Person201("Sam", 38.6, 90.19, "Common Market");                               
        Person201 q = new Person201("Fred", 41.88, 87.63, "Local 22");                             
                                                                                               
        double d = Person201Utilities.distance(p,q);
        System.out.printf("%s to %s distance = %1.3f\n",p.name(),q.name(),d);
    }
}
