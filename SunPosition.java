package gb.esac.tools;

import java.util.Date;
import java.awt.geom.Point2D;

import org.apache.log4j.Logger;

public final class SunPosition {


    static Logger logger = Logger.getLogger(SunPosition.class);

    public String[] monthList = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Dec"};

    public int[] monthListInt = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 , 12};



    // Convert radian angle to degrees

    public static double radToDeg(double angleRad) {
    
	return (180.0 * angleRad / Math.PI);
    }


    // Convert degree angle to radians

    public static double degToRad(double angleDeg) {

	return (Math.PI * angleDeg / 180.0);
    }



    /** 
	Name:    calcDayOfYear								
	Type:    Function									
	Purpose: Finds numerical day-of-year from mn, day and lp year info  
	Arguments:										
	month: January = 1								
	day  : 1 - 31									
	lpyr : 1 if leap year, 0 if not						
	Return value:										
	The numerical day of year							
	
    **/

    //  Not used in this class
    public static int calcDayOfYear(int mn, int dy, int lpyr) {

	int k = lpyr==1 ? 1 : 2;
	int doy = (int) (Math.floor((275 * mn)/9) - k * Math.floor((mn + 9)/12) + dy -30);
	return doy;
    }



    /** 
	Name:   jd2dow								
	Type:    Function									
	Purpose: Derives weekday from Julian Day					
	Arguments:										
	jd : Julian Day									
	Return value:										
	String containing name of weekday						
    **/

    //  Not used in this class
    public static String jd2dow(double jd) {

	int a = (int) (jd + 1.5) % 7;
	String dow = (a==0)?"Sunday":(a==1)?"Monday":(a==2)?"Tuesday":(a==3)?"Wednesday":(a==4)?"Thursday":(a==5)?"Friday":"Saturday";
	return dow;
    }



    /** 
	Name:    ymd2jd									
	Type:    Function									
	Purpose: Julian day from calendar day						
	Arguments:										
	year : 4 digit year								
	month: January = 1								
	day  : 1 - 31									
	Return value: The Julian day corresponding to the date
	**** NOTE ****
	Number is returned for start of day.  Fractional days should be	
	added later.									
    **/
    
    //  Not used in this class
    public static double ymd2jd(int year, int month, int day) {

	if (month <= 2) {
	    year -= 1;
	    month += 12;
	}
	double a = Math.floor(year/100);
	double b = 2 - a + Math.floor(a/4);
	double jd = Math.floor(365.25*(year + 4716)) + Math.floor(30.6001*(month+1)) + day + b - 1524.5;

	return jd;
    }

    
    /**  This method returns the precise value of the JD  **/
    public static double ymd2jd(int[] ymdhms) {

	int year = ymdhms[0];
	int month = ymdhms[1];
	int day = ymdhms[2];
	int hours = ymdhms[3];
	int min = ymdhms[4];
	int sec = ymdhms[5];

	if (month <= 2) {
	    year -= 1;
	    month += 12;
	}
	double a = Math.floor(year/100);
	double b = 2 - a + Math.floor(a/4);

	double jd = Math.floor(365.25*(year + 4716)) + Math.floor(30.6001*(month+1)) + day + b - 1524.5;
	double fracOfDay = hours/24.0 + min/1440.0 + sec/86400.0;
	jd += fracOfDay;

	return jd;
    }



    /** 
	Name:    jd2ymd								
	Type:    Function									
	Purpose: Calendar date from Julian Day					
	Arguments:										
	jd   : Julian Day									
	Return value:										
	String date in the form YYYY-mm-dd					
	Note:											
    **/

    //  Not used in this class	
    public static String jd2ymd(int jd) {

	int z = (int) Math.floor(jd + 0.5);
	double f = (jd + 0.5) - z;

	double a = 0;
	if (z < 2299161) {
	    a = z;
	} else {
	    double alpha = Math.floor((z - 1867216.25)/36524.25);
	    a = z + 1 + alpha - Math.floor(alpha/4);
	}

	double b = a + 1524;
	double c = Math.floor((b - 122.1)/365.25);
	double d = Math.floor(365.25 * c);
	double e = Math.floor((b - d)/30.6001);

	int day = (new Double(b - d - Math.floor(30.6001 * e) + f)).intValue();
	int month = (new Double((e < 14) ? e - 1 : e - 13)).intValue();
	int year = (new Double((month > 2) ? c - 4716 : c - 4715)).intValue();

	Date date = new Date(year, month, day);
	return date.toString();
    }




    /**   
	  Name:    jd2century						
	  Type:    Public Static Double 									
	  Purpose: convert Julian Day to centuries since J2000.0.			
	  Arguments:										
	  jd : the Julian Day to convert						
	  Return value:										
	  the T value corresponding to the Julian Day				

    **/

    //  Not used in this class
    public static double  jd2century(double jd) {

	double t = (jd - 2451545.0)/36525.0;
	return t;
    }



    /** 
	Name:    century2jd							
	Type:    Public Static Double 									
	Purpose: convert centuries since J2000.0 to Julian Day.			
	Arguments:										
	t : number of Julian centuries since J2000.0				
	Return value:										
	the Julian Day corresponding to the t value				

    **/
	
    //  Not used in this class
    public static double  century2jd(double t) {

	double jd = t * 36525.0 + 2451545.0;
	return jd;
    }


    

    /** 
	Name:    calGeomMeanLongSun							
	Type:    Public Static Double 									
	Purpose: calculate the Geometric Mean Longitude of the Sun		
	Arguments:										
	t : number of Julian centuries since J2000.0				
	Return value:										
	the Geometric Mean Longitude of the Sun in degrees			
    **/

    public static double  calcGeomMeanLongSun(double t) {

	double lZero = 280.46646 + t * (36000.76983 + 0.0003032 * t);
	while(lZero  > 360.0)
	{
	    lZero -= 360.0;
	}
	while(lZero < 0.0)
	{
	    lZero += 360.0;
	}
	return lZero;		// in degrees
    }



    /** 
	Name:    calGeomAnomalySun							
	Type:    Public Static Double 									
	Purpose: calculate the Geometric Mean Anomaly of the Sun		
	Arguments:										
	t : number of Julian centuries since J2000.0				
	Return value:										
	the Geometric Mean Anomaly of the Sun in degrees			

    **/

    public static double  calcGeomMeanAnomalySun(double t) {

	double m = 357.52911 + t * (35999.05029 - 0.0001537 * t);
	return m;		// in degrees
    }




    /**
	Name:    calcEccentricityEarthOrbit						
	Type:    Public Static Double 									
	Purpose: calculate the eccentricity of earth's orbit			
	Arguments:										
	t : number of Julian centuries since J2000.0				
	Return value:										
	the unitless eccentricity							
    **/

    
    public static double  calcEccentricityEarthOrbit(double t) {

	double e = 0.016708634 - t * (0.000042037 + 0.0000001267 * t);
	return e;		// unitless
    }


    
    /** 
	Name:    calcSunEqOfCenter	   
	Type:    Public Static Double 									
	Purpose: calculate the equation of center for the sun			
	Arguments:										
	t : number of Julian centuries since J2000.0				
	Return value:										
	in degrees										
    **/

    public static double  calcSunEqOfCenter(double t) {

	double m = calcGeomMeanAnomalySun(t);

	double mrad = degToRad(m);
	double sinm = Math.sin(mrad);
	double sin2m = Math.sin(mrad+mrad);
	double sin3m = Math.sin(mrad+mrad+mrad);

	double c = sinm * (1.914602 - t * (0.004817 + 0.000014 * t)) + sin2m * (0.019993 - 0.000101 * t) + sin3m * 0.000289;
	return c;		// in degrees
    }



    /** 
	Name:    calcSunTrueLong								
	Type:    Public Static Double 									
	Purpose: calculate the true longitude of the sun				
	Arguments:										
	t : number of Julian centuries since J2000.0				
	Return value:										
	sun's true longitude in degrees						
	
    **/
	

    public static double  calcSunTrueLong(double t) {

	double lZero = calcGeomMeanLongSun(t);
	double c = calcSunEqOfCenter(t);

	double o = lZero + c;
	return o;		// in degrees
    }

 

   /**  
	Name:    calcSunTrueAnomaly		
	Type:    Public Static Double 									
	Purpose: calculate the true anamoly of the sun				
	Arguments:										
	t : number of Julian centuries since J2000.0				
	Return value:										
	sun's true anamoly in degrees							
   **/
	
    public static double  calcSunTrueAnomaly(double t) {

	double m = calcGeomMeanAnomalySun(t);
	double c = calcSunEqOfCenter(t);

	double v = m + c;
	return v;		// in degrees
    }



    /**   
	  Name:    calcSunRadVector								
	  Type:    Public Static Double 									
	  Purpose: calculate the distance to the sun in AU				
	  Arguments:										
	  t : number of Julian centuries since J2000.0				
	  Return value:										
	  sun radius vector in AUs							
    **/

    public static double  calcSunRadVector(double t) {

	double v = calcSunTrueAnomaly(t);
	double e = calcEccentricityEarthOrbit(t);
 
	double r = (1.000001018 * (1 - e * e)) / (1 + e * Math.cos(degToRad(v)));
	return r;		// in AUs
    }




    /**  
	 Name:    calcSunApparentLong							
	 Type:    Public Static Double 									
	 Purpose: calculate the apparent longitude of the sun			
	 Arguments:										
	 t : number of Julian centuries since J2000.0				
	 Return value:										
	 sun's apparent longitude in degrees						
    **/

    public static double  calcSunApparentLong(double t) {

	double o = calcSunTrueLong(t);

	double omega = 125.04 - 1934.136 * t;
	double lambda = o - 0.00569 - 0.00478 * Math.sin(degToRad(omega));
	return lambda;		// in degrees
    }




    /**    
	   Name:    calcMeanObliquityOfEcliptic						
	   Type:    Public Static Double 									
	   Purpose: calculate the mean obliquity of the ecliptic			
	   Arguments:										
	   t : number of Julian centuries since J2000.0				
	   Return value:										
	   mean obliquity in degrees							
    **/
    
    public static double  calcMeanObliquityOfEcliptic(double t) {

	double seconds = 21.448 - t*(46.8150 + t*(0.00059 - t*(0.001813)));
	double eZero = 23.0 + (26.0 + (seconds/60.0))/60.0;
	return eZero;		// in degrees
    }



    /**   
	  Name:    calcObliquityCorrection						
	  Type:    Public Static Double 									
	  Purpose: calculate the corrected obliquity of the ecliptic		
	  Arguments:										
	  t : number of Julian centuries since J2000.0				
	  Return value:										
	  corrected obliquity in degrees						
    **/

    public static double  calcObliquityCorrection(double t) {

	double eZero = calcMeanObliquityOfEcliptic(t);

	double omega = 125.04 - 1934.136 * t;
	double e = eZero + 0.00256 * Math.cos(degToRad(omega));
	return e;		// in degrees
    }




    /**   
	  Name:    calcSunRA
	  Type:    Public Static Double 									
	  Purpose: calculate the right ascension of the sun				
	  Arguments:										
	  t : number of Julian centuries since J2000.0				
	  Return value:										
	  sun's right ascension in degrees						
    **/

    public static double  calcSunRA(double t) {

	double e = calcObliquityCorrection(t);
	double lambda = calcSunApparentLong(t);
 
	double tananum = (Math.cos(degToRad(e)) * Math.sin(degToRad(lambda)));
	double tanadenom = (Math.cos(degToRad(lambda)));
	double alpha = radToDeg(Math.atan2(tananum, tanadenom));
	return alpha;		// in degrees
    }



    /**  
	 Name:    calcSunDec
	 Type:    Public Static Double 									
	 Purpose: calculate the declination of the sun				
	 Arguments:										
	 t : number of Julian centuries since J2000.0				
	 Return value:										
	 sun's declination in degrees							
    **/

    public static double  calcSunDec(double t) {

	double e = calcObliquityCorrection(t);
	double lambda = calcSunApparentLong(t);

	double sint = Math.sin(degToRad(e)) * Math.sin(degToRad(lambda));
	double theta = radToDeg(Math.asin(sint));
	return theta;		// in degrees
    }


    /**  This combines the two previous ones into a single method  **/

    public static Point2D.Double getSunRaDec(double julianCenturiesSinceJ2000) {


	double t = julianCenturiesSinceJ2000;
	double e = calcObliquityCorrection(t);
	double lambda = calcSunApparentLong(t);

	// RA
	double tananum = (Math.cos(degToRad(e)) * Math.sin(degToRad(lambda)));
	double tanadenom = (Math.cos(degToRad(lambda)));
	double alpha = radToDeg(Math.atan2(tananum, tanadenom));
	if ( alpha < 0 ) 
	    alpha += 360;

	// Dec
	double sint = Math.sin(degToRad(e)) * Math.sin(degToRad(lambda));
	double theta = radToDeg(Math.asin(sint));

	Point2D.Double sunRaDec = new Point2D.Double(alpha, theta);

	return sunRaDec;

    }


    public static Point2D.Double getSunRaDec(int year, int month, int day, int hours, int min, int sec) {

	int[] ymdhms = new int[] {year, month, day, hours, min, sec};
	double jd = ymd2jd(ymdhms);
	double timeInJulianCenturiesSinceJ2000 = jd2century(jd);
	Point2D.Double sunRaDec = SunPosition.getSunRaDec(timeInJulianCenturiesSinceJ2000);

	return sunRaDec;
    }



    /**    
	   Name:    calcEquationOfTime 
	   Type:    Public Static Double 									
	   Purpose: calculate the difference between true solar time and mean	
	   solar time									
	   Arguments:										
	   t : number of Julian centuries since J2000.0				
	   Return value:										
	   equation of time in minutes of time						
    **/
    
    public static double  calcEquationOfTime(double t) {

	double epsilon = calcObliquityCorrection(t);
	double lZero = calcGeomMeanLongSun(t);
	double e = calcEccentricityEarthOrbit(t);
	double m = calcGeomMeanAnomalySun(t);

	double y = Math.tan(degToRad(epsilon)/2.0);
	y *= y;

	double sin2lZero = Math.sin(2.0 * degToRad(lZero));
	double sinm   = Math.sin(degToRad(m));
	double cos2lZero = Math.cos(2.0 * degToRad(lZero));
	double sin4lZero = Math.sin(4.0 * degToRad(lZero));
	double sin2m  = Math.sin(2.0 * degToRad(m));

	double Etime = y * sin2lZero - 2.0 * e * sinm + 4.0 * e * y * sinm * cos2lZero
	    - 0.5 * y * y * sin4lZero - 1.25 * e * e * sin2m;

	return radToDeg(Etime)*4.0;	// in minutes of time
    }


}