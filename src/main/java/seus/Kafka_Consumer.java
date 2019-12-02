package seus;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
/**
 * @author jae-liu
 */
public class Kafka_Consumer implements Runnable {

    private final KafkaConsumer consumer;
    private static final String GROUPID = "groupA";

    private Kafka_Consumer(String topicName) {
        //properties information
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", GROUPID);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer(props);
        this.consumer.subscribe(Collections.singletonList(topicName));
    }

    @Override
    public void run() {
        int messageNo = 1;
        System.out.println("---------开始消费---------");
        try {
            for (;;) {
                ConsumerRecords<String, String> msgList = consumer.poll(10000);
                if(null!= msgList && msgList.count()>0){
                    for (ConsumerRecord<String, String> record : msgList) {
                            System.out.println(record.value());
                    }
                }else{
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
    public static void main(String[] args) {
        Kafka_Consumer test1 = new Kafka_Consumer("test");
        Thread thread1 = new Thread(test1);
        thread1.start();
    }
}
