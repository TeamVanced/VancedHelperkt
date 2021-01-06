package commands.fun;
import org.apache.commons.math3.distribution.NormalDistribution;
public class IQLogic {
    public static int calcIQ() {
        NormalDistribution iqDist = new NormalDistribution(100, 15);
        int randIQ = (int) (Math.random() * 200);
        double luck = Math.random();
        boolean smallo = iqDist.cumulativeProbability(randIQ) < 0.5;
        if ((smallo && iqDist.cumulativeProbability(randIQ) > luck) || (!smallo && iqDist.cumulativeProbability(randIQ) < luck)) {
            return randIQ;
        }
        return calcIQ();
    }

}
