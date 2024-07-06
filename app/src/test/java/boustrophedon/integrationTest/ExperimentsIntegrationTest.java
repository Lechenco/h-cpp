package boustrophedon.integrationTest;

import org.junit.Test;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.graph.error.ElementNotFoundedException;
import boustrophedon.domain.walkers.error.AngleOffLimitsException;

public class ExperimentsIntegrationTest extends BaseIntegrationTest {

    @Test
    public void experiment1() throws ElementNotFoundedException, AngleOffLimitsException, ExceedNumberOfAttempts {
        ResultMetrics metrics = runNTimes("experiment1", 30, false);

        System.out.println("\nAverage: " + metrics);
    }
    @Test
    public void experiment1Special() throws ElementNotFoundedException, AngleOffLimitsException, ExceedNumberOfAttempts {
        ResultMetrics metrics = runNTimes("experiment1", 30, true);

        System.out.println("\nAverage: " + metrics);
    }

    @Test
    public void experiment1SpecialAreas() throws ElementNotFoundedException, AngleOffLimitsException, ExceedNumberOfAttempts {
        ResultMetrics metrics = runNTimes("experiment1-1", 30, false);

        System.out.println("\nAverage: " + metrics);
    }

    @Test
    public void experiment2() throws ElementNotFoundedException, AngleOffLimitsException, ExceedNumberOfAttempts {
        ResultMetrics metrics = runNTimes("experiment2", 30, false);

        System.out.println("\nAverage: " + metrics);
    }
    @Test
    public void experiment2Special() throws ElementNotFoundedException, AngleOffLimitsException, ExceedNumberOfAttempts {
        ResultMetrics metrics = runNTimes("experiment2", 30, true);

        System.out.println("\nAverage: " + metrics);
    }
    @Test
    public void experiment2SpecialAreas() throws ElementNotFoundedException, AngleOffLimitsException, ExceedNumberOfAttempts {
        ResultMetrics metrics = runNTimes("experiment2-1", 30, false);

        System.out.println("\nAverage: " + metrics);
    }

    @Test
    public void experiment3() throws ElementNotFoundedException, AngleOffLimitsException, ExceedNumberOfAttempts {
        ResultMetrics metrics = runNTimes("experiment3", 30, false);

        System.out.println("\nAverage: " + metrics);
    }
    @Test
    public void experiment3Special() throws ElementNotFoundedException, AngleOffLimitsException, ExceedNumberOfAttempts {
        ResultMetrics metrics = runNTimes("experiment3", 30, true);

        System.out.println("\nAverage: " + metrics);
    }
    @Test
    public void experiment3SpecialAreas() throws ElementNotFoundedException, AngleOffLimitsException, ExceedNumberOfAttempts {
        ResultMetrics metrics = runNTimes("experiment3-1", 30, false);

        System.out.println("\nAverage: " + metrics);
    }
}
