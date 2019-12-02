package seus;

import scala.Predef;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.util.LinkedList;
import java.util.List;

public class GenDate {

    public static void main(String[] args) {
        try {
            FileOutputStream fileInputStream=new FileOutputStream("url.txt");
            GenDate.writeDate(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    static void writeDate(FileOutputStream fileInputStream) throws IOException {
        int days=0;
        String string="http://en.people.cn/review/";
        for (int y=2012;y<2020;y++){
            for (int m=1;m<13;m++){
                if(m==4|m==6|m==9|m==11){
                 days=30;
                }
                if(m==2){
                    days=28;
                }
                if(m==1|m==3|m==5|m==7|m==8|m==10|m==12){
                    days=31;
                }
                for (int d=1;d<=days;d++){
                    String s= String.format(string+y+"%02d%02d.html"+"\n",m,d );
                        fileInputStream.write(s.getBytes());
                }
            }
        }
    }
}
