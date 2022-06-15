package com.example.plugins

import com.intelligt.modbus.jlibmodbus.Modbus
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters
import com.intelligt.modbus.jlibmodbus.serial.SerialPort
import com.intelligt.modbus.jlibmodbus.serial.SerialPortAbstractFactory
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException
import purejavacomm.PureJavaComm

class SerialPortFactoryJSerialCommModify : SerialPortAbstractFactory {
    @Throws(SerialPortException::class)
    override fun createSerial(sp: SerialParameters): SerialPort {
        try {
            Class.forName("com.fazecast.jSerialComm.SerialPort")
        } catch (e: ClassNotFoundException) {
            throw SerialPortException(e)
        }
        return SerialPortJSerialCommModify(sp)
    }

    override fun getPortIdentifiers(): List<String> {
        val ports = com.fazecast.jSerialComm.SerialPort.getCommPorts()
        val portIdentifiers: MutableList<String> = ArrayList(ports.size)
        for (i in ports.indices) {
            portIdentifiers.add(ports[i].systemPortName)
        }
        return portIdentifiers
    }

    override fun getVersion(): String {
        var version = "information about version is unavailable."
        try {
            Class.forName("purejavacomm.PureJavaComm")
            version = PureJavaComm.getVersion()
        } catch (e: ClassNotFoundException) {
            Modbus.log().warning("The PureJavaComm library is not found.")
        }
        return version
    }
}