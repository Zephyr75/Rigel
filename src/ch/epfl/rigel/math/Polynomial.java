package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

public class Polynomial {
    private  double[] coefficientTab;

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * Constructor for polynomial
     * @param coefficientN
     * @param coefficients
     */
    private Polynomial(double coefficientN, double... coefficients) {
        coefficientTab = new double[coefficients.length + 1];
        coefficientTab[0] = coefficientN;
        for (int i = 0; i < coefficients.length; i++) {
            coefficientTab[i+1]= coefficients[i];
        }
    }

    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param coefficientN : First coefficient
     * @param coefficients
     * @return Polynomial
     */
    public static Polynomial of(double coefficientN, double... coefficients) {
        Preconditions.checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }


    /**
     * @author Marin CORNUOT (311984)
     * @author Antoine ROGER (312065)
     * @param x : Variable du polynome
     * @return Somme par le polynome
     */
    public double at(double x) {
        double sum = coefficientTab[0];
        for(int i = 1; i < coefficientTab.length ; i++) {
            sum = sum * x + coefficientTab [i];
        }
        return sum;
    }


    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        int puissance = coefficientTab.length - 1;
        for(int i = 0; i<coefficientTab.length; i++) {
            if(coefficientTab[i] != 0) {
                if(puissance>1 && coefficientTab[i]!=1 && coefficientTab[i] != -1) {
                    result.append(coefficientTab[i]).append("x^").append(puissance);
                    result.append(coefficientTab[i+1]>0? "+":"");
                }
                if(puissance>1 && coefficientTab[i] == 1.0 || coefficientTab[i] == -1.0) {
                    if(coefficientTab[i] == 1) {
                        result.append("x^").append(puissance);
                    } else {
                        result.append("-x^").append(puissance);
                    }
                    result.append(coefficientTab[i+1]>0? "+":"");
                }
                if(puissance == 1) {
                    result.append(coefficientTab[i]).append("x");
                    result.append(coefficientTab[i+1]>0? "+":"");
                }
                if(puissance == 0) {
                    result.append(coefficientTab[i]);
                }
            }
            puissance -=1;
        }
        return result.toString();
    }
}