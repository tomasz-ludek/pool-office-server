//package com.example.plugins;
//
//import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
//import com.ghgande.j2mod.modbus.msg.ReadInputRegistersResponse;
//import com.ghgande.j2mod.modbus.net.SerialConnection;
//import com.ghgande.j2mod.modbus.util.SerialParameters;
//import com.intelligt.modbus.jlibmodbus.msg.request.ReadHoldingRegistersRequest;
//import com.intelligt.modbus.jlibmodbus.msg.response.ReadHoldingRegistersResponse;
//
//
//public class SimpleModbusRTURelaySecond {
//
//    public void test() {
//        SerialConnection con = null; // the connection
//        ModbusSerialTransaction trans = null; // the transaction
//        ReadHoldingRegistersRequest req = null;
//        ReadHoldingRegistersResponse res = null;
//        String portname = null; // the name of the serial port to be used
//        int unitid = 0; // the unit identifier we will be talking to
//        int ref = 0; // the reference, where to start reading from
//        int count = 0; // the count of IR's to read
//        int repeat = 1; // a loop for repeating the transaction
//        try {
//            portname = "/dev/ttyAMA1";
//            System.setProperty("gnu.io.rxtx.SerialPorts", portname);
//            unitid = 2;
//            ref = 4;
//            count = 4;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            System.exit(1);
//        }
//        // 2. Set master identifier
//        //  ModbusCoupler.createModbusCoupler(null);
//        //ModbusCoupler.getReference().setUnitID(1);
//        // 3. Setup serial parameters
//        SerialParameters params = new SerialParameters();
//        params.setPortName(portname);
//        params.setBaudRate(9600);
//        params.setDatabits(8);
//        params.setParity("None");
//        params.setStopbits(1);
//        params.setEncoding("rtu");
//        params.setEcho(false);
//        // 4. Open the connection
//        con = new SerialConnection(params);
//       // con.open();
//        // 5. Prepare a request
//      //  req = new ReadHoldingRegistersRequest(unitid, ref, count);
//        //req = new ReadInputRegistersRequest(ref, count);
//        //req.setUnitID(unitid);
//        //req.setHeadless();
//        // 6. Prepare a transaction
//        trans = new ModbusSerialTransaction(con);
//       // trans.setRequest(req);
//        int k = 0;
//     //   do {
//       //     trans.execute();
//         //   res = (ReadInputRegistersResponse) trans.getResponse();
//         //   //res = (ReadHoldingRegistersResponse) trans.getResponse();
//          //  for (int n = 0; n < res.getWordCount(); n++) {
//           //     System.out.println("Word " + n + "=" + res.getRegisterValue(n));
//         //   }
//            k++;
//       // } while (k < repeat);
//        // 8. Close the connection
//        con.close();
//    }
//}
