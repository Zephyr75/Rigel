package ch.epfl.rigel.coordinates;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marin CORNUOT (311984)
 * représente le catalogue des pays 
 */
public final class CountryCatalogue {
    private List<Country> countries;

    /**
     * constructeur
     * @author Marin CORNUOT (311984)
     * @param countries
     */
    public CountryCatalogue(List<Country> countries) {
        this.countries = countries;
    }
// getter liste de pays
    public List<Country> countries(){ return countries;}

    //buider du catalogue de pays
    public final static class Builder {
        private final List<Country> countriesBuilder = new ArrayList<>();

//méthode permettant d'ajouter un pays au catalogue d'étoile
        public Builder addCountry(Country country) {
            countriesBuilder.add(country);
            return this;
        }

        /**
         * @author Marin CORNUOT (311984)
         * permet de chargert les étoiles depuis le fichier .csv
         * @param inputStream
         * @param loader
         * @return le Builder
         * @throws IOException
         */
        public Builder countryLoadFrom(InputStream inputStream, countryLoader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        /**
         * @author Marin CORNUOT (311984)
         * permet de construire le catalogue d'étoile lors du démarrage du programme
         */
        public CountryCatalogue build() {
            return new CountryCatalogue(countriesBuilder);
        }
    }

         public interface countryLoader{
             void load(InputStream inputStream, CountryCatalogue.Builder builder) throws IOException;
         }



}