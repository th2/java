import java.lang.reflect.Field;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.umundo.core.Message;

public class KeyListener implements NativeKeyListener {

	Controller controller;
	
	public KeyListener(Controller controller) {
		this.controller=controller;
	}

	private void cancelEvent(NativeKeyEvent e) {
		try {
			Field f = NativeInputEvent.class.getDeclaredField("reserved");
			f.setAccessible(true);
			f.setShort(e, (short) 0x01);
		} catch (Exception ex) {
			// TODO exception handling
			ex.printStackTrace();
		}
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
			controller.exit();
		//setup
		} else if(controller.screenID==0 && e.getKeyCode()==NativeKeyEvent.VC_SPACE){ //space
			cancelEvent(e);
		} else if(controller.screenID != controller.screenActive) {
			//System.out.println("consumed key " + e.getKeyCode() + " press");
			cancelEvent(e);
			Message m = new Message();
			m.putMeta("event", "keyPress");
			m.putMeta("key", ((Integer)e.getKeyCode()).toString());
			controller.inputPub.send(m);
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
			controller.exit();
		//setup
		} else if(controller.screenID==0 && e.getKeyCode()==NativeKeyEvent.VC_SPACE){ //space
			cancelEvent(e);
			controller.screens.add(controller.inputPub.getUUID());
			controller.screenID=controller.screens.size();
			controller.listScreenSetup();
        	
        	Message m = new Message();
			m.putMeta("event", "screenSetup");
			m.putMeta("device", controller.inputPub.getUUID());
			controller.inputPub.send(m);
		} else if(controller.screenID != controller.screenActive) {
			//System.out.println("consumed key " + e.getKeyCode() + " release");
			cancelEvent(e);
			Message m = new Message();
			m.putMeta("event", "keyRelease");
			m.putMeta("key", ((Integer)e.getKeyCode()).toString());
			controller.inputPub.send(m);
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// not required combination of nativeKeyPressed + nativeKeyReleased
	}

}
