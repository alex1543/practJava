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
            t.sendResponseHeaders(200, 0);
            OutputStream os = t.getResponseBody();
			
			// чтение шаблона.
			File file = new File("select.html");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
				if ((line.indexOf("@tr") == -1) && (line.indexOf("@ver") == -1)) os.write(line.getBytes());
				if (line.indexOf("@tr") != -1) os.write(viewSelect().getBytes());
				if (line.indexOf("@ver") != -1) os.write(viewVer().getBytes());
				
                line = reader.readLine();
            }
            os.close();
        }
    }

	public static String viewSelect() {
		String line_all = "";

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","");
            Statement stmt = conn.createStatement();
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

			conn.close();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
		}
		return line_all;
	}

	public static String viewVer() {
		String line_ver = "";

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT VERSION() AS ver");

			while (rs.next()) {
			   line_ver = rs.getString(1);
			}

			conn.close();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
		}
		return line_ver;
	}
}