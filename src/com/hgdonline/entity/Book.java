package com.hgdonline.entity;

import java.sql.Date;

public class Book {
	//�鱾����
	private String bookName;
	//�鱾���
	private int bookId;
	//������
	private String publishingCompany;
	//�������
	private Date borrowDate;
	//�Ƿ����ڽ��� 0��ʾ�ѻ���1��ʾ���ڽ���
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
