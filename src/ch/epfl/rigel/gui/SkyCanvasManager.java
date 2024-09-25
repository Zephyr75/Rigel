package ch.epfl.rigel.gui;

import java.io.IOException;
import java.util.Optional;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class SkyCanvasManager {

    private final ObjectBinding<StereographicProjection> proj;
    private final ObjectBinding<Transform> planeToCanvas;
    private final ObjectBinding<ObservedSky> sky;
    private final ObjectBinding<CelestialObject> objectUnderMouse;
    private final ObjectBinding<Double> mouseAz;
    private final ObjectBinding<Double> mouseAlt;
    private final ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;
    private final ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>(Point2D.ZERO);
    private final ObjectProperty<Double> fieldOfViewDeg = new SimpleObjectProperty<>();
    private final ObjectProperty<Color> colorAsterism = new SimpleObjectProperty<>(Color.BLUE);
    private final ObjectProperty<Color> colorHorizon = new SimpleObjectProperty<>(Color.RED);
    private final ObjectProperty<Color> colorSky = new SimpleObjectProperty<>(Color.BLACK);
    private final Canvas canvas;
    private final static ClosedInterval INTERVAL_FOV = ClosedInterval.of(30, 150);
    private final static ClosedInterval INTERVAL_ALT = ClosedInterval.of(5, 90);
    private final static RightOpenInterval INTERVAL_AZ = RightOpenInterval.of(0, 360);
    private final ContextMenu contextMenu = new ContextMenu();
    private boolean showAsterism = true;
    private CheckMenuItem item1 = new CheckMenuItem("Centrer sur la Lune");
    private SkyCanvasPainter painter;
    // 1 signifie que l'on affiche toutes les étoiles, 2 affichage adptatif en fonction du FOV et 3 aucun nom affiché
    private int affichageNomEtoiles = 2;


    // 0 -> UI is off, 1 -> UI is on, 2 -> UI is turning off
    private int uiIsOn = 0;

    /**@author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructor which allows to define the different links and interaction between the user and the program such as zooming or moving around the map
     * @param stars
     * @param dateTimeB
     * @param observerLocationB
     * @param viewingParametersB
     * @throws IOException
     */
    public SkyCanvasManager(StarCatalogue stars, DateTimeBean dateTimeB, ObserverLocationBean observerLocationB, ViewingParametersBean viewingParametersB) throws IOException {

        BlackBodyColor.getColorTab();
        canvas = new Canvas();
        painter = new SkyCanvasPainter(canvas);
        item1.setSelected(false);


        fieldOfViewDeg.bind(viewingParametersB.fieldOfViewDegProperty());
        proj = Bindings.createObjectBinding(() -> new StereographicProjection(viewingParametersB.getCenter()), viewingParametersB.centerProperty());

        planeToCanvas = Bindings.createObjectBinding(() -> {
            StereographicProjection projection = proj.get();
            double dilatation = canvas.getWidth() / projection.applyToAngle(Angle.ofDeg(viewingParametersB.getFieldOfViewDeg()));
            return Transform.affine(dilatation, 0, 0, -dilatation, canvas.getWidth()/2, canvas.getHeight()/2);
        }, viewingParametersB.fieldOfViewDegProperty(), viewingParametersB.centerProperty(), proj, canvas.heightProperty(), canvas.widthProperty());
        planeToCanvas.addListener((o,oV, nV) -> paint(painter));

        sky = Bindings.createObjectBinding(() -> new ObservedSky(dateTimeB.getZonedDateTime(), observerLocationB.getCoordinates(),
                proj.getValue(), stars), dateTimeB.dateProperty(), observerLocationB.coordinatesProperty(),observerLocationB.latDegProperty(),observerLocationB.lonDegProperty(), dateTimeB.zoneIdProperty(), dateTimeB.timeProperty(), proj);
        sky.addListener(e -> paint(painter));
        
        dateTimeB.timeProperty().addListener(e -> {
            if (item1.isSelected()) {
                EquatorialToHorizontalConversion toHoriz = new EquatorialToHorizontalConversion(dateTimeB.getZonedDateTime(), observerLocationB.getCoordinates());
                viewingParametersB.setCenter(HorizontalCoordinates.of(toHoriz.apply(sky.get().moon().equatorialPos()).az(), toHoriz.apply(sky.get().moon().equatorialPos()).alt()));
            }
        });
        dateTimeB.dateProperty().addListener(e -> {
            if (item1.isSelected()) {
                EquatorialToHorizontalConversion toHoriz = new EquatorialToHorizontalConversion(dateTimeB.getZonedDateTime(), observerLocationB.getCoordinates());
                viewingParametersB.setCenter(HorizontalCoordinates.of(toHoriz.apply(sky.get().moon().equatorialPos()).az(), toHoriz.apply(sky.get().moon().equatorialPos()).alt()));
            }
        });
        
        item1.setOnAction(e -> {
            if (item1.isSelected()) {
                EquatorialToHorizontalConversion toHoriz = new EquatorialToHorizontalConversion(dateTimeB.getZonedDateTime(), observerLocationB.getCoordinates());
                viewingParametersB.setCenter(HorizontalCoordinates.of(toHoriz.apply(sky.get().moon().equatorialPos()).az(), toHoriz.apply(sky.get().moon().equatorialPos()).alt()));
            }
            else {
                HorizontalCoordinates center = viewingParametersB.getCenter();
                double centerAltDeg = center.altDeg();
                double centerAzDeg  = center.azDeg();
                viewingParametersB.setCenter(HorizontalCoordinates.ofDeg(centerAzDeg, INTERVAL_ALT.clip(centerAltDeg)));
            }
        });

        canvas.setOnMouseExited( e->{ uiIsOn = 0; paint(painter);});

        canvas.setOnMouseMoved(e -> {
            mousePosition.set(new Point2D(e.getX(), e.getY()));
            if (e.getY() < 25 || e.getY() > canvas.getHeight() - 25 ||
                    e.getX() > canvas.getWidth() - 25 || e.getX() < 25) {
                uiIsOn = 1;
            }
            else {
                uiIsOn = 2;
            }
            if (uiIsOn != 0) {
                paint(painter);
                if (uiIsOn == 2) {
                    uiIsOn = 0;
                }
            }
            e.consume();
        });

        objectUnderMouse = Bindings.createObjectBinding(() -> {
            try {
                Point2D temporaryPosition = planeToCanvas.get().inverseTransform(mousePosition.get());
                Optional<CelestialObject> object = sky.get().objectClosestTo(CartesianCoordinates.of(temporaryPosition.getX(), temporaryPosition.getY()),
                        planeToCanvas.get().inverseDeltaTransform(15, 0).getX());

                return object.orElse(null);
            } catch (NonInvertibleTransformException e) {
                return null;
            }
        }, mousePosition, sky, planeToCanvas);

        mouseHorizontalPosition = Bindings.createObjectBinding(()-> {
            try {
                Point2D temporaryPosition = planeToCanvas.get().inverseTransform(mousePosition.get());
                return proj.get().inverseApply(CartesianCoordinates.of(temporaryPosition.getX(), temporaryPosition.getY()));
            } catch(NonInvertibleTransformException e) {
                return null;
            }
        }, mousePosition, planeToCanvas,proj);

        mouseAz = Bindings.createObjectBinding(() ->
                        mouseHorizontalPosition.get() == null ? Double.NaN : mouseHorizontalPosition.get().azDeg()
                , mouseHorizontalPosition);
        mouseAlt = Bindings.createObjectBinding(() ->
                mouseHorizontalPosition.get() == null ? Double.NaN : mouseHorizontalPosition.get().altDeg(), mouseHorizontalPosition);

        canvas.setOnMousePressed(e -> {
            if(e.isPrimaryButtonDown()) {
                contextMenu.hide();
                canvas.requestFocus();
                paint(painter);
            }
            if (e.getY() < 25) {
                moveCanvas(1, viewingParametersB);
            }
            else if (e.getY() > canvas.getHeight() - 25) {
                moveCanvas(2, viewingParametersB);
            }
            if (e.getX() > canvas.getWidth() - 25) {
                moveCanvas(3, viewingParametersB);
            }
            else if (e.getX() < 25) {
                moveCanvas(4, viewingParametersB);
            }
            e.consume();
        });

        canvas.setOnKeyPressed(e -> {
            int code = 0;
            switch (e.getCode()) {
                case UP:
                    code = 1; break;
                case DOWN:
                    code = 2; break;
                case RIGHT:
                    code = 3; break;
                case LEFT:
                    code = 4; break;
                default: break;
            }
            moveCanvas(code, viewingParametersB);
            e.consume();
        });
        canvas.setOnScroll(e -> {
            double deltaX = e.getDeltaX();
            double deltaY = e.getDeltaY();
            double delta = Math.abs(deltaX) > Math.abs(deltaY) ? deltaX : deltaY;
            double newAngle = viewingParametersB.getFieldOfViewDeg() + delta;
            viewingParametersB.setFieldOfViewDeg(INTERVAL_FOV.clip(newAngle));
        });
        menuOptions();
        
    }

    // getter for the property related to the object under the mouse to be used in the Main
    public ObjectBinding<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    // getter for the mouse azimut position
    public ObjectBinding<Double> getMouseAz() {
        return mouseAz;
    }

    //getter for the mouse alt position
    public ObjectBinding<Double> getMouseAlt() {
        return mouseAlt;
    }

    //getter for canvas to be used in the Main
    public Canvas canvas() {
        return canvas;
    }

    //getter for the field of view to be displayed in the Main
    public ObjectProperty<Double> getFieldOfView() {
        return fieldOfViewDeg;
    }

    //setter for the UI activation state
    public void setUiIsOn (int i) {
        uiIsOn = i;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * allows the painting of every elements of the sky
     * @param painter
     */
    private void paint(SkyCanvasPainter painter){
        StereographicProjection projection = proj.get();
        ObservedSky observedSky = sky.get();
        Transform transformation = planeToCanvas.get();
        painter.clear(observedSky, colorSky.getValue());
        painter.drawStars(observedSky, projection, transformation, fieldOfViewDeg.getValue(), showAsterism, affichageNomEtoiles, colorAsterism.getValue());
        painter.drawPlanets(observedSky, projection, transformation);
        painter.drawMoon(observedSky, projection, transformation, colorSky.getValue());
        painter.drawSun(observedSky, projection, transformation);
        painter.drawHorizon(observedSky, projection, transformation, colorHorizon.getValue());
        if (uiIsOn == 1) {
            painter.drawUI();
        }
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Gère le menu d'options ouvert par un clic droit
     */
    private void menuOptions() {

        CheckMenuItem item2 = new CheckMenuItem("Afficher les constellations");
        item2.setSelected(true);
        item2.setOnAction(e -> {
            showAsterism = !showAsterism;
            paint(painter);
        });

        Menu item3 = new Menu( "Affichage nom des étoiles");
        RadioMenuItem subItem1 = new RadioMenuItem("Toutes les étoiles");
        RadioMenuItem subItem2 = new RadioMenuItem("Progressif");
        RadioMenuItem subItem3 = new RadioMenuItem("Aucune étoile");
        item3.getItems().addAll(subItem1,subItem2,subItem3);
        ToggleGroup optionAffichageEtoiles = new ToggleGroup();
        subItem1.setToggleGroup(optionAffichageEtoiles);
        subItem2.setToggleGroup(optionAffichageEtoiles);
        subItem3.setToggleGroup(optionAffichageEtoiles);
        subItem2.setSelected(true);

        subItem1.setOnAction(e -> { affichageNomEtoiles = 1; paint(painter);});
        subItem2.setOnAction(e -> { affichageNomEtoiles = 2; paint(painter);});
        subItem3.setOnAction(e -> { affichageNomEtoiles = 3; paint(painter);});

        MenuItem item4 = new MenuItem("Réglages couleur");
        item4.setOnAction( e -> {
            Stage stage = new Stage();
            stage.setTitle("Réglages couleur");
            ListView<HBox> list = new ListView<>();
            Label asterism = new Label("Couleur constellations :");
            ColorPicker asterismColorPicker = new ColorPicker();
            asterismColorPicker.setValue(Color.BLUE);
            asterismColorPicker.setOnAction( event -> {colorAsterism.set(asterismColorPicker.getValue()); paint(painter);});
            Pane pane1 = new Pane();
            HBox.setHgrow(pane1, Priority.ALWAYS);
            HBox asterismColor =  new HBox(asterism, pane1, asterismColorPicker);


            Label horizon = new Label("Couleur horizon :");
            ColorPicker horizonColorPicker = new ColorPicker();
            horizonColorPicker.setValue(Color.RED);
            horizonColorPicker.setOnAction( event -> {colorHorizon.set(horizonColorPicker.getValue()); paint(painter);});
            Pane pane2 = new Pane();
            HBox.setHgrow(pane2, Priority.ALWAYS);
            HBox horizonColor = new HBox(horizon, pane2, horizonColorPicker);

            Label sky = new Label ("Couleur du ciel :");
            ColorPicker skyColorPicker = new ColorPicker();
            skyColorPicker.setValue(Color.BLACK);
            skyColorPicker.setOnAction( event -> {colorSky.set(skyColorPicker.getValue()); paint(painter);});
            Pane pane3 = new Pane();
            HBox.setHgrow(pane3, Priority.ALWAYS);
            HBox skyColor = new HBox(sky, pane3, skyColorPicker);

            list.getItems().addAll(asterismColor, horizonColor, skyColor);

            stage.setScene(new Scene(list, 350, 125));
            stage.show();
        });

        MenuItem item5 = new MenuItem("quitter");
        item5.setOnAction(e -> Platform.exit());

        contextMenu.getItems().addAll(item1, item2, item3, new SeparatorMenuItem(), item4, new SeparatorMenuItem(), item5);
        canvas.setOnContextMenuRequested(event -> contextMenu.show(canvas, event.getScreenX(), event.getScreenY()));
    }


    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Actualise le centre de la vue du canvas
     * @param code
     * @param viewingParametersB
     */
    private void moveCanvas(int code, ViewingParametersBean viewingParametersB) {
        HorizontalCoordinates center = viewingParametersB.getCenter();
        double centerAltDeg = center.altDeg();
        double centerAzDeg  = center.azDeg();
        switch (code) {
            case 1:
                viewingParametersB.setCenter(HorizontalCoordinates.ofDeg(centerAzDeg, INTERVAL_ALT.clip(centerAltDeg + 5)));
                break;
            case 2:
                viewingParametersB.setCenter(HorizontalCoordinates.ofDeg(centerAzDeg, INTERVAL_ALT.clip(centerAltDeg - 5)));
                break;
            case 3:
                viewingParametersB.setCenter(HorizontalCoordinates.ofDeg(INTERVAL_AZ.reduce(centerAzDeg + 10), centerAltDeg));
                break;
            case 4 :
                viewingParametersB.setCenter(HorizontalCoordinates.ofDeg(INTERVAL_AZ.reduce(centerAzDeg - 10), centerAltDeg));
                break;
            default: break;
        }
    }
}