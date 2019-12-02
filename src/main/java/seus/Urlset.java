package seus;


import org.apache.kafka.common.protocol.types.Field;
import scala.sys.process.ProcessBuilderImpl;
import sun.applet.Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Urlset {
    public static Set<String> urls=new HashSet<>();
    public static int count=0;
    public static void getUrls(List<String> urlStrings) throws IOException, InterruptedException {
        for (String urlString : urlStrings) {
            String html = MyHttpClient.getHtml(urlString);
            if (html!=null){
                System.out.println(count++);
            urls.addAll(Objects.requireNonNull(MyHttpClient.getUrl(html)));
            Thread.sleep(1000);
            }
        }
    }
    public static List<String> getList(Set<String> set){
        return new ArrayList<>(set);
    }

    public static void main(String[] args){
        ArrayList<String> tmp=new ArrayList<>();
        tmp.add("http://en.people.cn/review/20191111.html");
        try {
            getUrls(tmp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int n=1;
        try {
            FileOutputStream fileInputStream=new FileOutputStream("./url.txt");
        while (n-->0){
            try {
                urls.remove("http://english.cntv.cn");
                for (String string:urls){
                    fileInputStream.write((string+"\n").getBytes());
                }
                getUrls(getList(urls));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(urls.size());
    }
}
