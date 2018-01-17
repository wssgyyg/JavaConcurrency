package chap4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Test {

    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader br = null;

        try {
            socket = new Socket("127.0.0.1", 8080);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null){
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                br.close();
            } catch (IOException e) {

            }

        }
    }

}
