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
package cpalaf.ilvata.ma.Logic;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadCoilsRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
import net.wimpi.modbus.msg.WriteMultipleRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;

/**
 * This class manages the interaction with slave devices
 * using modbus protocol
 * */
public class Modbus {
	private TCPMasterConnection conn;
	private int unitID = 1; // Default unit id
	
	public Modbus(String address, int port) {
		try {
			// Setup connection
			conn = new TCPMasterConnection(InetAddress.getByName(address));
			conn.setPort(port);
		} catch (UnknownHostException e) {
			conn = null;
		}
	}
	
	/**
	 * GETTER for slave address
	 * @return Slave net address
	 * */
	public String getAddress() {
		return conn.getAddress().getHostAddress();
	}
	
	/**
	 * SETTER for slave address
	 * @param address - Slave net address to be set
	 * */
	public void setAddress(String address) {
		try {
			conn.setAddress(InetAddress.getByName(address));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * GETTER for slave port
	 * @return Slave net port
	 * */
	public int getPort() {
		return conn.getPort();
	}
	
	/**
	 * SETTER for slave port
	 * @param port - Slave net port to be set
	 * */
	public void setPort(int port) {
		conn.setPort(port);
	}
	
	/**
	 * Read the value from a holding register
	 * @param register - Register number
	 * @return Returns the value hold in register or -1 if error occurs
	 * */
	public int read(int register) {
		int res;
		if (conn != null) {
			ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(register-1, 1); // Setup read request (only for holding register, addresses 4xxxxx)
			ModbusTCPTransaction trs = new ModbusTCPTransaction(conn); // Setup TCP transaction
			req.setUnitID(unitID); // Unit/node ID
			trs.setReconnecting(true);
			trs.setRequest(req); // Set request for transaction
			try {
				trs.execute(); // Transaction execution
				ReadMultipleRegistersResponse resp = (ReadMultipleRegistersResponse) trs.getResponse(); // Read data from transaction
				InputRegister ir = resp.getRegister(0); // Getting required register (always index '0', request for only one value)
				res = ir.getValue();
			} catch (Exception e) {
				res = -1; // Register read error
			}
		} else res = -1;
		return res;
	}
	
	/**
	 * Write a value into a holding register
	 * @param register - Register number
	 * @param value - Value to be written
	 * @return Returns number of bytes written or -1 if error occurs
	 * */
	public int write(int register, int value) {
		int res;
		if (conn != null) {
			Register reg = new SimpleRegister(value); // Instantiating Register for holding value to be written
			WriteMultipleRegistersRequest rq = new WriteMultipleRegistersRequest(register-1, new Register[] { reg }); // Setup write request (only for addresses 4xxxx)
			ModbusTCPTransaction trs = new ModbusTCPTransaction(conn); // Setup TCP transaction
			rq.setUnitID(unitID); // Unit/node ID
			trs.setReconnecting(true);
			trs.setRequest(rq); // Set request for transaction
			try {
				trs.execute(); // Transaction execution
				WriteMultipleRegistersResponse resp = (WriteMultipleRegistersResponse) trs.getResponse(); // Read response after transaction execution
				res = resp.getByteCount(); // Number of bytes read
			} catch (Exception e) {
				res = -1; // Register write error
			}
		} else res = -1;
		return res;
	}
	
	/**
	 * SETTER for slave Unit ID
	 * @param - Unit ID to be set
	 * */
	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}
	
	/**
	 * GETTER for slave Unit ID
	 * @return Returns the slave Unit ID
	 * */
	public int getUnitID() {
		return unitID;
	}
}
