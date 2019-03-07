import module.bootstrap.Bootstrap;
import module.sensorium.Arc;
import oshi.hardware.CentralProcessor;

public class April {
    public static void main(String[] args) {
        Bootstrap.load();
        CentralProcessor cpu = Arc.getCPU();
        System.out.println(cpu.getName());
    }
}
