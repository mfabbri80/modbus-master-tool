/*
 * Modbus Master Tool
 * Copyright (C) 2014 Luigi Vaira
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cpalaf.ilvata.ma.UI;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cpalaf.ilvata.ma.Logic.Modbus;
import cpalaf.ilvata.ma.Logic.Utils;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class MainWindow {

	private JFrame frmModbusGui;
	private JTextField slaveAddress;
	private JTextField slavePort;
	private JTextField registerText;
	private JTextField valueText;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private int selectedUnit;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmModbusGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frmModbusGui = new JFrame();
		frmModbusGui.setResizable(false);
		frmModbusGui.setTitle("Modbus GUI");
		frmModbusGui.setBounds(100, 100, 414, 234);
		frmModbusGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmModbusGui.getContentPane().setLayout(null);
		frmModbusGui.setLocationRelativeTo(null);
		
		JLabel lblSlaveAddress = new JLabel("Slave address");
		lblSlaveAddress.setBounds(10, 14, 88, 14);
		frmModbusGui.getContentPane().add(lblSlaveAddress);
		
		slaveAddress = new JTextField();
		slaveAddress.setHorizontalAlignment(SwingConstants.CENTER);
		slaveAddress.setBounds(110, 11, 123, 20);
		frmModbusGui.getContentPane().add(slaveAddress);
		slaveAddress.setColumns(10);
		
		slavePort = new JTextField();
		slavePort.setHorizontalAlignment(SwingConstants.CENTER);
		slavePort.setText("502");
		slavePort.setColumns(10);
		slavePort.setBounds(331, 11, 67, 20);
		frmModbusGui.getContentPane().add(slavePort);
		
		JLabel lblSlavePort = new JLabel("Slave port");
		lblSlavePort.setBounds(259, 14, 57, 14);
		frmModbusGui.getContentPane().add(lblSlavePort);
		
		final JTextArea logArea = new JTextArea();
		logArea.setBounds(10, 123, 388, 75);
		frmModbusGui.getContentPane().add(logArea);
		
		JLabel lblRegister = new JLabel("Register");
		lblRegister.setBounds(10, 54, 48, 14);
		frmModbusGui.getContentPane().add(lblRegister);
		
		registerText = new JTextField();
		registerText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				performAction(buttonGroup, logArea);
			}
		});
		registerText.setHorizontalAlignment(SwingConstants.CENTER);
		registerText.setColumns(10);
		registerText.setBounds(70, 51, 67, 20);
		frmModbusGui.getContentPane().add(registerText);
		
		JRadioButton readRB = new JRadioButton("Read");
		readRB.setSelected(true);
		readRB.setActionCommand("Read");
		buttonGroup.add(readRB);
		readRB.setBounds(145, 50, 57, 23);
		frmModbusGui.getContentPane().add(readRB);
		
		final JPanel writePanel = new JPanel();
		FlowLayout fl_writePanel = (FlowLayout) writePanel.getLayout();
		fl_writePanel.setAlignment(FlowLayout.LEFT);
		writePanel.setBorder(null);
		writePanel.setBounds(10, 79, 178, 31);
		frmModbusGui.getContentPane().add(writePanel);
		writePanel.setVisible(false);
		
		final JRadioButton writeRB = new JRadioButton("Write");
		writeRB.setActionCommand("Write");
		writeRB.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setWritePanelVisible(writePanel, writeRB);
			}
		});
		buttonGroup.add(writeRB);
		writeRB.setBounds(206, 50, 57, 23);
		frmModbusGui.getContentPane().add(writeRB);
		
		JLabel lblValue = new JLabel("Value");
		writePanel.add(lblValue);
		
		valueText = new JTextField();
		valueText.setHorizontalAlignment(SwingConstants.CENTER);
		valueText.setColumns(10);
		valueText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				performAction(buttonGroup, logArea);
			}
		});
		writePanel.add(valueText);
		
		JButton sendCommandBT = new JButton("Send command");
		sendCommandBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				performAction(buttonGroup, logArea);
			}
		});
		sendCommandBT.setBounds(259, 87, 137, 23);
		frmModbusGui.getContentPane().add(sendCommandBT);
		
		final JComboBox unitIDSelector = new JComboBox();
		unitIDSelector.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				// Set required UnitID: this value cand be in the range [0..10]
				// where '0' means "Broadcasting" (if supported by slave)
				selectedUnit = Integer.parseInt(arg0.getItem().toString());
			}
		});
		unitIDSelector.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}));
		unitIDSelector.setSelectedIndex(1);
		unitIDSelector.setBounds(341, 51, 57, 20);
		frmModbusGui.getContentPane().add(unitIDSelector);
		
		JLabel lblUnitId = new JLabel("Unit ID");
		lblUnitId.setBounds(295, 54, 46, 14);
		frmModbusGui.getContentPane().add(lblUnitId);
	}
	
	private void setWritePanelVisible(JPanel panel, JRadioButton bt) {
		panel.setVisible(bt.isSelected());
	}
	
	/**
	 * This procedure executes the modbus action on the base of
	 * selected radiobutton (Read/Write)
	 * */
	private void performAction(ButtonGroup group, JTextArea log) {
		if (!slaveAddress.getText().isEmpty() // If fields are correctly fulfilled
				&& !registerText.getText().isEmpty()
				&& !slavePort.getText().isEmpty()) {
			Modbus mb = new Modbus(slaveAddress.getText(), Integer.parseInt(slavePort.getText())); // Create Modbus communication instance
			mb.setUnitID(selectedUnit); // Set required UnitID
			int res = -1;
			switch (group.getSelection().getActionCommand()) {
				case "Read":					
					try {
						res = mb.read(Integer.parseInt(registerText.getText())); // Read value from slave
					} catch (NumberFormatException e) {
						res = -1;
					}
					if (res != -1) {
						byte[] bytes = Utils.toByteArray(res);
						// Write on LOG panel
						log.setText(" INT: \t" + res + "\n BYTE[High]: \t" + bytes[0] + "\n BYTE[Low]: \t" + bytes[1]
									+ "\n BINARY: \t" + Integer.toBinaryString(res));
					} else {
						log.setText(" An error has occured in modbus communication");
					}
					break;
				case "Write":
					if (valueText.getText().isEmpty()) { // If no value is in input textbox
						// Show error message
						JOptionPane.showMessageDialog(frmModbusGui, "Insert value to be written");
					} else {
						try {
							// Write value to slave
							res = mb.write(Integer.parseInt(registerText.getText()), Integer.parseInt(valueText.getText()));
						} catch (NumberFormatException e) {
							res = -1;
						}
						// Write on LOG panel
						log.setText("\n " + ((res != -1) ? "OK" : "An error has occured in modbus communication"));
					}		
					break;
				default:
			}
		} else { // If fields are incomplete
			// Show error message
			JOptionPane.showMessageDialog(frmModbusGui, "Some fields are incomplete");
		}
	}
}
