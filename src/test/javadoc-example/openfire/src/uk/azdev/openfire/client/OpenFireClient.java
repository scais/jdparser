/*
 * Created by JFormDesigner on Fri May 25 22:14:49 BST 2007
 */

package uk.azdev.openfire.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import uk.azdev.openfire.ConnectionEventListener;
import uk.azdev.openfire.XFireConnection;
import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.common.ReceivedInvitation;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.conversations.Conversation;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;

public class OpenFireClient extends JFrame implements ConnectionEventListener {
	
	private static final long serialVersionUID = 1L;
	
	private OpenFireConfiguration config;
	private XFireConnection connection;
	
	private FriendListModel onlineFriendsModel;
	private FriendListModel offlineFriendsModel;
	private FriendListModel friendsOfFriendsModel;
	
	private Map<SessionId, ConversationWindow> openConversations;
	
	private CountDownLatch exitLatch;
	
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel panel4;
	private JPanel panel1;
	private JLabel onlineFriendsLabel;
	private JScrollPane onlineFriendsContainer;
	private JTable onlineFriendsList;
	private JPanel panel2;
	private JLabel friendsOfFriendsLabel;
	private JScrollPane fofContainer;
	private JTable fofList;
	private JPanel panel3;
	private JLabel offlineFriendsLabel;
	private JScrollPane offlineFriendsContainer;
	private JTable offlineFriendsList;
	private JToolBar toolbar;
	private JButton connectBtn;
	private JButton disconnectBtn;
	private JPanel hSpacer1;
	private JButton helpBtn;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	public OpenFireClient(String configFilePath) {
		readConfig(configFilePath);
		
		exitLatch = new CountDownLatch(1);
		initComponents();
		
		disconnectBtn.setEnabled(false);
		connectBtn.setEnabled(true);
		
		onlineFriendsModel = new FriendListModel();
		onlineFriendsList.setModel(onlineFriendsModel);
		
		offlineFriendsModel = new FriendListModel();
		offlineFriendsList.setModel(offlineFriendsModel);
		
		friendsOfFriendsModel = new FriendListModel();
		fofList.setModel(friendsOfFriendsModel);
		
		openConversations = new HashMap<SessionId, ConversationWindow>();
		
		this.addWindowListener(new ExitListener());
		
		this.pack();
		this.setVisible(true);
	}

	/**
	 * @param configFilePath
	 */
	private void readConfig(String configFilePath) {
		try {
			FileInputStream reader = new FileInputStream(configFilePath);
			config = OpenFireConfiguration.readConfig(reader);
		} catch(Exception e) {
			config = new OpenFireConfiguration();
			config.setNetworkPort(50000);
			config.setLocalPort(50000);
		}
	}

