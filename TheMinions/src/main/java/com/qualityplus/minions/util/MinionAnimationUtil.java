package com.qualityplus.minions.util;

import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.minions.util.vector.VectorSection;
import lombok.experimental.UtilityClass;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class MinionAnimationUtil {
    private static final List<Vector> AXIS_FIRST = List.of(
            new Vector(0, 0, 1),
            new Vector(1, 0, 1),
            new Vector(1, 0, 0),
            new Vector(1, 0, -1),
            new Vector(0, 0, -1),
            new Vector(-1, 0, 1),
            new Vector(-1, 0, 0),
            new Vector(-1, 0, -1)
        );
    private static final List<Vector> AXIS_SEC = List.of(
            new Vector(0, 0, 2),
            new Vector(1, 0, 2),
            new Vector(2, 0, 0),
            new Vector(2, 0, 1),
            new Vector(2, 0, 2),
            new Vector(2, 0, -2),
            new Vector(2, 0, -1),
            new Vector(1, 0, -2),
            new Vector(0, 0, -2),
            new Vector(-1, 0, -2),
            new Vector(-2, 0, -1),
            new Vector(-2, 0, 0),
            new Vector(-2, 0, 1),
            new Vector(-1, 0, 2),
            new Vector(-2, 0, 2),
            new Vector(-2, 0, -2)
    );
    private static final List<Vector> AXIS_THIRD = List.of(
            new Vector(0, 0, 3),
            new Vector(1, 0, 3),
            new Vector(2, 0, 3),
            new Vector(3, 0, 1),
            new Vector(3, 0, 2),
            new Vector(3, 0, 3),
            new Vector(3, 0, 0),
            new Vector(3, 0, -1),
            new Vector(3, 0, -2),
            new Vector(3, 0, -3),
            new Vector(2, 0, -3),
            new Vector(1, 0, -3),
            new Vector(0, 0, -3),
            new Vector(-1, 0, -3),
            new Vector(-2, 0, -3),
            new Vector(-3, 0, -1),
            new Vector(-3, 0, -2),
            new Vector(-3, 0, -3),
            new Vector(-3, 0, 1),
            new Vector(-3, 0, 2),
            new Vector(-3, 0, 3),
            new Vector(-2, 0, 3),
            new Vector(-1, 0, 3),
            new Vector(-3, 0, 0)
    );

    static {
        SUGAR_WATER_POSITIONS = FastMap.builder(Integer.class, VectorSection.class)
                .put(1, new VectorSection(new Vector(0, 0, 2)))
                .put(3, new VectorSection(new Vector(2, 0, 1)))
                .put(7, new VectorSection(new Vector(2, 0, -1)))
                .put(9, new VectorSection(new Vector(0, 0, -2)))
                .put(11, new VectorSection(new Vector(-2, 0, -1)))
                .put(15, new VectorSection(new Vector(-2, 0, 1)))

                .build();
    }
    public EulerAngle[] rightArmUp = new EulerAngle[] {
            new EulerAngle(0.03D, 0.05D, 0.1D), new EulerAngle(0.05D, 0.1D, 0.2D), new EulerAngle(0.1D, 0.15D, 0.3D), new EulerAngle(0.12D, 0.2D, 0.4D), new EulerAngle(0.13D, 0.26D, 0.5D), new EulerAngle(0.15D, 0.27D, 0.6D), new EulerAngle(0.18D, 0.28D, 0.7D), new EulerAngle(0.2D, 0.29D, 0.9D), new EulerAngle(0.22D, 0.3D, 1.0D), new EulerAngle(0.23D, 0.31D, 1.1D),
            new EulerAngle(0.24D, 0.32D, 1.3D), new EulerAngle(0.25D, 0.34D, 1.5D), new EulerAngle(0.26D, 0.37D, 1.6D), new EulerAngle(0.27D, 0.4D, 1.8D), new EulerAngle(0.29D, 0.46D, 1.9D), new EulerAngle(0.3D, 0.5D, 2.1D), new EulerAngle(0.3D, 0.52D, 2.15D), new EulerAngle(0.31D, 0.57D, 2.2D), new EulerAngle(0.32D, 0.6D, 2.23D), new EulerAngle(0.34D, 0.57D, 2.25D) };

    public EulerAngle[] leftArmUp = new EulerAngle[] {
            new EulerAngle(0.03D, 0.05D, 6.28D), new EulerAngle(0.05D, 0.1D, 6.2D), new EulerAngle(0.1D, 0.15D, 6.1D), new EulerAngle(0.12D, 0.2D, 6.0D), new EulerAngle(0.13D, 0.26D, 5.9D), new EulerAngle(0.15D, 0.27D, 5.8D), new EulerAngle(0.18D, 0.28D, 5.5D), new EulerAngle(0.2D, 0.29D, 5.4D), new EulerAngle(0.22D, 0.3D, 5.3D), new EulerAngle(0.23D, 0.31D, 5.2D),
            new EulerAngle(0.24D, 0.32D, 5.1D), new EulerAngle(0.25D, 0.34D, 5.0D), new EulerAngle(0.26D, 0.37D, 4.9D), new EulerAngle(0.27D, 0.4D, 4.8D), new EulerAngle(0.29D, 0.46D, 4.7D), new EulerAngle(0.3D, 0.5D, 4.6D), new EulerAngle(0.3D, 0.52D, 4.55D), new EulerAngle(0.31D, 0.57D, 4.5D), new EulerAngle(0.32D, 0.6D, 4.45D), new EulerAngle(0.34D, 0.57D, 4.4D) };

    public EulerAngle[] moveHeadBack = new EulerAngle[] {
            new EulerAngle(6.28D, 0.0D, 0.0D), new EulerAngle(6.25D, 0.02D, 0.01D), new EulerAngle(6.2D, 0.04D, 0.02D), new EulerAngle(6.15D, 0.06D, 0.03D), new EulerAngle(6.1D, 0.08D, 0.04D), new EulerAngle(6.05D, 0.1D, 0.05D), new EulerAngle(6.0D, 0.12D, 0.06D), new EulerAngle(5.95D, 0.14D, 0.07D), new EulerAngle(5.9D, 0.16D, 0.08D), new EulerAngle(5.85D, 0.18D, 0.09D),
            new EulerAngle(5.8D, 0.2D, 0.1D), new EulerAngle(5.75D, 0.22D, 0.11D), new EulerAngle(5.7D, 0.24D, 0.12D), new EulerAngle(5.6D, 0.26D, 0.13D), new EulerAngle(5.5D, 0.3D, 0.14D), new EulerAngle(5.4D, 0.32D, 0.15D), new EulerAngle(5.3D, 0.34D, 0.16D), new EulerAngle(5.28D, 0.34D, 0.18D), new EulerAngle(5.25D, 0.34D, 0.2D), new EulerAngle(5.25D, 0.34D, 0.2D) };

    public EulerAngle[] rightHandFastPickaxeMovementNew = new EulerAngle[] {
            new EulerAngle(5.6D, 0.0D, 0.0D), new EulerAngle(5.3D, 0.0D, 0.0D), new EulerAngle(5D, 0.0D, 0.0D), new EulerAngle(4.7D, 0.0D, 0.0D), new EulerAngle(4.4D, 0.0D, 0.0D), new EulerAngle(4.1D, 0.0D, 0.0D)
    };

    public EulerAngle[] rightHandFastPickaxeMovementNew2 = new EulerAngle[] {
            new EulerAngle(4.1D, 0.0D, 0.0D), new EulerAngle(4.4D, 0.0D, 0.0D), new EulerAngle(4.7D, 0.0D, 0.0D), new EulerAngle(5D, 0.0D, 0.0D), new EulerAngle(5.3D, 0.0D, 0.0D), new EulerAngle(5.6D, 0.0D, 0.0D)
    };

    public static final Map<Integer, VectorSection> SUGAR_WATER_POSITIONS;

    public List<Vector> getThree() {
        List<Vector> vectors = getSecond();
        vectors.addAll(AXIS_THIRD);
        return vectors;
    }

    public List<Vector> getSecond() {
        final List<Vector> vectors = new ArrayList<>();
        vectors.addAll(AXIS_FIRST);
        vectors.addAll(AXIS_SEC);
        return vectors;
    }

    public List<Vector> getFirst() {
        return AXIS_FIRST;
    }
}

