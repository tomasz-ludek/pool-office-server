package com.example.plugins

import com.intelligt.modbus.jlibmodbus.Modbus
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters
import com.intelligt.modbus.jlibmodbus.serial.SerialPort
import com.intelligt.modbus.jlibmodbus.serial.SerialPortAbstractFactory
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException
import gnu.io.CommPortIdentifier
import gnu.io.RXTXVersion

class SerialPortFactoryRXTXModify : SerialPortAbstractFactory {
    @Throws(SerialPortException::class)
    override fun createSerial(sp: SerialParameters): SerialPort {
        try {
            Class.forName("gnu.io.SerialPort")
        } catch (e: ClassNotFoundException) {
            throw SerialPortException(e)
        }
        return SerialPortRXTXModify(sp)
    }

    override fun getPortIdentifiers(): List<String> {
        val ports = CommPortIdentifier.getPortIdentifiers()
        val list: MutableList<String> = ArrayList()
        while (ports.hasMoreElements()) {
            val id = ports.nextElement() as CommPortIdentifier
            if (id.portType == CommPortIdentifier.PORT_RS485 ||
                id.portType == CommPortIdentifier.PORT_SERIAL
            ) {
                list.add(id.name)
            }
        }
        return list
    }

    override fun getVersion(): String {
        var version = "information about version is unavailable."
        try {
            Class.forName("gnu.io.SerialPort")
            version = RXTXVersion.getVersion()
        } catch (e: ClassNotFoundException) {
            Modbus.log().warning("The RXTX library is not found.")
        }
        return version
    }
}