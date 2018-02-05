package Tools;

import Model.Book;

import java.util.Comparator;

public class BookComparator implements Comparator<Book>{
    @Override
    public int compare(Book b1,Book b2) {

        return Integer.valueOf(b1.getScore())-Integer.valueOf(b2.getScore());

    }


}
