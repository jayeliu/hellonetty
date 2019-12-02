package seus;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class Test {
    public static void main(String[] args) {
        LinkedBlockingDeque<String> responses = new LinkedBlockingDeque<>();
        List<String> stringList=TxtRead.readTxtFileIntoStringArrList("url.txt");
        System.out.println("end reading");
        //Server server = new Server(responses);
        //int num=stringList.size()/20;
        int size=Integer.parseInt(args[1]);
        int task=Integer.parseInt(args[2]);
        int num=size/task;
        System.out.println(Instant.now());
        ExecutorService exec= Executors.newCachedThreadPool();
        for (int i=0;i<task;i++){
            if (i ==task-1) {
                //exec.execute(new Crawler(stringList.subList(i * num, stringList.size()),responses));
                exec.execute(new Crawler(stringList.subList(i * num, size),responses));
            } else {
                exec.execute(new Crawler(stringList.subList(i * num, (i + 1) * num),responses));
            }
        }
        exec.shutdown();
    }
}
