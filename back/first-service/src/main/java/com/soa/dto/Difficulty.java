package com.soa.dto;

import com.soa.exception.IncreaseNotAvailableException;

public enum Difficulty {
    NORMAL(4, 0),
    VERY_HARD(3, 1),
    IMPOSSIBLE(2, 2),
    INSANE(1, 3),
    TERRIBLE(0, 4);

    private final int maxAvailableIncrease;
    private final int currentDifficulty;

    Difficulty(int maxAvailableIncrease, int currentDifficulty) {
        this.maxAvailableIncrease = maxAvailableIncrease;
        this.currentDifficulty = currentDifficulty;
    }

    public int getMaxAvailableIncrease() {
        return maxAvailableIncrease;
    }

    public int getCurrentDifficulty() {
        return currentDifficulty;
    }

    public static Difficulty getDifficulty(Integer difficulty) {
        for (Difficulty d : Difficulty.values()) {
            if (difficulty.equals(d.getCurrentDifficulty())) {
                return d;
            }
        }
        throw new IncreaseNotAvailableException(0);
    }
}
