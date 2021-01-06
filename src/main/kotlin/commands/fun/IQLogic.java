package commands.fun;
import org.apache.commons.math3.distribution.NormalDistribution;
public class IQLogic {
    public static int calcIQ() {
        NormalDistribution iqDist = new NormalDistribution(100, 15);
        int randIQ = (int) (Math.random() * 200);
        double luck = Math.random();
        if (iqDist.cumulativeProbability(randIQ) > luck) {
            return randIQ;
        }
        return calcIQ();
    }

}
