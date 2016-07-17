package gb.esac.tools;

import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister64;
import gb.esac.io.AsciiDataFileWriter;
import hep.aida.ref.histogram.FixedAxis;
import hep.aida.ref.histogram.Histogram1D;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

	
/**
 *
 * @version  Feb 2011 (last modified)
 * @author   Guillaume Belanger (ESAC, Spain)
 *
 **/

public final class DistributionFunc {

    public static Histogram1D getPDFHisto(Histogram1D histo, String dataType) {
		
	//   Get axis properties from original histo  
	FixedAxis axis = (FixedAxis) histo.axis();
	int nbins = axis.bins();
	double[] binWidths = new double[nbins+2];
	double[] binHeights = new double[nbins+2];
	for ( int i = 0; i < nbins+1; i++ ) {
	    if ( dataType.equals("counts") )  binWidths[i] = axis.binWidth(i);
	    else binWidths[i] = 1.0;
	    binHeights[i] = histo.binHeight(i);
	}
		
	//   Check number of "out of range" entries  
	int extraEntries = histo.extraEntries();
	if ( extraEntries != 0 ) {
	    System.out.println("Log  : There are "+extraEntries+" extra entries");
	    System.out.println("Warn : Out of range entries will be ignored");
	}
		
	//   Calculate mean occurence frequency for each bin  
	double mean = 0;
	double maxDt = 30*BasicStats.getMean(binWidths)/MinMax.getNonZeroMin(binHeights);
	double[] means = new double[nbins+2];
	for ( int i=0; i < nbins+1; i++ ) {
	    mean = binWidths[i]/binHeights[i];
	    if ( mean > 0.0 && mean <= maxDt )
		means[i]  = mean;
	}
	double min = MinMax.getMin(means);
	double max = MinMax.getMax(means);
	double pdfBinWidth = (max - min)/nbins;
	double sum = BasicStats.getSum(means);
	System.out.println(min+"\t"+max);
	
		
	//   Construct the PDF histo  
	FixedAxis pdfAxis = new FixedAxis(nbins, min, max);
	double[] pdf =  new double[nbins+2];
	double[] errors = new double[nbins+2];
	double[] binCentres = new double[nbins+2];
	for ( int i=0; i < nbins+1; i++ ) {
	    pdf[i] = means[i]/sum;
	    errors[i] = 0;
	    binCentres[i] = pdfAxis.binCenter(i);
	}
	String title = "Probability Distribution Function";
	Histogram1D pdfHisto = new Histogram1D("PDF", title, pdfAxis);
	pdfHisto.setContents(pdf, errors, null, null, null);
		
	//  For display purposes :
		 
 	//  Drop the last 2 elements of the histo (overflow and underflow)
// 	double[] times = new double[nbins];
// 	double[] prob = new double[nbins];
// 	for ( int i=0; i < nbins; i++ ) {
// 	    times[i] = binCentres[i];
// 	    prob[i] = pdf[i];
// 	}
		
 	//  Write the CDF as a QDP file
// 	DataFileWriter pdfFile = new DataFileWriter("pdf.qdp");
// 	String[] header = new String[] {
// 	    "! QDP File",
// 	    "DEV /XS", "READ 1 2", "TIME OFF",
// 	    "LINE STEP", "LAB T", "LAB F", "LW 3", "CS 1.3",
// 	    "LAB X Time (s)", "LAB Y PDF",
// 	    "VIEW 0.1 0.2 0.9 0.8",
// 	    "!"
// 	};
// 	pdfFile.writeData(header, times, prob);
		
	return pdfHisto;		
    }
	

