package pl.ludek.poolserver.plugins

import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster
import com.ghgande.j2mod.modbus.net.AbstractSerialConnection
import com.ghgande.j2mod.modbus.util.BitVector
import com.ghgande.j2mod.modbus.util.SerialParameters
import jssc.SerialPortList

/* mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 1 -R /dev/ttyAMA1 1
   -v            Verbose mode.  Causes mbpoll to print debugging messages about  its progress.  This is helpful in debugging connection...
 -a #(1)          Slave address (1-255 for rtu, 0-255 for tcp, 1 is default) for reading, it is possible to give an address list
 -b #  (9600)        Baudrate (1200-921600, 19200 is default) +
  -m #  (rtu)        mode (rtu or tcp, TCP is default) +
 -t 0          Discrete output (coil) data type (binary 0 or 1)
  -P #  (none)        Parity (none, even, odd, even is default) +
 -r #  (1)  (first rely)      Start reference (1 is default) +
  -R            RS-485 mode (/RTS on (0) after sending)
   /dev/ttyAMA1             device
  1 or 0                  write values
 */
class SimpleModbusRTURelay {
    private val noError = 0
    private val error = 1

    //   fun must be a not private for  sam testing...
    private fun connectionToPort(startAddress: Int, dataOnOff: Boolean): Int {
        val dev_list = SerialPortList.getPortNames()
        if (dev_list.size != 0) {
            var master: ModbusSerialMaster? = null
            val untild = 1
            var portNumber = startAddress - 1
            try {
                master = getMaster()
                master.connect()
                master.writeCoil(untild, portNumber, dataOnOff)
            } catch (data: Exception) {
                println("No connection to serial port")
                return error
            } finally {
                if (master != null) {
                    master.disconnect()
                }
            }
            return noError
        } else {
            println("No have serial port")
            return error
        }
    }

    fun switchRelay(startAddress: Int, state: Boolean): AnswerRelay {
        val result = connectionToPort(startAddress, state)
        return AnswerRelay(startAddress, state, result)
    }

    private fun getStateRelay(): BitVector? {
        var rez: BitVector
        val dev_list = SerialPortList.getPortNames()
        if (dev_list.size != 0) {
            val ref = 0
            val countBit = 8
            var master: ModbusSerialMaster? = null
            try {
                master = getMaster()
                master.connect()
                rez = master.readCoils(ref, countBit)
            } catch (data: Exception) {
                println("No connection to serial port")
                return null
            } finally {
                if (master != null) {
                    master.disconnect()
                }
            }
            return rez
        } else {
            println("No have port")
            return null
        }
    }

    fun getStateAllRelay(): RelayState {
        val dataState = getStateRelay()
        if (dataState == null) {
            return RelayState(Array(8) { false }, error)
        } else {
            val array = Array(8) { false }
            for (i in array.indices) {
                array[i] = dataState.getBit(i)
            }
            return RelayState(array, noError)
        }
    }

    private fun getMaster(): ModbusSerialMaster {
        var serialParameters: SerialParameters? = null
        val deviceName = "/dev/ttyAMA1"
        val baudrate = 9600
        val dataBits = 8
        val stopBit = AbstractSerialConnection.ONE_STOP_BIT
        val encoding = com.ghgande.j2mod.modbus.Modbus.SERIAL_ENCODING_RTU
        val parity = AbstractSerialConnection.NO_PARITY
        val paramsEcho = false
        val rs485Mode = true
        //val flowControlIn = AbstractSerialConnection.FLOW_CONTROL_RTS_ENABLED
        //val flowControlOut = AbstractSerialConnection.FLOW_CONTROL_CTS_ENABLED
        //val openDelay = AbstractSerialConnection.OPEN_DELAY
        //val rs485TxEnableActiveHigh = true
        // val rs485DelayBeforeTxMicroseconds
        // val rs485DelayAfterTxMicroseconds
        serialParameters = SerialParameters()
        serialParameters.portName = deviceName
        serialParameters.baudRate = baudrate
        serialParameters.databits = dataBits
        serialParameters.stopbits = stopBit
        serialParameters.parity = parity
        serialParameters.encoding = encoding
        serialParameters.isEcho = paramsEcho
        serialParameters.rs485Mode = rs485Mode
        // serialParameters.flowControlOut = flowControlOut
        // serialParameters.flowControlIn = flowControlIn
        // serialParameters.flowControlOut = flowControlOut
        return ModbusSerialMaster(serialParameters)
    }
}

@kotlinx.serialization.Serializable
data class AnswerRelay(val relayNumber: Int, val stateRelay: Boolean, val errorCode: Int)

@kotlinx.serialization.Serializable
data class RelayState(val relayAnswer: Array<Boolean>, val errorCode: Int)