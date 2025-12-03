import com.lindstrom.item.Helmet;
import com.lindstrom.item.HockeyStick;
import com.lindstrom.item.Inventory;
import com.lindstrom.item.Skate;
import com.lindstrom.member.MemberRegistry;
import com.lindstrom.member.MembershipService;
import com.lindstrom.rental.RentalService;

public class Main {
    public static void main(String[] args) {


        Inventory inventory = new Inventory();
        MemberRegistry memberRegistry = new MemberRegistry();
        RentalService rentalService = new RentalService(inventory, memberRegistry);
        MembershipService membershipService = new MembershipService(memberRegistry);

        addSampleData(inventory, membershipService);
        Menu menu = new Menu(inventory, memberRegistry, rentalService, membershipService);
        menu.start();

    }

    private static void addSampleData(Inventory inventory, MembershipService membershipService) {

        //Medlemmar
        membershipService.addMember("Anna Andersson", "Junior");
        membershipService.addMember("Bertil Bengtsson", "Junior");
        membershipService.addMember("Cecilia Karlsson", "Senior");
        membershipService.addMember("David Danielsson", "Senior");
        membershipService.addMember("Emma Eriksson", "Senior");
        membershipService.addMember("Fanny Fransson", "Junior");
        membershipService.addMember("Göran Göransson", "Senior");
        membershipService.addMember("Håkan Henriksson", "Junior");
        membershipService.addMember("Isebelle Isaksson", "Senior");


        //Skyddsutrustning
        inventory.addItem(new Helmet("Medium", "CCN"));
        inventory.addItem(new Helmet("Small", "Bauer"));
        inventory.addItem(new Helmet("Large", "CCN"));
        inventory.addItem(new Helmet("Small", "Bauer"));
        inventory.addItem(new Helmet("Medium", "CCN"));
        inventory.addItem(new Helmet("Small", "Bauer"));
        inventory.addItem(new Helmet("Large", "CCN"));
        inventory.addItem(new Helmet("Medium", "Bauer"));
        inventory.addItem(new Helmet("Small", "CCN"));
        inventory.addItem(new Helmet("Large", "Bauer"));
        inventory.addItem(new Helmet("Small", "CCN"));
        inventory.addItem(new Helmet("Medium", "Bauer"));


        //Klubbor
        inventory.addItem(new HockeyStick("Vapor CCN", "Composite", "85", "Right"));
        inventory.addItem(new HockeyStick("Snake CCN", "Composite", "80", "Left"));
        inventory.addItem(new HockeyStick("Vapor CCN", "Composite", "85", "Right"));
        inventory.addItem(new HockeyStick("CTX CCN", "Composite", "80", "Left"));
        inventory.addItem(new HockeyStick("WARRIOR HOCKEY", "Composite", "90", "Right"));
        inventory.addItem(new HockeyStick("Tyke Bauer", "Composite", "100", "Left"));
        inventory.addItem(new HockeyStick("WARRIOR HOCKEY", "Composite", "80", "Right"));
        inventory.addItem(new HockeyStick("JETSPEED Bauer", "Composite", "85", "Right"));
        inventory.addItem(new HockeyStick("Pulse Bauer", "Composite", "85", "Left"));
        inventory.addItem(new HockeyStick("Vapor CCN", "Composite", "85", "Left"));

        //SKridskor
        inventory.addItem(new Skate("Jetspeed CCN", 38));
        inventory.addItem(new Skate("Jetspeed CCN", 39));
        inventory.addItem(new Skate("Jetspeed CCN", 37));
        inventory.addItem(new Skate("Jetspeed CCN", 40));
        inventory.addItem(new Skate("Jetspeed CCN", 41));
        inventory.addItem(new Skate("Jetspeed CCN", 42));
        inventory.addItem(new Skate("Jetspeed CCN", 43));
        inventory.addItem(new Skate("Jetspeed CCN", 44));
        inventory.addItem(new Skate("Jetspeed CCN", 36));


    }
}
