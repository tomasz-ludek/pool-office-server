package com.example.plugins

import com.intelligt.modbus.jlibmodbus.Modbus
import com.intelligt.modbus.jlibmodbus.serial.*
import purejavacomm.CommPortIdentifier
import purejavacomm.PureJavaComm
import java.util.*

class SerialPortFactoryPJCModify : SerialPortAbstractFactory {
    @Throws(SerialPortException::class)
    override fun createSerial(sp: SerialParameters): SerialPort {
        try {
            Class.forName("purejavacomm.SerialPort")
        } catch (e: ClassNotFoundException) {
            throw SerialPortException(e)
        }
        return SerialPortPJCModify(sp)
    }

    override fun getPortIdentifiers(): List<String> {
        val ports: Enumeration<*> = CommPortIdentifier.getPortIdentifiers()
        val list: MutableList<String> = ArrayList()
        while (ports.hasMoreElements()) {
            val id = ports.nextElement() as CommPortIdentifier
            if (id.portType == CommPortIdentifier.PORT_SERIAL) {
                list.add(id.name)
            }
        }
        return list
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