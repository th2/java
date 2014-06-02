import java.lang.reflect.Field;

import org.jnativehook.NativeInputEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.umundo.core.Message;


public class MouseListener implements NativeMouseInputListener {
	
	Controller controller;
	
	public MouseListener(Controller controller) {
		this.controller=controller;
	}

	private void cancelEvent(NativeMouseEvent e) {
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
	public void nativeMouseClicked(NativeMouseEvent e) {
		// not required combination of nativeMousePressed + nativeMouseReleased
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent e) {
		//System.out.println("nativeMousePressed");
		if(controller.screenID != controller.screenActive){
			cancelEvent(e);
			Message m = new Message();
			m.putMeta("event", "mousePress");
			controller.inputPub.send(m);
		}
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		//System.out.println("nativeMouseReleased");
		if(controller.screenID != controller.screenActive){
			cancelEvent(e);
			Message m = new Message();
			m.putMeta("event", "mouseRelease");
			controller.inputPub.send(m);
		}
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
		//TODO
		System.out.println("nativeMouseDragged");
		// System.out.println("Mouse Dragged: " + e.getX() + ", " + e.getY());
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {
		int xMove = e.getX() - controller.screenCenter.x;
		int yMove = e.getY() - controller.screenCenter.y;
		
		if(controller.screenID != controller.screenActive){
			controller.robot.mouseMove(controller.screenCenter.x, controller.screenCenter.y);

			Message m = new Message();
			m.putMeta("event", "mouseMove");
			m.putMeta("x", ((Integer)xMove).toString());
			m.putMeta("y", ((Integer)yMove).toString());
			controller.inputPub.send(m);
		}else{
			//switch screen right
			if(e.getX()>=controller.screenDimensions.x-1 && controller.screenActive<controller.screens.size()){
				controller.screenActive++;
				System.out.println("changing active screen to "+controller.screenActive);

				Message m = new Message();
				m.putMeta("event", "changeActive");
				m.putMeta("active", ((Integer)controller.screenActive).toString());
				controller.inputPub.send(m);
			//switch screen left
			} else if(e.getX()<=0 && controller.screenActive>1){
				controller.screenActive--;
				System.out.println("changing active screen to "+controller.screenActive);

				Message m = new Message();
				m.putMeta("event", "changeActive");
				m.putMeta("active", ((Integer)controller.screenActive).toString());
				controller.inputPub.send(m);
			}
		}
	}

}
