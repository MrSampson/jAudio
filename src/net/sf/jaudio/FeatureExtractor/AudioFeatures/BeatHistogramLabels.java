/*
 * @(#)BeatHistogramLabels.java	1.0	April 7, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * A "feature extractor" that calculates the bin labels, in beats per minute, of
 * a beat histogram.
 *
 * <p>
 * Although this is not a useful feature for the purposes of classifying, it can
 * be useful for calculating other features.
 *
 * <p>
 * <b>IMPORTANT:
 * </P>
 * The window size of 256 RMS windows used in the BeatHistogram is hard-coded
 * into this class. Any changes to the value in that class must be made here as
 * well.
 *
 * <p>
 * Daniel McEnnis 05-08-05 added setBinNumber, getElement, setElement, and clone
 * 
 * @author Cory McKay
 */
public class BeatHistogramLabels extends FeatureExtractor {

    private int binNumber = 256;

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public BeatHistogramLabels() {
         name = "Beat Histogram Bin Labels";
         description = "The bin label, in beats per minute, of each beat "
                + "histogram bin. Not useful as a feature in itself, "
                + "but useful for calculating other features from "
                + "the beat histogram.";
        boolean is_sequential = true;
        int dimensions = 0;
        this.definition = new FeatureDefinition(name, description, is_sequential,
                dimensions);

        this.dependencies = new String[1];
        this.dependencies[0] = "Beat Histogram";

        this.offsets = new int[1];
        this.offsets[0] = 0;
    }

    /**
     * {@inheritDoc}
     * <p>
     * In the case of this feature, the <code>sampling_rate parameter</code> is
     * ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double[] beat_histogram = other_feature_values[0];

        if (beat_histogram != null) {
            double effective_sampling_rate = sampling_rate
                    / ((double) this.binNumber);

            int min_lag = (int) (0.286 * effective_sampling_rate);
            int max_lag = (int) (3.0 * effective_sampling_rate);
            double[] labels = net.sf.jaudio.FeatureExtractor.jAudioTools.DSPMethods
                    .getAutoCorrelationLabels(effective_sampling_rate, min_lag,
                            max_lag);

            for (int i = 0; i < labels.length; i++)
                labels[i] *= 60.0;
            return labels;
        } else
            return null;

    }

    /**
     * Sets the bin Number - changes should be linked to beatHistogramType
     * 
     * @param n
     *            new number of beat bins
     * @throws Exception
     *             thrown if new number of bins is les than 2
     */
    public void setBinNumber(int n) throws Exception {
        if (n < 2) {
            throw new Exception(
                    "There must be at least 2 bins in Beat Histogram Labels");
        } else {
            this.binNumber = n;
        }
    }

    /**
     * Function permitting an unintelligent outside function (ie. EditFeatures
     * frame) to get the default values used to populate the table's entries.
     * The correct index values are inferred from definition.attribute value.
     * 
     * @param index
     *            which of AreaMoment's attributes should be edited.
     */
    @Override
    public String getElement(int index) throws Exception {
        if (index != 0) {
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to BeatHistogramLabels:getElement");
        } else {
            return Integer.toString(this.binNumber);
        }
    }

    /**
     * Function permitting an unintelligent outside function (ie. EditFeatures
     * frame) to set the default values used to popylate the table's entries.
     * Like getElement, the correct index values are inferred from the
     * definition.attributes value.
     * 
     * @param index
     *            attribute to be set
     * @param value
     *            new value of the attribute
     */
    @Override
    public void setElement(int index, String value) throws Exception {
        if (index != 0) {
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to BeatHistogramLabels:setElement");
        } else {
            try {
                int type = Integer.parseInt(value);
                setBinNumber(type);
            } catch (Exception e) {
                throw new Exception(
                        "Length of Area Method of Moments must be an integer");
            }
        }
    }

    @Override
    public Object clone() {
        return new BeatHistogramLabels();
    }
}
