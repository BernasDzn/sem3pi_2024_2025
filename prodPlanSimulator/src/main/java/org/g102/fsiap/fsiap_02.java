package org.g102.fsiap;

import com.fazecast.jSerialComm.*;

import java.io.IOException;

import java.util.Timer;

public class fsiap_02{

    public static void main(String[] args) throws IOException {
        long timeStart = System.currentTimeMillis();
        SerialPort serialPort = SerialPort.getCommPorts()[0];

        serialPort.setComPortParameters(9600, Byte.SIZE, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        boolean hasOpened = serialPort.openPort();

        if(!hasOpened){
            throw new IllegalStateException("Failed to open port.");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {serialPort.closePort();}));

        var timer = new Timer();
        var timedSchedule = new TimerScheduleHandler(timeStart);

        serialPort.addDataListener(timedSchedule);
        timer.schedule(timedSchedule, 0, 1000);

    }
}