package tp4;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;

public class FrequencyJob {

	public static void main(final String args[]) {
		
		JavaSparkContext sc;
		File file = new File(args[0]);
		
		try {
		
			File output = new File(file.getParentFile().getAbsolutePath() + "/result.txt");
			output.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		     
	    	SparkConf conf = new SparkConf()
	    			.setAppName("factures-frequency")
	    			.setMaster(args[1]);
	    	
	    	sc = new JavaSparkContext(conf);
	    	
	    	JavaRDD<String> data = sc.textFile(args[0]);
	
			JavaRDD<List<String>> transactions = data.map(line -> Arrays.asList(line.split(" ")));
	    	
	    	FPGrowth fpg = new FPGrowth()
	    			  .setMinSupport(0.2)
	    			  .setNumPartitions(10);
			FPGrowthModel<String> model = fpg.run(transactions);
	
			for (FPGrowth.FreqItemset<String> itemset: model.freqItemsets().toJavaRDD().collect()) {
			  writer.write("[" + itemset.javaItems() + "], " + itemset.freq() + "\n");
			}
	
			double minConfidence = 0.8;
			for (AssociationRules.Rule<String> rule
			  : model.generateAssociationRules(minConfidence).toJavaRDD().collect()) {
			  writer.write(
			    rule.javaAntecedent() + " => " + rule.javaConsequent() + ", " + rule.confidence() + "\n");
			}
	    	
			sc.close();
	    	writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
