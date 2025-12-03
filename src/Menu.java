import com.lindstrom.item.Helmet;
import com.lindstrom.item.HockeyStick;
import com.lindstrom.item.Inventory;
import com.lindstrom.item.Skate;
import com.lindstrom.member.Member;
import com.lindstrom.member.MemberRegistry;
import com.lindstrom.member.MembershipService;
import com.lindstrom.rental.Rental;
import com.lindstrom.rental.RentalService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);

    private final Inventory inventory;
    private final MemberRegistry memberRegistry;
    private final RentalService rentalService;
    private final MembershipService membershipService;

    public Menu(Inventory inventory, MemberRegistry memberRegistry,
                RentalService rentalService, MembershipService membershipService) {
        this.inventory = inventory;
        this.memberRegistry = memberRegistry;
        this.rentalService = rentalService;
        this.membershipService = membershipService;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- VALBO HC Medlemsregister och uthyrningsregister ---");
            System.out.println("1. Medlemmar");
            System.out.println("2. Utrustning");
            System.out.println("3. Hyra/Avsluta Hyrningar");
            System.out.println("0. Avsluta");
            System.out.print("Ange val: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> memberMenu();
                case 2 -> equipmentMenu();
                case 3 -> rentalMenu();
                case 0 -> running = false;
                default -> System.out.println("Ogiltigt val, försök igen!");
            }
        }
        System.out.println("Avslutar programmet.");
    }

    private void memberMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Medlemsmeny ---");
            System.out.println("1. Lägg till medlem");
            System.out.println("2. Ta bort medlem");
            System.out.println("3. Lista medlemmar");
            System.out.println("4. Sök medlem efter namn");
            System.out.println("5. Lista medlemmar efter status");
            System.out.println("0. Tillbaka");
            System.out.print("Ange val: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> addMember();
                case 2 -> removeMember();
                case 3 -> listMembers();
                case 4 -> searchMember();
                case 5 -> filterMembersByStatus();
                case 0 -> inMenu = false;
                default -> System.out.println("Ogiltigt val, försök igen!");
            }
        }
    }

    private void equipmentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Utrustningsmeny ---");
            System.out.println("1. Lägg till hjälm");
            System.out.println("2. Lägg till skridsko");
            System.out.println("3. Lägg till klubba");
            System.out.println("4. Lista tillgängliga hjälmar");
            System.out.println("5. Lista tillgängliga skridskor");
            System.out.println("6. Lista tillgängliga klubbor");
            System.out.println("0. Tillbaka");
            System.out.print("Ange val: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> addHelmet();
                case 2 -> addSkate();
                case 3 -> addHockeyStick();
                case 4 -> listAvailableHemlet();
                case 5 -> listAvailableSkate();
                case 6 -> listAvailableHockeyStick();
                case 0 -> inMenu = false;
                default -> System.out.println("Ogiltigt val, försök igen!");
            }
        }
    }

    private void rentalMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Uthyrningsmeny ---");
            System.out.println("1. Hyr hjälm");
            System.out.println("2. Hyr skridskor");
            System.out.println("3. Hyr klubba");
            System.out.println("4. Avsluta hyrning");
            System.out.println("5. Visa pågående uthyrningar");
            System.out.println("6. Visa intäkter");
            System.out.println("0. Tillbaka");
            System.out.print("Ange val: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> rentHelmet();
                case 2 -> rentSkate();
                case 3 -> rentHockeyStick();
                case 4 -> endRental();
                case 5 -> listActiveRentals();
                case 6 -> showTotalRevenue();
                case 0 -> inMenu = false;
                default -> System.out.println("Ogiltigt val, försök igen!");
            }
        }
    }

    private void addMember() {
        System.out.print("Namn på medlem: ");
        String name = scanner.nextLine();
        System.out.print("Statusnivå (Standard/Junior): ");
        String status = scanner.nextLine();
        Member member = membershipService.addMember(name, status);
        System.out.println("Medlem tillagd: " + member);
    }

    private void removeMember() {
        System.out.print("Ange medlems-ID att ta bort: ");
        int id = readInt();
        if (membershipService.removeMember(id)) {
            System.out.println("Medlem borttagen.");
        } else {
            System.out.println("Medlemmen finns ej.");
        }
    }

    private void listMembers() {
        System.out.println("\n--- Medlemslista ---");
        for (Member m : membershipService.listAllMembers()) {
            System.out.println(m);
        }
    }

    private void searchMember() {
        System.out.print("Ange namn eller del av namn att söka: ");
        String searchTerm = scanner.nextLine();
        List<Member> results = membershipService.searchMembersByName(searchTerm);
        if (results.isEmpty()) {
            System.out.println("Inga medlemmar matchade sökningen.");
        } else {
            System.out.println("Matchande medlemmar:");
            for (Member m : results) {
                System.out.println(m);
            }
        }
    }

    private void filterMembersByStatus() {
        System.out.print("Ange status att filtrera på (Standard/Junior): ");
        String status = scanner.nextLine();
        List<Member> filtered = membershipService.filterMembersByStatus(status);
        if (filtered.isEmpty()) {
            System.out.println("Inga medlemmar med status " + status);
        } else {
            System.out.println("Medlemmar med status " + status + ":");
            for (Member m : filtered) {
                System.out.println(m);
            }
        }
    }

    private void addHelmet() {
        System.out.print("Märke: ");
        String brand = scanner.nextLine();
        System.out.print("Storlek: ");
        String size = scanner.nextLine();
        Helmet gear = new Helmet(brand, size);
        inventory.addItem(gear);
        System.out.println("Hjälm tillagd: " + gear);
    }

    private void addSkate() {
        System.out.print("Märke: ");
        String brand = scanner.nextLine();
        System.out.print("Storlek : ");
        int size = scanner.nextInt();
        Skate skate = new Skate(brand, size);
        inventory.addItem(skate);
        System.out.println("Skridsko tillagd: " + skate);
    }

    private void addHockeyStick() {
        System.out.print("Märke : ");
        String brand = scanner.nextLine();
        System.out.print("Flex på klubban : ");
        String flex = scanner.nextLine();
        System.out.print("Fattnig på klubban : ");
        String hand = scanner.nextLine();
        System.out.print("Material : ");
        String material = scanner.nextLine();
        HockeyStick stick = new HockeyStick(brand, material, flex, hand);
        inventory.addItem(stick);
        System.out.println("Hockeykklibba tillagd: " + stick);
    }

    private void listAvailableHemlet() {
        System.out.println("\n--- Tillgängliga hjälmar ---");
        for (Helmet h : inventory.filterByType(Helmet.class)) {
            if (h.isAvailable()) {
                System.out.println(h);
            }
        }
    }

    private void listAvailableSkate() {
        System.out.println("\n--- Tillgängliga skridskor ---");
        for (Skate s : inventory.filterByType(Skate.class)) {
            if (s.isAvailable()) {
                System.out.println(s);
            }
        }
    }

    private void listAvailableHockeyStick() {
        System.out.println("\n--- Tillgängliga klubbor ---");
        for (HockeyStick hs : inventory.filterByType(HockeyStick.class)) {
            if (hs.isAvailable()) {
                System.out.println(hs);
            }
        }
    }

    private void rentHelmet() {
        List<Member> listAllMembers = membershipService.listAllMembers();
        for (Member m : listAllMembers) {
            System.out.println(m);
        }
        List<Helmet> available = inventory.filterByType(Helmet.class);
        if (available.isEmpty()) {
            System.out.println("Ingen hjälm tillgängligt för uthyrning.");
            return;
        }
        System.out.println("\n--- Tillgängliga hjälmar ---");
        for (int i = 0; i < available.size(); i++) {
            Helmet h = available.get(i);
            System.out.println((i + 1) + ". " + h);
        }
        System.out.print("Ange medlems-ID: ");
        int memId = readInt();
        System.out.print("Välj hjälm med nummer: ");
        int choice = readIntInRange(1, available.size());
        Helmet selected = available.get(choice - 1);
        System.out.print("Startdatum (ÅÅÅÅ-MM-DD): ");
        LocalDate start = LocalDate.parse(scanner.nextLine());
        System.out.print("Slutdatum (ÅÅÅÅ-MM-DD): ");
        LocalDate end = LocalDate.parse(scanner.nextLine());
        Rental rental = rentalService.rentItem(memId, selected.getId(), start, end);
        if (rental != null) {
            System.out.println("Hyrning genomförd: " + rental);
        } else {
            System.out.println("Det gick ej att boka. Kontrollera medlems-ID och att hjälmen är ledig!");
        }
    }

    private void rentSkate() {
        List<Member> listAllMembers = membershipService.listAllMembers();
        for (Member m : listAllMembers) {
            System.out.println(m);
        }
        List<Skate> available = inventory.filterByType(Skate.class);
        if (available.isEmpty()) {
            System.out.println("Inga tillgängliga skridskor för uthyrning.");
            return;
        }
        System.out.println("\n--- Tillgängliga skridskor ---");
        for (int i = 0; i < available.size(); i++) {
            Skate s = available.get(i);
            System.out.println((i + 1) + ". " + s);
        }
        System.out.print("Ange medlems-ID: ");
        int memId = readInt();
        System.out.print("Välj skridsko med nummer: ");
        int choice = readIntInRange(1, available.size());
        Skate selected = available.get(choice - 1);
        System.out.print("Startdatum (ÅÅÅÅ-MM-DD): ");
        LocalDate start = LocalDate.parse(scanner.nextLine());
        System.out.print("Slutdatum (ÅÅÅÅ-MM-DD): ");
        LocalDate end = LocalDate.parse(scanner.nextLine());
        Rental rental = rentalService.rentItem(memId, selected.getId(), start, end);
        if (rental != null) {
            System.out.println("Hyrning genomförd: " + rental);
        } else {
            System.out.println("Det gick ej att boka. Kontrollera medlems-ID och att skridskon är ledig!");
        }
    }

    private void rentHockeyStick() {
        List<Member> listAllMembers = membershipService.listAllMembers();
        for (Member m : listAllMembers) {
            System.out.println(m);
        }
        List<HockeyStick> available = inventory.filterByType(HockeyStick.class);
        if (available.isEmpty()) {
            System.out.println("Inga tillgängliga klubbor för uthyrning.");
            return;
        }
        System.out.println("\n--- Tillgängliga klubbor ---");
        for (int i = 0; i < available.size(); i++) {
            HockeyStick hs = available.get(i);
            System.out.println((i + 1) + ". " + hs);
        }
        System.out.print("Ange medlems-ID: ");
        int memId = readInt();
        System.out.print("Välj klubba med nummer: ");
        int choice = readIntInRange(1, available.size());
        HockeyStick selected = available.get(choice - 1);
        System.out.print("Startdatum (ÅÅÅÅ-MM-DD): ");
        LocalDate start = LocalDate.parse(scanner.nextLine());
        System.out.print("Slutdatum (ÅÅÅÅ-MM-DD): ");
        LocalDate end = LocalDate.parse(scanner.nextLine());
        Rental rental = rentalService.rentItem(memId, selected.getId(), start, end);
        if (rental != null) {
            System.out.println("Hyrning genomförd: " + rental);
        } else {
            System.out.println("Det gick ej att boka. Kontrollera medlems-ID och att klubban är ledig!");
        }
    }

    private void endRental() {
        listActiveRentals();
        System.out.print("Ange utrustnings-ID för hyrning som ska avslutas: ");
        String itemId = scanner.nextLine();
        Rental rental = null;
        for (Rental r : rentalService.getActiveRentals()) {
            if (r.getItem().getId().equals(itemId)) {
                rental = r;
                break;
            }
        }
        if (rental == null) {
            System.out.println("Ingen pågående uthyrning för valt ID.");
            return;
        }
        System.out.print("Avslutsdatum (ÅÅÅÅ-MM-DD): ");
        LocalDate retDate = LocalDate.parse(scanner.nextLine());
        double total = rentalService.endRental(rental, retDate);
        System.out.println("Hyra avslutad. Slutpris: " + total + " kr");
    }

    private void listActiveRentals() {
        System.out.println("\n--- Pågående uthyrningar ---");
        for (Rental r : rentalService.getActiveRentals()) {
            System.out.println(r);
        }
    }

    private void showTotalRevenue() {
        System.out.println("Totala intäkter: " + rentalService.getTotalRevenue() + " kr");
    }

    private int readInt() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.print("Felaktig siffra, försök igen: ");
            }
        }
    }

    private int readIntInRange(int min, int max) {
        while (true) {
            int choice = readInt();
            if (choice >= min && choice <= max) {
                return choice;
            }
            System.out.print("Valet måste vara mellan " + min + " och " + max + ", försök igen: ");
        }
    }
}
