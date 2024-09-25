package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;


/**
 * @author Marin CORNUOT (311984)
 * @author Antoine ROGER (312065)
 *  Dessinateur du canvas du ciel
 */
public class SkyCanvasPainter {
    private Canvas canvas;
    private GraphicsContext context;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructeur pour le dessinateur
     * @param canvas
     */
    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        context = canvas.getGraphicsContext2D();
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Réinitialise le canvas avec un fond noir
     */
    public void clear(ObservedSky sky, Color color) {
        context.setFill(color);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Dessine les étoiles sur le canvas
     * @param sky : ciel à dessiner
     * @param proj : projection à appliquer
     * @param transform : transform à appliquer
     */
    public void drawStars(ObservedSky sky, StereographicProjection proj, Transform transform, double fieldOfView, boolean showAsterism, int affichageNomEtoiles, Color color) {
        double[] starCoords = new double[sky.starPositions().length];
        transform.transform2DPoints(sky.starPositions(), 0, starCoords, 0, sky.stars().size());
        context.setStroke(color);
        int first, second;
        if (showAsterism) {
            for (Asterism asterism : sky.asterisms()) {
                for (int i = 0; i < sky.asterismIndices(asterism).size() - 1; i++) {
                    first = sky.asterismIndices(asterism).get(i);
                    second = (i == sky.asterismIndices(asterism).size() - 1) ?
                            sky.asterismIndices(asterism).get(0) : sky.asterismIndices(asterism).get(i + 1);
                    if (canvas.getBoundsInLocal().contains(starCoords[first * 2], starCoords[first * 2 + 1]) ||
                            canvas.getBoundsInLocal().contains(starCoords[second * 2], starCoords[second * 2 + 1])) {
                        context.beginPath();
                        context.moveTo(starCoords[first * 2],
                                starCoords[first * 2 + 1]);
                        context.lineTo(starCoords[second * 2],
                                starCoords[second * 2 + 1]);
                        context.stroke();
                    }
                }
            }
        }

        double magnitude;
        Star star;
        for (int i = 0; i < sky.stars().size() * 2; i+=2) {
            star = sky.stars().get(i/2);
            magnitude = correctMagnitude(star.magnitude(), proj);
            magnitude = transform.deltaTransform(magnitude, 0).getX();
            double coordsX = starCoords[i] - magnitude/2;
            double coordsY = starCoords[i + 1] - magnitude/2;
            context.setFill(BlackBodyColor.colorForTemperature(star.colorTemperature()));
            context.fillOval(coordsX,  coordsY, magnitude, magnitude);
            if(affichageNomEtoiles == 2) {
                if (star.magnitude() * fieldOfView < 200 && star.magnitude() < 5) {
                    context.setFill(Color.LIGHTGRAY);
                    context.setTextBaseline(VPos.BASELINE);
                    context.fillText(star.name(), coordsX, coordsY - 2);
                }
            } else if(affichageNomEtoiles == 1) {
                context.setFill(Color.LIGHTGRAY);
                context.setTextBaseline(VPos.BASELINE);
                context.fillText(star.name(), coordsX, coordsY - 2);
            }
        }

    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Dessine les planètes sur le canvas
     * @param sky : ciel à dessiner
     * @param proj : projection à appliquer
     * @param transform : transform à appliquer
     */
    public void drawPlanets(ObservedSky sky, StereographicProjection proj, Transform transform) {
        double[] planetCoords = new double[sky.planetPositions().length];
        transform.transform2DPoints(sky.planetPositions(), 0, planetCoords, 0, sky.planets().size());
        double magnitude = 0;
        Planet planet;
        for (int i = 0; i < sky.planets().size(); i+=2) {
            planet = sky.planets().get(i/2);
            magnitude = correctMagnitude(planet.magnitude(), proj);
            magnitude = transform.deltaTransform(magnitude, 0).getX();
            double coordsX = planetCoords[i] - magnitude/2;
            double coordsY = planetCoords[i + 1] - magnitude/2;
            context.setFill(Color.web("FFF244"));
            context.setTextBaseline(VPos.BASELINE);
            context.fillText(planet.name(), coordsX ,  coordsY- 2);
            context.setFill(Color.LIGHTGRAY);
            context.fillOval(coordsX, coordsY, magnitude, magnitude);
        }
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Dessine le soleil sur le canvas
     * @param sky : ciel à dessiner
     * @param proj : projection à appliquer
     * @param transform : transform à appliquer
     */
    public void drawSun(ObservedSky sky, StereographicProjection proj, Transform transform) {
        double[] sunCoords = new double[2];
        sunCoords[0] = sky.sunPosition().x();
        sunCoords[1] = sky.sunPosition().y();


        transform.transform2DPoints(sunCoords, 0, sunCoords, 0, 1);
        double magnitude;
        magnitude = correctMagnitude(sky.sun().angularSize(), proj);
        magnitude = transform.deltaTransform(magnitude, 0).getX();
        context.setFill(Color.YELLOW.deriveColor(1, 1, 1, .25f));
        magnitude *= 2.2;

        context.fillOval(sunCoords[0] - magnitude/2,
                sunCoords[1] - magnitude/2,
                magnitude, magnitude);
        magnitude /= 2.2;
        magnitude += 2;
        context.setFill(Color.YELLOW);
        context.fillOval(sunCoords[0] - magnitude/2,
                sunCoords[1] - magnitude/2,
                magnitude, magnitude);
        magnitude -= 2;
        context.setFill(Color.WHITE);
        context.fillOval(sunCoords[0] - magnitude/2,
                sunCoords[1] - magnitude/2,
                magnitude, magnitude);
        context.setFill(Color.web("FFF244"));
        context.setTextBaseline(VPos.BASELINE);
        context.fillText(sky.sun().name(), sunCoords[0] ,  sunCoords[1] - 5);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Dessine la lune sur le canvas avec la phase appropriée
     * @param sky : ciel à dessiner
     * @param proj : projection à appliquer
     * @param transform : transform à appliquer
     */
    public void drawMoon(ObservedSky sky, StereographicProjection proj, Transform transform, Color color) {
        double[] moonCoords = new double[2];
        moonCoords[0] = sky.moonPosition().x();
        moonCoords[1] = sky.moonPosition().y();
        transform.transform2DPoints(moonCoords, 0, moonCoords, 0, 1);
        double magnitude;
        magnitude = correctMagnitude(sky.moon().angularSize(), proj);
        magnitude = transform.deltaTransform(magnitude, 0).getX();

        double F = (sky.moon().phase() - .5) * 2;
        double alphaSun = sky.sun().equatorialPos().ra();
        double deltaSun = sky.sun().equatorialPos().dec();
        double alphaMoon = sky.moon().equatorialPos().ra();
        double deltaMoon = sky.moon().equatorialPos().dec();
        double chi = Math.atan((Math.cos(deltaSun) * Math.sin(alphaSun - alphaMoon)) /
                (Math.cos(deltaMoon) * Math.sin(deltaSun) - Math.sin(deltaMoon) * Math.cos(deltaSun) * Math.cos(alphaSun - alphaMoon)));
        double xCoord = moonCoords[0];
        double yCoord = moonCoords[1];
        context.setFill(Color.WHITE);
        context.fillOval(xCoord - magnitude / 2,
                yCoord - magnitude / 2,
                magnitude, magnitude);

        double rayon = magnitude / 2;
        double rectRayon = 2 * Math.sqrt(2 * rayon * rayon);
        xCoord -= Math.cos(Math.PI + chi) * F * rayon;
        yCoord -= Math.sin(Math.PI + chi) * F * rayon;
        double[] xCoords = {xCoord + rayon * Math.cos(Math.PI / 2 + chi),
                xCoord + rectRayon * Math.cos(Math.PI / 4 + chi),
                xCoord + rectRayon * Math.cos(- Math.PI / 4 + chi),
                xCoord + rayon * Math.cos(- Math.PI / 2 + chi)};
        double[] yCoords = {yCoord + rayon * Math.sin(Math.PI / 2 + chi),
                yCoord + rectRayon * Math.sin(Math.PI / 4 + chi),
                yCoord + rectRayon * Math.sin(- Math.PI / 4 + chi),
                yCoord + rayon * Math.sin(- Math.PI / 2 + chi)};
        context.setFill(color);
        context.fillPolygon(xCoords, yCoords, 4);
        
        context.setFill(Color.web("FFF244"));
        context.setTextBaseline(VPos.BASELINE);
        context.fillText(sky.moon().name(), xCoord ,  yCoord - 3);
        
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Dessine l'horizon sur le canvas
     * @param sky : ciel à dessiner
     * @param proj : projection à appliquer
     * @param transform : transform à appliquer
     */
    public void drawHorizon(ObservedSky sky, StereographicProjection proj, Transform transform, Color color) {
        double[] horizCoords = new double[4];
        double radius = proj.circleRadiusForParallel(HorizontalCoordinates.of(0, 0));
        horizCoords[0] = proj.circleCenterForParallel(HorizontalCoordinates.of(0, 0)).x();
        horizCoords[1] = proj.circleCenterForParallel(HorizontalCoordinates.of(0, 0)).y();
        transform.transform2DPoints(horizCoords, 0, horizCoords, 0, 1);
        radius = transform.deltaTransform(radius, 0).getX() * 2;
        context.setStroke(color);
        context.setLineWidth(2);
        context.strokeOval(horizCoords[0] - radius / 2, horizCoords[1] - radius / 2, radius, radius);
        context.setTextAlign(TextAlignment.CENTER);
        context.setTextBaseline(VPos.BASELINE);
        context.setFill(color);

        CartesianCoordinates[] ptsCardinaux = new CartesianCoordinates[8];
        HorizontalCoordinates[] ptsHorizontaux = new HorizontalCoordinates[8];

        for (int i = 0; i < ptsCardinaux.length; i++) {
            ptsHorizontaux[i] = HorizontalCoordinates.ofDeg(i * 45, -0.5);
            ptsCardinaux[i] = proj.apply(ptsHorizontaux[i]);
        }

        Point2D[] points = new Point2D[8];
        for (int i=0; i<points.length; i++) {
            points[i] = transform.transform(ptsCardinaux[i].x(), ptsCardinaux[i].y());

        }

        for (int i=0; i<ptsCardinaux.length; i++) {
            context.setTextBaseline(VPos.TOP);
            context.fillText(ptsHorizontaux[i]
                    .azOctantName("N", "E", "S", "0"),points[i].getX(),points[i].getY());
        }
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Dessine l'interface utilisateur sur le canvas
     */
    public void drawUI() {
        context.setFill(Color.GREY.deriveColor(1, 1, 1, .25f));
        context.fillRect(0, 0, 25, canvas.getHeight());
        context.fillRect(25, 0, canvas.getWidth() - 25, 25);
        context.fillRect(25, canvas.getHeight() - 25, canvas.getWidth() - 25, 25);
        context.fillRect(canvas.getWidth() - 25, 25, 25, canvas.getHeight() - 50);
        context.setFill(Color.WHITE);
        double heightHalf = canvas.getHeight() / 2;
        double widthHalf = canvas.getWidth() / 2;
        double height = canvas.getHeight();
        double width = canvas.getWidth();
        double[] xCoords = new double[] {5, 20, 20};
        double[] yCoords = new double[] {heightHalf, heightHalf - 10, heightHalf + 10};
        context.fillPolygon(xCoords, yCoords, 3);
        xCoords = new double[] {width - 5, width - 20, width - 20};
        context.fillPolygon(xCoords, yCoords, 3);
        xCoords = new double[] {widthHalf, widthHalf - 10, widthHalf + 10};
        yCoords = new double[] {5, 20, 20};
        context.fillPolygon(xCoords, yCoords, 3);
        yCoords = new double[] {height - 5, height - 20, height - 20};
        context.fillPolygon(xCoords, yCoords, 3);
    }


    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Adapte la magnitude avec le transform
     * @param magnitude : magnitude à modifier
     * @param proj : projection à appliquer
     * @return la magnitude corrigée
     */
    private double correctMagnitude(double magnitude, StereographicProjection proj) {
        ClosedInterval interval = ClosedInterval.of(-2, 5);
        magnitude = interval.clip(magnitude);
        magnitude = (99 - 17 * magnitude) / 140;
        magnitude = magnitude * proj.applyToAngle(Angle.ofDeg(.5));
        return magnitude;
    }
}