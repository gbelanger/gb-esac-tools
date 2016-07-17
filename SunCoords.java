package gb.esac.tools;


import java.text.DecimalFormat;


public class SunCoords {

    public static DecimalFormat num = new DecimalFormat("0.000000");

    public double mjd2000 = 51544.5;

    public static void main(String[] args) {


	//  Handle args

	double mjd=0, longitude=0;
	//51544.5 = Modified Julian Date of Epoch J2000.0, which is defined as January 1, 2000, 12h UT.

	if ( args.length != 2 ) {
	    System.out.println("Usage: java SunCoords MJD longitude");
	    System.exit(-1);
	}
	
	mjd = (Double.valueOf(args[0])).doubleValue();
	longitude = (Double.valueOf(args[1])).doubleValue();

	double nDaysFromJ2000 = mjd - mjd2000;
	System.out.println("nDaysFromJ2000 = "+nDaysFromJ2000);

	double nCenturiesFromJ2000 = nDaysFromJ2000/36525;
	System.out.println("nCenturiesFromJ2000 = "+nCenturiesFromJ2000);

	//  times are in hours

	double greenwichSiderealTimeAtMidnight = 6.6974 + 2400.0513*nCenturiesFromJ2000;
	System.out.println("greenwichSiderealTimeAtMidnight = "+greenwichSiderealTimeAtMidnight);

	double greenwichSiderealTime = greenwichSiderealTimeAtMidnight + (366.2422 / 365.2422) * universalTime;
	System.out.println("greenwichSiderealTime = "+greenwichSiderealTime);
	
	double localSiderealTime = greenwichSiderealTime + longitude;
	System.out.println("localSiderealTime = "+localSiderealTime);
	
	double localNDaysFromJ2000 = nDaysFromJ2000 + localSiderealTime/24;
	System.out.println("localNDaysFromJ2000 = "+localNDaysFromJ2000);
	
	double localNCenturiesFromJ2000 = localNDaysFromJ2000 / 36525;
	System.out.println("localNCenturiesFromJ2000 = "+localNCenturiesFromJ2000);


	//MeanLongitude = 280.460 + 0.9856474 * Days
	//MeanAnomaly = 357.528 + 0.9856003 * Days



	double sunMeanLongitude = 280.466 + 36000.770*localNCenturiesFromJ2000;
	System.out.println("sunMeanLongitude = "+sunMeanLongitude);
	    
	double sunMeanAnomaly = 357.529 + 35999.050*localNCenturiesFromJ2000;
	System.out.println("sunMeanAnomaly = "+sunMeanAnomaly);

	double sunEqnOfCentre = (1.915 - 0.005*localNCenturiesFromJ2000)*Math.sin(sunMeanAnomaly) + 0.020*Math.sin(2*sunMeanAnomaly);
	System.out.println("sunEqnOfCentre = "+sunEqnOfCentre);

	double sunEclipticalLongitude = sunMeanLongitude + sunEqnOfCentre;
	System.out.println("sunEclipticalLongitude = "+sunEclipticalLongitude);

	double quandrantOffset = 0;
	if ( sunEclipticalLongitude >=0 && sunEclipticalLongitude < 90 ) quandrantOffset = 0;
	else if ( sunEclipticalLongitude >= 90 &&  sunEclipticalLongitude < 180 ) quandrantOffset = 90;
	else if ( sunEclipticalLongitude >= 180 && sunEclipticalLongitude < 270 ) quandrantOffset = 180;
	else quandrantOffset = 270;
	System.out.println("quandrantOffset = "+quandrantOffset);

	double obliquityOfEcliptic =  23.439 - 0.013 * localNCenturiesFromJ2000;  
	System.out.println("obliquityOfEcliptic = "+obliquityOfEcliptic);

	double sunRA = Math.atan(Math.tan(sunEclipticalLongitude) * Math.cos(obliquityOfEcliptic));  
	sunRA += quandrantOffset;
	
	double sunDec = Math.asin(Math.sin(sunRA) * Math.sin(obliquityOfEcliptic));

	System.out.println();
	System.out.println("Sun coords in RA and Dec (J2000): "+num.format(sunRA) +" "+num.format(sunDec));

    }

