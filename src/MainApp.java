import com.lindstrom.model.*;
import com.lindstrom.pricing.JuniorPricePolicy;
import com.lindstrom.pricing.PricePolicy;
import com.lindstrom.pricing.StandardPricePolicy;
import com.lindstrom.repository.Inventory;
import com.lindstrom.repository.MemberRegistry;
import com.lindstrom.service.RentalService;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MainApp extends Application {
    private MemberRegistry memberRegistry = new MemberRegistry();
    private Inventory inventory = new Inventory();
    private RentalService rentalService = new RentalService();

    private TableView<Member> memberTableView = new TableView<>();
    private TableView<Item> itemTableView = new TableView<>();
    private TableView<Item> helmetTable = new TableView<>();
    private TableView<Item> hockeyStickTable = new TableView<>();
    private TableView<Item> skateTable = new TableView<>();
    private TableView<Rental> rentalTable = new TableView<>();
    private List<Rental> rentals = new ArrayList<>();
    private Label statusLabel = new Label();
    private Label revenueLabel = new Label();

    @Override
    public void start(Stage primaryStage) {
        //Data
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
//        ComboBox<String> itemTypeCombo = new ComboBox<>();
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


        VBox content = new VBox(10,
                new Label("Medlemmar:"),
                new HBox(10, new Label("Namn:"), nameField, new Label("Nivå:"), levelCombo, searchField),
                buttonRow,  // ← Dina knappar: Lägg till, Ändra, Ta bort
                memberTableView,

                new Label("Lägg till Items:"),
                new HBox(10, new Label("Typ:"), itemTypeCombo, new Label("Märke:"), itemBrandField),
                new HBox(10, new Label("Storlek:"), itemSizeField, new Label("Flex:"), itemFlexField, new Label("Hand:"), itemHandField),


                new Label("Hjälmar:"), helmetTable,
                new Label("Klubbor:"), hockeyStickTable,
                new Label("Skridskor:"), skateTable,
                new Label("Uthyrningar:"), rentalTable,
                new Label("Statistik:"), statusLabel, revenueLabel,
                new HBox(10,rentButton, returnButton )


        );
        content.setStyle("-fx-padding: 12;");
        content.setStyle("-fx-padding: 12;");

        primaryStage.setTitle("Medlemsklubb - Wigells Hockey Uthyrning");
        primaryStage.setScene(new Scene(content, 1500, 1000));
        primaryStage.show();



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
        //Hjälmar
        helmetTable.setItems(FXCollections.observableArrayList(
                inventory.getAllItems().stream()
                        .filter(item -> item instanceof Helmet)
                        .collect(Collectors.toList())
        ));
        setupHelmetColumns();
        // Hockeyklubbor
        hockeyStickTable.setItems(FXCollections.observableArrayList(
                inventory.getAllItems().stream()
                        .filter(item -> item instanceof HockeyStick)
                        .collect(Collectors.toList())
        ));
        setupHockeyStickColumns();
        //Skridskor
        skateTable.setItems(FXCollections.observableArrayList(
                inventory.getAllItems().stream()
                        .filter(item -> item instanceof Skate)
                        .collect(Collectors.toList())
        ));
        setupSkateColumns();
    }

    private void setupSkateColumns() {
        TableColumn<Item, String> brandCol = new TableColumn<>("Märke");
        brandCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                ((Skate) d.getValue()).getBrand()));

        TableColumn<Item, Integer> sizeCol = new TableColumn<>("Storlek");
        sizeCol.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(
                ((Skate) d.getValue()).getSize()).asObject());

        TableColumn<Item, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().isAvailable() ? "Ledig" : "Uthyrd"));

        skateTable.getColumns().addAll(brandCol, sizeCol,  statusCol);

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

        hockeyStickTable.getColumns().addAll(modelCol, handCol, flexCol,  statusCol);

    }

    private void setupHelmetColumns() {
        TableColumn<Item, String> sizeCol = new TableColumn<>("Storlek");
        sizeCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                ((Helmet) d.getValue()).getSize()));
        TableColumn<Item, String> brandCol = new TableColumn<>("Märke");
        brandCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                ((Helmet) d.getValue()).getBrand()));

        TableColumn<Item, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().isAvailable() ? "Ledig" : "Uthyrd"));

        helmetTable.getColumns().addAll(sizeCol, brandCol,  statusCol);
    }


    private void setupItemTableView() {
        TableColumn<Item, String> nameCol = new TableColumn<>("Artikel");
        nameCol.setCellValueFactory(d -> new
                javafx.beans.property.SimpleStringProperty(d.getValue().getBrand()));

        TableColumn<Item, String> typeCol = new TableColumn<>("Typ");
        typeCol.setCellValueFactory(d -> {
            Item item = d.getValue();
            if (item instanceof Helmet) return new
                    javafx.beans.property.SimpleStringProperty("Hjälm");
            if (item instanceof HockeyStick) return new
                    javafx.beans.property.SimpleStringProperty("Hockeyklubba");
            if (item instanceof Skate) return new
                    javafx.beans.property.SimpleStringProperty("Skridskor");
            return new javafx.beans.property.SimpleStringProperty("Okänd");
        });

        TableColumn<Item, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> new
                javafx.beans.property.SimpleStringProperty(d.getValue().isAvailable() ? "Ledig" : "Uthyrd"));

        itemTableView.getColumns().addAll(nameCol, statusCol);

        itemTableView.setItems(FXCollections.observableArrayList(inventory.getAllItems()));
    }

    private void handleRent() {
        Member member = memberTableView.getSelectionModel().getSelectedItem();
        if (member == null) {
            showAlert("Fel ", "Väljen medlem först.");
            return;
        }
        Item selectedItem = null;
        if (!helmetTable.getSelectionModel().isEmpty()) {
            selectedItem = helmetTable.getSelectionModel().getSelectedItem();
        } else if (!hockeyStickTable.getSelectionModel().isEmpty()) {
            selectedItem = hockeyStickTable.getSelectionModel().getSelectedItem();
        } else  if (!skateTable.getSelectionModel().isEmpty()) {
            selectedItem = skateTable.getSelectionModel().getSelectedItem();
        }

        if (selectedItem == null) {
            showAlert("Fel ", "Välj en artikel från någon av tabellerna!");
            return;
        }
        if (!selectedItem.isAvailable()) {
            showAlert("Fel ", selectedItem.getBrand() + " är redan uthyrd.");
            return;
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);

        PricePolicy pricePolicy = member.getLevel().equalsIgnoreCase("Junior")
                ? new JuniorPricePolicy() : new StandardPricePolicy();
        rentalService.rentItem(member, selectedItem, 3, pricePolicy);

        Rental rental = new Rental(member, selectedItem, startDate, endDate);
        rentals.add(rental);

        setupSeperateTableView();
        rentalTable.setItems(FXCollections.observableArrayList(rentals));

        showAlert("Klart,", selectedItem.getBrand() + " hyrdes ut till " + member.getName() + ".");

        setupSeperateTableView();
        rentalTable.setItems(FXCollections.observableArrayList(rentals));
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

        // Ta bort från uthyrningslistan
        rentals.remove(selectedRental);

        //Slutdatum till idag
        selectedRental.setEndDate(LocalDate.now());

        //Uppdaterar tabeller
        setupSeperateTableView();
        rentalTable.setItems(FXCollections.observableArrayList(rentals));

        showAlert("Klart!", item.getBrand() + " är nu ledig igen (retunerad från " +  member.getName() + ").");

        setupSeperateTableView();
        rentalTable.setItems(FXCollections.observableArrayList(rentals));
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
            showAlert("Fel ", "Ange ett namn! ");
            return;
        }

        memberRegistry.addMember(new Member(name, level));
        refreshMemberTable();
        clearFields(nameField, null);
        showAlert("Klart ", "Ny medlem tillagd: " + name);
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
        memberTableView.refresh();
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

    private void addItem(ComboBox<String> typeCombo, TextField nameField, TextField brandField, TextField sizeField,
                         TextField FlexField, TextField handField, TextField materialField) {
        String type = typeCombo.getValue();
        String brand = brandField.getText().trim();

        if (brand.isEmpty() || brand == null) {
            showAlert("Fel ", "Ange märke och typ");
            return;
        }
        switch (type) {
            case "Hjälm":
                inventory.addItem(new Helmet(sizeField.getText(), brand));
                setupSeperateTableView();  // Uppdatera tabeller
                break;
            case "Hockeyklubba":
                if (FlexField.getText().isEmpty() || handField.getText().isEmpty()) {
                    showAlert("Fel", "Klubba behöver flex och hand!");
                    return;
                }
                inventory.addItem(new HockeyStick(brand, "Composite", FlexField.getText(), handField.getText()));
                setupSeperateTableView();
                break;
            case "Skridskor":
                try {
                    int size = Integer.parseInt(sizeField.getText());
                    inventory.addItem(new Skate(brand, size));
                    setupSeperateTableView();
                } catch (NumberFormatException e) {
                    showAlert("Fel", "Skridskostorlek måste vara siffra!");
                }
                break;
        }

        clearItemFields(brandField, sizeField, FlexField, handField);
        showAlert("Klart ", "Ny " + type + " tillagd: " + brand + ".");
    }

    private void clearItemFields(TextField brandField, TextField sizeField, TextField flexField, TextField handField) {
        brandField.clear();
        sizeField.clear();
        flexField.clear();
        handField.clear();
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

    public static void main(String[] args) {
        launch(args);
    }
}
