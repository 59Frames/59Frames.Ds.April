import concurrent.Promise;
import module.bootstrap.Bootstrap;
import module.sensorium.Arc;
import oshi.hardware.CentralProcessor;
import util.RandomUtil;
import util.ThreadService;

public class April {
    public static void main(String[] args) {
        for (int count = 0; count < 100; count++) {
            for (int threadCount = 0; threadCount < 20; threadCount++) {
                final var index = String.format("Series: %d | Thread: %d", count, threadCount);
                new Promise<>(() -> {
                    var n = RandomUtil.random();
                    for (int i = 0; i < RandomUtil.random(100, 50000); i++) {
                        n = Math.tan(n);
                    }
                    return n;
                }).then(res -> System.out.println(String.format("Result of %s is: %s", index, res)));
            }
        }

        ThreadService.shutdownAndAwaitTermination();
    }
}
