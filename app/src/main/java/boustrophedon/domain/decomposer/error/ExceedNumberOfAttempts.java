package boustrophedon.domain.decomposer.error;

public class ExceedNumberOfAttempts extends Exception{
    public ExceedNumberOfAttempts() {
            super("Split Exceeded the max number of Attempts");
        }
}
