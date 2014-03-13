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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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

public class MainWindow {

	private JFrame frmModbusGui;
	private JTextField slaveAddress;
	private JTextField slavePort;
	private JTextField registerText;
	private JTextField valueText;
	private JPanel bitPanel;
	private JCheckBox bitmask;
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
		frmModbusGui.setTitle("Modbus GUI");
		frmModbusGui.setBounds(100, 100, 425, 318);
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
		logArea.setBounds(0, 200, 409, 75);
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
		writePanel.setBounds(10, 82, 241, 30);
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
		
		final JCheckBox bit0 = new JCheckBox("0");
		bit0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit1 = new JCheckBox("1");
		bit1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit2 = new JCheckBox("2");
		bit2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit3 = new JCheckBox("3");
		bit3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit4 = new JCheckBox("4");
		bit4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit5 = new JCheckBox("5");
		bit5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit6 = new JCheckBox("6");
		bit6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit7 = new JCheckBox("7");
		bit7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit8 = new JCheckBox("8");
		bit8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit9 = new JCheckBox("9");
		bit9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit10 = new JCheckBox("10");
		bit10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit11 = new JCheckBox("11");
		bit11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit12 = new JCheckBox("12");
		bit12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit13 = new JCheckBox("13");
		bit13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit14 = new JCheckBox("14");
		bit14.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		final JCheckBox bit15 = new JCheckBox("15");
		bit15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueText((JCheckBox)arg0.getSource(), valueText);
			}
		});

		valueText = new JTextField();
		valueText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (!valueText.getText().isEmpty()) {
					byte[] convBin = Utils.toBinaryArray(Integer.parseInt(valueText.getText()));
					bit0.setSelected((convBin[0] == 1) ? true :  false);
					bit1.setSelected((convBin[1] == 1) ? true :  false);
					bit2.setSelected((convBin[2] == 1) ? true :  false);
					bit3.setSelected((convBin[3] == 1) ? true :  false);
					bit4.setSelected((convBin[4] == 1) ? true :  false);
					bit5.setSelected((convBin[5] == 1) ? true :  false);
					bit6.setSelected((convBin[6] == 1) ? true :  false);
					bit7.setSelected((convBin[7] == 1) ? true :  false);
					bit8.setSelected((convBin[8] == 1) ? true :  false);
					bit9.setSelected((convBin[9] == 1) ? true :  false);
					bit10.setSelected((convBin[10] == 1) ? true :  false);
					bit11.setSelected((convBin[11] == 1) ? true :  false);
					bit12.setSelected((convBin[12] == 1) ? true :  false);
					bit13.setSelected((convBin[13] == 1) ? true :  false);
					bit14.setSelected((convBin[14] == 1) ? true :  false);
					bit15.setSelected((convBin[15] == 1) ? true :  false);
				}
			}
		});
		valueText.setText("0");
		valueText.setHorizontalAlignment(SwingConstants.CENTER);
		valueText.setColumns(10);
		valueText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				performAction(buttonGroup, logArea);
			}
		});
		writePanel.add(valueText);
			
		bitPanel = new JPanel();
		bitPanel.setVisible(false);
		bitPanel.setBounds(10, 123, 388, 66);
		frmModbusGui.getContentPane().add(bitPanel);
		bitPanel.setLayout(new GridLayout(2, 9, 0, 0));
		
		bitmask = new JCheckBox("Bit mask");
		bitmask.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JCheckBox source = (JCheckBox) arg0.getSource();
				bitPanel.setVisible(source.isSelected());
			}
		});
		writePanel.add(bitmask);
		
		JButton sendCommandBT = new JButton("Send command");
		sendCommandBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				performAction(buttonGroup, logArea);
			}
		});
		sendCommandBT.setBounds(259, 89, 137, 23);
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
		
		
		bitPanel.add(bit0);
		bitPanel.add(bit1);
		bitPanel.add(bit2);
		bitPanel.add(bit3);
		bitPanel.add(bit4);
		bitPanel.add(bit5);
		bitPanel.add(bit6);
		bitPanel.add(bit7);
		bitPanel.add(bit8);
		bitPanel.add(bit9);
		bitPanel.add(bit10);
		bitPanel.add(bit11);
		bitPanel.add(bit12);
		bitPanel.add(bit13);
		bitPanel.add(bit14);
		bitPanel.add(bit15);
	}
	
	private void setValueText(JCheckBox source, JTextField valueText2) {
			int pos = Integer.parseInt(source.getText());
			int startValue = Integer.parseInt(valueText2.getText());
			if (source.isSelected()) {
				startValue += 1 << pos;
			} else {
				startValue -= 1 << pos;
			}
			valueText2.setText(String.valueOf(startValue));
	}

	private void setWritePanelVisible(JPanel panel, JRadioButton bt) {
		panel.setVisible(bt.isSelected());
		bitPanel.setVisible(bt.isSelected() && bitmask.isSelected());
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
