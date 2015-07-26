/*
 * @(#)ZeroCrossings.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * A feature extractor that extracts the Zero Crossings from a set of samples.
 * This is a good measure of the pitch as well as the noisiness of a signal.
 *
 * <p>
 * Zero crossings are calculated by finding the number of times the signal
 * changes sign from one sample to another (or touches the zero axis).
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 *
 * @author Cory McKay
 */
public class ZeroCrossings extends FeatureExtractor {
    /**
     * Constructor.
     */
    public ZeroCrossings() {
        this.name = "Zero Crossings";
        this.description = "The number of times the waveform changed sign. "
                + "An indication of frequency as well as noisiness.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(this.name, this.description,
                is_sequential, dimensions);

        this.dependencies = null;

        this.offsets = null;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * In the case of this feature, the <code>sampling_rate</code> and
     * <code>other_feature_values</code> parameters are ignored.
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        long count = 0;
        for (int samp = 0; samp < samples.length - 1; samp++) {
            if (samples[samp] > 0.0 && samples[samp + 1] < 0.0)
                count++;
            else if (samples[samp] < 0.0 && samples[samp + 1] > 0.0)
                count++;
            else if (samples[samp] == 0.0 && samples[samp + 1] != 0.0)
                count++;
        }
        double[] result = new double[1];
        result[0] = (double) count;
        return result;
    }

    @Override
    public Object clone() {
        return new ZeroCrossings();
    }
}