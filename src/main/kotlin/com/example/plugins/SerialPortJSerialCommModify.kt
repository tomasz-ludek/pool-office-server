package com.example.plugins

import com.intelligt.modbus.jlibmodbus.serial.SerialParameters
import com.intelligt.modbus.jlibmodbus.serial.SerialPort
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class SerialPortJSerialCommModify(sp: SerialParameters?) : SerialPort(sp) {
    private var port: com.fazecast.jSerialComm.SerialPort? = null
    private var `in`: InputStream? = null
    private var out: OutputStream? = null
    @Throws(IOException::class)
    override fun write(b: Int) {
        if (!isOpened) {
            throw IOException("Port not opened")
        }
        try {
            out!!.write(b.toByte().toInt())
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    override fun write(bytes: ByteArray) {
        if (!isOpened) {
            throw IOException("Port not opened")
        }
        try {
            out!!.write(bytes)
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    @Throws(SerialPortException::class)
    override fun open() {
        val sp = serialParameters
        port = com.fazecast.jSerialComm.SerialPort.getCommPort(sp.device)
        port?.setComPortParameters(sp.baudRate, sp.dataBits, sp.stopBits, sp.parity.value)
        port?.setComPortParameters(sp.baudRate,sp.dataBits,sp.stopBits,sp.parity.value)
        port?.setFlowControl(com.fazecast.jSerialComm.SerialPort.FLOW_CONTROL_RTS_ENABLED) ///disabled/RTS/CTS/DSR/DTR/IN/OUT
        `in` = port?.getInputStream()
        out = port?.getOutputStream()
        port?.setComPortTimeouts(com.fazecast.jSerialComm.SerialPort.TIMEOUT_READ_BLOCKING, readTimeout, 0)

    }

    override fun setReadTimeout(readTimeout: Int) {
        super.setReadTimeout(readTimeout)
        if (isOpened) port!!.setComPortTimeouts(
            com.fazecast.jSerialComm.SerialPort.TIMEOUT_NONBLOCKING,
            getReadTimeout(),
            getReadTimeout()
        )
    }

    @Throws(IOException::class)
    override fun read(): Int {
        if (!isOpened) {
            throw IOException("Port not opened")
        }
        val c: Int
        c = try {
            `in`!!.read()
        } catch (e: Exception) {
            throw IOException(e)
        }
        return if (c > -1) c else throw IOException("Read timeout")
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray, off: Int, len: Int): Int {
        if (!isOpened) {
            throw IOException("Port not opened")
        }
        val c: Int
        c = try {
            `in`!!.read(b, off, len)
        } catch (e: Exception) {
            throw IOException(e)
        }
        return if (c > -1) c else throw IOException("Read timeout")
    }

    override fun close() {
        try {
            if (isOpened) {
                `in`!!.close()
                out!!.close()
                port!!.closePort()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun isOpened(): Boolean {
        return port!!.isOpen
    }
}