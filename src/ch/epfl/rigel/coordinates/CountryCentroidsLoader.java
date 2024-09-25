package ch.epfl.rigel.coordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Marin CORNUOT (311984)
 * permet de charger le catalogue d'étoiles depuis le fichier dans les ressources
 */
public enum CountryCentroidsLoader implements CountryCatalogue.countryLoader{
    INSTANCE;

    // enum permettant de n'utiliser que les catégories du tableau qui nous intéressent
    private enum Column{
        FID,the_geom,FID_,scalerank,featurecla,labelrank,sovereignt,sov_a3,adm0_dif,level,type,admin,adm0_a3,geou_dif,
        geounit,gu_a3,su_dif,subunit,su_a3,brk_diff,name,name_long,brk_a3,brk_name,brk_group,abbrev,postal,formal_en,formal_fr,
        note_adm0,note_brk,name_sort,name_alt,mapcolor7,mapcolor8,mapcolor9,mapcolor13,pop_est,gdp_md_est,pop_year,
        lastcensus,gdp_year,economy,income_grp,wikipedia,fips_10,iso_a2,iso_a3,iso_n3,un_a3,wb_a2,wb_a3,woe_id,adm0_a3_is,adm0_a3_us,
        adm0_a3_un,adm0_a3_wb,continent,region_un,subregion,region_wb,name_len,long_len,abbrev_len,tiny,homepart,Longitude,Latitude;
    }

    /**
     * @author Marin CORNUOT (311984)
     * méthode permettant d'exploiter les données dans notre fichier d'étoiles au format .csv
     * @param inputStream
     * @param builder
     * @throws IOException
     */
    @Override
    public void load(InputStream inputStream, CountryCatalogue.Builder builder) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
        reader.readLine();
        String[] columns;
        String line;
        while ((line = reader.readLine()) != null) {
            columns = line.split(",");
            String name = assignString(columns[Column.name_long.ordinal()], null);
            double longitude = assignDouble(columns[Column.Longitude.ordinal()], 0);
            double latitude = assignDouble(columns[Column.Latitude.ordinal()], 0);
            builder.addCountry(new Country(name, longitude, latitude));
        }
    }

    /**
     * permet de gérer ce que l'on place dans notre tableau si une case est vide
     * @param element
     * @param newElement
     * @return
     */
        private String assignString(String element, String newElement) {
            return !element.isBlank() ? element : newElement;
        }

    /**
     * permet de gérer ce que l'on place dans notre tableau si une case est vide
     * @param element
     * @param newElement
     * @return
     */
        private double assignDouble(String element, double newElement) {
            return !element.isBlank() ? Double.parseDouble(element) : newElement;
        }
}