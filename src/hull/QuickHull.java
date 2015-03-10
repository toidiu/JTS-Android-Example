package hull;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by toidiu on 3/9/15.
 */
public class QuickHull {

    public static final Double PRECISION = 100000d;
    private static Double minX;
    private static Double maxX;
    private static Double minY;
    private static Double maxY;

    public static void main(String[] args) {
        System.out.println("Hull World!");
        ArrayList<Point> points = new ArrayList<Point>();


        String nyc = "40.92000050153431,-70.73781352490187:41.956247395953746,-72.79242150485516:41.22115770000884,-74.78233091533184:39.54396045257809,-74.55334771424532:40.77925883409848,-73.4349694848060";
        String center = "2.4794429897243755,32.08909805864096:2.4742550843288624,32.08483502268791:2.4803148961577492,32.081158719956875:2.4790571131632424,32.08520717918873:2.4822871510787605,32.0865660533309:2.4849309997377493,32.09032852202654:2.481944150434283,32.09204513579607:2.481572342994521,32.08906956017017";
        String negative = "-29.522703432755556,-41.27741876989603:5.046915913082957,-46.55085526406765:11.766741412208532,-81.1211659759283:0.13158235308441862,-65.06648037582636:-14.590917094013337,-92.72272579371929:-27.981681905455275,-67.41023015230894:-20.280064770973283,-58.555124290287";

        String[] pts = negative.split(":");
        for (String pt : pts) {
            String[] split = pt.split(",");
            Double x = Double.parseDouble(split[0]);
            Double y = Double.parseDouble(split[1]);

            int xi = (int) (x * PRECISION);
            int yi = (int) (y * (PRECISION));

            points.add(new Point(xi, yi));
        }

//        points.add(new Point(1, 3));
//        points.add(new Point(2, 5));
//        points.add(new Point(3, 4));
//        points.add(new Point(5, 4));
//        points.add(new Point(4, 1));
//        points.add(new Point(2, 2));
//        points.add(new Point(3, 2));

        QuickHull qh = new QuickHull();
        ArrayList<Point> p = qh.quickHull(points);



        System.out
                .println("The points in the Convex hull using Quick Hull are: ");
        for (Point aP : p) {
            Double x = aP.x / PRECISION;
            Double y = aP.y / PRECISION;

            getMinMax(x, y);
            System.out.println("(" + x + ", " + y + ")");
        }


        System.out
                .println("Min value: \n" + minX + "," + minY);
        System.out
                .println("Max value: \n" + maxX + ","+ maxY);


    }

    private static void getMinMax(Double x, Double y) {
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


    public ArrayList<Point> quickHull(ArrayList<Point> points) {
        ArrayList<Point> convexHull = new ArrayList<Point>();
        if (points.size() < 3)
            return (ArrayList) points.clone();

        int minPoint = -1, maxPoint = -1;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).x < minX) {
                minX = points.get(i).x;
                minPoint = i;
            }
            if (points.get(i).x > maxX) {
                maxX = points.get(i).x;
                maxPoint = i;
            }
        }
        Point A = points.get(minPoint);
        Point B = points.get(maxPoint);
        convexHull.add(A);
        convexHull.add(B);
        points.remove(A);
        points.remove(B);

        ArrayList<Point> leftSet = new ArrayList<Point>();
        ArrayList<Point> rightSet = new ArrayList<Point>();

        for (Point p : points) {
            if (pointLocation(A, B, p) == -1)
                leftSet.add(p);
            else if (pointLocation(A, B, p) == 1)
                rightSet.add(p);
        }
        hullSet(A, B, rightSet, convexHull);
        hullSet(B, A, leftSet, convexHull);

        return convexHull;
    }

    private void hullSet(Point A, Point B, ArrayList<Point> set,
                         ArrayList<Point> hull) {
        int insertPosition = hull.indexOf(B);
        if (set.size() == 0)
            return;
        if (set.size() == 1) {
            Point p = set.get(0);
            set.remove(p);
            hull.add(insertPosition, p);
            return;
        }
        int dist = Integer.MIN_VALUE;
        int furthestPoint = -1;
        for (int i = 0; i < set.size(); i++) {
            Point p = set.get(i);
            int distance = distance(A, B, p);
            if (distance > dist) {
                dist = distance;
                furthestPoint = i;
            }
        }
        Point P = set.get(furthestPoint);
        set.remove(furthestPoint);
        hull.add(insertPosition, P);

        // Determine who's to the left of AP
        ArrayList<Point> leftSetAP = new ArrayList<Point>();
        for (Point M : set) {
            if (pointLocation(A, P, M) == 1) {
                leftSetAP.add(M);
            }
        }

        // Determine who's to the left of PB
        ArrayList<Point> leftSetPB = new ArrayList<Point>();
        for (Point M : set) {
            if (pointLocation(P, B, M) == 1) {
                leftSetPB.add(M);
            }
        }
        hullSet(A, P, leftSetAP, hull);
        hullSet(P, B, leftSetPB, hull);

    }

    private int distance(Point A, Point B, Point C) {
        int ABx = B.x - A.x;
        int ABy = B.y - A.y;
        int num = ABx * (A.y - C.y) - ABy * (A.x - C.x);
        if (num < 0)
            num = -num;
        return num;
    }

    private int pointLocation(Point A, Point B, Point P) {
        int cp1 = (B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x);
        if (cp1 > 0)
            return 1;
        else if (cp1 == 0)
            return 0;
        else
            return -1;
    }

}
