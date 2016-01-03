package src.util;

import java.awt.Point;

/**
 * Generateur de point et de nombre aleatoire.
 */
public class RandomNumber {
    /**
     * Renvoi un nombre aleatoire entre min et max.
     * @param min Minimun possible.
     * @param max Maximum possible.
     * @return Un nombre aleatoire entre min et max.
     */
    public static int randomValue(int min, int max) {
        return ((int) (Math.random() * (max - min + 1)) + min);
    }

    /**
     * Renvoi un point de coordonnees aleatoires.
     * @param min_x Minimum en abscisse.
     * @param max_x Maximum en abscisse.
     * @param min_y Minimum en ordonnee.
     * @param max_y Maximum en ordonee.
     * @return Un point aleatoire (x, y) avec  min_x < x < max_x et min_y < y < max_y.
     */
    public static Point randomPoint(int min_x, int max_x, int min_y, int max_y) {
        return new Point(randomValue(min_x, max_x), randomValue(min_y, max_y));
    }
}
