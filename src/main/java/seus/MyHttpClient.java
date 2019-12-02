package seus;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


public class MyHttpClient {
    public static Set<String> filterUrl(String inputString){
        String www="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        Set<String> urls=new HashSet<>();
        java.util.regex.Pattern p_url;
        java.util.regex.Matcher m_url;
            String regEx_script = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
            p_url = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_url = p_url.matcher(inputString);
        while (m_url.find()) {
            urls.add(m_url.group());
        }
        urls.remove(www);
        return urls;
    }
    public static String Html2Text(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {System.err.println("Html2Text: " + e.getMessage()); }
        //剔除空格行
        textStr=textStr.replaceAll("[ ]+", " ");
        textStr=textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        return textStr;// 返回文本字符串
    }
    static String getHtml(String url) throws IOException {
        //create HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //create request
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.36");
        //send request & receive response
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //judge the status code
        if (response.getStatusLine().getStatusCode() == 200) {
            if (response.getEntity() != null) {
                //System.out.println(text);
                //FileUtils.writeStringToFile(new File("./my.html"), text,"utf-8");
                //Document document= Jsoup.parse(EntityUtils.toString(response.getEntity(), "UTF-8"));
                //Elements elements=document.getElementsByClass("p1_content");
                //StringBuilder content = new StringBuilder();
                /*for (Element e:elements){
                    content.append(e.text()).append("\n");
                    //System.out.println(e.text());
                }*/
                //return content.toString();
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        }
        return null;
    }
    static String getText(String html){
        if (html!=null){
            return Html2Text(html);
        }
        else {
            return null;
        }
    }
    static Set<String> getUrl(String html){
        if (html!=null){
            return filterUrl(html);
        }
        else {
            return null;
        }
    }
}
