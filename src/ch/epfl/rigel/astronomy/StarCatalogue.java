package ch.epfl.rigel.astronomy;

import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ch.epfl.rigel.Preconditions;

/**
 * Représente le catalogue des étoiles que l'on représente dans notre programme et les index des astérismes auxquels elles appartiennent
 */
public final class StarCatalogue {
    private List<Star> stars;
    private final Map<Asterism, List<Integer>> index = new HashMap<>();


    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructor for the StarCatalogue
     * @param stars
     * @param asterisms
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms){
        Map<Star, Integer> starIndex = new HashMap<>();
        for(Star star : stars){
            starIndex.put(star, starIndex.size());
        }
        for (Asterism asterism : asterisms) {
            List<Integer> starList = new ArrayList<>();
            Preconditions.checkArgument(stars.containsAll(asterism.stars()));
            for (Star star : asterism.stars()) {
                starList.add(starIndex.get(star));
            }
            index.put(asterism, starList);
        }
        this.stars = List.copyOf(stars);
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return stars in the catalogue
     */
    public List<Star> stars(){
        return stars;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return asterisms in the catalogue
     */
    public Set<Asterism> asterisms(){
        return Collections.unmodifiableSet(index.keySet());
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param asterism
     * @return all the indices of the stars in the asterism
     */
    public List<Integer> asterismIndices(Asterism asterism){
        Preconditions.checkArgument(index.containsKey(asterism));
        return Collections.unmodifiableList(index.get(asterism));
    }

    /**
     * Permet de construire un catalogue d'étoiles
     */
    public final static class starBuilder{
        private final List<Star> builderStars = new ArrayList<Star>();
        private final List<Asterism> builderAsterisms = new ArrayList<Asterism>();

        /**
         * @author Marin CORNUOT (311984)
         * @author Antoine ROGER (312065)
         * @param star
         * @return builder with the added star
         */
        public starBuilder addStar(Star star) {
            builderStars.add(star);
            return this;
        }

        /**
         * @author Marin CORNUOT (311984)
         * @author Antoine ROGER (312065)
         * @return stars in the builder
         */
        public List<Star> stars(){
            return List.copyOf(builderStars);
        }

        /**
         * @author Marin CORNUOT (311984)
         * @author Antoine ROGER (312065)
         * @param asterism
         * @return builder with the added asterism
         */
        public starBuilder addAsterism(Asterism asterism) {
            builderAsterisms.add(asterism);
            return this;
        }

        /**
         * @author Marin CORNUOT (311984)
         * @author Antoine ROGER (312065)
         * @return asterisms in the builder
         */
        public List<Asterism> asterisms(){
            return List.copyOf(builderAsterisms);
        }

        /**
         * @author Marin CORNUOT (311984)
         * @author Antoine ROGER (312065)
         * @param inputStream
         * @param loader
         * @return builder loaded from inputStream
         * @throws IOException
         */
        public starBuilder loadFrom(InputStream inputStream, starLoader loader) throws IOException{
            loader.load(inputStream, this);
            return this;
        }

        /**
         * @author Marin CORNUOT (311984)
         * @author Antoine ROGER (312065)
         * @return catalogue built from the builder
         */
        public StarCatalogue build() {
            return new StarCatalogue(stars(), asterisms());
        }
    }

     public interface starLoader{
        /**
         * @author Marin CORNUOT (311984)
         * @author Antoine ROGER (312065)
         * @param inputStream
         * @param builder
         * @throws IOException
         */
        void load(InputStream inputStream, starBuilder builder) throws IOException;
    }
}