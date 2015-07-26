/*
 * @(#)StrongestBeat.java	1.0	April 7, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * A feature extractor that finds the strongest beat in a signal, int beats per
 * minute.
 *
 * <p>
 * This is found by finding the highest bin in the beat histogram.
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 *
 * @author Cory McKay
 */
public class StrongestBeat extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public StrongestBeat() {
        this.name = "Strongest Beat";
        this.description = "The strongest beat in a signal, in beats per minute, "
                + "found by finding the strongest bin in the beat histogram.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(this.name, this.description,
                is_sequential, dimensions);

        this.dependencies = new String[2];
        this.dependencies[0] = "Beat Histogram";
        this.dependencies[1] = "Beat Histogram Bin Labels";

        this.offsets = new int[2];
        this.offsets[0] = 0;
        this.offsets[1] = 0;
    }

    /**
     * {@inheritDoc}
     * <p>
     * In the case of this feature, the sampling_rate parameter is ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double[] beat_histogram = other_feature_values[0];

        if (beat_histogram != null) {
            double[] labels = other_feature_values[1];
            int highest_bin = net.sf.jaudio.FeatureExtractor.GeneralTools.Statistics
                    .getIndexOfLargest(beat_histogram);
            double[] result = new double[1];
            result[0] = labels[highest_bin];
            return result;
        } else
            return null;
    }

    @Override
    public Object clone() {
        return new StrongestBeat();
    }
}