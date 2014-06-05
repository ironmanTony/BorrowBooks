package com.hgdonline.entity;

import java.sql.Date;

public class Book {
	//书本名称
	private String bookName;
	//书本编号
	private int bookId;
	//出版社
	private String publishingCompany;
	//借出日期
	private Date borrowDate;
	//是否正在借阅 0表示已还，1表示正在借阅
	private int isBorrowing;
	
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getPublishingCompany() {
		return publishingCompany;
	}
	public void setPublishingCompany(String publishingCompany) {
		this.publishingCompany = publishingCompany;
	}
	public Date getBorrowDate() {
		return borrowDate;
	}
	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}
	public int getIsBorrowing() {
		return isBorrowing;
	}
	public void setIsBorrowing(int isBorrowing) {
		this.isBorrowing = isBorrowing;
	}
	
	

}
