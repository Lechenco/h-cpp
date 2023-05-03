package boustrophedon.domain.backAndForth.error;

public class AngleOffLimitsException extends Exception{
    public AngleOffLimitsException() {
        super("The Angle must stay between - PI/2 and PI/2");
    }
}
