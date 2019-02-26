import java.util.List;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author qjl
 * @Description
 * @date 2019-01-24 10:45
 */
public class OschinaBlogPageProcesser implements PageProcessor {

  /**
   * 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    */
  private Site site = Site.me().setDomain("my.oschina.net").setRetryTimes(3).setSleepTime(1000);

  /**
   * process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
   * @param page
   */
  public void process(Page page) {
    List<String> links = page.getHtml().links()
        .regex("http://my\\.oschina\\.net/flashsword/blog/\\d+").all();
    page.addTargetRequests(links);
    page.putField("title",
        page.getHtml().xpath("//div[@class='BlogEntity']/div[@class='BlogTitle']/h1").toString());
    page.putField("content", page.getHtml().$("div.content").toString());
    page.putField("tags", page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
  }

  public Site getSite() {
    return site;

  }

  public static void main(String[] args) {
    Spider.create(new OschinaBlogPageProcesser()).addUrl("http://my.oschina.net/flashsword/blog")
        .addPipeline(new ConsolePipeline()).run();
  }
}
