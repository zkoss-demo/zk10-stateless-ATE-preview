package org.zkoss.zephyr.demo.richlet;

import static java.util.Arrays.asList;
import static org.zkoss.zephyr.action.ActionTarget.SELF;

import java.util.List;

import org.zkoss.zephyr.annotation.Action;
import org.zkoss.zephyr.annotation.ActionVariable;
import org.zkoss.zephyr.annotation.RichletMapping;
import org.zkoss.zephyr.demo.util.Helper;
import org.zkoss.zephyr.ui.Locator;
import org.zkoss.zephyr.ui.StatelessRichlet;
import org.zkoss.zephyr.ui.UiAgent;
import org.zkoss.zephyr.zpr.IAnyGroup;
import org.zkoss.zephyr.zpr.IButton;
import org.zkoss.zephyr.zpr.IComponent;
import org.zkoss.zephyr.zpr.IHlayout;
import org.zkoss.zephyr.zpr.ILabel;
import org.zkoss.zephyr.zpr.IStyle;
import org.zkoss.zephyr.zpr.ITextbox;
import org.zkoss.zephyr.zpr.IVlayout;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;

@RichletMapping("/simple")
public class SimpleRichlet implements StatelessRichlet {
	
	private static final String SIMPLE_CSS = "/css/simple.css";
	
	@RichletMapping("")
	public List<IComponent> index() {
		return asList(
			IStyle.ofSrc(SIMPLE_CSS),
			IVlayout.of(asList(
				ILabel.of("Simple form demo").withSclass("main-title"),
				IHlayout.of(asList(
					ITextbox.ofId("tbUserId")
						.withPlaceholder("User Id")
						.withConstraint("no empty")
						.withWidth("200px")
						.withInstant(true)
						.withAction(this::inputUpdate),
					ITextbox.ofId("tbUserDisplayName")
						.withPlaceholder("User display name")
						.withWidth("200px")
						.withInstant(true)
						.withAction(this::inputUpdate),
					ITextbox.ofId("tbUserPassword")
						.withPlaceholder("Password")
						.withType("password")
						.withWidth("200px")
						.withInstant(true)
						.withAction(this::inputUpdate)
				)).withHflex("min"),
				(IHlayout) displayLabelTemplate("lbUserId", "User Id").withStyle("padding-top: 20px;"),
				displayLabelTemplate("lbUserDisplayName", "User Name"),
				displayLabelTemplate("lbUserPassword", "User Password"),
				IButton.of("Send data").withAction(this::sendFormData)
			)).withHflex("min").withSclass("main-view")
		);
	}

	private IHlayout displayLabelTemplate(String id, String label) {
		return IHlayout.of(asList(
				ILabel.of(label + ": ").withId(id + "Label").withSclass("data-label"),
				ILabel.ofId(id).withSclass("data-content")
		)).withWidth("600px");
	}

	@Action(type = Events.ON_CHANGE)
	public void inputUpdate(@ActionVariable(targetId = SELF, field = "value") String newValue, @ActionVariable(targetId = SELF, field = "id") String id) {
		Helper.log("Recieved onChange");
		String targetLabelId = "l" + id.substring(1);
		Locator locator = Locator.ofId(targetLabelId);
		UiAgent.getCurrent().smartUpdate(locator, new ILabel.Updater().value(newValue));
	}

	@Action(type = Events.ON_CLICK)
	public void sendFormData(@ActionVariable(targetId = "tbUserId", field = "value") String userId,
			@ActionVariable(targetId = "tbUserDisplayName", field = "value") String userDisplayName,
			@ActionVariable(targetId = "tbUserPassword", field = "value") String userPassword) {
		Helper.log("do something with data " + asList(userId, userDisplayName, userPassword));
	}
	
	
}
