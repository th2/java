import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class Analyzer {

	public static void main(String[] args) {
		int numFiles = 1417;
		System.out.println("stage 1: load files: 0/"+numFiles+" done");
		String[] files = new String[numFiles];
		for(int i=0;i<numFiles;i++){
			files[i]=getFile("result/"+i+".html");
			System.out.println("stage 1: load files: "+i+"/"+numFiles+" done");
		}

		int[] minDistFile = new int[numFiles];
		int[] minDistValue = new int[numFiles];
		int currentMin;
		for(int i=0;i<numFiles;i++){
			System.out.println("stage 2: compared "+i);
			currentMin = 100000;
			for(int j=0;j<numFiles;j++){
				if(i!=j){;
					int diff = levenshteinDistance(files[i], files[j]);
					if(diff<currentMin){
						currentMin=diff;
						minDistFile[i]=j;
						minDistValue[i]=currentMin;
					}
				}
			}
		}
		for(int i=0;i<1417;i++){
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
	
	/**
	 * Calculate the distance between two strings using the Levenshtein algorithm
	 * Source: http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
	 * @param s0 first string
	 * @param s1 second string
	 * @return the higher the result, the greater the distance between the two strings
	 */
	public static int levenshteinDistance (String s0, String s1) {
		int len0 = s0.length()+1;
		int len1 = s1.length()+1;
		// the array of distances
		int[] cost = new int[len0];
		int[] newcost = new int[len0];
		// initial cost of skipping prefix in String s0
		for(int i=0;i<len0;i++) cost[i]=i;
		// dynamicaly computing the array of distances
		// transformation cost for each letter in s1
		for(int j=1;j<len1;j++) {
			// initial cost of skipping prefix in String s1
			newcost[0]=j-1;
			// transformation cost for each letter in s0
			for(int i=1;i<len0;i++) {
				// matching current letters in both strings
				int match = (s0.charAt(i-1)==s1.charAt(j-1))?0:1;
				// computing cost for each transformation
				int cost_replace = cost[i-1]+match;
				int cost_insert  = cost[i]+1;
				int cost_delete  = newcost[i-1]+1;
				// keep minimum cost
				newcost[i] = Math.min(Math.min(cost_insert, cost_delete),cost_replace );
			}
			// swap cost/newcost arrays
			int[] swap=cost; cost=newcost; newcost=swap;
		}
		// the distance is the cost for transforming all letters in both strings
		return cost[len0-1];
	}
}
