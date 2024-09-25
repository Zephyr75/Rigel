package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marin CORNUOT (311984)
 * @author Antoine ROGER (312065)
 *  Couleurs des astres
 */
public class BlackBodyColor {

    private static Map<String, String> colorMap = new HashMap<>();

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Initialise la map qui relie les couleurs et les températures
     * @throws IOException
     */
    public static void getColorTab() throws IOException{
        try {
            InputStream colorStream = BlackBodyColor.class.getResourceAsStream("/bbr_color.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(colorStream, StandardCharsets.US_ASCII));
            String line, temp, color;

            while ((line = reader.readLine()) != null) {
                if (line.charAt(0) != '#') {
                    if(line.charAt(1) != ' '){
                        temp = line.substring(1, 6);
                        color = line.substring(80, 87);}
                    else{
                        temp = line.substring(2, 6);
                        color = line.substring(80,87);
                    }
                    colorMap.put(temp,color);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param temperature
     * @return couleur correspondant à la température étudiée
     */
    public static Color colorForTemperature(int temperature)  {
        String colorString;
        Preconditions.checkInInterval(RightOpenInterval.of(950, 40050), temperature);
        colorString = colorMap.get(String.valueOf(Math.round(temperature / 100) * 100));
        return Color.web(colorString);
    }
}