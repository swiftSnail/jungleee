package com.swiftsnail.probe.serialize;

import com.swiftsnail.probe.model.Person;
import javassist.bytecode.ByteArray;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by yaoxm on 2016/3/3.
 */
public class SerializeSample {

    public static void main(String[] args) throws IOException {
        Person person = new Person(13,"yuao",false,2000000D,3);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DatumWriter<Person> writer = new SpecificDatumWriter<Person>();
        Encoder encoder = EncoderFactory.get().binaryEncoder(baos, null);
        writer.write(person, encoder);
        encoder.flush();
        baos.close();
    }

}
