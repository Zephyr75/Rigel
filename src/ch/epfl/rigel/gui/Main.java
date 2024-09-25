package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TreeSet;
import java.util.function.UnaryOperator;

import static javafx.beans.binding.Bindings.when;

// classe gérant généralisant l'affichage de tous les éléments du programme
public class Main extends Application {
    private final ObserverLocationBean observerLocationBean = new ObserverLocationBean();
    private final DateTimeBean dateTimeBean = new DateTimeBean();
    private SkyCanvasManager canvasManager;
    private final ObjectProperty<String> objectUnderMouse = new SimpleObjectProperty<>();
    private TimeAnimator timeAnimator;
    private ComboBox<ZoneId> timeZone = new ComboBox<>();
    private ComboBox<NamedTimeAccelerator> timeAccelerator = new ComboBox<>();
    private DatePicker datePicker;
    private TextField timeTextField;
    private Canvas sky;
    private CountryCatalogue countryCatalogue;

    Scene scene;

    public static void real_main(String[] args) { launch(args); }

    private InputStream resourceStream(String resourceName) { return getClass().getResourceAsStream(resourceName); }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * méthode permettant d'initialiser les différents paramètres nécessaires à la bonne exécution du programme
     * et de créer la fenêtre rassemblant les différents éléments à afficher
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try (InputStream hs = resourceStream("/hygdata_v3.csv")) {
            StarCatalogue starCatalogue = new StarCatalogue.starBuilder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE).loadFrom(getClass()
                            .getResourceAsStream("/asterisms.txt"), AsterismLoader.INSTANCE).build();

            try (InputStream cc = resourceStream("/country_centroids_az8.csv")) {
                countryCatalogue = new CountryCatalogue.Builder()
                        .countryLoadFrom(cc, CountryCentroidsLoader.INSTANCE).build();
            }


