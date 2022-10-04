import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import scala.concurrent.impl.CallbackRunnable;

public class KafkaStreamProducer {
	String message;

	public static void main(String[] args) {
		
		Properties properties =new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
		properties.put(ProducerConfig.CLIENT_ID_CONFIG,"client-producer-1");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
		KafkaProducer<String,String>kafkaProducer=new KafkaProducer<String,String>(properties);
		Random random=new Random();
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(()->{
		    String key=String.valueOf(random.nextInt(1000));
		    String value=String.valueOf(random.nextDouble()*999999);
		    kafkaProducer.send(new ProducerRecord<String,String>("test",key,value),(metadata,ex)->{
		       System.out.println("Sending message=>"+value+"Partition=>"+metadata.partition()
		               +"=>"+metadata.offset());
		    });
		},3000,3000,TimeUnit.MILLISECONDS);

}
}
