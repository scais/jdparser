package uk.azdev.openfire.relay;

import uk.azdev.openfire.XFireConnection;
import uk.azdev.openfire.conversations.Conversation;
import uk.azdev.openfire.friendlist.Friend;

public class InviteCommand implements Command {

	private String DEFAULT_INVITE_MESSAGE = "Add me to your friends list";
	
	public void execute(String[] args, Friend executor,
			XFireConnection connection) {
		String response;
		if(args.length == 0) {
			response = "User to invite not specified.";
		} else {
			response = inviteUser(args[0], DEFAULT_INVITE_MESSAGE, executor, connection);
		}
		
		Conversation conv = connection.getConversation(executor.getSessionId());
		conv.sendMessage(response);
	}

	private String inviteUser(String userName, String inviteMessage, Friend executor, XFireConnection connection) {
		if(connection.getFriendList().containsFriend(userName)) {
			return "That user is already known to the relay";
		}
		
		connection.sendInvitation(userName, inviteMessage);
		return "this feature is not yet implemented";
	}

	public String getHelpText() {
		return "invite <user>\nInvites the specified user to the relay's friends list.";
	}

	public String getName() {
		return "invite";
	}

}
