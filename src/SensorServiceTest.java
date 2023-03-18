import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SensorServiceTest {

    @Test
    public void testToBytesStart() {
        SensorAction action = SensorAction.START;
        byte[] expectedBytes = { (byte) 0xFF, 0x01 };
        byte[] actualBytes = SensorService.toBytes(action);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testToBytesStop() {
        SensorAction action = SensorAction.STOP;
        byte[] expectedBytes = { (byte) 0xFF, 0x02 };
        byte[] actualBytes = SensorService.toBytes(action);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testToBytesReset() {
        SensorAction action = SensorAction.RESET;
        byte[] expectedBytes = { (byte) 0xFF, 0x03 };
        byte[] actualBytes = SensorService.toBytes(action);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testFromBytes() {
        // Temperature with positive value
        byte[] tempData = { 0x00, 0x01, 0x23 };
        Temperature expectedTemp = new Temperature(35, Temperature.Scale.CELSIUS);
        MeasureValue actualTemp = SensorService.fromBytes(tempData);
        assertEquals(expectedTemp, actualTemp);

        // Temperature with negative value
        byte[] negTempData = { 0x00, 0x01, (byte) 0x81 };
        Temperature expectedNegTemp = new Temperature(-127, Temperature.Scale.CELSIUS);
        MeasureValue actualNegTemp = SensorService.fromBytes(negTempData);
        assertEquals(expectedNegTemp, actualNegTemp);

        // Pressure with 2-byte payload
        byte[] pressData2 = { 0x00, 0x02, (byte) 0x87, 0x65 };
        Pressure expectedPress2 = new Pressure(34661, Pressure.Scale.PASCAL);
        MeasureValue actualPress2 = SensorService.fromBytes(pressData2);
        assertEquals(expectedPress2, actualPress2);

        // Pressure with 4-byte payload
        byte[] pressData4 = { 0x01, 0x02, 0x00, 0x00, (byte) 0xFC, 0x65 };
        Pressure expectedPress4 = new Pressure(64613, Pressure.Scale.PASCAL);
        MeasureValue actualPress4 = SensorService.fromBytes(pressData4);
        assertEquals(expectedPress4, actualPress4);

        // Unknown message type
        byte[] unknownData = { 0x00, 0x03 };
        assertThrows(IllegalArgumentException.class, () -> SensorService.fromBytes(unknownData));

        // Invalid pressure payload length
        byte[] invalidLengthData = { 0x00, 0x02, (byte) 0x87 };
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> SensorService.fromBytes(invalidLengthData));
    }
}
