import com.lindstrom.model.*;
import com.lindstrom.pricing.JuniorPricePolicy;
import com.lindstrom.pricing.PricePolicy;
import com.lindstrom.pricing.StandardPricePolicy;
import com.lindstrom.repository.Inventory;
import com.lindstrom.repository.MemberRegistry;
import com.lindstrom.service.MembershipService;
import com.lindstrom.service.RentalService;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MainApp extends Application {
    private final MemberRegistry memberRegistry = new MemberRegistry();
    private final Inventory inventory = new Inventory();
    private final RentalService rentalService = new RentalService();
    private final MembershipService membershipService = new MembershipService(memberRegistry);
    private final TableView<Member> memberTableView = new TableView<>();
    private final TableView<Item> helmetTable = new TableView<>();
    private final TableView<Item> hockeyStickTable = new TableView<>();
    private final TableView<Item> skateTable = new TableView<>();
    private final TableView<Rental> rentalTable = new TableView<>();
    private final List<Rental> rentals = new ArrayList<>();
    private final Label statusLabel = new Label();
    private final Label revenueLabel = new Label();
    private final ObservableList<Item> helmetItems = FXCollections.observableArrayList();
    private final ObservableList<Item> hockeyStickItems = FXCollections.observableArrayList();
    private final ObservableList<Item> skateItems = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        loadDataFromFile();

        //Tabeller
        setupMemberTableView();
        setupSeperateTableView();
        setupRentalTable();
        updateStats();


        //Knapp för att hyra
        Button rentButton = new Button("Hyr vald artikel");
        rentButton.setOnAction(e -> handleRent());

        Button returnButton = new Button("Avsluta uthyrning");
        returnButton.setOnAction(e -> handleReturn());


        //Medlemskontroller
        TextField nameField = new TextField();
        nameField.setPromptText("Nytt namn");
        ComboBox<String> levelCombo = new ComboBox<>();
        levelCombo.getItems().addAll("Junior", "Senior");
        levelCombo.setValue("Junior");
        TextField searchField = new TextField();
        searchField.setPromptText("Sök namn");

        //Knappar för Medlemmar
        Button addBtn = new Button("Lägg till");
        addBtn.setOnAction(e -> addMember(nameField, levelCombo));

        Button updateBtn = new Button("Ändra nivå");
        updateBtn.setOnAction(e -> updateSelectedMember(levelCombo));

        Button deleteBtn = new Button("Ta bort");
        deleteBtn.setOnAction(e -> deleteSelectedMember());

        HBox buttonRow = new HBox(10, addBtn, updateBtn, deleteBtn);

        //Sökfunktion
        searchField.textProperty().addListener((observable, oldText, newText) ->
                filterMembers(newText));

        //Medlemssektion med kontroller
        VBox memberSection = new VBox(10,
                new Label("Medlemmar;"),
                new HBox(10, new Label("Namn: "), nameField, new Label("Nivå: "), levelCombo, searchField),
                buttonRow,
                memberTableView
        );


        // Medlemssektion med kontroller
        ComboBox<String> itemTypeCombo = new ComboBox<>();


        // Item kontroller
        itemTypeCombo.getItems().

                addAll("Hjälm", "Klubba", "Skridskor");
        itemTypeCombo.setValue("Hjälm");

        TextField itemBrandField = new TextField();
        itemBrandField.setPromptText("Märke");

        TextField itemSizeField = new TextField();
        itemSizeField.setPromptText("Storlek");

        TextField itemFlexField = new TextField();
        TextField itemHandField = new TextField();
        TextField itemMaterialField = new TextField();
        itemFlexField.setPromptText("Flex");
        itemHandField.setPromptText("Hand (Left/Right)");
        itemMaterialField.setPromptText("Material");


        Button addItemBtn = new Button("Lägg till Item");
        addItemBtn.setOnAction(e -> addItem(itemTypeCombo, itemBrandField, itemSizeField,
                itemFlexField, itemHandField, itemMaterialField));

        HBox sizeRow = new HBox(10,
                new Label("Storlek:"), itemSizeField,
                new Label("Flex:"), itemFlexField,
                new Label("Hand:"), itemHandField,
                new Label("Material:"), itemMaterialField
        );

        VBox content = new VBox(10,
                new Label("Medlemmar:"),
                new HBox(10, new Label("Namn:"), nameField, new Label("Nivå:"), levelCombo, searchField),
                buttonRow,
                memberTableView,

                new Label("Lägg till Items:"),
                new HBox(10, new Label("Typ:"), itemTypeCombo, new Label("Märke:"), itemBrandField),
                sizeRow,
                addItemBtn,
                new Label("Hjälmar:"), helmetTable,
                new Label("Klubbor:"), hockeyStickTable,
                new Label("Skridskor:"), skateTable,
                new Label("Uthyrningar:"), rentalTable,
                new HBox(10, new Label("Statistik:"), statusLabel, revenueLabel),
                new HBox(10, rentButton, returnButton)
        );
        content.setStyle("-fx-padding: 12;");
        content.setStyle("-fx-padding: 12;");

        primaryStage.setTitle("Medlemsklubb - Wigells Hockey Uthyrning");
        primaryStage.setScene(new Scene(content, 1500, 1000));
        primaryStage.show();


    }

    private void loadDataFromFile() {
        try {
            membershipService.loadFromFile("members.txt");

            System.out.println("Data laddad från filer!");
        } catch (IOException e) {
            System.err.println("Fil saknas, laddar standarddata...");
            loadDefaultData();
        }
    }

    private void setupMemberTableView() {
        TableColumn<Member, String> nameCol = new TableColumn<>("Namn");
        nameCol.setCellValueFactory(d -> new
                javafx.beans.property.SimpleStringProperty(d.getValue().getName()));
        TableColumn<Member, String> levelCol = new TableColumn<>("Nivå");
        levelCol.setCellValueFactory(d -> new
                javafx.beans.property.SimpleStringProperty(d.getValue().getLevel()));

        memberTableView.getColumns().addAll(nameCol, levelCol);
        memberTableView.setItems(FXCollections.observableArrayList(memberRegistry.getAllMembers()));
    }


    private void setupSeperateTableView() {
        helmetItems.setAll(inventory.getAllItems().stream()
                .filter(item -> item instanceof Helmet)
                .collect(Collectors.toList()));
        helmetTable.setItems(helmetItems);
        setupHelmetColumns();

        hockeyStickItems.setAll(inventory.getAllItems().stream()
                .filter(item -> item instanceof HockeyStick)
                .collect(Collectors.toList()));
        hockeyStickTable.setItems(hockeyStickItems);
        setupHockeyStickColumns();

        skateItems.setAll(inventory.getAllItems().stream()
                .filter(item -> item instanceof Skate)
                .collect(Collectors.toList()));
        skateTable.setItems(skateItems);
        setupSkateColumns();
    }

    private void setupSkateColumns() {
        TableColumn<Item, String> brandCol = new TableColumn<>("Märke");
        brandCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getBrand()));

        TableColumn<Item, Integer> sizeCol = new TableColumn<>("Storlek");
        sizeCol.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(
                ((Skate) d.getValue()).getSize()).asObject());

        TableColumn<Item, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().isAvailable() ? "Ledig" : "Uthyrd"));

        skateTable.getColumns().addAll(brandCol, sizeCol, statusCol);

    }

    private void setupHockeyStickColumns() {
        TableColumn<Item, String> modelCol = new TableColumn<>("Material");
        modelCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                ((HockeyStick) d.getValue()).getMaterial()));

        TableColumn<Item, String> handCol = new TableColumn<>("Hand");
        handCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                ((HockeyStick) d.getValue()).getHand()));
        TableColumn<Item, String> flexCol = new TableColumn<>("Flex");
        flexCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                ((HockeyStick) d.getValue()).getFlex()));

        TableColumn<Item, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().isAvailable() ? "Ledig" : "Uthyrd"));

        hockeyStickTable.getColumns().addAll(modelCol, handCol, flexCol, statusCol);

    }

    private void setupHelmetColumns() {
        TableColumn<Item, String> sizeCol = new TableColumn<>("Storlek");
        sizeCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                ((Helmet) d.getValue()).getSize()));
        TableColumn<Item, String> brandCol = new TableColumn<>("Märke");
        brandCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getBrand()));

        TableColumn<Item, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().isAvailable() ? "Ledig" : "Uthyrd"));

        helmetTable.getColumns().addAll(sizeCol, brandCol, statusCol);
    }


    private void handleRent() {
        Member member = memberTableView.getSelectionModel().getSelectedItem();
        if (member == null) {
            showAlert("Fel", "Välj en medlem först.");
            return;
        }

        Item selectedItem = null;

        if (!helmetTable.getSelectionModel().isEmpty()) {
            selectedItem = helmetTable.getSelectionModel().getSelectedItem();
        } else if (!hockeyStickTable.getSelectionModel().isEmpty()) {
            selectedItem = hockeyStickTable.getSelectionModel().getSelectedItem();
        } else if (!skateTable.getSelectionModel().isEmpty()) {
            selectedItem = skateTable.getSelectionModel().getSelectedItem();
        }

        if (selectedItem == null) {
            showAlert("Fel", "Välj en artikel från någon av tabellerna!");
            return;
        }

        if (!selectedItem.isAvailable()) {
            showAlert("Fel", selectedItem.getBrand() + " är redan uthyrd.");
            return;
        }

        helmetTable.getSelectionModel().clearSelection();
        hockeyStickTable.getSelectionModel().clearSelection();
        skateTable.getSelectionModel().clearSelection();


        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);

        PricePolicy pricePolicy = member.getLevel().equalsIgnoreCase("Junior")
                ? new JuniorPricePolicy() : new StandardPricePolicy();
        rentalService.rentItem(member, selectedItem, 3, pricePolicy);

        Rental rental = new Rental(member, selectedItem, startDate, endDate);
        rentals.add(rental);

        rentalTable.setItems(FXCollections.observableArrayList(rentals));

        showAlert("Klart,", selectedItem.getBrand() + " hyrdes ut till " + member.getName() + ".");

        selectedItem.setAvailable(false);
        helmetTable.refresh();
        hockeyStickTable.refresh();
        skateTable.refresh();

        updateStats();
    }

    private void handleReturn() {
        Rental selectedRental = rentalTable.getSelectionModel().getSelectedItem();
        if (selectedRental == null) {
            showAlert("Fel ", "Välj en uthyrning från tabellen först!");
            return;
        }
        Item item = selectedRental.getItem();
        Member member = selectedRental.getMember();

        //Återställer artikelns tillgänglighet
        item.setAvailable(true);

        helmetTable.refresh();
        hockeyStickTable.refresh();
        skateTable.refresh();

        // Ta bort från uthyrningslistan
        rentals.remove(selectedRental);

        //Slutdatum till idag
        selectedRental.setEndDate(LocalDate.now());


        //Uppdaterar tabeller
        rentalTable.setItems(FXCollections.observableArrayList(rentals));

        showAlert("Klart!", item.getBrand() + " är nu ledig igen (retunerad från " + member.getName() + ").");

        updateStats();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addMember(TextField nameField, ComboBox<String> levelCombo) {
        String name = nameField.getText().trim();
        String level = levelCombo.getValue();

        if (name.isEmpty()) {
            showAlert("Fel", "Ange ett namn!");
            return;
        }

        memberRegistry.addMember(new Member(name, level));
        refreshMemberTable();
        nameField.clear();
        levelCombo.setValue("Junior");
        showAlert("Klart!", "Ny medlem tillagd: " + name);
    }


    private void updateSelectedMember(ComboBox<String> levelCombo) {
        Member selectedMember = memberTableView.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            showAlert("Fel ", "Välj en melem först!");
            return;
        }


        String newLevel = levelCombo.getValue();
        if (newLevel == null) {
            showAlert("Fel ", "Välj en nivå!");
            return;
        }

        memberRegistry.getAllMembers().remove(selectedMember);
        memberRegistry.addMember(new Member(selectedMember.getName(), newLevel));

        refreshMemberTable();
        showAlert("Klart ", selectedMember.getName() + " ändrades till " + newLevel + ".");
    }

    private void filterMembers(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            memberTableView.setItems(FXCollections.observableArrayList(memberRegistry.getAllMembers()));
        } else {
            List<Member> filtered = memberRegistry.getAllMembers().stream()
                    .filter(m ->
                            m.getName().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
            memberTableView.setItems(FXCollections.observableArrayList(filtered));
        }
    }

    private void refreshMemberTable() {
        memberTableView.setItems(FXCollections.observableArrayList(memberRegistry.getAllMembers()));
    }

    private void clearFields(TextField nameField, TextField searchField) {
        if (nameField != null) nameField.clear();
        if (searchField != null) searchField.clear();
    }

    private void deleteSelectedMember() {
        Member selectedMember = memberTableView.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            memberRegistry.getAllMembers().remove(selectedMember);
            refreshMemberTable();
            showAlert("Klart ", "Medlem borttagen!");
        } else {
            showAlert("Fel ", "Välj en medlem först!");
        }
    }

    private void addItem(ComboBox<String> typeCombo, TextField brandField, TextField sizeField,
                         TextField flexField, TextField handField, TextField materialField) {
        String type = typeCombo.getValue();
        String brand = brandField.getText().trim();

        if (brand.isEmpty() || type == null) {
            showAlert("Fel", "Ange märke och typ!");
            return;
        }

        switch (type) {
            case "Hjälm":
                String helmetSize = sizeField.getText().trim();
                if (helmetSize.isEmpty()) {
                    showAlert("Fel", "Hjälm behöver storlek!");
                    return;
                }
                inventory.addItem(new Helmet(helmetSize, brand));
                break;

            case "Klubba":
                String flex = flexField.getText().trim();
                String hand = handField.getText().trim();
                if (flex.isEmpty() || hand.isEmpty()) {
                    showAlert("Fel", "Klubba behöver flex och hand!");
                    return;
                }
                inventory.addItem(new HockeyStick(brand, materialField.getText().trim(), flex, hand));
                break;

            case "Skridskor":
                try {
                    int size = Integer.parseInt(sizeField.getText().trim());
                    inventory.addItem(new Skate(brand, size));
                } catch (NumberFormatException e) {
                    showAlert("Fel", "Skridskostorlek måste vara siffra!");
                    return;
                }
                break;
        }

        // Uppdatera tabeller och rensa fält
        setupSeperateTableView();
        clearItemFields(brandField, sizeField, flexField, handField, materialField);
        showAlert("Klart!", "Ny " + type + " tillagd: " + brand);
    }

    private void clearItemFields(TextField brandField, TextField sizeField,
                                 TextField flexField, TextField handField, TextField materialField) {
        brandField.clear();
        sizeField.clear();
        flexField.clear();
        handField.clear();
        materialField.clear();
    }

    private void setupRentalTable() {
        TableColumn<Rental, String> memberCol = new TableColumn<>("Medlem");
        memberCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getMember().getName()));

        TableColumn<Rental, String> itemCol = new TableColumn<>("Artikel");
        itemCol.setCellValueFactory(d -> new
                SimpleStringProperty(d.getValue().getItem().getBrand()));

        TableColumn<Rental, String> typeCol = new TableColumn<>("Typ");
        typeCol.setCellValueFactory(d -> {
            Item item = d.getValue().getItem();
            if (item instanceof Helmet) return new SimpleStringProperty("Hjälm");
            if (item instanceof HockeyStick) return new SimpleStringProperty("Hockeyklubba");
            return new SimpleStringProperty("Skridskor");
        });
        TableColumn<Rental, LocalDate> startDateCol = new TableColumn<>("Start");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));


        TableColumn<Rental, String> costCol = new TableColumn<>("Kostnad");
        costCol.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("%.2f kr", d.getValue().getTotalCost(
                        d.getValue().getMember().getLevel().equalsIgnoreCase("Junior") ?
                                new JuniorPricePolicy() : new StandardPricePolicy()))));

        rentalTable.getColumns().addAll(memberCol, itemCol, typeCol, startDateCol, costCol);
        rentalTable.setItems(FXCollections.observableArrayList(rentals));


    }

    private void updateStats() {
        int activeRentals = rentals.size();
        double totalRevenue = 0;

        for (Rental rental : rentals) {
            PricePolicy policy =
                    rental.getMember().getLevel().equalsIgnoreCase("Junior")
                            ? new JuniorPricePolicy() : new StandardPricePolicy();
            totalRevenue += rental.getTotalCost(policy);
        }
        statusLabel.setText("Aktiva uthyrningar: " + activeRentals);
        revenueLabel.setText(String.format("Total intäkt: %.2f kr", totalRevenue));
    }

    private void loadDefaultData() {
        //Medlemmar
        memberRegistry.addMember(new Member("Anna Andersson", "Junior"));
        memberRegistry.addMember(new Member("Bertil Bengtsson", "Junior"));
        memberRegistry.addMember(new Member("Cecilia Karlsson", "Senior"));
        memberRegistry.addMember(new Member("David Danielsson", "Senior"));
        memberRegistry.addMember(new Member("Emma Eriksson", "Senior"));
        memberRegistry.addMember(new Member("Fanny Fransson", "Junior"));
        memberRegistry.addMember(new Member("Göran Göransson", "Senior"));
        memberRegistry.addMember(new Member("Håkan Henriksson", "Junior"));
        memberRegistry.addMember(new Member("Isebelle Isaksson", "Senior"));


        //Skyddsutrustning
        inventory.addItem(new Helmet("CCM", "Medium"));
        inventory.addItem(new Helmet("Bauer", "Small"));
        inventory.addItem(new Helmet("CCM", "Large"));
        inventory.addItem(new Helmet("Bauer", "Small"));
        inventory.addItem(new Helmet("CCM", "Medium"));
        inventory.addItem(new Helmet("Bauer", "Small"));
        inventory.addItem(new Helmet("CCM", "Large"));
        inventory.addItem(new Helmet("Bauer", "Medium"));
        inventory.addItem(new Helmet("CCM", "Small"));
        inventory.addItem(new Helmet("Bauer", "Large"));
        inventory.addItem(new Helmet("CCM", "Small"));
        inventory.addItem(new Helmet("Bauer", "Medium"));


        //Klubbor
        inventory.addItem(new HockeyStick("Vapor CCM", "Composite", "85", "Right"));
        inventory.addItem(new HockeyStick("Snake CCM", "Composite", "80", "Left"));
        inventory.addItem(new HockeyStick("Vapor CCM", "Composite", "85", "Right"));
        inventory.addItem(new HockeyStick("CTX CCM", "Composite", "80", "Left"));
        inventory.addItem(new HockeyStick("WARRIOR HOCKEY", "Composite", "90", "Right"));
        inventory.addItem(new HockeyStick("Tyke Bauer", "Composite", "100", "Left"));
        inventory.addItem(new HockeyStick("WARRIOR HOCKEY", "Composite", "80", "Right"));
        inventory.addItem(new HockeyStick("JETSPEED Bauer", "Composite", "85", "Right"));
        inventory.addItem(new HockeyStick("Pulse Bauer", "Composite", "85", "Left"));
        inventory.addItem(new HockeyStick("Vapor CCM", "Composite", "85", "Left"));

        //SKridskor
        inventory.addItem(new Skate("Jetspeed CCM", 38));
        inventory.addItem(new Skate("Jetspeed CCM", 39));
        inventory.addItem(new Skate("Jetspeed CCM", 37));
        inventory.addItem(new Skate("Jetspeed CCM", 40));
        inventory.addItem(new Skate("Jetspeed CCM", 41));
        inventory.addItem(new Skate("Jetspeed CCM", 42));
        inventory.addItem(new Skate("Jetspeed CCM", 43));
        inventory.addItem(new Skate("Jetspeed CCM", 44));
        inventory.addItem(new Skate("Jetspeed CCM", 36));

    }

    public static void main(String[] args) {
        launch(args);
    }
}
