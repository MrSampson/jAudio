package net.sf.jaudio.FeatureExtractor;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jaudio.FeatureExtractor.ACE.XMLParsers.XMLDocumentParser;
import net.sf.jaudio.FeatureExtractor.Aggregators.Aggregator;
import net.sf.jaudio.FeatureExtractor.Aggregators.AggregatorContainer;
import net.sf.jaudio.FeatureExtractor.AudioFeatures.FeatureExtractor;
import net.sf.jaudio.FeatureExtractor.jAudioTools.FeatureProcessor;

/**
 * Created with IntelliJ IDEA.
 * User: dmcennis
 * Date: 9/23/13
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class AudioStreamProcessor {
    private FeatureProcessor analysisEngine;
    private AggregatorContainer container;
    private boolean window = false;
    private boolean overall = false;

    public AudioStreamProcessor(String featureFile,String settingsFile){
        DataModel model = new DataModel(featureFile,null);
        try{
            Object[] contents = (Object[])XMLDocumentParser.parseXMLDocument(settingsFile,"save_settings");
            int windowLength = Integer.parseInt((String)contents[0]);
            double windowOverlap = Double.parseDouble((String)contents[1]);
            double sampleRate = (Double)contents[2];
            boolean normalise = (Boolean)contents[3];
            boolean savePerWindow = (Boolean)contents[4];
            this.window = savePerWindow;
            boolean saveOverall = (Boolean)contents[5];
            this.overall = saveOverall;
            Map<String,Boolean> checked = (Map<String,Boolean>)contents[7];
            Map<String,String[]> attribute = (Map<String,String[]>)contents[8];
            List<String> aggregatorNames = (List<String>)contents[9];
            List<String[]> aggregatorFeatures = (List<String[]>)contents[10];
            List<String[]> aggregatorParameters = (List<String[]>)contents[11];

            LinkedList<FeatureExtractor> features = new LinkedList<FeatureExtractor>();
            for (int i = 0; i < model.features.length; ++i) {
                String name = model.features[i].getFeatureDefinition().name;
                if (attribute.containsKey(name)) {
                    model.defaults[i] = checked.get(name);
                    String[] att = attribute.get(name);
                    for (int j = 0; j < att.length; ++j) {
                        try {
                            model.features[i].setElement(j, att[j]);
                        } catch (Exception e) {
                            System.out.println("Feature " + name
                                    + "failed apply its " + j + " attribute");
                            e.printStackTrace();
                        }
                    }
                } else {
                    model.defaults[i] = false;
                }
            }
            boolean[] featuresToSave;

            Aggregator[] list = new Aggregator[aggregatorNames.size()];
            Iterator<String> nameIT = aggregatorNames.iterator();
            Iterator<String[]> featIT = aggregatorFeatures.iterator();
            Iterator<String[]> paramIT = aggregatorParameters.iterator();
            for(int i=0;i<list.length;++i){
                Aggregator a = (Aggregator)model.aggregatorMap.get(nameIT.next()).clone();
                a.setParameters(featIT.next(),paramIT.next());
                list[i] = a;
            }
            this.container = new AggregatorContainer();
            this.container.add(list);
            this.container.add(model.features,model.defaults);
            this.analysisEngine = new FeatureProcessor(windowLength,
                                         windowOverlap,
                                         sampleRate,
                                         normalise,
                                         model.features,
                                         model.defaults,
                                         savePerWindow,
                                         saveOverall,
                                         null,
                                         null,
                                         2,
                                         null,
                                         this.container
                    );
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void output(String filePrefix) throws java.io.IOException{
 	if(this.window){       
	FileWriter windowWriter = new FileWriter(filePrefix+"window.json");
//        double[][][] w = this.analysisEngine.getWindow_feature_values();
        windowWriter.write("[\n");
        boolean w_first = true;
//        for (double[][] window : w){
//            if(w_first){
//                w_first = false;
//            }else{
//                windowWriter.write(",");
//            }
//            windowWriter.write("\t{\n");
//            boolean f_first = true;
//            for(int count = 0; count< window.length;++count){
//                double[] feature = window[count];
//                if(f_first){
//                    f_first = false;
//                }else{
//                    windowWriter.write(",");
//                }
////                windowWriter.write("\t\t\""+this.analysisEngine.feature_extractors[count]+"\" : [\n");
//                boolean e_first = true;
//                if(feature != null){
//		for(double element : feature){
//                    if(e_first){
//                       e_first = false;
//                    }else{
//                        windowWriter.write(",");
//                    }
//                    windowWriter.write(Double.toString(element));
//                }
//		}
//                windowWriter.write("\t\t]\n");
//            }
//            windowWriter.write("\t}\n");
//        }
        windowWriter.write("]\n");
        windowWriter.flush();
        windowWriter.close();
	}
        if(this.overall){
        FileWriter overallWriter = new FileWriter(filePrefix+"overall.json");
//        try{
//            this.container.outputJSONEntries(overallWriter);
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//        }
        overallWriter.flush();
        overallWriter.close();
	System.out.println("Analysis of "+filePrefix+" finished");
        }
    }

//    public void process(double[] samples) throws Exception{
//        this.analysisEngine.extractFeaturesBySample(samples,null);
//    }
//
//    public double[][][] getPerWindowValues(){
//        return this.analysisEngine.getWindow_feature_values();
//    }
//
//    public int[] getWindowStartIndices(){
//        return this.analysisEngine.getWindow_start_indices();
//    }

    public double[][] getOverallValues(){
        return this.container.getResults();
    }
}
