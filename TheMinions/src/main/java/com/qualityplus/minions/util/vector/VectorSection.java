package com.qualityplus.minions.util.vector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@RequiredArgsConstructor
public final class VectorSection {
    private final Vector first;
    private final Vector second;
    private final Vector third;

    public VectorSection(Vector vector) {
        this.first = vector;
        this.second = null;
        this.third = null;
    }

    @NotNull
    public List<Vector> getFirsts() {
        return Collections.singletonList(first);
    }

    @NotNull
    public List<Vector> getSeconds() {
        return Arrays.asList(first, second);
    }

    @NotNull
    public List<Vector> getThirds() {
        return Arrays.asList(first, second, third);
    }
}
