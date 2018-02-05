package Tools;

import Model.Book;
import org.htmlparser.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlParser implements Runnable{


    private List<Book> books;
    private String url;
    public HtmlParser(ArrayList<Book> books) {
        this.books = books;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public void run() {
        try {
            parsePage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //get the data about the book
    //jsoup解析部分并未掌握，
    public void parsePage (String url) throws IOException {
            String htmlPage = Jsoup.connect(url).get().toString();
            Document doc = Jsoup.parse(htmlPage);
            Elements elements = doc.select("ul.subject-list li.subject-item div.info");
            for (Element element : elements) {
                synchronized (HtmlParser.class) {
                    //name
                    String name = element.select("h2 a").attr("title");
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


    public static void main(String[] args) {
        ArrayList<Book> books=new ArrayList<>();
        HtmlParser hp = new HtmlParser(books);
        try {
            hp.parsePage("https://book.douban.com/tag/%E7%BC%96%E7%A8%8B?start=0&type=T");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(books.size());
    }

    }






