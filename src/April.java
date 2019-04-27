import module.bootstrap.Bootstrap;

import static util.Toolbox.*;

public class April {

    public static void main(String[] args) throws Exception {

        double[] arr = new double[10];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = random();
        }

        System.out.println(average(arr));

        System.out.println(mph2kmh(180));

        new Bootstrap().boot();
    }
}
