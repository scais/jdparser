package uk.azdev.openfire.relay;

import uk.azdev.openfire.XFireConnection;
import uk.azdev.openfire.friendlist.Friend;

public interface Command {

	public String getName();
	public String getHelpText();
	public void execute(String[] args, Friend executor, XFireConnection connection);
	
}
