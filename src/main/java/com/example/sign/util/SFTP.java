package com.example.sign.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import lombok.Data;

@Data
public class SFTP{

    private Session session;//会话
    private Channel channel;//连接通道
    private ChannelSftp sftp;// sftp操作类




}