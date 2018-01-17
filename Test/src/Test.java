import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Test {
    public static void main(String[] args) {
        try {
            ServerSocket ss=new ServerSocket(8888);

            while(true){
                Socket socket=ss.accept();
                BufferedReader bd=new BufferedReader(new InputStreamReader(socket.getInputStream()));

                /**
                 * 接受HTTP请求
                 */
                String requestHeader;
                int contentLength=0;
                while((requestHeader=bd.readLine())!=null&&!requestHeader.isEmpty()){
                    System.out.println(requestHeader);
                    /**
                     * 获得GET参数
                     */
                    if(requestHeader.startsWith("GET")){
                        int begin = requestHeader.indexOf("/?")+2;
                        int end = requestHeader.indexOf("HTTP/");
                        String condition=requestHeader.substring(begin, end);
                        System.out.println("GET参数是："+condition);
                    }

                }
                StringBuffer sb=new StringBuffer();

                PrintWriter pw=new PrintWriter(socket.getOutputStream());

                pw.println("HTTP/1.1 200 OK");
                pw.println("Content-type:text/html");
                pw.println();

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/bruce/Documents/GitHub/JavaConcurrency/test.html")));
                String line = null;
                while ((line = br.readLine()) != null) {
                    pw.println(line);
                }
                pw.println("<h1>访问成功！</h1>");

                pw.flush();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}