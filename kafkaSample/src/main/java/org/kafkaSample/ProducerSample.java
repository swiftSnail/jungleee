package org.kafkaSample;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * Hello world!
 */
public class ProducerSample {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("metadata.broker.list","61.135.217.81:9092");
        //properties.put("zk.connect", "myservice..myservice.com:2188");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("key.serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        
        ProducerConfig config = new ProducerConfig(properties);
        Producer<String, String> producer = new Producer<String, String>(config);
        
        for (int i = 0; i < 100; i++) {
            KeyedMessage<String, String> message = new KeyedMessage<String, String>("java","aaa" + i);
            producer.send(message);
        }
        
        producer.close();
    }
}
