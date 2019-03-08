import module.knowledge.crawler.Spider;

public class April {
    public static void main(String[] args) {
        final var spider = new Spider();

        var matches = spider.search("https://www.bwdbern.ch/bwd/", "IMS");

        for (var match : matches) {
            System.out.println(match);
        }
    }
}
