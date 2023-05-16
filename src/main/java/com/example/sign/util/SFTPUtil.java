package com.example.sign.util;

import com.example.sign.config.SftpConfig;
import com.jcraft.jsch.*;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SFTPUtil {

    public static void getConnect(SFTP s) throws Exception {
        /** 密钥的密码  */
//      String privateKey ="key";
//        /** 密钥文件路径  */
//      String passphrase ="path";
        /** 主机 */
        String host ="192.168.239.100";
        /** 端口 */
        int port =22;
        /** 用户名 */
        String username ="sftpuser";
        /** 密码 */
        String password ="111111";
        Session session = null;
        Channel channel = null;
        ChannelSftp sftp = null;// sftp操作类
        JSch jsch = new JSch();

        //设置密钥和密码
        //支持密钥的方式登陆，只需在jsch.getSession之前设置一下密钥的相关信息
//      if (privateKey != null && !"".equals(privateKey)) {
//             if (passphrase != null && "".equals(passphrase)) {
//              //设置带口令的密钥
//                 jsch.addIdentity(privateKey, passphrase);
//             } else {
//              //设置不带口令的密钥
//                 jsch.addIdentity(privateKey);
//             }
//      }
        session = jsch.getSession(username, host, port);
        session.setPassword(password);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no"); // 不验证 HostKey
        session.setConfig(config);
        try {
            session.connect();
        } catch (Exception e) {
            if (session.isConnected())
                session.disconnect();
        }
        channel = session.openChannel("sftp");
        try {
            channel.connect();
        } catch (Exception e) {
            if (channel.isConnected())
                channel.disconnect();
        }
        sftp = (ChannelSftp) channel;
        s.setChannel(channel);
        s.setSession(session);
        s.setSftp(sftp);
    }
    /*断开连接*/
    public static void disConn(Session session,Channel channel,ChannelSftp sftp)throws Exception{
        if(null != sftp){
            System.out.println(1);
            sftp.disconnect();
            sftp.exit();
            sftp = null;
        }
        if(null != channel){
            System.out.println(2);
            channel.disconnect();
            channel = null;
        }
        if(null != session){
            System.out.println(3);
            session.disconnect();
            session = null;
        }

        System.out.println(SftpConfig.sftpObjectPool.getNumActive());

        System.out.println(SftpConfig.sftpObjectPool.getNumIdle());
    }

    /**
     * 上传文件
     * @param directory 上传的目录-相对于SFPT设置的用户访问目录，
     * 为空则在SFTP设置的根目录进行创建文件（除设置了服务器全磁盘访问）
     * @param uploadFile 要上传的文件全路径
     */
    public static void upload(String directory,String uploadFile) throws Exception {
        SFTP s=new SFTP();
        getConnect(s);//建立连接
        Session session = s.getSession();
        Channel channel = s.getChannel();
        ChannelSftp sftp = s.getSftp();// sftp操作类
        try {
            try{
                sftp.cd(directory); //进入目录
            }catch(SftpException sException){
                if(sftp.SSH_FX_NO_SUCH_FILE == sException.id){ //指定上传路径不存在
                    sftp.mkdir(directory);//创建目录
                    sftp.cd(directory);  //进入目录
                }
            }
            File file = new File(uploadFile);
            InputStream in= new FileInputStream(file);
            sftp.put(in, file.getName());
            in.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage(),e);
        } finally {
            disConn(session,channel,sftp);
        }
    }

    public static void doUpload(SFTP sftpObject, String directory,String uploadFile) throws Exception {
        Session session = sftpObject.getSession();
        Channel channel = sftpObject.getChannel();
        ChannelSftp sftp = sftpObject.getSftp();// sftp操作类
        try {
            try{
                sftp.cd(directory); //进入目录
            }catch(SftpException sException){
                if(sftp.SSH_FX_NO_SUCH_FILE == sException.id){ //指定上传路径不存在
                    sftp.mkdir(directory);//创建目录
                    sftp.cd(directory);  //进入目录
                }
            }
            File file = new File(uploadFile);
            InputStream in= new FileInputStream(file);
            sftp.put(in, file.getName());
            in.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage(),e);
        } finally {
            disConn(session,channel,sftp);
        }
    }

    public static void getStream(SFTP sftpObject) throws Exception {
        try {


            Session session = sftpObject.getSession();
            Channel channel = sftpObject.getChannel();
            ChannelSftp sftp = sftpObject.getSftp();

            System.out.println(sftp.isConnected());
            System.out.println(sftp.isClosed());
            System.out.println(sftp.isEOF());


            System.out.println(SftpConfig.sftpObjectPool.getNumActive());

            System.out.println(SftpConfig.sftpObjectPool.getNumIdle());

            String pwd = sftp.pwd();
            System.out.println(pwd);
            InputStream inputStream = sftp.get("upload/2.txt");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

//        long count = bufferedReader.lines().count();
//        System.out.println(count);
            List<String> collect = bufferedReader.lines().filter(StringUtils::hasText).filter(item -> !item.equals("4")).collect(Collectors.toList());

            System.out.println(collect.toString());

            System.out.println(SftpConfig.sftpObjectPool.getNumActive());

            System.out.println(SftpConfig.sftpObjectPool.getNumIdle());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            SftpConfig.sftpObjectPool.returnObject(sftpObject);
            System.out.println(sftpObject.getSftp().isConnected());
            System.out.println(sftpObject.getSftp().isClosed());
            System.out.println(sftpObject.getSftp().isEOF());
        }
    }

    public static List<String> query(SFTP sftp) throws Exception{
        List<String> strings = new ArrayList<>();
        ChannelSftp sftp1 = sftp.getSftp();
        sftp1.cd("/upload");
        String pwd = sftp1.pwd();
        System.out.println(pwd);
        Vector<ChannelSftp.LsEntry> ls = sftp1.ls("/upload");
        for (ChannelSftp.LsEntry next : ls) {
            String filename = next.getFilename();
            strings.add(filename);
            System.out.println(filename);
        }
        for(String a : strings){
            SftpATTRS lstat = sftp1.lstat("/upload" + "/" + a);
            if(!lstat.isDir()){
                System.out.println(123);
                System.out.println(a);
                long mTime = lstat.getMTime()*1000L;
            }
        }
//        sftp1.rm("/upload/1.txt");//没文件会抛异常 no such file
//        sftp1.rename("/upload/Finish.log", "/upload/retain/Finish.log");//没文件会抛异常 no such file --- newPath有同名的会被覆盖
        return strings;

    }


}