    public double getGMST(double mjd) {

// ----------------------------
// Greenwich Mean Sidereal Time
// ----------------------------

// http://www.pcigeomatics.com/cgi-bin/pcihlp/AVHRRAD%7CDETAILS%7CANGLE+GENERATION

// Preliminaries: We work with Modified Julian Dates here instead of
//                Julian Dates because MJDs turnover at midnight instead
//                of at noon - hence they are a little easier to work with.
//                Refer to the Astronomical Almanac for details.

// Given:   MJD       = Modified Julian Date

//          MJD2000   = 51544.5 = Modified Julian Date of Epoch J2000.0,
//                      which is defined as January 1, 2000, 12h UT.

	
	
	double secPerDay = 1440.0;

	double solarSiderealDayRatio = 1.00273790934;

// Step 1.  Compute the integral and fractional MJD.

	double intMJD = Math.floor(mjd);
		
	double fracMJD = mjd - intMJD;

// Step 2.  Compute Tu, which is the interval of time, measured in Julian
//          centuries of 36525 days of universal time (mean solar days),
//          elapsed since January 1, 2000, 12h UT.

//          The Ast. Almanac gives the following equation for Tu:

//            Tu = (JD - 2451545.0) / 36525

//          We will modify this equation to use a Modified Julian Date
//          instead. The value of Tu is not affected because there is a
//          constant offset between JD and MJD values - and MJD is also
//          referenced against the January 1, 2000, 12h UT epoch.

	double tu = (intMJD - mjd2000) / 36525.0;

// Step 3.  Compute the GMST value at 0h UT. This value is in seconds.

	double gmst = 24110.54841 + 8640184.812866 * tu + 0.093104 * Math.pow(tu, 2) +
	    (-6.2e-6) * Math.pow(tu, 3);

// Step 4.  Now add the appropriate mean sidereal time interval to GMST using the fractional day value.
         
	double gmst = gmst + fracMJD * secPerDay * solarSiderealDayRatio;

// Step 5.  Reduce the GMST value to between 0 and 86400 seconds.
//          Then convert it to an angular measure (degrees).

// 	double gmst = gmst / secPerDay * 360.0;

	return gmst;

    }

    public double[] getSolarRaAndDec(double mjd) {

// --------------------------------
// Solar Hour Angle and Declination
// --------------------------------

// Given:   MJD   = Modified Julian Date
//          GMST  = Greenwich Mean Sidereal Time corresponding to MJD

	
//          Lon   = Longitude of some point on Earth's surface (degrees East)
//          Lat   = Latitude of some point on Earth's surface (degrees North)
                 

// Step 1.  Compute the number of days from J2000.0.

	double days = mjd - mjd2000;

// Step 2.  Compute the mean longitude and the mean anomaly of the Sun.
//          The formulae can be found on page C24 of the 1988 Ast Almanac.

	double meanLongitude = 280.460 + 0.9856474 * days;

	double MeanAnomaly = 357.528 + 0.9856003 * days;

// Step 3.  Compute the ecliptic longitude of the Sun. See page C24 of the Ast. Almanac.

	double eclipticLongitude = meanLongitude + 1.915*Math.sin(meanAnomaly) + 0.020*Math.sin(2.0*meanAnomaly);

// Step 4.  Compute the obliquity of the ecliptic in radians. See C24.

	double obliquityOfEcliptic = 23.439 - 0.0000004*days;


// Step 5.  Compute the right ascension of the Sun. See page C24.
//          Ensure that the right ascension and ecliptic longitude values
//          are in the same hemisphere.

	double solarRA = Math.atan(cos(obliquityOfEcliptic) * tan(eclipticLongitude));

// Step 6.  Compute the declination of the Sun.

	
	double solarDec = Math.asin(obliquityOfEcliptic) * Math.sin(eclipticLongitude);


	return new double[] {solarRA, solarDec};


// Step 7.  Compute the local (apparent) sidereal time (as an angle).
//          Use GMST as a close approximation to GST (Greenwich
//          Apparent Sidereal Time).

//          LST = GST + Lon

// Step 8.  Compute the hour angle of the Sun. A negative hour angle
//          means the Sun is east of the observer's location.

//          SolarHourAngle = LST - SolarRightAscension


    }

    public double dmy2jd(double day, double month, double year) {

	double jd = 367*year - 7*( Math.floor( year + Math.flour( (month+9)/12 ) )/4 ) + Math.floor(275*month/9) + day + 1721014;
	
	return jd;

    }

    public double jd2mjd(double jd) {

	return jd - 2400000.5;

    }

    public double dmy2mjd(double day, double month, double year) {

	double jd = dmy2jd(day, month, year);
	
	return jd2mjd(jd);

    }

}