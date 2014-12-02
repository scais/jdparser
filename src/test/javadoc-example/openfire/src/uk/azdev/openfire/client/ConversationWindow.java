/*
 * Created by JFormDesigner on Sat Jun 02 16:02:42 BST 2007
 */

package uk.azdev.openfire.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import uk.azdev.openfire.conversations.Conversation;
import uk.azdev.openfire.conversations.IConversationListener;

/**
 * @author Iain McGinniss
 */
public class ConversationWindow extends JFrame implements IConversationListener {
	
	private static final long serialVersionUID = 1L;
	private Conversation conversation;
	private OpenFireClient owner;
	
	public ConversationWindow(OpenFireClient owner, Conversation conversation) {
		this.owner = owner;
		this.conversation = conversation;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Conversation with " + conversation.getPeer().getDisplayName());
		initComponents();
		
		chatLogUpdated();
		conversation.addChatListener(this);
		this.setVisible(true);
	}

	public void chatLogUpdated() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				conversationBox.setText(conversation.getChatLog(true));
				activityLabel.setText(" ");
			}
		});
	}
	
	public void peerIsTyping() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				activityLabel.setText(conversation.getPeer().getDisplayName() + " is typing a message");
			}
		});
	}
	
	public void peerIsNotTyping() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				activityLabel.setText(" ");
			}
		});
	}

	private void sendMessage(ActionEvent e) {
		if(!(myMessageBox.getText().length() == 0)) {
			conversation.sendMessage(myMessageBox.getText());
			myMessageBox.setText("");
		}
	}

	private void myMessageBoxKeyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			sendButton.doClick();
			e.consume();
		}
	}

	private void windowClosing(WindowEvent e) {
		owner.conversationClosed(conversation.getPeer().getSessionId());
	}
	
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		contentPane = new JPanel();
		conversationContainer = new JScrollPane();
		conversationBox = new JTextArea();
		myMessageContainer = new JScrollPane();
		myMessageBox = new JTextArea();
		sendButton = new JButton();
		activityLabel = new JLabel();

		//======== this ========
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ConversationWindow.this.windowClosing(e);
			}
		});
		Container contentPane2 = getContentPane();
		contentPane2.setLayout(new BorderLayout());

		//======== contentPane ========
		{
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new GridBagLayout());
			((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0, 0};
			((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
			((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0E-4};
			((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};

			//======== conversationContainer ========
			{

				//---- conversationBox ----
				conversationBox.setColumns(30);
				conversationBox.setRows(10);
				conversationBox.setEditable(false);
				conversationBox.setLineWrap(true);
				conversationBox.setWrapStyleWord(true);
				conversationContainer.setViewportView(conversationBox);
			}
			contentPane.add(conversationContainer, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//======== myMessageContainer ========
			{

				//---- myMessageBox ----
				myMessageBox.setRows(2);
				myMessageBox.setColumns(20);
				myMessageBox.setWrapStyleWord(true);
				myMessageBox.setLineWrap(true);
				myMessageBox.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						myMessageBoxKeyPressed(e);
					}
				});
				myMessageContainer.setViewportView(myMessageBox);
			}
			contentPane.add(myMessageContainer, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

			//---- sendButton ----
			sendButton.setText("Send");
			sendButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sendMessage(e);
				}
			});
			contentPane.add(sendButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//---- activityLabel ----
			activityLabel.setText(" ");
			contentPane.add(activityLabel, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane2.add(contentPane, BorderLayout.CENTER);
		setSize(390, 280);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel contentPane;
	private JScrollPane conversationContainer;
	private JTextArea conversationBox;
	private JScrollPane myMessageContainer;
	private JTextArea myMessageBox;
	private JButton sendButton;
	private JLabel activityLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
