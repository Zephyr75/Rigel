package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Loader pour les étoiles
 */
public enum HygDatabaseLoader implements StarCatalogue.starLoader{
    INSTANCE;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Enumération permettant l'accès aux colonnes du tableau d'informations sur les étoiles
     */
    private enum Column{
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
        RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
        RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
        COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
    }

    @Override
    public void load(InputStream inputStream, StarCatalogue.starBuilder builder)
            throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
        reader.readLine();
        String[] columns;
        String line;
        while ((line = reader.readLine()) != null) {
            columns = line.split(",");
            String name = assignString(columns[Column.PROPER.ordinal()],
                    assignString(columns[Column.BAYER.ordinal()], "?") + " " + columns[Column.CON.ordinal()]);
            double ra = Double.parseDouble(columns[Column.RARAD.ordinal()]);
            double dec = Double.parseDouble(columns[Column.DECRAD.ordinal()]);
            double magnitude = assignDouble(columns[Column.MAG.ordinal()], 0);
            double color = assignDouble(columns[Column.CI.ordinal()], 0);
            int hipparcos = assignInt(columns[Column.HIP.ordinal()], 0);
            builder.addStar(new Star(hipparcos, name, EquatorialCoordinates.of(ra, dec), (float) magnitude, (float) color));
        }
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param element
     * @param newElement
     * @return element if element isn't blank or default
     */
    private int assignInt(String element, int newElement) {
        return !element.isBlank() ? Integer.parseInt(element) : newElement;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param element
     * @param newElement
     * @return element if element isn't blank or default
     */
    private String assignString(String element, String newElement) {
        return !element.isBlank() ? element : newElement;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param element
     * @param newElement
     * @return element if element isn't blank or default
     */
    private double assignDouble(String element, double newElement) {
        return !element.isBlank() ? Double.parseDouble(element) : newElement;
    }

}