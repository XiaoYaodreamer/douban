package Tools;

import Model.Book;

import java.util.Comparator;
//比较器
public class BookComparator implements Comparator<Book>{
    @Override
    public int compare(Book b1,Book b2) {

        String scoreStr1 = b1.getScore();
        String scoreStr2 = b2.getScore();
        if (scoreStr1 == null || "".equals(scoreStr1)) {
            scoreStr1 = "0";
        }
        if (scoreStr2 == null || "".equals(scoreStr2)) {
            scoreStr2 = "0";
        }
        double score1 = Double.parseDouble(scoreStr1);
        double score2 = Double.parseDouble(scoreStr2);
        return Double.compare(score2,score1);
    }
    }



