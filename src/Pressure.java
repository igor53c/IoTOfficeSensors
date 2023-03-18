
public class Pressure implements MeasureValue {
    private final int value;

    public Pressure(int value, Scale scale) {
        if (scale == Scale.PASCAL) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("Unknown scale: " + scale);
        }
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Pressure)) {
            return false;
        }
        return this.value == ((Pressure) other).getValue();
    }

    @Override
    public String toString() {
        return value + " Pa";
    }

    @Override
    public int getValue() {
        return this.value;
    }

    enum Scale {
        PASCAL
    }
}
