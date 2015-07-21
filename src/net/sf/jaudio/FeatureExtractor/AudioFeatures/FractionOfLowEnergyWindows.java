/*
 * @(#)FractionOfLowEnergyWindows.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * <p>
 * A feature extractor that extracts the Fraction Of Low Energy Windows from
 * window to window. This is a good measure of how much of a signal is quiet
 * relative to the rest of a signal.
 * </p>
 * <p>
 * This is calculated by taking the mean of the RMS of the last 100 windows and
 * finding what fraction of these 100 windows are below the mean.
 * </p>
 * <p>
 * No extracted feature values are stored in objects of this class.
 * </p>
 * 
 * @author Cory McKay
 * @author Daniel McEnnis 05-07-05 added number_of_windows as editable property.
 *         Added getElement, setElement, and clone
 * @author Daniel McEnnis 05-08-05 added setWindow to permit this feature to be
 *         edited by GlobalWindow frame.
 */
public class FractionOfLowEnergyWindows extends FeatureExtractor {

    private int number_windows = 100;

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public FractionOfLowEnergyWindows() {
        String name = "Fraction Of Low Energy Windows";
        String description = "The fraction of the last 100 windows that has an "
                + "RMS less than the mean RMS in the last 100 windows. "
                + "This can indicate how much of a signal is quiet "
                + "relative to the rest of the signal.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(name, description, is_sequential,
                dimensions);

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
     * In the case of this feature the sampling_rate is ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double average = 0.0;
        for (int i = 0; i < other_feature_values.length; i++)
            average += other_feature_values[i][0];
        average = average / ((double) other_feature_values.length);

        int count = 0;
        for (int i = 0; i < other_feature_values.length; i++)
            if (other_feature_values[i][0] < average)
                count++;

        double[] result = new double[1];
        result[0] = ((double) count) / ((double) other_feature_values.length);

        return result;
    }

    /**
     * Function that must be overridden to allow this feature to be set globally
     * by GlobalChange frame.
     * 
     * @param n
     *            the number of windows of offset to be used in calculating this
     *            feature
     */
    @Override
    public void setWindow(int n) throws Exception {
        if (n < 2) {
            throw new Exception(
                    "Fraction Of Low Energy Frames's window length must be 2 or greater");
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

   
    @Override
    public String getElement(int index) throws Exception {
        if (index != 0) {
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to FractionOfLowEnergyFrames:getElement");
        } else {
            return Integer.toString(this.number_windows);
        }
    }

  
    @Override
    public void setElement(int index, String value) throws Exception {
        if (index != 0) {
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to FractionOfLowEnergyFrames:setElement");
        } else {
            try {
                int type = Integer.parseInt(value);
                setWindow(type);
            } catch (Exception e) {
                throw new Exception(
                        "Length of Fraction Of Low Energy Frames's window must be an integer");
            }
        }
    }

    @Override
    public Object clone() {
        FractionOfLowEnergyWindows ret = new FractionOfLowEnergyWindows();
        ret.number_windows = this.number_windows;
        return ret;
    }

}