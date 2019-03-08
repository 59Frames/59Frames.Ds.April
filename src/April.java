import module.knowledge.crawler.Spider;

public class April {
    public static void main(String[] args) {
        final var spider = new Spider();

        spider.search("https://www.bwdbern.ch/bwd/", "IMS");
    }
}
