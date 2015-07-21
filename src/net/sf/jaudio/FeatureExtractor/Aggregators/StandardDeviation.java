/**
 * Standard Deviation
 * 
 * Created by Daniel McEnnis for the 2006 jAudio release
 * 
 */
package net.sf.jaudio.FeatureExtractor.Aggregators;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.AggregatorDefinition;
import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;
import net.sf.jaudio.FeatureExtractor.AudioFeatures.FeatureExtractor;

/**
 * <h2>Standard Deviation</h2>
 * <p>
 * This generic aggregator calculates standard deviation of each feature
 * dimension independently. It is one of the original aggregators used in MIR
 * research, present in the original Marsyas (2000) by Tzanetakis and Cook.
 * </p>
 * 
 * @author Daniel McEnnis
 * 
 */
public class StandardDeviation extends Aggregator {

    int feature = -1;

    /**
     * Constructs a new standard deviation aggregator.
     */
    public StandardDeviation() {
        this.metadata = new AggregatorDefinition("Standard Deviation",
                "Standard Deviation of the window-by-window data", true, null);
    }

    @Override
    public void aggregate(double[][][] values) {
        if (values[values.length - 1][this.feature] == null) {
            this.definition.dimensions = 1;
            this.result = new double[] { 0.0 };
        } else {
            int max = values[values.length - 1][this.feature].length;
            this.definition.dimensions = max;
            this.result = new double[max];
            for (int i = 0; i < max; ++i) {
                int count = 0;
                double average = 0.0;
              
                for (int j = 0; j < values.length; ++j) {
                    if ((values[j][this.feature] != null)
                            && (values[j][this.feature].length > i)) {
                        average += values[j][this.feature][i];
                        count++;
                    }
                }
                if (count < 2) {
                    this.result[i] = 0.0;
                } else {
                    average /= ((double) count);
                    for (int j = 0; j < values.length; ++j) {
                        if ((values[j][this.feature] != null)
                                && (values[j][this.feature].length > i)) {
                            this.result[i] += Math.pow(values[j][this.feature][i]
                                    - average, 2.0);
                        }
                    }
                    this.result[i] = Math.sqrt(this.result[i] / (((double) count) - 1.0));
                }
            }
        }
    }

    @Override
    public Object clone() {
        return new StandardDeviation();
    }

    @Override
    public FeatureDefinition getFeatureDefinition() {
        return this.definition;
    }

    @Override
    public String[] getFeaturesToApply() {
        return null;
    }

    @Override
    public void init(int[] featureIndecis) throws Exception {
        this.feature = featureIndecis[0];
    }

    @Override
    public void setSource(FeatureExtractor feature) {
        FeatureDefinition this_def = feature.getFeatureDefinition();
        this.definition = new FeatureDefinition(this_def.name
                + " Overall Standard Deviation", this_def.description
                + LINE_SEP
                + "This is the overall standard deviation over all windows.",
                this_def.is_sequential, this_def.dimensions);
    }

}
