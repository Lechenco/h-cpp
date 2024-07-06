package boustrophedon.integrationTest;

import androidx.annotation.NonNull;

public class ResultMetrics {
    double totalDuration;
    double decomposeDuration;
    double objectiveDuration;
    double cellOrderDuration;
    double walkAllDuration;

    double totalDurationStd;
    double decomposeDurationStd;
    double objectiveDurationStd;
    double cellOrderDurationStd;
    double walkAllDurationStd;

    double pathLength;
    static long startTime;

    public ResultMetrics() {
    }

    public void calcTotalDuration() {
        this.totalDuration = this. decomposeDuration +
                this.objectiveDuration +
                this.cellOrderDuration +
                this.walkAllDuration;
    }

    public static void startTimer() {
        startTime = System.nanoTime();
    }
    public static double endTimer() {
        return (System.nanoTime() - startTime) / 1000000.0; // ms
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(
                "ResultMetrics( %f; %f; %f; %f; %f; %f; %f; %f; %f; %f; %f )",
                pathLength,
                decomposeDuration, decomposeDurationStd,
                objectiveDuration, objectiveDurationStd,
                cellOrderDuration, cellOrderDurationStd,
                walkAllDuration, walkAllDurationStd,
                totalDuration, totalDurationStd
                );
    }
}
