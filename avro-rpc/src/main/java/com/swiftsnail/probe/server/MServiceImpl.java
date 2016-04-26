package com.swiftsnail.probe.server;

import com.swiftsnail.avro.probe.model.Person;
import com.swiftsnail.avro.probe.model.QueryParameter;
import com.swiftsnail.avro.probe.service.MService;
import org.apache.avro.AvroRemoteException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaoxm on 2016/2/20 0020.
 */
public class MServiceImpl implements MService {
    @Override
    public CharSequence ping() throws AvroRemoteException {
        return "pong";
    }

    @Override
    public List<Person> getPersonList(QueryParameter queryParameter) throws AvroRemoteException {
        System.out.println(queryParameter.getAgeStart() + " to " + queryParameter.getAgeEnd());

        List<Person> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Person p = new Person();
            p.setAge(i);
            p.setChildrenCount(i);
            p.setName("name" + i);
            p.setSalary(1000D * i);
            p.setSex(true);
            list.add(p);
        }
        return list;
    }

}
