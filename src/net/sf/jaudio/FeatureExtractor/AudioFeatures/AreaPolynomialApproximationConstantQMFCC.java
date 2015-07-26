package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

/**
 * 2D Polynomial Approximation Feature
 *
 * Creates a set of polynomial factors for a 2D polynomial of order k*l where k
 * is the number of terms in the x direction and l is the number of terms in the
 * y direction. The source is a square matrix of DSP data of some sort. (This
 * version is over FFT data.) The output is a vector of the coeffecients of the
 * polynomial that best fits this data.
 * 
 * @author Daniel McEnnis
 */
public class AreaPolynomialApproximationConstantQMFCC extends FeatureExtractor {

    int windowLength = 50;

    int featureLength = 13;

    int k = 10;

    int l = 5;

    DenseDoubleMatrix2D terms;

    DenseDoubleMatrix2D z;

    /**
     * Constructor that sets description, dependencies, and offsets from
     * FeatureExtractor
     */
    public AreaPolynomialApproximationConstantQMFCC() {
        this.name = "2D Polynomial Approximation ConstantQ MFCC";
        this.description = "coeffecients of 2D polynomial best describing the input matrtix.";
        this.attributes = new String[] { "horizontal size (window length)",
                "vertical size (number of feature dimensions)",
                "number of x (horizontal) terms",
                "number of y (vertical) terms" };

        this.definition = new FeatureDefinition(this.name, this.description,
                true, 0, this.attributes);

        this.dependencies = new String[this.windowLength];
        for (int i = 0; i < this.dependencies.length; ++i) {
            this.dependencies[i] = "ConstantQ-derived MFCC";
        }

        this.offsets = new int[this.windowLength];
        for (int i = 0; i < this.offsets.length; ++i) {
            this.offsets[i] = 0 - i;
        }

        this.terms = new DenseDoubleMatrix2D(this.k * this.l, this.windowLength
                * this.featureLength);
        this.z = new DenseDoubleMatrix2D(1, this.featureLength
                * this.windowLength);
        calcTerms(this.terms);
    }

