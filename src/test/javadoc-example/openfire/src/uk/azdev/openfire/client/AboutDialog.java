/*
 * Created by JFormDesigner on Mon Jun 30 23:17:22 BST 2008
 */

package uk.azdev.openfire.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Iain McGinniss
 */
public class AboutDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	public static final String VERSION_NUM = "v2.0";
	public static final String ABOUT_TEXT 
		= "Copyright (c) Iain McGinniss 2007, released under the GNU LGPL 2.1 license\n\n" +
		  "Bug reports, queries and contributions should be directed to:\nhttp://code.google.com/p/openfire\n\n" +
		  "OpenFire is an open source library and suite of tools for the XFire instant messaging network. " +
		  "This client you are running is currently for testing only - I make no guarantees that it is robust " +
		  "enough for day-to-day use as an XFire client replacement. At some point in the future, a more 'official' " +
		  "cross-platform OpenFire client may be released.\n\n" +
		  "Acknowledgements:\n" +
		  "This test client uses some of the icons from the Silk icon set by Mark James, released under the " +
		  "Creative Commons Attribution 2.5 license [ http://creativecommons.org/licenses/by/2.5/ ].\n" +
		  "The full icon set can be found here: http://www.famfamfam.com/lab/icons/silk/\n\n";
	
	public AboutDialog(Frame owner) {
		super(owner);
		initComponents();
		
		headerLabel.setText(headerLabel.getText() + " " + VERSION_NUM);
		mainContents.setText(ABOUT_TEXT);
	}

	public AboutDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void okPressed(ActionEvent e) {
		this.setVisible(false);
		this.dispose();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		headerLabel = new JLabel();
		mainContentsArea = new JScrollPane();
		mainContents = new JTextArea();
		buttonBar = new JPanel();
		okButton = new JButton();

		//======== this ========
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new GridBagLayout());
				((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0};
				((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
				((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0E-4};

				//---- headerLabel ----
				headerLabel.setText("OpenFire Test Client");
				headerLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
				contentPanel.add(headerLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

				//======== mainContentsArea ========
				{

					//---- mainContents ----
					mainContents.setEditable(false);
					mainContents.setLineWrap(true);
					mainContents.setWrapStyleWord(true);
					mainContentsArea.setViewportView(mainContents);
				}
				contentPanel.add(mainContentsArea, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
				((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

				//---- okButton ----
				okButton.setText("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okPressed(e);
					}
				});
				buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		setSize(400, 300);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel headerLabel;
	private JScrollPane mainContentsArea;
	private JTextArea mainContents;
	private JPanel buttonBar;
	private JButton okButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