	private void showHelp(ActionEvent e) {
		AboutDialog dialog = new AboutDialog(this);
		dialog.setVisible(true);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		panel4 = new JPanel();
		panel1 = new JPanel();
		onlineFriendsLabel = new JLabel();
		onlineFriendsContainer = new JScrollPane();
		onlineFriendsList = new JTable();
		panel2 = new JPanel();
		friendsOfFriendsLabel = new JLabel();
		fofContainer = new JScrollPane();
		fofList = new JTable();
		panel3 = new JPanel();
		offlineFriendsLabel = new JLabel();
		offlineFriendsContainer = new JScrollPane();
		offlineFriendsList = new JTable();
		toolbar = new JToolBar();
		connectBtn = new JButton();
		disconnectBtn = new JButton();
		hSpacer1 = new JPanel(null);
		helpBtn = new JButton();

		//======== this ========
		setTitle("OpenFire");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== panel4 ========
		{
			panel4.setLayout(new GridLayout(3, 0, 0, 2));

			//======== panel1 ========
			{
				panel1.setLayout(new BorderLayout());

				//---- onlineFriendsLabel ----
				onlineFriendsLabel.setText("Online Friends:");
				onlineFriendsLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
				panel1.add(onlineFriendsLabel, BorderLayout.NORTH);

				//======== onlineFriendsContainer ========
				{
					onlineFriendsContainer.setPreferredSize(new Dimension(300, 150));

					//---- onlineFriendsList ----
					onlineFriendsList.setFont(new Font("Dialog", Font.PLAIN, 10));
					onlineFriendsList.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							onlineFriendClicked(e);
						}
					});
					onlineFriendsContainer.setViewportView(onlineFriendsList);
				}
				panel1.add(onlineFriendsContainer, BorderLayout.CENTER);
			}
			panel4.add(panel1);

			//======== panel2 ========
			{
				panel2.setLayout(new BorderLayout());

				//---- friendsOfFriendsLabel ----
				friendsOfFriendsLabel.setText("Friends of Friends:");
				friendsOfFriendsLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
				panel2.add(friendsOfFriendsLabel, BorderLayout.NORTH);

				//======== fofContainer ========
				{
					fofContainer.setPreferredSize(new Dimension(300, 150));

					//---- fofList ----
					fofList.setFont(new Font("Dialog", Font.PLAIN, 10));
					fofContainer.setViewportView(fofList);
				}
				panel2.add(fofContainer, BorderLayout.CENTER);
			}
			panel4.add(panel2);

			//======== panel3 ========
			{
				panel3.setLayout(new BorderLayout());

				//---- offlineFriendsLabel ----
				offlineFriendsLabel.setText("Offline Friends:");
				offlineFriendsLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
				panel3.add(offlineFriendsLabel, BorderLayout.NORTH);

				//======== offlineFriendsContainer ========
				{
					offlineFriendsContainer.setPreferredSize(new Dimension(300, 150));

					//---- offlineFriendsList ----
					offlineFriendsList.setFont(new Font("Dialog", Font.PLAIN, 10));
					offlineFriendsContainer.setViewportView(offlineFriendsList);
				}
				panel3.add(offlineFriendsContainer, BorderLayout.CENTER);
			}
			panel4.add(panel3);
		}
		contentPane.add(panel4, BorderLayout.CENTER);

		//======== toolbar ========
		{
			toolbar.setRollover(true);
			toolbar.setFloatable(false);

			//---- connectBtn ----
			connectBtn.setText("Login");
			connectBtn.setIcon(new ImageIcon(getClass().getResource("/uk/azdev/openfire/client/icons/connect.png")));
			connectBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					connect(e);
				}
			});
			toolbar.add(connectBtn);

			//---- disconnectBtn ----
			disconnectBtn.setText("Logout");
			disconnectBtn.setIcon(new ImageIcon(getClass().getResource("/uk/azdev/openfire/client/icons/disconnect.png")));
			disconnectBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					disconnect(e);
				}
			});
			toolbar.add(disconnectBtn);
			toolbar.add(hSpacer1);

			//---- helpBtn ----
			helpBtn.setText("About");
			helpBtn.setIcon(new ImageIcon(getClass().getResource("/uk/azdev/openfire/client/icons/help.png")));
			helpBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showHelp(e);
				}
			});
			toolbar.add(helpBtn);
		}
		contentPane.add(toolbar, BorderLayout.NORTH);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}
	
	private void connect(ActionEvent e) {
		LoginDialog dialog = new LoginDialog(this);
		dialog.setVisible(true);
		
		if(!dialog.shouldProceed()) {
			return;
		}
		
		config.setUsername(dialog.getUserName());
		config.setPassword(dialog.getPassword());
		
		connection = new XFireConnection(config);
		connection.addListener(this);
		try {
			connection.blockingConnect();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, "Error occurred while attempting to connect", "Connection Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				disconnectBtn.setEnabled(true);
				connectBtn.setEnabled(false);
			}
		});
	}
	
	private void disconnect(ActionEvent e) {
		try {
			connection.blockingDisconnect();
			connectBtn.setEnabled(true);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, "Error occurred while attempting to disconnect", "Connection Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				disconnectBtn.setEnabled(false);
				connectBtn.setEnabled(true);
			}
		});
	}
	
	public void disconnected() {
		onlineFriendsModel.clear();
		offlineFriendsModel.clear();
		friendsOfFriendsModel.clear();
	}
	
	private void onlineFriendClicked(MouseEvent e) {
		if(e.getClickCount() != 2) {
			return;
		}
		
		int selectedFriendIndex = onlineFriendsList.getSelectedRow();
		Friend selectedFriend = onlineFriendsModel.getFriendAt(selectedFriendIndex);
		ConversationWindow window = getConversationWindow(selectedFriend.getSessionId());
		window.requestFocus();
	}

	private ConversationWindow getConversationWindow(final SessionId friendSid) {
		Conversation conversation = connection.getConversation(friendSid);
		if(openConversations.containsKey(friendSid)) {
			return openConversations.get(friendSid);
		}
		
		ConversationWindow convWindow = new ConversationWindow(this, conversation);
		openConversations.put(friendSid, convWindow);
		
		return convWindow;
	}
	
	public void waitForExit() throws InterruptedException {
		exitLatch.await();
	}
	
	

	public void conversationUpdate(final SessionId sessionId) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(!openConversations.containsKey(sessionId)) {
					getConversationWindow(sessionId);
				}
			}
		});
	}

	public void friendsListUpdated() {
		FriendsList friendsList = connection.getFriendList();
		SwingUtilities.invokeLater(new UpdateFriendList(friendsList));
	}

	public void loginFailed() {
		JOptionPane.showMessageDialog(this, "Incorrect login details", "Login failed", JOptionPane.ERROR_MESSAGE);
		disconnectBtn.setEnabled(false);
		connectBtn.setEnabled(true);
	}
	
	public void connectionError() {
		JOptionPane.showMessageDialog(this, "An error occurred which required your connection with the XFire server to be closed", "Connection error", JOptionPane.ERROR_MESSAGE);
		disconnectBtn.setEnabled(false);
		connectBtn.setEnabled(true);
	}
	
	public void internalError(Exception e) {
		JOptionPane.showMessageDialog(this, "An internal error has occurred. It is recommended that you restart the program now\n Details:\n" + e.getMessage(), "Internal error", JOptionPane.ERROR_MESSAGE);
		disconnectBtn.setEnabled(false);
		connectBtn.setEnabled(true);
	}

	public void conversationClosed(SessionId sessionId) {
		openConversations.remove(sessionId);
	}
	
	private final class ExitListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			
			for(ConversationWindow window : openConversations.values()) {
				window.setVisible(false);
				window.dispose();
			}
			
			if(connection != null) {
				try {
					connection.blockingDisconnect();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(OpenFireClient.this, "Error occurred while attempting to disconnect", "Connection Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			exitLatch.countDown();
		}
	}

	private class UpdateFriendList implements Runnable {

		private FriendsList friendsList;
		
		public UpdateFriendList(FriendsList friendsList) {
			this.friendsList = friendsList;
		}
		
		public void run() {
			ArrayList<Friend> onlineFriends = new ArrayList<Friend>();
			ArrayList<Friend> offlineFriends = new ArrayList<Friend>();
			ArrayList<Friend> onlineFriendsOfFriends = new ArrayList<Friend>();

			for(Friend f : friendsList.getAllFriends()) {
				if(friendsList.areConnected(friendsList.getSelf(), f)) {
					if(f.isOnline()) {
						onlineFriends.add(f);
					} else {
						offlineFriends.add(f);
					}
				} else {
					if(f.isOnline()) {
						onlineFriendsOfFriends.add(f);
					}
				}
			}
			
			onlineFriendsModel.updateList(onlineFriends);
			offlineFriendsModel.updateList(offlineFriends);
			friendsOfFriendsModel.updateList(onlineFriendsOfFriends);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		String configFilePath = null;
		if(args.length > 0) {
			configFilePath = args[0];
		}
		OpenFireClient client = new OpenFireClient(configFilePath);
		client.waitForExit();
	}

	public void inviteReceived(ReceivedInvitation invite) {
		// TODO Auto-generated method stub
		
	}
}
