package rud1.modelo;

public class Book {

	private Integer id;
	private String author;
	
	private String title;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Book(String author, String title) {
		super();
		this.author = author;
		this.title = title;
	}
	@Override
	public String toString() {
		return "Book [id=" + id + ", author=" + author + ", title=" + title + "]";
	}
	
}
