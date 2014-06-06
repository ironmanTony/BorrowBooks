package com.hgdonline.net;

import java.util.ArrayList;
import java.util.List;

import com.hgdonline.entity.Book;

public class ConnectNet {
	
	private String userName;
	private String password;
	
	public ConnectNet(String userName, String pass){
		//do something
		this.password = pass;
		this.userName = userName;
	}
	
	public boolean login(){
		//write something here
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	//get book list 
	public List<Book> getBooks(){
		ArrayList<Book> books = new ArrayList<Book>();
		for(int i = 0;i<10;i++){
			Book book = new Book();
			book.setBookName("book"+i);
			books.add(book);
		}
		return books;
	}
	
	
	
	

}
