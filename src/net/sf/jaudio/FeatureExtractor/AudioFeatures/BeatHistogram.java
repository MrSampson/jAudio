/*
 * @(#)BeatHistogram.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * <p>
 * A feature extractor that extracts the Beat Histogram from a signal. This is
 * histogram showing the strength of different rhythmic periodicities in a
 * signal.
 * </p>
 * <p>
 * This is calculated by taking the RMS of 256 windows and then taking the FFT
 * of the result.
 * </p>
 * <p>
 * No extracted feature values are stored in objects of this class.
 * </p>
 * <p>
 * <b>IMPORTANT:</b>
 * </p>
 * <p>
 * <b>The window size of 256 RMS windows used here is hard-coded into the class
 * BeatHistogramLabels. Any changes to the window size in this class must be
 * made there as well.</b>
 * </p>
 * 
 * @author Cory McKay
 * @author Daniel McEnnis 05-07-05 Added setElement, getElement, setElement, and
 *         clone functions.
 */
public class BeatHistogram extends FeatureExtractor {

    private int number_windows = 256;

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public BeatHistogram() {
         name = "Beat Histogram";
         description = "A histogram showing the relative strength of different "
                + "rhythmic periodicities (tempi) in a signal. Found by "
                + "calculating the auto-correlation of the RMS.";
        boolean is_sequential = true;
        int dimensions = 0;
        this.definition = new FeatureDefinition(name, description,
                is_sequential, dimensions);

        this.dependencies = new String[this.number_windows];
        for (int i = 0; i < this.dependencies.length; i++)
            this.dependencies[i] = "Root Mean Square";

        this.offsets = new int[this.number_windows];
        for (int i = 0; i < this.offsets.length; i++)
            this.offsets[i] = 0 - i;
    }

    /**
     * {@inheritDoc}
     * <p>
     * In the case of this feature the <code>sampling_rate</code> is ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double[] rms = new double[other_feature_values.length];
        for (int i = 0; i < rms.length; i++)
            rms[i] = other_feature_values[i][0];

        double effective_sampling_rate = sampling_rate / ((double) rms.length);

        int min_lag = (int) (0.286 * effective_sampling_rate);
        int max_lag = (int) (3.0 * effective_sampling_rate);
        double[] auto_correlation = net.sf.jaudio.FeatureExtractor.jAudioTools.DSPMethods
                .getAutoCorrelation(rms, min_lag, max_lag);
        return auto_correlation;
    }

    /**
     * Helper function to set window length for this feature. Note that this
     * feature does *not* conform to the syntax of setWindow so this feature is
     * not affected by a global window change. This is necessary since the beat
     * bins have a different meaning than most windowed features.
     * 
     * @param n
     *            new number of beat bins
     * @throws Exception
     *             thrown if the new value is less than 2
     */
    public void setWindowLength(int n) throws Exception {
        if (n < 2) {
            throw new Exception(
                    "BeatHistogram window length must be greater than 1");
        } else {
            this.number_windows = n;
            this.dependencies = new String[this.number_windows];
            this.offsets = new int[this.number_windows];
            for (int i = 0; i < this.number_windows; ++i) {
                this.dependencies[i] = "Root Mean Square";
                this.offsets[i] = 0 - i;
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @param index
     *            which of Beat Histograms's attributes should be edited.
     */
    @Override
    public String getElement(int index) throws Exception {
        if (index != 0) {
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to AreaMoments:getElement");
        } else {
            return Integer.toString(this.number_windows);
        }
    }

    @Override
    public void setElement(int index, String value) throws Exception {
        if (index != 0) {
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to AreaMoments:setElement");
        } else {
            try {
                int type = Integer.parseInt(value);
                setWindowLength(type);
            } catch (Exception e) {
                throw new Exception(
                        "Length of Area Method of Moments must be an integer");
            }
        }
    }

    @Override
    public Object clone() {
        BeatHistogram ret = new BeatHistogram();
        ret.number_windows = this.number_windows;
        return ret;
    }

}