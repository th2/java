import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class LanguageClassifier {
	static HashMap<Character, Double> german = new HashMap<Character, Double>();
	static HashMap<Character, Double> english = new HashMap<Character, Double>();
	static HashMap<Character, Double> spanish = new HashMap<Character, Double>();
	
	public static void main(String[] args) {
		german.put('a',0.0651);
		german.put('b',0.0189);
		german.put('c',0.0306);
		german.put('d',0.0508);
		german.put('e',0.1740);
		german.put('f',0.0166);
		german.put('g',0.0301);
		german.put('h',0.0476);
		german.put('i',0.0755);
		german.put('j',0.0027);
		german.put('k',0.0121);
		german.put('l',0.0344);
		german.put('m',0.0253);
		german.put('n',0.0978);
		german.put('o',0.0251);
		german.put('p',0.0079);
		german.put('q',0.0002);
		german.put('r',0.0700);
		german.put('s',0.0727);
		german.put('ß',0.0031);
		german.put('t',0.0615);
		german.put('u',0.0435);
		german.put('v',0.0067);
		german.put('w',0.0189);
		german.put('x',0.0003);
		german.put('y',0.0978);
		german.put('z',0.0978);

		english.put('a',0.0816);
		english.put('b',0.0149);
		english.put('c',0.0278);
		english.put('d',0.0425);
		english.put('e',0.1270);
		english.put('f',0.0222);
		english.put('g',0.0202);
		english.put('h',0.0609);
		english.put('i',0.0696);
		english.put('j',0.0153);
		english.put('k',0.0077);
		english.put('l',0.0403);
		english.put('m',0.0241);
		english.put('n',0.0675);
		english.put('o',0.0751);
		english.put('p',0.0193);
		english.put('q',0.0095);
		english.put('r',0.0598);
		english.put('s',0.0633);
		english.put('ß',0.0000);
		english.put('t',0.0906);
		english.put('u',0.0276);
		english.put('v',0.0091);
		english.put('w',0.0236);
		english.put('x',0.0015);
		english.put('y',0.0197);
		english.put('z',0.0074);

		spanish.put('a',0.1253);
		spanish.put('b',0.0142);
		spanish.put('c',0.0468);
		spanish.put('d',0.0586);
		spanish.put('e',0.1368);
		spanish.put('f',0.0069);
		spanish.put('g',0.0101);
		spanish.put('h',0.0070);
		spanish.put('i',0.0625);
		spanish.put('j',0.0044);
		spanish.put('k',0.0000);
		spanish.put('l',0.0497);
		spanish.put('m',0.0315);
		spanish.put('n',0.0671);
		spanish.put('o',0.0868);
		spanish.put('p',0.0251);
		spanish.put('q',0.0088);
		spanish.put('r',0.0687);
		spanish.put('s',0.0798);
		spanish.put('ß',0.0000);
		spanish.put('t',0.0463);
		spanish.put('u',0.0393);
		spanish.put('v',0.0090);
		spanish.put('w',0.0002);
		spanish.put('x',0.0022);
		spanish.put('y',0.0090);
		spanish.put('z',0.0052);

		int numFiles = 1416;
		for(int i=0;i<numFiles;i++){
			String file = "";
			file=getFile("result/"+i+".html");
			file=file.replace(" ", ""); //replace spaces
			file=file.replace("	", ""); //replace tab
			file=file.replaceAll("\\<script.*?script\\>", ""); //remove scripts
			file=file.replaceAll("\\<head.*?head\\>", ""); //remove head
			file=file.replaceAll("\\<.*?\\>", ""); //replace tags
			file=file.replaceAll("\\&.[a-z]{0,10};", ""); //replace some special characters
			file=file.replace(".", "").replace(",", "")
					.replace(":", "").replace(";", "")
					.replace("!", "").replace("?", "")
					.replace("-", "").replace("_", ""); //replace punctuation
			
			System.out.println(i+" "+classify(file));
		}
	}
	
	private static String classify(String text){
		char[] chars = text.toCharArray();
		int occourences = chars.length;

		HashMap<Character, Double> textlger=new HashMap<Character, Double>();
		HashMap<Character, Double> textleng=new HashMap<Character, Double>();
		HashMap<Character, Double> textlspa=new HashMap<Character, Double>();
	  	for(char key : chars) { 
			if (textlger.containsKey(key))
				textlger.put(key, textlger.get(key)+1);
			else
				textlger.put(key, 1.0);
			if (textleng.containsKey(key))
				textleng.put(key, textleng.get(key)+1);
			else
				textleng.put(key, 1.0);
			if (textlspa.containsKey(key))
				textlspa.put(key, textlspa.get(key)+1);
			else
				textlspa.put(key, 1.0);
	  	} 

	  	double totaldeviationslgerman = 0;
	  	double totaldeviationslenglish = 0;
	  	double totaldeviationslspanish = 0;
		for(char key : chars) {
			//System.out.println("key:"+key);
			if (german.containsKey(key))
				totaldeviationslgerman += german.get(key) - (occourences / textlger.get(key));
			if (english.containsKey(key))
				totaldeviationslenglish += english.get(key) - (occourences / textleng.get(key));
			if (spanish.containsKey(key))
				totaldeviationslspanish += spanish.get(key) - (occourences / textlspa.get(key));
		}
		//System.out.println("GER SL="+totaldeviationslgerman);
		//System.out.println("ENG SL="+totaldeviationslenglish);
		//System.out.println("SPA SL="+totaldeviationslspanish);	

		if (totaldeviationslgerman < totaldeviationslenglish && totaldeviationslgerman < totaldeviationslspanish) {
			return "german";	
		}
		else if (totaldeviationslspanish < totaldeviationslenglish && totaldeviationslspanish < totaldeviationslgerman) {
			return "spanish";	
		}
		else if (totaldeviationslenglish < totaldeviationslgerman && totaldeviationslenglish < totaldeviationslspanish) {
			return "english";	
		}	
		else {
			return "unknown";
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
