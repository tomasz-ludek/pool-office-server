package com.example.plugins

import com.intelligt.modbus.jlibmodbus.serial.SerialParameters
import com.intelligt.modbus.jlibmodbus.serial.SerialPort
import com.intelligt.modbus.jlibmodbus.serial.SerialPortAbstractFactory
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException
import jssc.SerialPortList
import java.util.*

class SerialPortFactoryJSSCModify : SerialPortAbstractFactory {
    @Throws(SerialPortException::class)
    override fun createSerial(sp: SerialParameters): SerialPort {
        try {
            Class.forName("jssc.SerialPort")
        } catch (e: ClassNotFoundException) {
            throw SerialPortException(e)
        }
        return SerialPortJSSCModify(sp)
    }

    override fun getPortIdentifiers(): List<String> {
        return Arrays.asList(*SerialPortList.getPortNames())
    }

    override fun getVersion(): String {
        return "information about version is unavailable."
    }
}