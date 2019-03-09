import module.knowledge.crawler.WebSpider;

public class April {
    public static void main(String[] args) {
        final var spider = new WebSpider();

        var matches = spider.search("how old is ryan reynolds");

        for (var match : matches) {
            System.out.println(match);
        }
    }
}
