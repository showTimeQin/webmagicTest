import java.util.List;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * @author qjl
 * @Description
 * @date 2019-01-24 10:59
 */
@TargetUrl("http://my.oschina.net/flashsword/blog/\\d+")
public class OschinaBlog {

  @ExtractBy("//title")
  private String title;

  @ExtractBy(value = "div.BlogContent",type = ExtractBy.Type.Css)
  private String content;

  @ExtractBy(value = "//div[@class='BlogTags']/a/text()", multi = true)
  private List<String> tags;

  public static void main(String[] args) {
    OOSpider.create(
        Site.me(),
        new ConsolePageModelPipeline(), OschinaBlog.class).addUrl("http://my.oschina.net/flashsword/blog").run();
  }
}
