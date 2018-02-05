import Model.Book;
import Tools.BookComparator;
import Tools.ExcelMaker;
import Tools.HtmlParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    public static void main(String[] args) throws IOException {
        //先获取总页数，建立相应的线程
        //页面的url为https://book.douban.com/tag/%E7%BC%96%E7%A8%8B?start="页码数-1*20"&type=T
        String html = Jsoup.connect("https://book.douban.com/tag/%E7%BC%96%E7%A8%8B").get().toString();
        Document doc = Jsoup.parse(html);
        int totalPage = Integer.parseInt(doc.select("div.paginator > a").last().text());
        StringBuilder sb;
        ArrayList<Book> books = new ArrayList<>();
        for (int i = 0; i < totalPage; i++) {
            HtmlParser hp = new HtmlParser(books);
            sb = new StringBuilder("https://book.douban.com/tag/编程?start=");
            sb.append(i * 20).append("&type=T");
            hp.setUrl(sb.toString());
            executorService.execute(hp);

        }
        executorService.shutdown();
        Collections.sort(books, new BookComparator());

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

        //导出Excel
        Map<String, String> title = new LinkedHashMap<>();
        title.put("id", "序号");
        title.put("name", "书名");
        title.put("score", "评分");
        title.put("num", "评价人数");
        title.put("author", "作者");
        title.put("press", "出版社");
        title.put("date", "出版日期");
        title.put("price", "价格");
        String sheet = "豆瓣编程书籍排行";
        ExcelMaker.export(fortyBooks, title, sheet);
    }
}
