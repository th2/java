import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.JFrame;
 
public class Mandelbrot extends JFrame {
	private static final long serialVersionUID = -2105345324636075152L;
	private final double size = 400;
    private final int height = 1200;
    private final int width = 1920;
    private BufferedImage canvas;
    private double z1;
    private double z2;
 
    public Mandelbrot() {
        super("Mandel");
        setBounds(100, 100, width, height);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
        	HashMap<Integer, Integer> line = calculateLine(y, width);
        	for (int x = 10; x < width; x++) {
        		canvas.setRGB(x, y, 0x00FF00+(line.get(x)*0xF));
        	}
        }
    }
    
    private HashMap<Integer, Integer> calculateLine(int y, int yMax){
    	HashMap<Integer, Integer> results = new HashMap<Integer, Integer>();
    	 for (int x = 0; x < yMax; x++) {
             z1 = 0;
             z2 = 0;
             int i = 1000;
             while (i > 0 && (z1*z1 + z2*z2) < 5) {
                 double z3 = z1*z1 - z2*z2 + x/size - 2;
                 z2 = 2.0 * z1*z2 + y/size - 1.5;
                 z1 = z3;
                 i--;
             }
             results.put(x, i);
         }
		return results;
    }
 
    @Override
    public void paint(Graphics g) {
        g.drawImage(canvas, 0, 0, this);
    }
 
    public static void main(String[] args) {
        new Mandelbrot().setVisible(true);
    }
}