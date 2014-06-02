import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.util.ArrayList;

import org.umundo.core.Message;
import org.umundo.core.Receiver;

public class InputReceiver extends Receiver {
	
	Controller controller;
	
	public InputReceiver(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void receive(Message message) {
		if (message.getMeta().containsKey("participant")) {
			controller.participants.put(message.getMeta("subscriber"), message.getMeta("participant"));
			System.out.println(message.getMeta("participant") + " joined the channel");
			System.out.println("configuring screen setup, please press space from left to right");
			controller.screenID=0;
			controller.screens = new ArrayList<String>();
		} else if(message.getMeta("event").equals("screenSetup")) {
			controller.screens.add(message.getMeta("device"));
			controller.listScreenSetup();
		} else if(message.getMeta("event").equals("mouseMove")) {
			if(controller.screenID == controller.screenActive){
				//System.out.println("mouse move received: "+msg.getMeta("x")+" "+msg.getMeta("y")+" by "+msg.getMeta("device"));
				Point pointer = MouseInfo.getPointerInfo().getLocation();
				if(pointer.x+Integer.parseInt(message.getMeta("x"))>=controller.screenDimensions.x && controller.screenActive<controller.screens.size()){
					controller.screenActive++;
					System.out.println("changing active screen to "+controller.screenActive);

					Message m = new Message();
					m.putMeta("event", "changeActive");
					m.putMeta("active", ((Integer)controller.screenActive).toString());
					controller.inputPub.send(m);
				} else if(pointer.x+Integer.parseInt(message.getMeta("x"))<=0 && controller.screenActive>1){
					controller.screenActive--;
					System.out.println("changing active screen to "+controller.screenActive);

					Message m = new Message();
					m.putMeta("event", "changeActive");
					m.putMeta("active", ((Integer)controller.screenActive).toString());
					controller.inputPub.send(m);
				} else {
					int x=Integer.parseInt(message.getMeta("x"));
					if(pointer.x+x<0) x=0; else if(pointer.x+x>controller.screenDimensions.x) x=0;
					int y=Integer.parseInt(message.getMeta("y"));
					if(pointer.y+y<0) y=0; else if(pointer.y+y>controller.screenDimensions.y) x=0;
					controller.robot.mouseMove(pointer.x+x, pointer.y+y);
					//System.out.println(pointer.x+"+"+x+" p "+pointer.y+"+"+y);
				}
			}
		} else if(message.getMeta("event").equals("changeActive")) {
			controller.screenActive=Integer.parseInt(message.getMeta("active"));
			System.out.println("received active screen change: screen "+controller.screenActive+" now active this="+controller.screenID);
			if(controller.screenID == controller.screenActive){
				System.out.println("this screen is now active");
			}
		}  else if(message.getMeta("event").equals("mouseClicked")) {
			if(controller.screenID == controller.screenActive){
				controller.robot.mousePress(InputEvent.BUTTON1_MASK);
				controller.robot.mouseRelease(InputEvent.BUTTON1_MASK);
			}
		}  else if(message.getMeta("event").equals("mousePress")) {
			if(controller.screenID == controller.screenActive){
				controller.robot.mousePress(InputEvent.BUTTON1_MASK);
			}
		}  else if(message.getMeta("event").equals("mouseRelease")) {
			if(controller.screenID == controller.screenActive){
				controller.robot.mouseRelease(InputEvent.BUTTON1_MASK);
			}
		} else if(message.getMeta("event").equals("keyPress")) {
			if(controller.screenID == controller.screenActive){
				controller.robot.keyPress(Integer.parseInt(message.getMeta("key")));
			}
		} else if(message.getMeta("event").equals("keyRelease")) {
			if(controller.screenID == controller.screenActive){
				controller.robot.keyRelease(Integer.parseInt(message.getMeta("key")));
			}
		} else {
			System.out.println("unknow message received");
		}
	}
}
