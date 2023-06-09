/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rud1.main;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rud1.modelo.Book;

/**
 *
 * @author maria
 */
public class BookFilesMain {

	private static final String BOOK_TAG = "book";
	private static final String BOOK_TITLE = "title";
	private static final String BOOK_AUTHOR_TAG = "author";
	private static final String BOOK_ID_ATT = "id";

	private static final int STRING_LENGTH = 30;

	private static final String BOOKS_INPUT_FILE = Paths.get("src", "rud1", "docs", "bookstore.xml").toString();
	private static final String BOOKS_OUTPUT_FILE = Paths.get("src", "rud1", "docs", "books.dat").toString();

	private static final int BOOK_RECORD_LENGTH = 4 + STRING_LENGTH * 2 * 2;

	public static void main(String[] args) {

		List<Book> books = fileToList(BOOKS_INPUT_FILE);
		mostrarBooks(books);
		listToFile(books);
		Book book = readBookInPosition(4);
		if (book != null) {
			System.out.println(book);
		}

	}

	private static void mostrarBooks(List<Book> books) {
		System.out.println("----------------------------");
		for (Book book : books) {
			System.out.println(book);
		}
		System.out.println("----------------------------");
	}

	private static ArrayList<Book> fileToList(String ruta) {
		String author = "", title = "";
		int id = -1;
		Book book = null;
		ArrayList<Book> booksList = new ArrayList<>();

		try {
			File inputFile = new File(ruta);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			// elimina hijos con texto vacío y fusiona en un único nodo de texto varios
			// adyacentes.
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName(BOOK_TAG);

			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					id = Integer.parseInt(eElement.getAttribute(BOOK_ID_ATT));
					title = eElement.getElementsByTagName(BOOK_TITLE).item(0).getTextContent();
					author = eElement.getElementsByTagName(BOOK_AUTHOR_TAG).item(0).getTextContent();

					book = new Book(author, title);
					book.setId(id);

					booksList.add(book);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Ha ocurrido una exception: " + e.getMessage());
		}
		return booksList;

	}

	private static void listToFile(List<Book> books) {

		try (RandomAccessFile raf = new RandomAccessFile(BOOKS_OUTPUT_FILE, "rw")) {

			for (Book book : books) {

				raf.writeInt(book.getId());

				StringBuffer sb = new StringBuffer(book.getTitle());
				sb.setLength(STRING_LENGTH);
				raf.writeChars(sb.toString());

				// eliminamos lo añadido hasta ahora
				// sb.delete(0, sb.length());
				sb.setLength(0);

				sb = new StringBuffer(book.getAuthor());
				sb.setLength(STRING_LENGTH);
				raf.writeChars(sb.toString());

			}

		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

	}

	private static Book readBookInPosition(int pos) {
		// tamaño registro 30*2*2+4 = 124
		// id 4 bytes
		// autor = STRING_LENGTH*2 BYTES
		// title = STRING_LENGTH*2 BYTES
		int id;
		String title = "", author = "";
		Book book = null;
		StringBuffer sb = new StringBuffer();

		try (RandomAccessFile raf = new RandomAccessFile(BOOKS_OUTPUT_FILE, "r")) {

			raf.seek(BOOK_RECORD_LENGTH * (pos - 1));

			id = raf.readInt();

			for (int i = 0; i < STRING_LENGTH; i++) {
				sb.append(raf.readChar());
			}
			title = sb.toString();

			sb = new StringBuffer();
			for (int i = 0; i < STRING_LENGTH; i++) {
				sb.append(raf.readChar());
			}
			author = sb.toString();

			book = new Book(author, title);
			book.setId(id);

		} catch (EOFException eof) {
			// TODO Auto-generated catch block
			eof.printStackTrace();
			System.out.println("Se ha alcanzado el final del fichero");
		}

		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return book;

	}

}
