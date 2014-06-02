import org.umundo.core.Greeter;
import org.umundo.core.Message;
import org.umundo.core.Publisher;
import org.umundo.core.SubscriberStub;

public class InputGreeter extends Greeter {
	Controller controller;
	
	public InputGreeter(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void welcome(Publisher pub, SubscriberStub subStub) {
		Message m = Message.toSubscriber(subStub.getUUID());
		m.putMeta("participant", pub.getUUID());
		m.putMeta("subscriber", controller.inputSub.getUUID());
		pub.send(m);
	}

	@Override
	public void farewell(Publisher pub, SubscriberStub subStub) {
		if (controller.participants.containsKey(subStub.getUUID())) {
			System.out.println(controller.participants.get(subStub.getUUID()) + " left the channel");
		} else {
			System.out.println("An unknown device left the channel: " + subStub.getUUID());	
		}
	}	
}
