package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * This feature calculates the log of the derivative of last 2 versions of RMS.
 * This is useful for onset detection.
 * 
 * @author Daniel McEnnis
 */
public class RelativeDifferenceFunction extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public RelativeDifferenceFunction() {
        this.name = "Relative Difference Function";
        this.description = "log of the derivative of RMS.  Used for onset detection.";
        this.definition = new FeatureDefinition(this.name, this.description,
                true, 1);
        this.dependencies = new String[] { "Root Mean Square",
                "Root Mean Square" };
        this.offsets = new int[] { 0, -1 };
    }

 
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double[] ret = new double[1];
        double difference = Math.abs(other_feature_values[0][0]
                - other_feature_values[1][0]);
        if (difference < 1E-50) {
            difference = 1E-50;
        }
        ret[0] = Math.log(difference);

        return ret;

    }

    @Override
    public Object clone() {
        return new RelativeDifferenceFunction();
    }

}
