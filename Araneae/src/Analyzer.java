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
		int numFiles = 51;
		System.out.println("stage 1: load files: 0/"+numFiles+" done");
		String[] files = new String[numFiles];
		for(int i=0;i<numFiles;i++){
			System.out.println(files[i]);
			System.out.println("stage 1: load files: "+i+"/"+(numFiles-1)+" done");
		}
		/*
		double[] minDistFile = new double[numFiles];
		double[] minDistValue = new double[numFiles];
		JaroWinkler algorithm = new JaroWinkler();
		double currentMax;
		for(int i=0;i<numFiles;i++){
			System.out.println("stage 2: compared "+i);
			currentMax = 0;
			for(int j=0;j<numFiles;j++){
				if(i!=j){;
					double diff = algorithm.getSimilarity(files[i], files[j]);
					System.out.println(new Timestamp((new Date()).getTime())+" diff: "+diff+" (file "+j+")");
					if(diff>currentMax){
						currentMax=diff;
						minDistFile[i]=j;
						minDistValue[i]=currentMax;
					}
				}
			}
			System.out.println("result: file="+minDistFile[i]+" dist="+minDistValue[i]);
		}
		for(int i=0;i<numFiles;i++){
			System.out.println(i+".html "+minDistFile[i]+".html "+minDistValue[i]);
		}*/
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
