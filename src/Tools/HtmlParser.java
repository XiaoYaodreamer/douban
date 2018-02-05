package Tools;

import Model.Book;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlParser implements Runnable{


    private List<Book> books;
    private String url;
    public HtmlParser(List<Book> books) {
        this.books = books;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public void run() {
        try {
            parsePage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //get the data about the book
    //jsoup解析部分并未掌握，
    public void parsePage () throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity =  response.getEntity();
        String html= EntityUtils.toString(entity, "UTF-8");
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("ul.subject-list li.subject-item div.info");
        int j=1;
        for (Element element : elements) {
            System.out.println(j++);
            synchronized (HtmlParser.class) {
                //name
                String name = element.select("h2 a").attr("title");
                System.out.println(name);
                //phouse,price,and date
                String[] pub = element.select("div.pub").text().split("/");
                String price = pub[pub.length - 1];
                String date = pub[pub.length - 2];
                String phouse = pub[pub.length - 3];
                StringBuilder author = new StringBuilder();
                // author,the number of the author limted to 3
                int loop = 3;
                for (int i = 0; i < pub.length - loop; i++) {
                    author.append(pub[i]);
                }
                //score
                String score = element.select("div.star span.rating_nums").text();
                //comments
                String comments = element.select("div.star span.pl").text();
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(comments);
                comments = m.replaceAll("").trim();
                Book book = new Book("", name, score, comments, author.toString(), phouse, date, price);
                books.add(book);
                }
            }
        }




    }






