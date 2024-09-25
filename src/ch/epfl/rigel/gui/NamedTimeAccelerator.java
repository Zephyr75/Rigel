package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * @author Marin CORNUOT (311984)
 * @author Antoine ROGER (312065)
 *  Enumération d'accélérateurs du temps s'écoulant
 */
public enum NamedTimeAccelerator {
    TIMES_1("1×", TimeAccelerator.continuous(1)),
    TIMES_30("30×", TimeAccelerator.continuous(30)),
    TIMES_300("300×", TimeAccelerator.continuous(300)),
    TIMES_3000("3000×", TimeAccelerator.continuous(3000)),
    TIMES_10000("10000x", TimeAccelerator.continuous(10000)),
    DAY("jour", TimeAccelerator.discrete(30, Duration.ofHours(24))),
    SIDEREAl_DAY("jour sidéral", TimeAccelerator.discrete(30, Duration.ofSeconds(23*3600 + 56*60 + 4)));
    String name;
    TimeAccelerator accelerator;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructeur pour l'accélérateur énuméré
     * @param name : le nom de l'accélérateur énuméré
     * @param accelerator : le type de l'accélérateur énuméré
     */
    NamedTimeAccelerator(String name, TimeAccelerator accelerator){
        this.name = name;
        this.accelerator = accelerator;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return le nom de l'accélérateur
     */
    public String getName() {
        return name;
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @return le type de l'accélérateur
     */
    public TimeAccelerator getAccelerator() {
        return accelerator;
    }

    @Override
    public String toString() {
        return name;
    }

}