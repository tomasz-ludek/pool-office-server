package com.example.plugins

import com.intelligt.modbus.jlibmodbus.Modbus
import com.intelligt.modbus.jlibmodbus.ModbusMasterFactory
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters
import com.intelligt.modbus.jlibmodbus.serial.SerialPort
import jssc.SerialPortList

/* mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 1 -R /dev/ttyAMA1 1
   -v            Verbose mode.  Causes mbpoll to print debugging messages about  its progress.  This is helpful in debugging connection...
 -a #(1)          Slave address (1-255 for rtu, 0-255 for tcp, 1 is default) for reading, it is possible to give an address list
 -b #  (9600)        Baudrate (1200-921600, 19200 is default)
  -m #  (rtu)        mode (rtu or tcp, TCP is default)
 -t 0          Discrete output (coil) data type (binary 0 or 1)
  -P #  (none)        Parity (none, even, odd, even is default)
 -r #  (1)  (first rely)      Start reference (1 is default)
  -R            RS-485 mode (/RTS on (0) after sending)
   /dev/ttyAMA1             device
  1 or 0                  write values
 */
class SimpleModbusRTURelay {
//   fun must be a not private for  sam testing...
    private fun connectionToPort(startAddress:Int, dataOnOff:Int ):Boolean {
        val dev_list = SerialPortList.getPortNames()
        if (dev_list.size > 0) {
            var rez: String = ""
            val sp = SerialParameters()
            Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG)                 //debug enable
            try {
                sp.device =
                    "/dev/ttyAMA1"     //                       /dev/ttyAMA1     -   device   the name(path) of the serial port
                sp.setBaudRate(SerialPort.BaudRate.BAUD_RATE_9600)   //   -b 9600        - baud rate
                sp.dataBits = 8                          //0                             -  the number of data bits
                sp.parity = SerialPort.Parity.NONE //                     -P none        - parity check (NONE, EVEN, ODD, MARK, SPACE)
                sp.stopBits = 1                    //1                                   -  the number of stop bits(1,2)

                val m = ModbusMasterFactory.createModbusMasterRTU(sp)   //  -m rtu
                m.connect()

                val serverAddres: Int = 1   //         -a 1         serverAddress – a slave address
                //val startAddress: Int = 1  //          -r #  ()          startAddress – reference address
                //val dataOnOff: Int = 1                //   data 1 or 0
                try {
                    rez = m.writeSingleRegister(serverAddres,startAddress,dataOnOff).toString()
                    /// or    writeSingleCoil()  true or false
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
            return rez.length == 0

    }else {return true}
    }

    fun onRelay(startAddress:Int):AnswerRelay{
        val relayOn = 1
        val rez:Boolean = connectionToPort(startAddress,relayOn)
        return AnswerRelay(startAddress,relayOn,rez)
    }

    fun offRelay(startAddress:Int):AnswerRelay{
        val relayOff = 0;
        val rez:Boolean =   connectionToPort(startAddress,relayOff)
        return AnswerRelay(startAddress,relayOff,rez)
    }
}
@kotlinx.serialization.Serializable
data class AnswerRelay(val relayNumber: Int, val stateRelay:Int, val errorRelay:Boolean)