    public static double[] getCDF(double[] binHeights) {

	//   Construct the cumulative probability distribution function 
		
	double sumOfBinHeights = BasicStats.getSum(binHeights);
	int nbins = binHeights.length;
	double[] pdf = new double[nbins];
	double[] cumProbDistFunc = new double[nbins];
	double sum = 0;
	for ( int i=0; i < nbins; i++ ) {
	    pdf[i] = binHeights[i]/sumOfBinHeights;
	    sum += pdf[i];
	    cumProbDistFunc[i] = sum;
	    //System.out.println(cumProbDistFunc[i]);
	}
	return cumProbDistFunc;
    }
	
	
    public static Histogram1D getCDFHisto(Histogram1D histo) {
		
	//   Get axis and number of bins from original histo  
	FixedAxis axis = (FixedAxis) histo.axis();
	int nbins = axis.bins();
		
	//   Check number of "out of range" entries  
	int extraEntries = histo.extraEntries();
	if ( extraEntries != 0 ) {
	    System.out.println("Log  : There are "+extraEntries+" extra entries");
	    System.out.println("Warn : Out of range entries will be ignored");
	}
				
	//   Construct the cumulative histo  
	double sumOfBinHeights = histo.sumBinHeights();
	double[] cumProb =  new double[nbins+2];
	double[] errors = new double[nbins+2];
	double sumOfProbs = 0;
	for ( int i=0; i < nbins+1; i++ ) {
	    sumOfProbs += histo.binHeight(i)/sumOfBinHeights;
	    cumProb[i] = sumOfProbs;
	    errors[i] = 0;
	}
	String title = "Cumulative Distribution Function";
	Histogram1D cumProbDistHisto = new Histogram1D("CDF", title, axis);
	cumProbDistHisto.setContents(cumProb, errors, null, null, null);

	//   Plot CDF
// 	AsciiDataFileWriter cdfFile = new AsciiDataFileWriter("cdf.qdp");
// 	boolean showStats = false;
// 	cdfFile.writeHisto(cumProbDistHisto, "Time (s)", showStats);

	return cumProbDistHisto;
    }

    public static double[] getRandom(Histogram1D cdfHisto, int nevents) {

 	MersenneTwister64 engine = new MersenneTwister64(new java.util.Date());
	return getRandom(cdfHisto, nevents, engine);
    }

    public static double[] getRandom(Histogram1D cdfHisto, int nevents, MersenneTwister64 engine) {


	//  Get axis properties and bin edges
	FixedAxis axis = (FixedAxis) cdfHisto.axis();
	int nbins = axis.bins();
	double[] binEdges = new double[nbins+1];
	for ( int i=1; i <= nbins+1; i++ )
	    binEdges[i-1] = axis.binLowerEdge(i);
	   

	//  Get the bin heights
	double[] cdfBinHeights = new double[nbins];
	for ( int i=0; i < nbins; i++ ) {
	    cdfBinHeights[i] = cdfHisto.binHeight(i);
	}

	//  Perform inversion on cdf and fill events array
  	Uniform uniform = new Uniform(0, 1, engine);
	double[] events = new double[nevents];
	int i=0;
 	while  ( i < nevents ) {

	    double r = uniform.nextDouble();

	    //  Almost always r is not found in the array, and therefore, the method return: - (insertionPoint -1), 
	    //  where insertionPoint is the point at which the number would be inserted if it were in the array
	    int bin = -(Arrays.binarySearch(cdfBinHeights, r) +1);

	    //  In the rare case that the number is actually found, then we need to revert back to the right index
	    if ( bin < 0 ) bin = -bin -1;

	    double x = binEdges[bin];
	    double binWidth = axis.binWidth(bin);
	    if ( bin >= 0 &&  bin < (nbins-1) ) {

		if ( bin == 0 ) {
		    x -= binWidth*(r/cdfBinHeights[bin]);
		    events[i] = x;
		    //logger.debug(r+"\t"+cdfBinHeights[bin]+"\t"+binEdges[bin]+"\t"+x);
		}
		else if ( r > cdfBinHeights[bin-1] ) {
		    x += binWidth*(r - cdfBinHeights[bin-1]) / (cdfBinHeights[bin] - cdfBinHeights[bin-1]);
		    events[i] = x;		    
		    //logger.debug(r+"\t"+cdfBinHeights[bin]+"\t"+binEdges[bin]+"\t"+x);
		}
		else {
		    events[i] = x;
		}
		i++;
	    }
	}
	return events;
    }


}