//import com.lindstrom.model.Helmet;
//import com.lindstrom.model.HockeyStick;
//import com.lindstrom.repository.Inventory;
//import com.lindstrom.model.Skate;
//import com.lindstrom.model.Member;
//import com.lindstrom.repository.MemberRegistry;
//import com.lindstrom.service.MembershipService;
//import com.lindstrom.model.Rental;
//import com.lindstrom.service.RentalService;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.time.LocalDate;
//import java.util.stream.Collectors;
//
//public class Menu {
//    private final Inventory inventory;
//    private final MemberRegistry memberRegistry;
//    private final RentalService rentalService;
//    private final MembershipService membershipService;
//
//
//    public Menu(Inventory inventory, MemberRegistry memberRegistry,
//                RentalService rentalService, MembershipService membershipService) {
//        this.inventory = inventory;
//        this.memberRegistry = memberRegistry;
//        this.rentalService = rentalService;
//        this.membershipService = membershipService;
//
//    }
//    //Medlemmar
//
//    public Member addMember(String name, String status) {
//        return membershipService.addMember(name, status);
//    }
//
//    public boolean removeMember(int id) {
//        return membershipService.removeMember(id);
//    }
//
//    public ObservableList<Member> listAllMembers() {
//        return FXCollections.observableArrayList(membershipService.listAllMembers());
//    }

//    public ObservableList<Member> searchMembersByName(String searchName) {
//        return FXCollections.observableArrayList(membershipService.searchMembersByName(searchName));
//    }
//    public ObservableList<Member> getAllMembers() {
//        return FXCollections.observableArrayList(listAllMembers());
//    }
//
//    public ObservableList<Member> getMembersByName(String name) {
//        return FXCollections.observableArrayList(searchMembersByName(name));
//    }
//
//    //Uthyrning
//    public Helmet addHelmet(String brand, String size) {
//        Helmet helmet = new Helmet(brand, size);
//        inventory.addItem(helmet);
//        return helmet;
//    }
//
//    public Skate addSkate(String brand, int size) {
//        Skate skate = new Skate(brand, size);
//        inventory.addItem(skate);
//        return skate;
//    }
//
//    public HockeyStick addHockeyStick(String brand, String material, String flex, String hand) {
//        HockeyStick hockeyStick = new HockeyStick(brand, material, flex, hand);
//        inventory.addItem(hockeyStick);
//        return hockeyStick;
//    }
//
//    public ObservableList<Helmet> getAvailableHelmets() {
//        return FXCollections.observableList(
//                inventory.filterByType(Helmet.class).stream()
//                        .filter(Helmet::isAvailable)
//                        .collect(Collectors.toList())
//        );
//    }
//
//    public ObservableList<Skate> getAvailableSkates() {
//        return FXCollections.observableList(
//                inventory.filterByType(Skate.class).stream()
//                        .filter(Skate::isAvailable)
//                        .collect(Collectors.toList())
//        );
//    }
//
//    public ObservableList<HockeyStick> getAvailableHockeySticks() {
//        return FXCollections.observableList(
//                inventory.filterByType(HockeyStick.class).stream()
//                        .filter(HockeyStick::isAvailable)
//                        .collect(Collectors.toList())
//        );
//    }
//
//    public Rental rentHelmet(int memberId, String itemId, LocalDate startDate, LocalDate endDate) {
//        return rentalService.rentItem(memberId, itemId, startDate, endDate);
//    }
//
//    public Rental rentSkate(int memberId, String itemId, LocalDate startDate, LocalDate endDate) {
//        return rentalService.rentItem(memberId, itemId, startDate, endDate);
//    }
//
//    public Rental rentHockeyStick(int memberId, String itemId, LocalDate startDate, LocalDate endDate) {
//        return rentalService.rentItem(memberId, itemId, startDate, endDate);
//    }

//    public double endRental(Rental rental, LocalDate returnDate) {
//        if (rental == null || !rentalService.getActiveRentals().contains(rental)) {
//            return -1;
//        }
//
//        return rentalService.endRental(rental, returnDate);
//    }
//
//    public ObservableList<Rental> getAvailableRentals() {
//        return FXCollections.observableArrayList(rentalService.getActiveRentals());
//
//    }
//
//    public double getTotalRevenue() {
//        return rentalService.getTotalRevenue();
//    }
//
//
//
//    private static void addSampleData(Inventory inventory, MembershipService membershipService) {
//
//        //Medlemmar
//        membershipService.addMember("Anna Andersson", "Junior");
//        membershipService.addMember("Bertil Bengtsson", "Junior");
//        membershipService.addMember("Cecilia Karlsson", "Senior");
//        membershipService.addMember("David Danielsson", "Senior");
//        membershipService.addMember("Emma Eriksson", "Senior");
//        membershipService.addMember("Fanny Fransson", "Junior");
//        membershipService.addMember("Göran Göransson", "Senior");
//        membershipService.addMember("Håkan Henriksson", "Junior");
//        membershipService.addMember("Isebelle Isaksson", "Senior");
//
//
//        //Skyddsutrustning
//        inventory.addItem(new Helmet("Medium", "CCN"));
//        inventory.addItem(new Helmet("Small", "Bauer"));
//        inventory.addItem(new Helmet("Large", "CCN"));
//        inventory.addItem(new Helmet("Small", "Bauer"));
//        inventory.addItem(new Helmet("Medium", "CCN"));
//        inventory.addItem(new Helmet("Small", "Bauer"));
//        inventory.addItem(new Helmet("Large", "CCN"));
//        inventory.addItem(new Helmet("Medium", "Bauer"));
//        inventory.addItem(new Helmet("Small", "CCN"));
//        inventory.addItem(new Helmet("Large", "Bauer"));
//        inventory.addItem(new Helmet("Small", "CCN"));
//        inventory.addItem(new Helmet("Medium", "Bauer"));
//
//
//        //Klubbor
//        inventory.addItem(new HockeyStick("Vapor CCN", "Composite", "85", "Right"));
//        inventory.addItem(new HockeyStick("Snake CCN", "Composite", "80", "Left"));
//        inventory.addItem(new HockeyStick("Vapor CCN", "Composite", "85", "Right"));
//        inventory.addItem(new HockeyStick("CTX CCN", "Composite", "80", "Left"));
//        inventory.addItem(new HockeyStick("WARRIOR HOCKEY", "Composite", "90", "Right"));
//        inventory.addItem(new HockeyStick("Tyke Bauer", "Composite", "100", "Left"));
//        inventory.addItem(new HockeyStick("WARRIOR HOCKEY", "Composite", "80", "Right"));
//        inventory.addItem(new HockeyStick("JETSPEED Bauer", "Composite", "85", "Right"));
//        inventory.addItem(new HockeyStick("Pulse Bauer", "Composite", "85", "Left"));
//        inventory.addItem(new HockeyStick("Vapor CCN", "Composite", "85", "Left"));
//
//        //SKridskor
//        inventory.addItem(new Skate("Jetspeed CCN", 38));
//        inventory.addItem(new Skate("Jetspeed CCN", 39));
//        inventory.addItem(new Skate("Jetspeed CCN", 37));
//        inventory.addItem(new Skate("Jetspeed CCN", 40));
//        inventory.addItem(new Skate("Jetspeed CCN", 41));
//        inventory.addItem(new Skate("Jetspeed CCN", 42));
//        inventory.addItem(new Skate("Jetspeed CCN", 43));
//        inventory.addItem(new Skate("Jetspeed CCN", 44));
//        inventory.addItem(new Skate("Jetspeed CCN", 36));
//
//
//    }
//}
