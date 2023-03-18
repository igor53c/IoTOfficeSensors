
public class Temperature implements MeasureValue {
    private final int value;

    public Temperature(int value, Scale scale) {
        if (scale == Scale.CELSIUS) {
            this.value = value;
        } else if (scale == Scale.FAHRENHEIT) {
            this.value = (int) ((value - 32) * 5.0 / 9.0);
        } else if (scale == Scale.KELVIN) {
            this.value = value - 273;
        } else {
            throw new IllegalArgumentException("Unknown scale: " + scale);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "°C";
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Temperature)) {
            return false;
        }
        return this.value == ((Temperature) other).getValue();
    }

    enum Scale {
        CELSIUS,
        FAHRENHEIT,
        KELVIN
    }
}