import Model.Book;
import Tools.BookComparator;
import Tools.ExcelMaker;
import Tools.HtmlParser;

import com.sun.deploy.net.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException, InterruptedException {
        //先获取总页数，建立相应的线程
        //页面的url为https://book.douban.com/tag/%E7%BC%96%E7%A8%8B?start="页码数-1*20"&type=T
        //https://book.douban.com/tag/%E7%BC%96%E7%A8%8B
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://book.douban.com/tag/%E7%BC%96%E7%A8%8B");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity =  response.getEntity();
        String html= EntityUtils.toString(entity, "UTF-8");

        Document doc=Jsoup.parse(html);
        int totalPage = Integer.parseInt(doc.select("div.paginator > a").last().text());
        StringBuilder sb;
        List<Book> books = new Vector<>();
        for (int i = 0; i < totalPage; i++) {
            HtmlParser hp = new HtmlParser(books);
            sb = new StringBuilder("https://book.douban.com/tag/编程?start=");
            sb.append(i * 20).append("&type=T");
            hp.setUrl(sb.toString());
            executorService.execute(hp);
            Thread.sleep(1000L);
            }
        executorService.shutdown();
        Collections.sort(books, new BookComparator());
        System.out.println(books.size());
        List<Book> fortyBooks = new ArrayList<>();
        int no = 1;
        for (Book book : books) {
            //保留评价人数超过1000的记录
            //前40条
            if(Integer.parseInt(book.getComments()) > 1000 && no <= 40){
                book.setNo(no + "");
                fortyBooks.add(book);
                no++;
            }
        }
        System.out.println(fortyBooks.size());

        //导出Excel
        Map<String, String> title = new LinkedHashMap<>();
        title.put("no", "序号");
        title.put("name", "书名");
        title.put("score", "评分");
        title.put("comments", "评价人数");
        title.put("author", "作者");
        title.put("phouse", "出版社");
        title.put("date", "出版日期");
        title.put("price", "价格");
        String sheet = "豆瓣编程书籍排行";
        ExcelMaker.export(fortyBooks, title, sheet);
    }
}
