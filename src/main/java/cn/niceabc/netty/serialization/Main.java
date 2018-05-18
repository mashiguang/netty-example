package cn.niceabc.netty.serialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        User obj = new User();
        obj.setId(1L);
        obj.setName("Jack");
        obj.setAge(18);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(bout);

        oout.writeObject(obj);
        oout.flush();
        oout.close();

        byte[] arr = bout.toByteArray();
        log.debug("byte[].length={}", arr.length);
        log.debug("byte[]={}", arr);

        bout.close();
    }
}
