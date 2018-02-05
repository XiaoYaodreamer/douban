package Model;

public class Book {
    //no
    private String no;
    //book name
    private String name;
    //book score
    private String score;
    //book comments
    private String comments;
    //book author
    private String author;
    //publishing house
    private String phouse;
    //publishing date
    private String date;
    //book price
    private String price;

    public Book(String no,String name,String score,String comments,String author,String phouse,String date,String price){
        this.no=no;
        this.name=name;
        this.score=score;
        this.comments=comments;
        this.author=author;
        this.phouse=phouse;
        this.date=date;
        this.price=price;
    }

    public void setNo(String no){
        this.no=no;
    }

    public String getNo(){
        return no;
    }

    public String getName(){
        return name;
    }

    public String getScore(){
        return score;
    }

    public String getComments(){
        return comments;
    }

    public String getAuthor(){
        return author;
    }

    public String getPhouse(){
        return phouse;
    }

    public String getDate(){
        return date;
    }

    public String getPrice(){
        return price;
    }



    public String toString(){
        return "there is no need to code it in this project.";
    }

}
