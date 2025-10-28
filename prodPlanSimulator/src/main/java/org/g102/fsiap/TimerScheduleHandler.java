package org.g102.fsiap;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.g102.domain.Time;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TimerTask;

public class TimerScheduleHandler extends TimerTask implements SerialPortDataListener {
    private final long timeStart;
    private final int time = 900;
    private File file;
    private FileWriter writer;
    private StringBuilder sb;

    public TimerScheduleHandler (long timeStart) throws IOException {
        this.timeStart = timeStart;
        file = new File("prodPlanSimulator/src/main/resources/fsiap.txt");
        writer = new FileWriter("prodPlanSimulator/src/main/resources/fsiap.txt");
        sb = new StringBuilder();
    }

    @Override
    public void run(){
        if ((System.currentTimeMillis() - timeStart) / 1000 == time + 1) {
            System.exit(0);
        }
        Time time = new Time((int) (System.currentTimeMillis() - timeStart) / 1000);
        System.out.println(time);
        sb.append(time + "\n");
    }


    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.getEventType() == SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
            byte data[] = serialPortEvent.getReceivedData();

            for (byte dataChar : data) {
                    System.out.printf("%c", dataChar);
                    sb.append((char) dataChar);
            }
            if((System.currentTimeMillis() - timeStart) / 1000 == time){
                try {
                    writer.write(sb.toString());
                    writer.close();
                    wait(1000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
