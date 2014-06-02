import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Robot;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.umundo.core.Discovery;
import org.umundo.core.Discovery.DiscoveryType;
import org.umundo.core.Node;
import org.umundo.core.Publisher;
import org.umundo.core.Subscriber;

// used libraries: umundo https://github.com/tklab-tud/umundo & jnativehook https://github.com/kwhat/jnativehook
public class Controller {

	public static void main(String[] args) {
		new Controller();
	}
	
	//hook
	KeyListener keyListener;
	MouseListener mouseListener;
	
	//umundo
	Discovery discovery;
	Node inputNode;
	Subscriber inputSub;
	Publisher inputPub;
	HashMap<String, String> participants;
	
	//logic
	ArrayList<String> screens;
	int screenID;
	int screenActive;
	Robot robot;
	Point screenDimensions;
	Point screenCenter;
	
	public Controller(){
		//hook
		keyListener = new KeyListener(this);
		mouseListener= new MouseListener(this);
		GlobalScreen.getInstance().setEventDispatcher(new VoidExecutorService());
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			System.err.println("There was a problem registering the native hook.");
			// TODO exception handling
			e.printStackTrace();
			System.exit(1);
		}
		GlobalScreen.getInstance().addNativeKeyListener(keyListener);
		GlobalScreen.getInstance().addNativeMouseListener(mouseListener);
		GlobalScreen.getInstance().addNativeMouseMotionListener(mouseListener);
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        LogManager.getLogManager().reset();
        logger.setLevel(Level.SEVERE);
		
		//umundo
		participants = new HashMap<String, String>();
		discovery = new Discovery(DiscoveryType.MDNS);
		inputNode = new Node();
		inputSub = new Subscriber("inputshare", new InputReceiver(this));
		inputPub = new Publisher("inputshare");
		discovery.add(inputNode);
		inputPub.setGreeter(new InputGreeter(this));
		inputNode.addPublisher(inputPub);
		inputNode.addSubscriber(inputSub);
		
		//logic
		screens = new ArrayList<String>();
		screenID = 1;
		screenActive = 1;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO exception handling
			e.printStackTrace();
			exit();
		}
		screenDimensions = new Point(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth(),
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());
		screenCenter = new Point(screenDimensions.x/2, screenDimensions.y/2);
	}

	 void exit(){
		//GlobalScreen.unregisterNativeHook();
		inputNode.removePublisher(inputPub);
		inputNode.removeSubscriber(inputSub);
		System.exit(0);
	 }

	public void listScreenSetup() {
		System.out.println("--- screen setup ---");
		for(int i=0; i< screens.size(); i++)
			if(screens.get(i).equals(inputPub.getUUID()))
				System.out.println(i+" "+screens.get(i)+" < me ("+screenID+")");
			else
				System.out.println(i+" "+screens.get(i));
			
		System.out.println("--- /screen setup ---");
	}
}
