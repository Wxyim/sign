package com.example.sign.config;

import com.example.sign.util.SFTP;
import com.example.sign.util.SFTPUtil;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SftpConfig {

    public static ObjectPool<SFTP> sftpObjectPool  = null;

    public SftpConfig() {

        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setTestOnBorrow(true);
        genericObjectPoolConfig.setTestOnReturn(true);

        sftpObjectPool = new GenericObjectPool<>(new PooledObjectFactory<SFTP>() {
            @Override
            public void activateObject(PooledObject<SFTP> pooledObject) throws Exception {
                System.out.println("active1！");
            }

            @Override
            public void destroyObject(PooledObject<SFTP> pooledObject) throws Exception {
                System.out.println("被销毁！");
            }

            @Override
            public PooledObject<SFTP> makeObject() throws Exception {

                System.out.println("创建了新连接！");
                SFTP sftp = new SFTP();
                SFTPUtil.getConnect(sftp);
                return new DefaultPooledObject<SFTP>(sftp);
            }

            @Override
            public void passivateObject(PooledObject<SFTP> pooledObject) throws Exception {
                System.out.println("被钝化！");
            }

            @Override
            public boolean validateObject(PooledObject<SFTP> pooledObject) {
                System.out.println(222);
                return pooledObject.getObject().getSftp().isConnected();
            }
        }, genericObjectPoolConfig);

    }
}
