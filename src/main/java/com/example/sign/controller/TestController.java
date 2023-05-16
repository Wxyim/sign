package com.example.sign.controller;

import com.example.sign.config.SftpConfig;
import com.example.sign.util.SFTP;
import com.example.sign.util.SFTPUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("put")
    public String putFile() throws Exception{

        SFTP sftp = SftpConfig.sftpObjectPool.borrowObject();
//        System.out.println(sftp == null);
//        System.out.println(sftp.toString());
//        SFTPUtil.doUpload(sftp, "upload/retain", "C:\\_2023-03-10");
        SFTPUtil.upload("upload/retain", "C:\\2.txt");


        SftpConfig.sftpObjectPool.returnObject(sftp);
//        System.out.println(sftp == null);
//        System.out.println(sftp.toString());
        return "Ok!";
    }

    @GetMapping("get")
    public String getFile() throws Exception{

        SFTP sftp = SftpConfig.sftpObjectPool.borrowObject();
//        System.out.println(sftp == null);
//        System.out.println(sftp.toString());
//        SFTPUtil.doUpload(sftp, "upload/retain", "C:\\_2023-03-10");
//        SFTPUtil.upload("upload/retain", "C:\\Finish.log");

        SFTPUtil.getStream(sftp);


        System.out.println(SftpConfig.sftpObjectPool.getNumActive());
        System.out.println(SftpConfig.sftpObjectPool.getNumIdle());
//        System.out.println(sftp == null);
//        System.out.println(sftp.toString());
        return "Ok!";
    }

    @GetMapping("test")
    public String test() throws Exception{
        SFTP sftp = SftpConfig.sftpObjectPool.borrowObject();
        SFTPUtil.disConn(sftp.getSession(), sftp.getChannel(), sftp.getSftp());

        SftpConfig.sftpObjectPool.returnObject(sftp);

        System.out.println(SftpConfig.sftpObjectPool.getNumActive());

        System.out.println(SftpConfig.sftpObjectPool.getNumIdle());
        return "Ok";
    }

    @GetMapping("query")
    public String query() throws Exception{
        SFTP sftp = SftpConfig.sftpObjectPool.borrowObject();
        SFTPUtil.query(sftp);

        SftpConfig.sftpObjectPool.returnObject(sftp);

        System.out.println(SftpConfig.sftpObjectPool.getNumActive());

        System.out.println(SftpConfig.sftpObjectPool.getNumIdle());
        return "Ok";
    }

}
