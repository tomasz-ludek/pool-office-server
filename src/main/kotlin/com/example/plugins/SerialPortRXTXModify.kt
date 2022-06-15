package com.example.plugins

import com.intelligt.modbus.jlibmodbus.Modbus
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters
import com.intelligt.modbus.jlibmodbus.serial.SerialPort
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException
import gnu.io.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.atomic.AtomicBoolean

class SerialPortRXTXModify(sp: SerialParameters?) : SerialPort(sp), SerialPortEventListener {
    private var port: gnu.io.SerialPort? = null
    private val opened = AtomicBoolean(false)
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
        try {
            val sp = serialParameters
            val portIdentifier: CommPortIdentifier
            portIdentifier = CommPortIdentifier.getPortIdentifier(sp.device)
            if (portIdentifier.isCurrentlyOwned) {
                val msg = "Cannot open serial port " + sp.device
                Modbus.log().warning(msg)
                throw SerialPortException(msg)
            } else {
                val commPort = portIdentifier.open(this.javaClass.name, Modbus.MAX_CONNECTION_TIMEOUT)
                if (commPort is gnu.io.SerialPort) {
                    port = commPort
                    port!!.setSerialPortParams(sp.baudRate, sp.dataBits, sp.stopBits, sp.parity.value)
                    port!!.flowControlMode = gnu.io.SerialPort.FLOWCONTROL_RTSCTS_IN
                    `in` = port!!.inputStream
                    out = port!!.outputStream
                    port!!.enableReceiveTimeout(readTimeout)
                    isOpened = true
                } else {
                    Modbus.log().severe(sp.device + " is not a serial port.")
                }
            }
        } catch (e: NoSuchPortException) {
            throw SerialPortException(e)
        } catch (ex: Exception) {
            throw SerialPortException(ex)
        }
    }

    override fun setReadTimeout(readTimeout: Int) {
        super.setReadTimeout(readTimeout)
        try {
            if (isOpened) port!!.enableReceiveTimeout(readTimeout)
        } catch (e: UnsupportedCommOperationException) {
            Modbus.log().warning(e.localizedMessage)
        }
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
                isOpened = false
                `in`!!.close()
                out!!.close()
                port!!.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun isOpened(): Boolean {
        return opened.get()
    }

    fun setOpened(opened: Boolean) {
        this.opened.set(opened)
    }

    override fun serialEvent(serialPortEvent: SerialPortEvent) {
        when (serialPortEvent.eventType) {
            SerialPortEvent.OE, SerialPortEvent.PE, SerialPortEvent.FE -> Modbus.log().warning(
                port!!.name + ": framing error."
            )
            else -> {}
        }
    }
}