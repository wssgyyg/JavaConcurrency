package chap4;

import jdk.internal.util.xml.impl.Input;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleHttpServer {
    static ThreadPool<HttpRequestHandler> threadPool = new DefaultThreadPool<>(10);
    // SimpleHttpServer的根路径
    static String basePath;
    static ServerSocket serverSocket;
    //服务监听端口
    static int port = 8080;

    public static void main(String[] args) {
        SimpleHttpServer simpleHttpServer = new SimpleHttpServer();
        simpleHttpServer.setPort(8080);
        simpleHttpServer.setBasePath("/Users/bruce/Documents/GitHub/JavaConcurrency");
        try {
            simpleHttpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setPort(int port) {
        if (port > 0) {
            SimpleHttpServer.port = port;
        }
    }

    public static void setBasePath(String basePath) {
        if (basePath != null && new File(basePath).exists() && new File(basePath).isDirectory()) {
            SimpleHttpServer.basePath = basePath;
        }
    }

    //启动SimpleHttpServer
    public static void start() throws IOException {
        serverSocket = new ServerSocket(port);
        Socket socket = null;
        while ((socket = serverSocket.accept()) != null){
            System.out.println("one client connected");
            //接收一个客户端Socket，生成一个HttpRequestHandler, 放入线程池中执行
            threadPool.execute(new HttpRequestHandler(socket));
        }
        serverSocket.close();
    }


    static class HttpRequestHandler implements Runnable{

        private Socket socket;
        public HttpRequestHandler (Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            String line = null;
            BufferedReader br = null;
            BufferedReader reader = null;
            PrintWriter out = null;
            InputStream in = null;

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String header = reader.readLine();
                //由相对路径计算出绝对路径
                String filePath = basePath + header.split(" ")[1];
                System.out.println("filePath requested is: " + filePath);
                out = new PrintWriter(socket.getOutputStream());
                //如果请求资源的后缀为jpg或者ico,则读取资源并输出
                if (filePath.endsWith("jpg") || filePath.endsWith("ico")) {
                    File f = new File(filePath);
                    InputStream file = new FileInputStream(f);
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: image/jpeg");
                    out.println("Server: yyg");
                    out.println();
                    OutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
                    byte[] buffer = new byte[1024];
                    while (file.available() > 0){
                        outputStream.write(buffer, 0, file.read(buffer));
                    }
                    outputStream.flush();

                    /*in = new FileInputStream(filePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i = 0;
                    while ((i = in.read()) != -1) {
                        baos.write(i);
                    }
                    byte[] array = baos.toByteArray();
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Molly");
                    out.println();

                    socket.getOutputStream().write(array, 0, array.length);*/
                } else {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Molly");
                    out.println("Content-Type: text/html; charset=UTF-8");
                    out.println();
                    while ((line = br.readLine()) != null) {
                        out.println(line);
                    }

                }
                out.flush();
                System.out.println("here2");
            } catch (IOException e) {
                out.println("HTTP/1.1 500");
                out.println("");
                out.flush();
            } finally {
                System.out.println("here3");
                close(br, in, reader, out, socket);
            }
        }

    }

    private static void close(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    if (closeable != null) {
                        closeable.close();
                    }
                } catch (IOException e) {
                    System.out.println("Close error");
                }
            }
        }
    }
}