            //Initialisation
            dateTimeBean.setZonedDateTime(ZonedDateTime.now());
            timeAnimator = new TimeAnimator(dateTimeBean);
            timeAnimator.setAccelerator(NamedTimeAccelerator.TIMES_300.getAccelerator());
            observerLocationBean.setCoordinates(GeographicCoordinates.ofDeg(48.29, 4.08));
            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180.000000000001, 15));
            viewingParametersBean.setFieldOfViewDeg(100);
            objectUnderMouse.set("");

            canvasManager = new SkyCanvasManager(starCatalogue, dateTimeBean, observerLocationBean, viewingParametersBean);
            canvasManager.objectUnderMouseProperty().addListener(
                    (p, o, n) -> objectUnderMouse.set(n != null ? n.name() : ""));

            sky = canvasManager.canvas();
            Pane skyPane = new Pane(sky);
            sky.widthProperty().bind(skyPane.widthProperty());
            sky.heightProperty().bind(skyPane.heightProperty());


            BorderPane root = new BorderPane(skyPane, controlBar(primaryStage), null, informationBar(), null);
            scene = new Scene(root);

            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(800);
            primaryStage.setTitle("Rigel");
            primaryStage.setScene(scene);
            primaryStage.show();
            sky.requestFocus();
        }
    }


    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * gère la création de la barre d'information comprenant le nom de l'object sous la souris,
     * la position de la souris et le champ de vue de l'ulisateur en bas de la fenêtre
     * @return informationPane
     */
    private BorderPane informationBar(){
        Text fov = new Text();
        fov.textProperty().bind(
                Bindings.format(Locale.ROOT, "Champ de vue : %.1f°" , canvasManager.getFieldOfView()));
        Text coordinates = new Text();
        coordinates.textProperty().bind(
                Bindings.format(Locale.ROOT, "Azimut : %.1f° , Hauteur : %.1f°", canvasManager.getMouseAz(), canvasManager.getMouseAlt()));
        Text closestObject = new Text();

        closestObject.textProperty().bind(
                Bindings.format(Locale.ROOT, "%s", objectUnderMouse));

        BorderPane informationPane = new BorderPane(closestObject,null,coordinates,null,fov);
        informationPane.setStyle("-fx-padding: 4; -fx-background-color: white;");
        return informationPane;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * méthode regroupant les éléments de la barre de controle en haut de l'écran
     * @return controlBar
     */
    private HBox controlBar(Stage stage){
        HBox controlBar = new HBox();

        controlBar.getChildren().addAll( observationPosition(), observationInstant(), setTiming(), skyShot(stage));
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");
        return controlBar;
    }


    private void takeSkyShot(File selectedDirectory){
        canvasManager.setUiIsOn(0);
        WritableImage skyShot = sky.snapshot(null, null);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(skyShot, null), "png", selectedDirectory);
        } catch (IOException e) { }
    }


    private HBox skyShot(Stage stage)  {
        Button takeSkyShot = new Button("\uF083");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("Capture_du_ciel.png");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg")
        );

        InputStream fontStream = getClass().getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf");
        Font fontAwesome = Font.loadFont(fontStream, 15);
        takeSkyShot.setFont(fontAwesome);

        takeSkyShot.setOnAction(e -> {
            File selectedDirectory = fileChooser.showSaveDialog(stage);
            if (selectedDirectory != null) {
                takeSkyShot(selectedDirectory);
            }
        });

        HBox canvasShot = new HBox(takeSkyShot);
        takeSkyShot.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        return canvasShot;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * création d'une Hbox permettant de gérer les différents paramètres d'accélération
     * ainsi que les boutons nécessaire à son bon fonctionnement
     * @return setTiming
     */
    private HBox setTiming(){
        timeAccelerator.getItems().addAll(NamedTimeAccelerator.values());
        timeAccelerator.setValue(NamedTimeAccelerator.TIMES_300);

        timeAccelerator.setOnAction(e -> timeAnimator.setAccelerator(timeAccelerator.getValue().getAccelerator()));

        InputStream fontStream = getClass().getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf");
        Font fontAwesome = Font.loadFont(fontStream, 15);

        String resetImage = "\uf0e2";
        String playImage = "\uf04b";
        String playBackImage = "\uf04a";
        String pauseImage = "\uf04c";
        String playForwardImage = "\uF04E";
        Button resetButton = new Button(resetImage);
        resetButton.setFont(fontAwesome);
        resetButton.setOnAction(e ->
                dateTimeBean.setZonedDateTime(ZonedDateTime.now()));
        Button playPauseButton = new Button(playImage);
        playPauseButton.textProperty().bind(when(timeAnimator.running()).then(pauseImage).otherwise(playImage));
        playPauseButton.setFont(fontAwesome);
        Button playBackButton = new Button(playBackImage);
        Button playForwardButton = new Button(playForwardImage);
        playBackButton.setFont(fontAwesome);
        playForwardButton.setFont(fontAwesome);

        playPauseButton.setOnAction(e ->
        {   boolean buttonState = !timeAnimator.running().get();
            if (buttonState) {
                timeAnimator.setDirection(true);
                timeAnimator.start();
            } else {
                timeAnimator.stop();
            }
            playBackButton.setDisable(buttonState);
            playForwardButton.setDisable(buttonState);
            resetButton.setDisable(buttonState);
            datePicker.setDisable(buttonState);
            timeTextField.setDisable(buttonState);
            timeZone.setDisable(buttonState);
            timeAccelerator.setDisable(buttonState);
        } );

        playForwardButton.setOnMousePressed(e -> {
            timeAnimator.setDirection(true);
            timeAnimator.start();
        } );

        playForwardButton.setOnMouseReleased( e -> timeAnimator.stop());

        playBackButton.setOnMousePressed(e -> {
                timeAnimator.setDirection(false);
                timeAnimator.start();
        } );
        playBackButton.setOnMouseReleased( e -> timeAnimator.stop());


        HBox setTiming = new HBox(timeAccelerator, resetButton, playBackButton, playPauseButton, playForwardButton);
        setTiming.setStyle("-fx-spacing: inherit;");
        return setTiming;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * permet de gérer la date et l'heure d'observation du ciel
     * @return observationInstant
     */
    private HBox observationInstant(){
        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(stringConverter);

        Label date = new Label("Date :");
        datePicker = new DatePicker(dateTimeBean.getDate());
        datePicker.setStyle("-fx-pref-width: 120;");
        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateProperty());

        Label time = new Label("Heure :");
        timeTextField = new TextField();
        timeTextField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
        timeTextField.setTextFormatter(timeFormatter);
        timeFormatter.valueProperty().bindBidirectional(dateTimeBean.timeProperty());

        timeZone = new ComboBox<>();
        timeZone.setStyle("-fx-pref-width: 180;");
        TreeSet<String> test = new TreeSet<>(ZoneId.getAvailableZoneIds());
        for (String zoneId : test) {
            timeZone.getItems().add(ZoneId.of(zoneId));
        }
        timeZone.valueProperty().bindBidirectional(dateTimeBean.zoneIdProperty());

        HBox observationInstant = new HBox(date, datePicker, time,timeTextField, timeZone);
        observationInstant.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        return observationInstant;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * permet à l'utilisateur de régler sa position sur la surface de la terre en tapant sa longitude et sa lattitude d'observation
     * @return observationPosition
     */
    private HBox observationPosition() {
        Label longitudeLabel = new Label("Longitude (°) :");
        Label latitudeLabel = new Label("Latitude (°) :");

        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");
        UnaryOperator<TextFormatter.Change> lonFilter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newLonDeg = stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLonDeg(newLonDeg) ? change : null;
            } catch (Exception e) {
                return null;
            }
        });
        TextFormatter<Number> lonTextFormatter =
                new TextFormatter<>(stringConverter, 48.29, lonFilter);

        TextField longitudeTextField = new TextField();
        longitudeTextField.setTextFormatter(lonTextFormatter);
        longitudeTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        UnaryOperator<TextFormatter.Change> latFilter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newLatDeg = stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLatDeg(newLatDeg) ? change : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> latTextFormatter =
                new TextFormatter<>(stringConverter, 4.08, latFilter);

        TextField latitudeTextField = new TextField();
        latitudeTextField.setStyle("-fx-pref-width: 60;\n -fx-alignment: baseline-right;");
        latitudeTextField.setTextFormatter(latTextFormatter);

        longitudeTextField.setOnMouseClicked(e -> {
            lonTextFormatter.valueProperty().unbind();
            observerLocationBean.lonDegProperty().unbind();
        });

        longitudeTextField.setOnAction(e -> {
            observerLocationBean.lonDegProperty().bind(lonTextFormatter.valueProperty());
        });

        latitudeTextField.setOnMouseClicked(e -> {
            latTextFormatter.valueProperty().unbind();
            observerLocationBean.latDegProperty().unbind();
        });

        latitudeTextField.setOnAction(e -> {
            observerLocationBean.latDegProperty().bind(latTextFormatter.valueProperty());
        });

        Label positionLabel = new Label("Pays :");
        ComboBox<Country> countryChoice = new ComboBox<>();
        countryChoice.setValue(new Country("France", 48.29, 4.08));
        countryChoice.getItems().addAll(countryCatalogue.countries());

        countryChoice.setOnAction(e -> {
                    observerLocationBean.latDegProperty().unbind();
                    observerLocationBean.lonDegProperty().unbind();
                    latTextFormatter.valueProperty().bind(Bindings.select(countryChoice.valueProperty(), "latitude"));
                    lonTextFormatter.valueProperty().bind(Bindings.select(countryChoice.valueProperty(), "longitude"));
                    observerLocationBean.latDegProperty().bind(Bindings.select(countryChoice.valueProperty(), "latitude"));
                    observerLocationBean.lonDegProperty().bind(Bindings.select(countryChoice.valueProperty(), "longitude"));
                }
        );

        countryChoice.setStyle("-fx-pref-width: 180;");

        HBox observationPosition = new HBox(longitudeLabel, longitudeTextField, latitudeLabel, latitudeTextField, positionLabel, countryChoice);
        observationPosition.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return observationPosition;
    }
}