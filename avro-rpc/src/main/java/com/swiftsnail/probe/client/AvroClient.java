package com.swiftsnail.probe.client;

import com.swiftsnail.avro.probe.model.Person;
import com.swiftsnail.avro.probe.model.QueryParameter;
import com.swiftsnail.avro.probe.service.MService;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by yaoxm on 2016/2/20 0020.
 */
public class AvroClient {

    public static void main(String[] args) throws IOException {
        NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(8888));

        MService proxy = SpecificRequestor.getClient(MService.class, client);
        System.out.println(proxy.ping());

        QueryParameter queryParameter = new QueryParameter();
        queryParameter.setAgeStart(1);
        queryParameter.setAgeEnd(100);
        List<Person> personList = proxy.getPersonList(queryParameter);
        for (Person p : personList) {
            System.out.println(p.getName());
        }
    }
}
