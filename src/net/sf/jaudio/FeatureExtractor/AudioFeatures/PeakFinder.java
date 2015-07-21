package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import java.util.LinkedList;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * <p>
 * Implements a very basic peak detection algorithm. Peaks are calculated by
 * finding local maximum in the values of the frequency bins. All maxima within
 * a threshold of the largest value is considered a peak. The thresholds of all
 * peaks are provided in order without its bin location in the original signal.
 * </p>
 * 
 * @author Daniel McEnnis
 */
public class PeakFinder extends FeatureExtractor {

    int peakThreshold = 10;

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public PeakFinder() {
        String name = "Peak Detection";
        String description = "All peaks that are within an order of magnitude of the highest point";
        this.definition = new FeatureDefinition(name, description, true, 0,
                new String[] { "Threshold for peak detection" });
        this.dependencies = new String[] { "Magnitude Spectrum" };
        this.offsets = new int[] { 0 };
    }

    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        int count = 0;
        double max = 0.0;
        double bins[] = other_feature_values[0];
        for (int i = 0; i < other_feature_values[0].length; ++i) {
            if (other_feature_values[0][i] > max) {
                max = other_feature_values[0][i];
            }
        }
        max /= this.peakThreshold;
        // double[] tmp = fv.getFeatureVector();
        LinkedList<Double> val = new LinkedList<Double>();
        for (int i = 1; i < bins.length - 1; ++i) {
            if ((bins[i - 1] < bins[i]) && (bins[i + 1] < bins[i])
                    && (bins[i] > max)) {
                val.add(bins[i]);
            }
        }
        Double[] ret_tmp = val.toArray(new Double[] {});
        double[] ret = new double[ret_tmp.length];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = ret_tmp[i].doubleValue();
        }
        return ret;
    }

    @Override
    public String getElement(int index) throws Exception {
        if (index != 0) {
            throw new Exception("INTERNAL ERROR: PeakFinder index != 0 ("
                    + index + ")");
        } else {
            return Integer.toString(this.peakThreshold);
        }
    }

    /**
     * <p>
     * Sets the minimum fraction of the max point that will register as a peak.
     * The value is interpreted as 1/N of the maximum.
     * </p>
     * 
     * @param peak
     *            sets 1/N as threshold for peak detection.
     * @throws Exception
     *             thrown if a non-positive threshold is set.
     */
    public void setPeakThreshold(int peak) throws Exception {
        if (peak <= 0) {
            throw new Exception(
                    "PeakFinder peakThreshold must be a positive value.");
        } else {
            this.peakThreshold = peak;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * As a metafeature, recursively calls children to set the feature
     * requested.
     * </p>
     */
    @Override
    public void setElement(int index, String value) throws Exception {
        if (index != 0) {
            throw new Exception("INTERNAL ERROR: PeakFinder index != 0 ("
                    + index + ")");
        } else {
            try {
                setPeakThreshold(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new Exception("Peak Threshold Must be an integer");
            }

        }
    }

    @Override
    public Object clone() {
        PeakFinder ret = new PeakFinder();
        try {
            ret.setPeakThreshold(this.peakThreshold);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