    /**
     * Calculates based on windows of magnitude spectrum. Encompasses portion of
     * Moments class, but has a delay of lengthOfWindow windows before any
     * results are calculated.
     * 
     * @param samples
     *            The samples to extract the feature from.
     * @param sampling_rate
     *            The sampling rate that the samples are encoded with.
     * @param other_feature_values
     *            The values of other features that are needed to calculate this
     *            value. The order and offsets of these features must be the
     *            same as those returned by this class's getDependencies and
     *            getDependencyOffsets methods respectively. The first indice
     *            indicates the feature/window and the second indicates the
     *            value.
     * @return The extracted feature value(s).
     * @throws Exception
     *             Throws an informative exception if the feature cannot be
     *             calculated.
     */
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        if ((this.featureLength != other_feature_values[0].length)
                || (this.windowLength != other_feature_values.length)) {
            this.terms = new DenseDoubleMatrix2D(this.k * this.l,
                    this.windowLength * this.featureLength);
            this.z = new DenseDoubleMatrix2D(1, this.featureLength
                    * this.windowLength);
            calcTerms(this.terms);
        }
        for (int i = 0; i < this.windowLength; ++i) {
            for (int j = 0; j < this.featureLength; ++j) {
                this.z.set(0, this.windowLength * i + j,
                        other_feature_values[i][j]);
            }
        }
        DoubleMatrix2D retMatrix = (new Algebra()).solve(this.terms, this.z);
        return retMatrix.viewRow(0).toArray();
    }

    /**
     * Function that must be overridden to allow this feature to be set globally
     * by GlobalChange frame.
     * 
     * @param n
     *            the number of windows of offset to be used in calculating this
     *            feature
     */
    public void setWindow(int n) throws Exception {
        if (n < 1) {
            throw new Exception(
                    "Area Polynomial Approximation window length must be positive");
        } else {
            this.windowLength = n;
            this.dependencies = new String[this.windowLength];
            this.offsets = new int[this.windowLength];
            for (int i = 0; i < this.windowLength; ++i) {
                this.dependencies[i] = "Magnitude Spectrum";
                this.offsets[i] = 0 - i;
            }
            this.terms = new DenseDoubleMatrix2D(this.k * this.l,
                    this.windowLength * this.featureLength);
            this.z = new DenseDoubleMatrix2D(1, this.featureLength
                    * this.windowLength);
            calcTerms(this.terms);
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
    public String getElement(int index) throws Exception {
        switch (index) {
        case 0:
            // get windowLength
            return Integer.toString(this.windowLength);

        case 1:
            // get featureLength
            return Integer.toString(this.featureLength);

        case 2:
            // get number of x terms
            return Integer.toString(this.k);

        case 3:
            // get number of y terms
            return Integer.toString(this.k);

        default:
            // get number of y terms
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to AreaPolynomialApproximation:getElement");
        }
    }

    /**
     * Function permitting an unintelligent outside function (i.e. EditFeatures
     * frame) to set the default values used to populate the table's entries.
     * Like getElement, the correct index values are inferred from the
     * definition.attributes value.
     * 
     * @param index
     *            attribute to be set
     * @param value
     *            new value of the attribute
     */
    public void setElement(int index, String value) throws Exception {
        switch (index) {
        case 0:
            // get windowLength
            try {
                int val = Integer.parseInt(value);
                if (val < 1) {
                    throw new Exception(
                            "Area Polynomial Approximation window length must be positive");
                } else {
                    this.windowLength = val;
                    this.dependencies = new String[this.windowLength];
                    this.offsets = new int[this.windowLength];
                    for (int i = 0; i < this.windowLength; ++i) {
                        this.dependencies[i] = "Magnitude Spectrum";
                        this.offsets[i] = 0 - i;
                    }
                    this.terms = new DenseDoubleMatrix2D(this.k * this.l,
                            this.windowLength * this.featureLength);
                    this.z = new DenseDoubleMatrix2D(1, this.featureLength
                            * this.windowLength);
                    calcTerms(this.terms);
                }
            } catch (Exception e) {
                throw new Exception(
                        "horizontal (windowLength) of Area Polynomial Approximation must be an integer");
            }
            break;

        case 1:
            // get featureLength
            try {
                int val = Integer.parseInt(value);
                if (val < 1) {
                    throw new Exception(
                            "Area Polynomial Approximation feature dimension length must be positive");
                } else {
                    this.featureLength = val;
                    this.terms = new DenseDoubleMatrix2D(this.k * this.l,
                            this.windowLength * this.featureLength);
                    this.z = new DenseDoubleMatrix2D(1, this.featureLength
                            * this.windowLength);
                    // calcTerms(terms);
                }
            } catch (Exception e) {
                throw new Exception(
                        "vertical (feature dimensions) of Area Polynomial Approximation must be an integer");
            }
            break;

        case 2:
            // get number of x terms
            try {
                int val = Integer.parseInt(value);
                if (val < 1) {
                    throw new Exception(
                            "Number of x terms in Area Polynomial Approximation must be positive");
                } else {
                    this.k = val;
                    this.terms = new DenseDoubleMatrix2D(this.k * this.l,
                            this.windowLength * this.featureLength);
                    this.z = new DenseDoubleMatrix2D(1, this.featureLength
                            * this.windowLength);
                    calcTerms(this.terms);
                }
            } catch (Exception e) {
                throw new Exception(
                        "Number of x terms in Area Polynomial Approximation must be an integer");
            }
            break;

        case 3:
            // get number of y terms
            try {
                int val = Integer.parseInt(value);
                if (val < 1) {
                    throw new Exception(
                            "Number of y terms in Area Polynomial Approximation must be positive");
                } else {
                    this.l = val;
                    this.terms = new DenseDoubleMatrix2D(this.k * this.l,
                            this.windowLength * this.featureLength);
                    this.z = new DenseDoubleMatrix2D(1, this.featureLength
                            * this.windowLength);
                    calcTerms(this.terms);
                }
            } catch (Exception e) {
                throw new Exception(
                        "Number of y terms of Area Polynomial Approximation must be an integer");
            }
            break;

        default:
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to AreaPolynomialApproximation:getElement");
        }
    }

    /**
     * Create an identical copy of this feature. This permits FeatureExtractor
     * to use the prototype pattern to create new composite features using
     * metafeatures.
     */
    public Object clone() {
        AreaPolynomialApproximation ret = new AreaPolynomialApproximation();
        return ret;
    }

    private void calcTerms(DoubleMatrix2D terms) {
        terms.assign(0.0);
        for (int x = 0; x < this.windowLength; ++x) {
            for (int y = 0; y < this.featureLength; ++y) {
                for (int i = 0; i < this.k; ++i) {
                    for (int j = 0; j < this.l; ++j) {
                        terms.set(this.l * i + j, this.featureLength * x + y,
                                Math.pow(x, i) * Math.pow(y, j));
                    }
                }
            }
        }
    }

}
