package com.hgdonline.net;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hgdonline.entity.Book;
import com.hgdonline.entity.Message;

public class ConnectNet {
	
	private static final ConnectNet conn = new ConnectNet();
	
	//ip����
	public static final int IP_ERROR = 403;
	//����
	public static final int ERROR = -1;
	//�ɹ�
	public static final int OK = 1;
	//ʧ��
	public static final int FAILED = 0;
	//û���������
	public static final int NO_SUCH_DATA = 2;
	
	private static HttpClient httpClient = null;
	
	private ConnectNet(){
	}
	
	public static ConnectNet getConnect(){
		return conn;
	}
	
	/**
	 * 
	 * @return 1.��ʾ�û������������-1����ʾip����0����ʾ��½�ɹ�
	 * @throws IOException
	 */
	
	public Message login(String userName, String password,boolean isSuperUser) throws IOException{
		//write something here
		Message message = new Message();
		message.setInfo("��ʼ������Ϣ��");
		String url;
		if(isSuperUser){
			url = "http://book.hgdonline.net/index.php/Api/Index/loginAdmin.html?username=" + userName + "&password=" + password;
		}else{
			url = "http://book.hgdonline.net/index.php/Api/Index/login.html?stu_id=" + userName + "&password="+password;
		}
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = getHttpClient().execute(httpGet);
		//��ʾip������ʾ�û��ڰ칫�ҵ�½
		if(response.getStatusLine().getStatusCode() == 403){
			message.setStatus(IP_ERROR);
			message.setInfo("ip��ַ����ȷ�����ڰ칫��wifi�µ�½��");
		}else if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			try{
				JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity(), "utf-8"));
//				String info = json.getString("info");
//				message.setInfo(info);
				int status = json.getInt("status");
				if(200 == status){
					message.setStatus(OK);
				}else if(404 == status){
					message.setStatus(FAILED);
					message.setInfo("�û������������");
				}else{
					message.setInfo("δ֪����");
					message.setStatus(ERROR);
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
		}else{
			message.setStatus(ERROR);
			message.setInfo("��½����");
		}
		//���ٽ���httpGet���ص���Ϣ
		httpGet.abort();
		return message;
	}
	
	
	//����
	public Message borrowBooks(String userName, String pass, String bookId, boolean isSuperUser) throws IOException{
//		bookId = "TP312";
		Message message = new Message();
		message.setStatus(ERROR);
		message.setInfo("��ʼ������Ϣ");
		if(login(userName, pass, isSuperUser).getStatus() == OK){
			String url = "http://book.hgdonline.net/index.php/Api/Admin/borrowBook?stu_id="+ userName+"&bookID="+bookId;
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = getHttpClient().execute(httpGet);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				try{
					JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity(), "utf-8"));
					int status = json.getInt("status");
					String info = json.getString("info");
					message.setInfo(info);
					if(1 == status){
						message.setStatus(OK);
					}else if(-1 == status){
						message.setStatus(FAILED);
					}
				}catch(JSONException e){
					e.printStackTrace();
				}
			}else{
				message.setInfo("�������Ӵ���");
			}
			//���ٽ���httpGet���ص���Ϣ
			httpGet.abort();
		}else{
			message.setInfo("��½����");
		}
		return message;
	}
	
	// ����
	public Message returnBooks(String userName, String pass, String bookId,boolean isSuperUser)
			throws IOException {
		Message message = new Message();
		message.setStatus(ERROR);
		if (login(userName, pass, isSuperUser).getStatus() == OK) {
			String url = "http://book.hgdonline.net/index.php/Api/Admin/returnBook?stu_id="
					+ userName + "&bookID=" + bookId;
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = getHttpClient().execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				try {
					JSONObject json = new JSONObject(EntityUtils.toString(
							response.getEntity(), "utf-8"));
					int status = json.getInt("status");
					String info = json.getString("info");
					message.setInfo(info);
					if (1 == status) {
						message.setStatus(OK);
					} else if (-1 == status) {
						message.setStatus(FAILED);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			// ���ٽ���httpGet���ص���Ϣ
			httpGet.abort();
		}else{
			message.setInfo("��½����");
		}
		return message;
	}
	
	//��ѯ�����¼
	public List<Book> getBorrowedBooks(){
		return null;
	}
	
	//��ѯ��ǰ����
	public List<Book> getBorrowBooks(String userName, String pass, boolean isSuperUser, int status) throws IOException{
		ArrayList<Book> books  = new ArrayList<Book>();
		if (login(userName, pass, isSuperUser).getStatus() == OK) {
			String url;
			if(1 == status){
				url = "http://book.hgdonline.net/index.php/Api/Admin/borrowStatus?stu_id=" + userName;
			}else{
				url = "http://book.hgdonline.net/index.php/Api/Admin/borrowHistory?stu_id=" + userName;
			}
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = getHttpClient().execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				try {
					JSONArray array = new JSONArray(EntityUtils.toString(response.getEntity(), "utf-8"));
					int length = array.length();
					Book book = null;
					JSONObject obj = null;
					for(int i = 0;i < length;i++){
						obj = array.getJSONObject(i);
						book = new Book();
						book.setBookName(obj.getString("name"));
						book.setBookId(obj.getString("isbn"));
						book.setSearchId(obj.getString("bookID"));
						book.setBorrowDate(stringToDate(obj.getString("bor_time")));
						book.setIsBorrowing(status);
						books.add(book);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return null;
				}
			}
			// ���ٽ���httpGet���ص���Ϣ
			httpGet.abort();
		}else{
			return null;
		}
		return books;
	}
	
	public Message addBooks(String userName, String password, Book book) throws IOException {
		Message message = new Message();
		message.setInfo("��ʼ��������Ϣ");
		String from = "";
		String bookId = "";
		if(book.getSearchId() != null){
			bookId = book.getSearchId();
		}
		if(book.getDonator() != null){
			from = book.getDonator();
		}
		if(login(userName, password, true).getStatus() == OK){
			String url = "http://book.hgdonline.net/index.php/Api/Admin/addBook?stu_id="+userName
					+ "&isbn="+book.getBookId()+"&bookID="+bookId+"&from="+from;
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = getHttpClient().execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				try {
					JSONObject json = new JSONObject(EntityUtils.toString(
							response.getEntity(), "utf-8"));
					int status = json.getInt("status");
					String info = json.getString("info");
					message.setInfo(info);
					if (1 == status) {
						message.setStatus(OK);
					} else if (0 == status) {
						message.setStatus(FAILED);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			// ���ٽ���httpGet���ص���Ϣ
			httpGet.abort();
		}else{
			message.setStatus(ERROR);
			message.setInfo("��½����");
		}
//		message.setInfo("�ɹ��ˣ��Ҳ�");
		return message;
	}
	
	
	
	 private static synchronized HttpClient getHttpClient() {
	        if (httpClient == null) {
	            final HttpParams httpParams = new BasicHttpParams();
	            HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
	            HttpConnectionParams.setSoTimeout(httpParams, 8000);
	            httpClient = new DefaultHttpClient(httpParams);
	        }
	        return httpClient;
	    }
	
	private  Date stringToDate(String str) {  
		     Date date = null;  
		     date = java.sql.Date.valueOf(str);  
		     return date; 
		   }

	
	
	
	

}
