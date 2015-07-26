/*
 * @(#)StrengthOfStrongestBeat.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * <p>
 * A feature extractor that extracts the Strength of Strongest Beat from a
 * signal. This is a measure of how strong the strongest beat is compared to
 * other possible beats.
 * </p>
 *
 * <p>
 * This is calculated by finding the entry in the beat histogram corresponding
 * to the strongest beat and dividing it by the sum of all entries in the beat
 * histogram.
 * </p>
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 * </p>
 *
 * @author Cory McKay
 */
public class StrengthOfStrongestBeat extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public StrengthOfStrongestBeat() {
        this.name = "Strength Of Strongest Beat";
        this.description = "How strong the strongest beat in the beat histogram "
                + "is compared to other potential beats.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(this.name, this.description,
                is_sequential, dimensions);

        this.dependencies = new String[2];
        this.dependencies[0] = "Beat Histogram";
        this.dependencies[1] = "Beat Sum";

        this.offsets = new int[2];
        this.offsets[0] = 0;
        this.offsets[1] = 0;
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
            double beat_sum = other_feature_values[1][0];
            int highest_bin = net.sf.jaudio.FeatureExtractor.GeneralTools.Statistics
                    .getIndexOfLargest(beat_histogram);
            double highest_strength = beat_histogram[highest_bin];
            double normalized_strength = highest_strength / beat_sum;

            double[] result = new double[1];
            result[0] = normalized_strength;
            return result;
        } else
            return null;
    }

    @Override
    public Object clone() {
        return new StrengthOfStrongestBeat();
    }
}