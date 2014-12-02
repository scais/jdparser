/*
 * Created by JFormDesigner on Sun May 27 19:13:54 BST 2007
 */

package uk.azdev.openfire.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private boolean proceed = false;
	
	public LoginDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public LoginDialog(Dialog owner) {
		super(owner);
		initComponents();
	}
	
	public String getUserName() {
		return userNameField.getText();
	}
	
	public String getPassword() {
		return new String(passwordField.getPassword());
	}

	private void okClicked(ActionEvent e) {
		proceed = true;
		this.setVisible(false);
	}

	private void cancelClicked(ActionEvent e) {
		proceed = false;
		this.setVisible(false);
	}
	
	public boolean shouldProceed() {
		return proceed;
	}

	private void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			okButton.doClick();
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		promptLabel = new JLabel();
		userNameLabel = new JLabel();
		userNameField = new JTextField();
		passwordLabel = new JLabel();
		passwordField = new JPasswordField();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();

		//======== this ========
		setTitle("Login");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setName("this");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
		dialogPane.setName("dialogPane");
		dialogPane.setLayout(new BorderLayout());

		//======== contentPanel ========
		contentPanel.setName("contentPanel");
		contentPanel.setLayout(new GridBagLayout());
		((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0};
		((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
		((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
		((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

		//---- promptLabel ----
		promptLabel.setText("Please enter your credentials:");
		promptLabel.setName("promptLabel");
		contentPanel.add(promptLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- userNameLabel ----
		userNameLabel.setText("User name:");
		userNameLabel.setName("userNameLabel");
		contentPanel.add(userNameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//---- userNameField ----
		userNameField.setName("userNameField");
		userNameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				LoginDialog.this.keyPressed(e);
			}
		});
		contentPanel.add(userNameField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- passwordLabel ----
		passwordLabel.setText("Password:");
		passwordLabel.setName("passwordLabel");
		contentPanel.add(passwordLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 5), 0, 0));

		//---- passwordField ----
		passwordField.setName("passwordField");
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				LoginDialog.this.keyPressed(e);
			}
		});
		contentPanel.add(passwordField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		dialogPane.add(contentPanel, BorderLayout.CENTER);

		//======== buttonBar ========
		buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
		buttonBar.setName("buttonBar");
		buttonBar.setLayout(new GridBagLayout());
		((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
		((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

		//---- okButton ----
		okButton.setText("OK");
		okButton.setName("okButton");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okClicked(e);
			}
		});
		buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 5), 0, 0));

		//---- cancelButton ----
		cancelButton.setText("Cancel");
		cancelButton.setName("cancelButton");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelClicked(e);
			}
		});
		buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		dialogPane.add(buttonBar, BorderLayout.SOUTH);
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel promptLabel;
	private JLabel userNameLabel;
	private JTextField userNameField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
