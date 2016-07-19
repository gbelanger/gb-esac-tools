package gb.esac.tools;

import gb.esac.timeseries.TimeSeries;
import gb.esac.timeseries.TimeSeriesMaker;
import gb.esac.timeseries.TimeSeriesUtils;

public class TestDataUtils {


    public static void testFillGaps() throws Exception {

	TimeSeries ts = TimeSeriesMaker.makeTimeSeries("lc7.qdp");

	ts.writeCountsAsQDP("lc-gaps.qdp");
	ts = TimeSeriesUtils.fillGaps(ts);
	ts.writeCountsAsQDP("lc-noGaps.qdp");

    }

    public static void main(String[] args) throws Exception {

	testFillGaps();
    }

}