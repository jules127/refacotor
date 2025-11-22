package theater;

public class AbstractPerformanceCaculator {
    private final Performance performance;
    private final Play play;

    public AbstractPerformanceCaculator(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public Performance getPerformance() {
        return performance;
    }

    public Play getPlay() {
        return play;
    }

    /**
     *
     * @param performance
     * @param play
     * @return
     */
    public static AbstractPerformanceCaculator createPerformanceCaculator(Performance performance, Play play) {
        return null;

    }
}
