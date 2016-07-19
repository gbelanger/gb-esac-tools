package gb.esac.tools;

import java.awt.geom.Point2D;
import jsky.coords.WorldCoords;
import jsky.coords.wcscon;


/**
 *
 *  @version              November 2011 (last modified)
 *  @author 	Guillaume Belanger (ESAC, Spain)
 *
 **/

public final class CoordUtils {

    public static Point2D.Double raDecToGal(Point2D.Double radec) {

	wcscon convert = new wcscon();
	return convert.fk52gal(raDec);
    }

    public static double getAngularDist(double ra1, double dec1, double ra2, double dec2) {

	WorldCoords coords1 = new WorldCoords(ra1, dec1);
	WorldCoords coords2 = new WorldCoords(ra2, dec2);
	double angDist = coords1.dist(coords2); // the 'dist' method returns arcminutes

	return angDist;
    }

    public static double getAngularDist(Point2D.Double radec1, Point2D.Double radec2) {

	WorldCoords coords1 = new WorldCoords(radec1);
	WorldCoords coords2 = new WorldCoords(radec2);
	double angDist = coords1.dist(coords2); // the 'dist' method returns arcminutes

	return angDist;
    }

    public static double[] getAngularDist(Point2D.Double[] radec, Point2D.Double refRadec) {

	WorldCoords refCoords = new WorldCoords(refRadec);
	WorldCoords coords = null;
	double[] angDist = new double[radec.length];
	for ( int i=0; i < radec.length; i++ ) {
	    coords = new WorldCoords(radec[i]);
	    angDist[i] = refCoords.dist(coords); // return arcmins
	}

	return angDist;
    }

    public static double getXDist(Point2D.Double coord1, Point2D.Double coord2) {

	double xDist = Math.abs(coord1.getX() - coord2.getX());
	return xDist;
    }

    public static double[] getXDist(Point2D.Double[] coords, Point2D.Double refCoords) {

	double[] xDist = new double[coords.length];
	double refX = refCoords.getX();
	for ( int i=0; i < coords.length; i++ ) {
	    try { xDist[i] = Math.abs(coords[i].getX() - refX); }
	    catch (NullPointerException e) {xDist[i]=Double.MAX_VALUE;}
	}
	return xDist;
    }

    public static double getYDist(Point2D.Double coord1, Point2D.Double coord2) {

	double yDist = Math.abs(coord1.getY() - coord2.getY());
	return yDist;
    }

    public static double[] getYDist(Point2D.Double[] coords, Point2D.Double refCoords) {

	double[] yDist = new double[coords.length];
	double refY = refCoords.getY();
	for ( int i=0; i < coords.length ; i++ ) {
	    try { yDist[i] = Math.abs(coords[i].getY() - refY); }
	    catch (NullPointerException e) {yDist[i]=Double.MAX_VALUE;}
	}
	return yDist;
    }

    public static double getDist(Point2D.Double coord1, Point2D.Double coord2) {

	double dist = Math.sqrt(Math.pow(coord1.getX() - coord2.getX(), 2) + 
			     Math.pow(coord1.getY() - coord2.getY(), 2));
	return dist;
    }

    public static double[] getDist(Point2D.Double[] coords, Point2D.Double refCoords) {

	double[] dist = new double[coords.length];
	double refX = refCoords.getX();
	double refY = refCoords.getY();
	for ( int i=0; i < coords.length ; i++ ) {
	    try { dist[i] = Math.sqrt(Math.pow(coords[i].getX() - refX, 2) +
		        Math.pow(coords[i].getY() - refY, 2)); }
	    catch (NullPointerException e) {dist[i]=Double.MAX_VALUE;}
	}
	return dist;
    }

    public static Point2D.Double[] constructPoint2DArray(double[] x, double[] y) {

	Point2D.Double[] xy = new Point2D.Double[x.length];
	for ( int i=0; i < x.length; i++ )
	    xy[i] = new Point2D.Double(x[i], y[i]);
	return xy;
    }


}