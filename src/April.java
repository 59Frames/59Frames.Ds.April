import static util.Kryptonite.decrypt;
import static util.Kryptonite.encrypt;
import static util.Validator.isOddNumber;

public class April {
    public static void main(String[] args) throws Exception {
        // TODO: 28/04/2019 bootstrap
        // TODO: 28/04/2019 sensorium
        // TODO: 28/04/2019 speech
        // TODO: 28/04/2019 volition
        // TODO: 28/04/2019 knowledge
        // TODO: 28/04/2019 emotion
        // TODO: 28/04/2019 motorium


        var cipher = encrypt("test", 16);
        System.out.println(cipher);
        System.out.println(cipher.length());
        System.out.println(decrypt(cipher, 16));
    }
}
