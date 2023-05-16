import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import java.util.Map;
import java.util.HashMap;

public class Test {

    public static void main(String[] args) throws Exception {
		System.out.println("Service running at http://localhost:8000/ ...");
		
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());
       // server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
			// открытие подкл. к MySQL.
			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","");
				Statement stmt = conn.createStatement();
				
				// разбор параметров и добавление строки.
				String qStr = t.getRequestURI().getQuery();
				System.out.println(qStr);
				Map<String, String> params = queryToMap(qStr);
				if (params != null)	rowInsert(stmt, params.get("col1"), params.get("col2"), params.get("col3"));
				
				// отображение страницы в браузере.
				t.sendResponseHeaders(200, 0);
				OutputStream os = t.getResponseBody();
				
				// чтение шаблона.
				File file = new File("select.html");
				FileReader fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);
				String line = reader.readLine();
				while (line != null) {
					if ((line.indexOf("@tr") == -1) && (line.indexOf("@ver") == -1)) os.write(line.getBytes());
					if (line.indexOf("@tr") != -1) os.write(viewSelect(stmt).getBytes());
					if (line.indexOf("@ver") != -1) os.write(viewVer(stmt).getBytes());
					
					line = reader.readLine();
				}
				os.close();
				
				conn.close();
			
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
        }
    }

	// разбор параметров GET.
	public static Map<String, String> queryToMap(String query) {
		if(query == null) {
			return null;
		}
		Map<String, String> result = new HashMap<>();
		for (String param : query.split("&")) {
			String[] entry = param.split("=");
			if (entry.length > 1) {
				result.put(entry[0], entry[1]);
			}else{
				result.put(entry[0], "");
			}
		}
		return result;
	}

	// получение таблицы с заголовком.
	public static String viewSelect(Statement stmt) {
		String line_all = "";
		try {

			ResultSet rs1 = stmt.executeQuery("SHOW COLUMNS FROM myarttable");
			line_all = line_all + "<tr>";
			while (rs1.next()) {
			   String head = rs1.getString(1);
			   System.out.printf("head: %s %n", head);
			   line_all = line_all + "<td>" + head + "</td>";
			}
			line_all = line_all + "</tr>";
			
			
            ResultSet rs = stmt.executeQuery("SELECT * FROM myarttable WHERE id>14 ORDER BY id DESC");

			while (rs.next()) {
			   int id = rs.getInt(1);
			   String text = rs.getString(2);
			   String description = rs.getString(3);
			   String keywords = rs.getString(4);
			   System.out.printf("id: %d, %s, %s, %s %n", id, text, description, keywords);
			   line_all = line_all + "<tr><td>" + id + "</td><td>" + text + "</td><td>" + description + "</td><td>" + keywords + "</td></tr>";
			}

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
		return line_all;
	}

	// получение версии БД.
	public static String viewVer(Statement stmt) {
		String line_ver = "";
		try {
            ResultSet rs = stmt.executeQuery("SELECT VERSION() AS ver");
			while (rs.next()) {
			   line_ver = rs.getString(1);
			}
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
		}
		return line_ver;
	}
	
	// добавление одной строки.
	public static void rowInsert(Statement stmt, String par1, String par2, String par3) {
		try {
			stmt.executeUpdate("INSERT INTO myarttable (text, description, keywords) VALUES ('"+par1+"', '"+par2+"', '"+par3+"')");
			System.out.println("A row added.");
		} catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
		}
	}
}