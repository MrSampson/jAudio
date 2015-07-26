/*
 * @(#)FFTBinFrequencies.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * <p>
 * A "feature extractor" that calculates the bin labels, in Hz, of power
 * spectrum or magnitude spectrum bins that would be produced by the FFT of a
 * window of the size of that provided to the feature extractor.
 * </p>
 *
 * <p>
 * Although this is not a useful feature for the purposes of classifying, it can
 * be useful for calculating other features.
 * </p>
 * 
 * @author Cory McKay
 * @author Daniel McEnnis 05-07-05 Added <code>clone</code>
 */
public class FFTBinFrequencies extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public FFTBinFrequencies() {
         name = "FFT Bin Frequency Labels";
         description = "The bin label, in Hz, of each power spectrum or "
                + "magnitude spectrum bin. Not useful as a feature in "
                + "itself, but useful for calculating other features "
                + "from the magnitude spectrum and power spectrum.";
        boolean is_sequential = true;
        int dimensions = 0;
        this.definition = new FeatureDefinition(name, description,
                is_sequential, dimensions);

        this.dependencies = null;

        this.offsets = null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * In the case of this feature, the <code>sampling_rate</code> and
     * <code>other_feature_values</code> parameters are ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        // Find the size that an FFT window would be. This is the size
        // of the given samples, or the next highes power of 2 if it
        // is not a power of 2.
        int fft_size = net.sf.jaudio.FeatureExtractor.GeneralTools.Statistics
                .ensureIsPowerOfN(samples.length, 2);

        // Find the width in Hz of each bin
        int number_bins = fft_size;
        double bin_width = sampling_rate / (double) number_bins;
        double offset = bin_width / 2.0;

        // Find the number of bins in the power or magnitude spectrum
        int number_unfolded_bins = fft_size / 2;
        double[] labels = new double[number_unfolded_bins];
        for (int bin = 0; bin < labels.length; bin++)
            labels[bin] = (bin * bin_width) + offset;

        // Return the result
        return labels;
    }

    @Override
    public Object clone() {
        return new FFTBinFrequencies();
    }
}
