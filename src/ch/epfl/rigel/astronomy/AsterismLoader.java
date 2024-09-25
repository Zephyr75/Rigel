package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loader pour les astérismes
 */
public enum AsterismLoader implements StarCatalogue.starLoader{
    INSTANCE;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Permet de charger un catalogue d'asterisme a partir des etoiles préalablement créées
     * @param inputStream
     * @param builder
     * @throws IOException
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.starBuilder builder)
            throws IOException {
        List<Star> stars = List.copyOf(builder.stars());
        Map <Integer, Star> test = new HashMap<>() ;

        for(Star elem : stars){
            test.put(elem.hipparcosId(), elem);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
        String[] columns;
        String etoiles;
        while((etoiles = reader.readLine()) != null){
            List<Star> nom = new ArrayList<>();
            columns = etoiles.split(",");
            for(String elem : columns){
                nom.add(test.get(Integer.parseInt(elem)));
            }
            builder.addAsterism(new Asterism(nom));
        }
        inputStream.close();
    }
}