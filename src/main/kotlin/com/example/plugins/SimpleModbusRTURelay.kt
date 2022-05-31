package com.example.plugins

import com.intelligt.modbus.jlibmodbus.Modbus
import com.intelligt.modbus.jlibmodbus.ModbusMasterFactory
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters
import com.intelligt.modbus.jlibmodbus.serial.SerialPort
import jssc.SerialPortList

// mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 1 -R /dev/ttyAMA1 1
//   -v            Verbose mode.  Causes mbpoll to print debugging messages about  its progress.  This is helpful in debugging connection...
// -a #(1)          Slave address (1-255 for rtu, 0-255 for tcp, 1 is default) for reading, it is possible to give an address list
// -b #  (9600)        Baudrate (1200-921600, 19200 is default)
//  -m #  (rtu)        mode (rtu or tcp, TCP is default)
// -t 0          Discrete output (coil) data type (binary 0 or 1)
//  -P #  (none)        Parity (none, even, odd, even is default)
// -r #  (1)  (first rely)      Start reference (1 is default)
//  -R            RS-485 mode (/RTS on (0) after sending)
//   /dev/ttyAMA1             device
//  1 or 0                  write values
class SimpleModbusRTURelay {

    // Discrete output (coil) data type (binary 0 or 1)   -t 0
    // Start reference (1 is default)                     -r 4
    // RS-485 mode (/RTS on (0) after sending)             -R
    //  writevalues...                                    0 or 1

    fun setTest1() {
        val sp = SerialParameters()
        Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG)                 //debug enable
        try {

                sp.device = "/dev/ttyAMA1"     //                       /dev/ttyAMA1
                sp.setBaudRate(SerialPort.BaudRate.BAUD_RATE_9600)   //   -b 9600
                sp.dataBits = 8                          //0
                sp.parity = SerialPort.Parity.NONE //                     -P none
                sp.stopBits = 1                    //1

                val m = ModbusMasterFactory.createModbusMasterRTU(sp)   //  -m rtu
                    m.connect()   //   Probably a bug here.   Perhaps the parameters are not correct.
              // println(m.isConnected)
                val slaveId = 1 //                       slave address ?    -a 1
                var offset = 4
                val quantity = 1

                try {
                    val registerValues = m.readHoldingRegisters(slaveId, offset, quantity)
                    for (value in registerValues) {
                        println("Address: " + offset++ + ", Value: " + value)
                    }
                } catch (e: RuntimeException) {
                    throw e
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        m.disconnect()
                    } catch (e1: ModbusIOException) {
                        e1.printStackTrace()
                    }
                }

        } catch (e: RuntimeException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}