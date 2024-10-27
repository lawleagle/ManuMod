package network.manu.manumod.manulib;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class V2 {
    public double x;
    public double y;

    // for use in texture code
    // (u, v) sintactic pair mapped to (x, y)
    public double getU() {
        return x;
    }
    public V2 setU(double u) {
        x = u;
        return this;
    }
    public double getV() {
        return y;
    }
    public V2 setV(double v) {
        y = v;
        return this;
    }

    public V2 plus(V2 other) {
        return new V2(this.x + other.x, this.y + other.y);
    }

    public V2 minus(V2 other) {
        return new V2(this.x - other.x, this.y - other.y);
    }

    public V2 copy() {
        return new V2(this.x, this.y);
    }
}
