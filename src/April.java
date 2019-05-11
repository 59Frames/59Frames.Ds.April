import data.DataAnalysis;
import model.parallel.Kernel;
import module.sensorium.physical.Arc;
import util.CollectionUtil;

public class April {
    public static void main(String[] args) throws Exception {
        // TODO: 28/04/2019 bootstrap
        // TODO: 28/04/2019 sensorium
        // TODO: 28/04/2019 speech
        // TODO: 28/04/2019 volition
        // TODO: 28/04/2019 knowledge
        // TODO: 28/04/2019 emotion
        // TODO: 28/04/2019 motorium

        // new DataAnalysis(CollectionUtil.createRandomDoubleArray(50, 1, 10, 1)).printAnalysis();
        final Kernel kernel = Kernel.load("kernels/matrixMultiplication.cl");

        System.out.println(kernel.getKernelName());

        for (var gpu : Arc.getGPUS()) {
            System.out.println("Name:           "+gpu.getName());
            System.out.println("Vendor:         "+gpu.getVendor());
            System.out.println("Driver Version: "+gpu.getDriverVersion());
            System.out.println("OpenCL Version: "+gpu.getOpenCLVersion());
            System.out.println("Platform:       "+gpu.getPlatform());
            System.out.println("Profile:        "+gpu.getProfile());
        }
    }
}
