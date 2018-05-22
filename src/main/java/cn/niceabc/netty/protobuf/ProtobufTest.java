package cn.niceabc.netty.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProtobufTest {
    private static Logger log = LoggerFactory.getLogger(ProtobufTest.class);

    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();

        builder.setSubReqID(1);
        builder.setUserName("Jack");
        builder.setProductName("Phone");

        List<String> address = new ArrayList<String>();
        address.add("Beijing");
        address.add("Nanjing");
        address.add("Shanghai");

        builder.addAllAddress(address);

        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();

        log.debug("before encode: {}", req.toString());

        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));

        log.debug("after decode: {}", req2.toString());

        log.debug("Assert equal: {}", req.equals(req2));
    }
}
