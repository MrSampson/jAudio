/**
 * 
 */
package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * Calculates linear predictive coefficients of a signal. Also includes a
 * warping factor lambda that is disabled by default. Based upon code published
 * at www.musicdsp.org.
 * <p>
 * 2005. <i>Music-dsp source code archive</i> [online]. [cited 17 May 2005].
 * Available from the World Wide Web:
 * (http://musicdsp.org/archive.php?classid=2#137)
 * 
 * @author Daniel McEnnis
 */
public class LPC extends FeatureExtractor {

    double lambda = 0.0;

    int numDimensions = 10;

    /**
     * Basic constructor for LPC that sets definition, dependencies, and offsets
     * field.
     */
    public LPC() {
        String name = "LPC";
        String description = "Linear Prediction Coeffecients calculated using autocorrelation and Levinson-Durbin recursion.";
        String[] attributes = new String[] { "lambda for frequency warping",
                "number of coeffecients to calculate" };
        this.definition = new FeatureDefinition(name, description, true, 10,
                attributes);
        this.dependencies = null;
        this.offsets = null;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Code taken from www.musicdsp.org.
     * </p>
     * <p>
     * mail.mutagene.net.2005. <i>Music dsp source archive</i> [online] [cited
     * May 10, 2005] Available on world wide web
     * (http://musicdsp.org/archive.php?classid=2#137)
     * 
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        // find the order-P autocorrelation array, R, for the sequence x of
        // length L and warping of lambda
        // wAutocorrelate(&pfSrc[stIndex],siglen,R,P,0);

        double[] R = new double[this.numDimensions + 1];
        double K[] = new double[this.numDimensions];
        double A[] = new double[this.numDimensions];
        double[] dl = new double[samples.length];
        double[] Rt = new double[samples.length];
        double r1, r2, r1t;
        R[0] = 0;
        Rt[0] = 0;
        r1 = 0;
        r2 = 0;
        r1t = 0;
        for (int k = 0; k < samples.length; k++) {
            Rt[0] += samples[k] * samples[k];

            dl[k] = r1 - this.lambda * (samples[k] - r2);
            r1 = samples[k];
            r2 = dl[k];
        }
        for (int i = 1; i < R.length; i++) {
            Rt[i] = 0;
            r1 = 0;
            r2 = 0;
            for (int k = 0; k < samples.length; k++) {
                Rt[i] += dl[k] * samples[k];

                r1t = dl[k];
                dl[k] = r1 - this.lambda * (r1t - r2);
                r1 = r1t;
                r2 = dl[k];
            }
        }
        for (int i = 0; i < R.length; i++)
            R[i] = Rt[i];

        // LevinsonRecursion(unsigned int P, float *R, float *A, float *K)
        double Am1[] = new double[62];
        ;

        if (R[0] == 0.0) {
            for (int i = 1; i < this.numDimensions; i++) {
                K[i] = 0.0;
                A[i] = 0.0;
            }
        } else {
            double km, Em1, Em;
            int k, s, m;
            for (k = 0; k < this.numDimensions; k++) {
                A[0] = 0;
                Am1[0] = 0;
            }
            A[0] = 1;
            Am1[0] = 1;
            km = 0;
            Em1 = R[0];
            for (m = 1; m < this.numDimensions; m++) // m=2:N+1
            {
                double err = 0.0f; // err = 0;
                for (k = 1; k <= m - 1; k++)
                    // for k=2:m-1
                    err += Am1[k] * R[m - k]; // err = err + am1(k)*R(m-k+1);
                km = (R[m] - err) / Em1; // km=(R(m)-err)/Em1;
                K[m - 1] = -km;
                A[m] = km; // am(m)=km;
                for (k = 1; k <= m - 1; k++)
                    // for k=2:m-1
                    A[k] = Am1[k] - km * Am1[m - k]; // am(k)=am1(k)-km*am1(m-k+1);
                Em = (1 - km * km) * Em1; // Em=(1-km*km)*Em1;
                for (s = 0; s < this.numDimensions; s++)
                    // for s=1:N+1
                    Am1[s] = A[s]; // am1(s) = am(s)
                Em1 = Em; // Em1 = Em;
            }
        }
        return K;
    }

    @Override
    public Object clone() {
        LPC ret = new LPC();
        ret.lambda = this.lambda;
        try {
            ret.setNumDimensions(this.numDimensions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ret.parent = this.parent;
        return ret;
    }

 
    @Override
    public String getElement(int index) throws Exception {
        switch (index) {
        case 0:
            return Double.toString(this.lambda);
        case 1:
            return Integer.toString(this.numDimensions);
        default:
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " passed to LPC:getElement()");
        }
    }

 
    @Override
    public void setElement(int index, String value) throws Exception {
        switch (index) {
        case 0:
            try {
                setLambda(Double.parseDouble(value));
            } catch (NumberFormatException e) {
                throw new Exception("Lambda value must be a double");
            }
            break;
        case 1:
            try {
                setNumDimensions(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new Exception("Number of Dimensions must be an integer");
            }
            break;
        default:
            throw new Exception(
                    "INTERNAL ERROR: invalid index passed to LPC:setElement");
        }
    }

    /**
     * Edits the number of LPC coeffecients to be calculated. This is a unique
     * feature in that the number of dimensions of the feature are changed by
     * this function, requiring a reference back to the parent to redraw the
     * table displaying this information.
     * 
     * @param n
     *            number of coeffecients to be calculated.
     * @throws Exception
     *             thrown if less than 1 feature is to be calculated.
     */
    public void setNumDimensions(int n) throws Exception {
        if (n < 1) {
            throw new Exception("Must have at least 1 LPC coeffecient - " + n
                    + " provided");
        } else {
            this.numDimensions = n;
            String name = this.definition.name;
            String description = this.definition.description;
            String[] attributes = this.definition.attributes;
            this.definition = new FeatureDefinition(name, description, true,
                    this.numDimensions, attributes);
            if (this.parent != null) {
                this.parent.updateTable();
            }
        }
    }

    /**
     * Provides a mechanism for editing the 'frequency warping' factor in the
     * LPC code from musicdsp.
     * 
     * @param l
     *            new lmbda value
     * @throws Exception
     *             throws if the lambda value is not a real number.
     */
    public void setLambda(double l) throws Exception {
        if (Double.isNaN(l) || Double.isInfinite(l)) {
            throw new Exception("lambda must be a real number");
        } else {
            this.lambda = l;
        }
    }

}
