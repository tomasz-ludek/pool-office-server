package com.example.plugins

import com.intelligt.modbus.jlibmodbus.Modbus
import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory
import com.intelligt.modbus.jlibmodbus.msg.request.ReadHoldingRegistersRequest
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters
import com.intelligt.modbus.jlibmodbus.serial.SerialPort
import com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryJSSC
import com.intelligt.modbus.jlibmodbus.serial.SerialUtils
import com.intelligt.modbus.jlibmodbus.utils.DataUtils
import com.intelligt.modbus.jlibmodbus.utils.FrameEvent
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListener
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
    private fun connectionToPort(startAddress:Int, dataOnOff:Boolean ):String {

        val dev_list = SerialPortList.getPortNames()
        if (dev_list.size > 0) {
            var rez: String = ""



            try {
                Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG) //debug enable

                val sp = SerialParameters()

              // SerialUtils.trySelectConnector()


               // SerialUtils.createSerial(sp.)
                // jssc.SerialPort.FLOWCONTROL_RTSCTS_IN
              //  jssc.SerialPort.FLOWCONTROL_RTSCTS_OUT

                sp.device = "/dev/ttyAMA1"     //                       /dev/ttyAMA1     -   device   the name(path) of the serial port
                sp.setBaudRate(SerialPort.BaudRate.BAUD_RATE_9600)   //   -b 9600        - baud rate
                sp.dataBits = 8                          //0                             -  the number of data bits
                sp.parity = SerialPort.Parity.NONE //                     -P none        - parity check (NONE, EVEN, ODD, MARK, SPACE)
                sp.stopBits = 1                    //1                                   -  the number of stop bits(1,2)

                 SerialUtils.setSerialPortFactory(SerialPortFactoryJSSC())
                     jssc.SerialPort.FLOWCONTROL_RTSCTS_IN
                //  SerialUtils.setSerialPortFactory(SerialPortFactoryJSerialComm())
                 //SerialUtils.setSerialPortFactory(SerialPortFactoryRXTX())
                // SerialUtils.setSerialPortFactory(SerialPortFactoryPJC())
                //SerialUtils.setSerialPortFactory(SerialPortFactoryJavaComm())



               // SerialUtils.setSerialPortFactory(SerialPortFactoryJSerialComm(FLOW_CONTROL_CTS_ENABLE))



                // SerialUtils.setSerialPortFactory(SerialPortFactoryRXTX())
               val master = ModbusMasterFactory.createModbusMasterRTU(sp) //  -m rtu
               // val slave = ModbusSlaveFactory.createModbusSlaveRTU(sp)




                val listener: FrameEventListener = object : FrameEventListener {
                    override fun frameSentEvent(event: FrameEvent) {
                        System.out.println("frame sent " + DataUtils.toAscii(event.getBytes()))
                    }

                    override fun frameReceivedEvent(event: FrameEvent) {
                        System.out.println("frame recv " + DataUtils.toAscii(event.getBytes()))
                    }
                }


                master.addListener(listener)
                master.connect()

                val holdingRegisters:ModbusHoldingRegisters = ModbusHoldingRegisters(1000)

                for (i in 0 until holdingRegisters.quantity) {
                    //fill
                    holdingRegisters[i] = i + 1
                }
                holdingRegisters.setFloat64At(0, Math.PI);

                // slave.dataHolder.writeCoil()


               // val por =ModbusMasterFactory.createModbusMasterRTU(sp)

                val serverAddres: Int = 1   //         -a 1         serverAddress – a slave address
                //val startAddress: Int = 1  //          -r #  ()          startAddress – reference address
                //val dataOnOff: Int = 1                //   data 1 or 0
                try {
                   // m.writeSingleRegister(serverAddres,startAddress,dataOnOff)
                    master.writeSingleCoil(serverAddres,startAddress,dataOnOff)
                    /// or    writeSingleCoil()  true or false
                   // master.writeSingleRegister(serverAddres,startAddress,dataOnOff)
                } catch (e: RuntimeException) {
                    throw e
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        master.disconnect()
                    } catch (e1: ModbusIOException) {
                        e1.printStackTrace()
                    }
                }

            } catch (e: RuntimeException) {
                throw e
            } catch (e: Exception) {
                e.printStackTrace()
            }

             return rez
    }else {return "No have port"}
    }

    fun onRelay(startAddress:Int):String{
        val noPortStr = "No have port"
        val relayOn = true;
       val rez:String = connectionToPort(startAddress,relayOn)
        return if(rez == noPortStr){
            noPortStr
        }else{
            rez
        }
    }

    fun offRelay(startAddress:Int):String{
        val noPortStr = "No have port"
        val relayOff = false;
        val rez:String =   connectionToPort(startAddress,relayOff)
        return if(rez == noPortStr){
            noPortStr
        }else{
            rez
        }
    }
}