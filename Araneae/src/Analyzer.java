import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;


public class Analyzer {

	public static void main(String[] args) {
		int numFiles = 1416;
		System.out.println("stage 1: load files: 0/"+numFiles+" done");
		String[] files = new String[numFiles];
		for(int i=0;i<numFiles;i++){
			files[i]=getFile("result/"+i+".html");
			files[i]=files[i].replace(" ", ""); //replace spaces
			files[i]=files[i].replace("	", ""); //replace tab
			files[i]=files[i].replaceAll("\\<script.*?script\\>", ""); //remove scripts
			files[i]=files[i].replaceAll("\\<head.*?head\\>", ""); //remove head
			files[i]=files[i].replaceAll("\\<.*?\\>", ""); //replace tags
			files[i]=files[i].replaceAll("\\&.[a-z]{0,10};", ""); //replace some special characters
			files[i]=files[i].replace(".", "").replace(",", "")
					.replace(":", "").replace(";", "")
					.replace("!", "").replace("?", "")
					.replace("-", "").replace("_", ""); //replace punctuation
			System.out.println("stage 1: load files: "+i+"/"+(numFiles-1)+" done");
		}

		double[] maxDistFile = new double[numFiles];
		double[] maxDistValue = new double[numFiles];
		double[] minDistFile = new double[numFiles];
		double[] minDistValue = new double[numFiles];
		JaroWinkler algorithm = new JaroWinkler();
		double currentMax;
		double currentMin;
		System.out.println("stage 2: compare");
		for(int i=0;i<numFiles;i++){
			currentMax = 0;
			currentMin = 100000;
			for(int j=0;j<numFiles;j++){
				if(i!=j){;
					double diff = algorithm.getSimilarity(files[i], files[j]);
					System.out.println(new Timestamp((new Date()).getTime())+" diff "+i+": "+diff+" (file "+j+")");
					if(diff>currentMax){
						currentMax=diff;
						maxDistFile[i]=j;
						maxDistValue[i]=currentMax;
					}
					if(diff<currentMin){
						currentMin=diff;
						minDistFile[i]=j;
						minDistValue[i]=currentMin;
					}
				}
			}
			System.out.println(new Timestamp((new Date()).getTime())+" result "+i+
					": file="+maxDistFile[i]+" dist="+maxDistValue[i]+
					": file="+minDistFile[i]+" dist="+minDistValue[i]);
		}
		System.out.println("Max: ");
		for(int i=0;i<numFiles;i++){
			System.out.println(i+".html "+maxDistFile[i]+".html "+maxDistValue[i]);
		}
		System.out.println("Min: ");
		for(int i=0;i<numFiles;i++){
			System.out.println(i+".html "+minDistFile[i]+".html "+minDistValue[i]);
		}
	}
	
	private static String getFile(String path){
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}
}
