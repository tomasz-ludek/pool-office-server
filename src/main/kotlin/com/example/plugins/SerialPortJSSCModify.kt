package com.example.plugins

import com.intelligt.modbus.jlibmodbus.serial.SerialParameters
import com.intelligt.modbus.jlibmodbus.serial.SerialPort
import jssc.SerialPortException
import java.io.IOException

class SerialPortJSSCModify(sp: SerialParameters) : SerialPort(sp) {
    private val port: jssc.SerialPort

    init {
        port = jssc.SerialPort(sp.device)
    }

    @Throws(IOException::class)
    override fun write(b: Int) {
        try {
            port.writeByte(b.toByte())
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    override fun write(bytes: ByteArray) {
        try {
            port.writeBytes(bytes)
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    @Throws(com.intelligt.modbus.jlibmodbus.serial.SerialPortException::class)
    override fun open() {
        try {
            port.openPort()
            val sp = serialParameters
            port.setParams(sp.baudRate, sp.dataBits, sp.stopBits, sp.parity.value,true,true)
        } catch (ex: Exception) {
            throw com.intelligt.modbus.jlibmodbus.serial.SerialPortException(ex)
        }
    }

    @Throws(IOException::class)
    override fun read(): Int {
        return try {
            port.flowControlMode = jssc.SerialPort.FLOWCONTROL_RTSCTS_IN
            if (readTimeout > 0) port.readBytes(1, readTimeout)[0].toInt() else port.readBytes(1)[0].toInt()
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray, off: Int, len: Int): Int {
        return try {
            port.flowControlMode = jssc.SerialPort.FLOWCONTROL_RTSCTS_OUT
            val rb = port.readBytes(len, readTimeout)
            System.arraycopy(rb, 0, b, off, len)
            rb.size
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    override fun close() {
        if (isOpened) {
            try {
                port.closePort()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun isOpened(): Boolean {
        return port.isOpened
    }
}