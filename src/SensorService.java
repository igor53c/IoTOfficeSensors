public class SensorService {
    static public byte[] toBytes(SensorAction sensorAction) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) 0xFF; // Command byte

        switch (sensorAction) {
            case START -> bytes[1] = 0x01;
            case STOP -> bytes[1] = 0x02;
            case RESET -> bytes[1] = 0x03;
            default -> throw new IllegalArgumentException("Unknown sensor action: " + sensorAction);
        }

        return bytes;
    }

    static public MeasureValue fromBytes(byte[] bytes) {
        // check message type
        if (bytes[0] == 0x00 && bytes[1] == 0x01) { // Temperature
            return decodeTemperature(bytes);
        } else if (bytes[0] == 0x00 && bytes[1] == 0x02) { // Pressure (2 bytes)
            return decodePressure(bytes, 2);
        } else if (bytes[0] == 0x01 && bytes[1] == 0x02) { // Pressure (4 bytes)
            return decodePressure(bytes, 4);
        } else {
            throw new IllegalArgumentException("Unknown message type");
        }
    }

    private static Temperature decodeTemperature(byte[] bytes) {
        boolean isPositive = (bytes[2] & 0x80) == 0;
        int temperature = bytes[2] & 0x7F;
        if (!isPositive) {
            temperature = -((~temperature & 0x7F) + 1);
        }
        return new Temperature(temperature, Temperature.Scale.CELSIUS);
    }

    private static Pressure decodePressure(byte[] bytes, int length) {
        int pressure = 0;
        for (int i = 0; i < length; i++) {
            pressure = (pressure << 8) + (bytes[i + 2] & 0xFF);
        }
        return new Pressure(pressure, Pressure.Scale.PASCAL);
    }
}