import java.util.List;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class GithubRepoPageProcessor implements PageProcessor {

  // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
  private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
  private static int count = 0;


  public Site getSite() {
    return site;
  }

  public void process(Page page) {
    //判断链接是否符合http://www.cnblogs.com/任意个数字字母-/p/7个数字.html格式
    List<String> links = page.getHtml().links()
        .regex("https://www.cnblogs.com/[a-z A-Z 0-9 -]*/p/[0-9]*.html").all();
    page.addTargetRequests(links);
    String title = page.getHtml().xpath("//*[@id=\"topics\"]/div/h1[@class='postTitle']/a/text()")
        .get();
    if(StringUtils.isEmpty(title)){
      page.setSkip(true);
    }
    page.putField("题目", title);
    page.putField("内容", page.getHtml().xpath("//*[@id=\"topics\"]/div/div[@class='postBody']/div[@id='cnblogs_post_body']/p/text()").toString());
    count++;
  }

  public static void main(String[] args) {
    long startTime, endTime;
    System.out.println("开始爬取...");
    startTime = System.currentTimeMillis();
    Spider.create(new GithubRepoPageProcessor()).addUrl("https://www.cnblogs.com/").thread(5).run();
    endTime = System.currentTimeMillis();
    System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒，抓取了" + count + "条记录");
  }

}
