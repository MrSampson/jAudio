/*
 * @(#)BeatSum.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * A feature extractor that extracts the Beat Sum from a signal. This is a good
 * measure of how important a role regular beats play in a piece of music.
 *
 * <pThis is calculated by finding the sum of all values in the beat histogram.
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 *
 * <p>
 * Daniel McEnnis 05-07-05 Added clone
 * 
 * @author Cory McKay
 */
public class BeatSum extends FeatureExtractor {
    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public BeatSum() {
         name = "Beat Sum";
         description = "The sum of all entries in the beat histogram. "
                + "This is a good measure of the importance of "
                + "regular beats in a signal.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(name, description,
                is_sequential, dimensions);

        this.dependencies = new String[1];
        this.dependencies[0] = "Beat Histogram";

        this.offsets = new int[1];
        this.offsets[0] = 0;
    }

    /**
     * {@inheritDoc}
     * <p>
     * In the case of this feature the sampling_rate is ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double[] beat_histogram = other_feature_values[0];

        if (beat_histogram != null) {
            double sum = 0.0;
            for (int i = 0; i < beat_histogram.length; i++)
                sum += beat_histogram[i];

            double[] result = new double[1];
            result[0] = sum;
            return result;
        } else
            return null;
    }

    @Override
    public Object clone() {
        return new BeatSum();
    }
}