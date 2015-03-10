
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import utils.AlertBounds;

import static utils.Stuff.printLog;

/**
 * Created by toidiu on 3/9/15.
 */
public class JtsTest {

    public static final String NYBIG = "43.03022607142358,-72.00730036944151:42.94772393716741,-75.27812853455544:39.31764743089888,-76.41163945198059:38.836088274750054,-73.12953293323517:41.195564472082125,-70.8285368978977";
    public static final String randalIsland = "40.80465661389707,-73.90132650732994:40.810871760615214,-73.93818911164999:40.78305349487216,-73.94814748317003:40.77110289291357,-73.92083521932364:40.77590979670586,-73.9075576141476";

    public static void main(String[] args) {
        try {
            jts();


            nycAlerts();


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void nycAlerts() {

        AlertBounds alertBounds = new AlertBounds(NYBIG);
        boolean withinBoundning = alertBounds.isWithinBoundning(43d, -73d);

        printLog("Is it within bound: " + withinBoundning);
    }


    /* GRID
        x x x x x x 6
        x x x x x 5 x
        x x x x 4 x x
        x - x 3 - x x
        x x 2 x x x x
        x 1 x x x x x
        - x x - x x x
    */
    private static void jts() throws ParseException {

        GeometryFactory gf = new GeometryFactory();
        Coordinate[] coordinates;
        coordinates = new Coordinate[]{
                new Coordinate(0, 0),
                new Coordinate(0,5),
                new Coordinate(1,2),
                new Coordinate(2,2),

                new Coordinate(2,0)
        };
        Geometry g1 = gf.createLineString(coordinates);
        Geometry convexHull = g1.convexHull();
        Point p = gf.createPoint(new Coordinate(1,2));




        printLog("ConvexHull: ");
        for (Coordinate c : convexHull.getCoordinates()) {
            printLog(c.toString());
        }
        boolean contains = convexHull.contains(p);
        String containsPoint = contains ? "contains" : "doesn't contain";
        printLog(containsPoint + " point " + p.toString());


    }


}
