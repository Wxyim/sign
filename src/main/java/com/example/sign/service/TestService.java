package com.example.sign.service;

import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class TestService {

    public static void main(String[] args) throws Exception{
        String s = "apiCode=app-btkey/v1/ev/vehicleMachine/queryFlowrate&appId=202069311861295711&body={\"dpid\":\"276DC0F5239DC89B7EABD073EE1A304CD290D56B\",\"vin\":\"\"}&randomstr=FCkeMW4paIikXmDgtVrVEAZFPJ5aFNWd&timestamp=20230404094833113&transationid=20206931186129571120230404094833yFLDV9XN&secretKey=c2Th7Jp8Kw8Ly9Px5Hl8Kn1Uu6Kq1Bf0b2Iz4Es8Vi9Za9Mv9Md5Qo8Gg7Ji1Zz2";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(s.getBytes(StandardCharsets.UTF_8));
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String sign = base64Encoder.encode(md.digest()).toUpperCase();
        System.out.println(sign);
        System.out.println(sign.equals("FVUIQJ4/SCSMV3VEPW3SIVTAW/EIFEBQIBOIYSIWUH4="));
    }


}
