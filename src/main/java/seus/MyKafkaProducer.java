package seus;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;

class MyKafkaProducer{
    private final KafkaProducer<String ,String > producer;
    private final String topic;
    MyKafkaProducer(String topicName){
        //properties kafka producer
        Properties props = new Properties();
        ////bootstrapping proxy list.connect to port.Default port is 9092.
        props.put("bootstrap.servers", "localhost:9092");
        //
        props.put("acks", "all");
        //retry
        props.put("retries", 0);
        //buffer size
        props.put("batch.size", 16384);
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        this.producer = new KafkaProducer<String, String>(props);
        //config topic
        this.topic = topicName;
    }
    public void sendMsg(String string){
        if(string!=null){
            producer.send(new ProducerRecord<>(topic, string));
        }
    }
}
