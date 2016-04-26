package com.swiftsnail.probe.serialize;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import outfox.ead.data.SdkAdSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * SerializeSample Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>三月 3, 2016</pre>
 */
public class SerializeSampleTest {

    Schema schema = null;

    @Before
    public void before() throws Exception {
        Schema.Parser parser = new Schema.Parser();
        schema = parser.parse(getClass().getResourceAsStream("/SdkAdSlot.avsc"));
    }

    @After
    public void after() throws Exception {
    }


    @Test
    public void testGenericMapping() throws Exception {
        GenericRecord datum = new GenericData.Record(schema);
        datum.put("slotId", "sdafasdfas");
        datum.put("appId", "asdfasd");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(schema);

        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        writer.write(datum, encoder);
        encoder.flush();
        out.close();

        DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(schema);
        Decoder decoder = DecoderFactory.get().binaryDecoder(out.toByteArray(), null);
        GenericRecord result = reader.read(null, decoder);

        System.out.println(result.toString());
    }


    @Test
    public void testSprecificMapping() throws Exception {
        SdkAdSlot slot = new SdkAdSlot();
        slot.setSlotId("aaa");
        slot.setAppId("bbb");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DatumWriter<SdkAdSlot> writer = new SpecificDatumWriter<SdkAdSlot>(SdkAdSlot.class);
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        writer.write(slot, encoder);
        encoder.flush();
        out.close();

        DatumReader<SdkAdSlot> reader = new SpecificDatumReader<SdkAdSlot>(SdkAdSlot.class);
        Decoder decoder = DecoderFactory.get().binaryDecoder(out.toByteArray(), null);
        slot = reader.read(null, decoder);

        System.out.println(slot.toString());
    }

} 
