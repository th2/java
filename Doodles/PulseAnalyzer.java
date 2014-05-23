import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class PulseAnalyzer {

	static class Pulse{
		public Date date;
		public int ka, kr, ca, cr, da, dr, ua, ur, ta, tr;
		
		public Pulse(String input) throws ParseException{
			input=input.replace("\"", "");
			String[] values = input.split(";");
			date=new SimpleDateFormat("MMMMM dd, yyyy 'at' KK:mma", Locale.ENGLISH).parse(values[0]);
			ka=Integer.parseInt(values[1].replace(",", ""));
			kr=Integer.parseInt(values[2].replace("s", "").replace("t", "").replace("h", "").replace("n", "").replace("d", ""));
			ca=Integer.parseInt(values[3].replace(",", ""));
			cr=Integer.parseInt(values[4].replace("s", "").replace("t", "").replace("h", "").replace("n", "").replace("d", ""));
			da=Integer.parseInt(values[5].replace(",", "")); //TODO
			dr=Integer.parseInt(values[6].replace("s", "").replace("t", "").replace("h", "").replace("n", "").replace("d", ""));
			ua=Integer.parseInt(values[7].replace(",", "")); //TODO
			ur=Integer.parseInt(values[8].replace("s", "").replace("t", "").replace("h", "").replace("n", "").replace("d", ""));
			ua=Integer.parseInt(values[9].replace(",", "")); //TODO
			ur=Integer.parseInt(values[10].replace("s", "").replace("t", "").replace("h", "").replace("n", "").replace("d", ""));
		}
		
		int parseSize(String in){
			int result = 0;
			if(in.contains("MB"))
				result = Integer.parseInt(in.substring(0,in.indexOf('.')));
			else 
				if(in.contains("GB")){
					result = Integer.parseInt(in.substring(0,in.indexOf('.')))*1024; 
				}
			return result;
		}
	}
	
	public static void main(String[] args) {
		ArrayList<Pulse> pulses = new ArrayList<Pulse>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("import/pulse.csv"));
			String line;
			while ((line = br.readLine()) != null)
				pulses.add(new Pulse(line));
			
			System.out.println("loaded "+pulses.size()+" pulses");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
