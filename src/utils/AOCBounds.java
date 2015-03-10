package utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


/**
 * Created by toidiu on 3/9/15.
 */
public class AOCBounds {

    //---------Static
    private static GeometryFactory gf = new GeometryFactory();

    //---------Fields
    Double minX;
    Double maxX;
    Double minY;
    Double maxY;
    private Geometry convexHull;
    private Coordinate coorInstance = new Coordinate();


    public AOCBounds(String aocString) {

        convertStringToAOC(aocString);
    }

    private void convertStringToAOC(String aocString) {

        String[] points = aocString.split(":");

        Coordinate[] coordinateList = new Coordinate[points.length];
        for (int i = 0, pointsLength = points.length; i < pointsLength; i++) {
            String pt = points[i];
            String[] latLon = pt.split(",");
            Double x = Double.valueOf(latLon[0]);
            Double y = Double.valueOf(latLon[1]);

            setMinMaxBounding(x, y);
            coordinateList[i] = new Coordinate(x, y);
        }

        Geometry aoc = gf.createLineString(coordinateList);
        convexHull = aoc.convexHull();
    }

    private void setMinMaxBounding(Double x, Double y) {
        //----------X
        if (minX == null) {
            minX = maxX = x;
            minY = maxY = y;
            return;
        }

        if (x < minX) {
            minX = x;
        } else if (x > maxX) {
            maxX = x;
        }

        //----------Y
        if (y < minY) {
            minY = y;
        } else if (y > maxY) {
            maxY = y;
        }
    }

    public boolean isWithinBoundning(Double x, Double y) {
        if (x < minX || x > maxX || y < minY || y > maxY) {
            return false;
        } else {
            coorInstance.x = x;
            coorInstance.y = y;
            Point point = gf.createPoint(coorInstance);

            return convexHull.contains(point);
        }
    }

}
