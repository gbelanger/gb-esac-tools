import gb.esac.eventlist.EventList;
import gb.esac.periodogram.PeriodogramMaker;
import gb.esac.periodogram.ModifiedRayleighPeriodogram;

public class RkForBinnedData {

    public static void main(String[] args) {

	String filename = "oversamplingArtefacts_evlist.fits";
	EventList evlist = new EventList(filename);
	double nuMin = 1e-4;
	double nuMax = 1e-2;
	int sampling = 21;
	int harmonic = 1;
	ModifiedRayleighPeriodogram r2_1 = PeriodogramMaker.makeModifiedRaleighPeriodogram(evlist, nuMin, nuMax, harmonic);
	r2_1.writeAsQDP("r2_1.qdp");
    }

}