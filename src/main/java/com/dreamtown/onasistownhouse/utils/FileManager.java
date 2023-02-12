package com.dreamtown.onasistownhouse.utils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileManager {

    @Autowired
    private Environment env;

    @Autowired
    private HttpSession httpSession;

    private ChannelSftp setupJsch() throws JSchException {
        JSch jsch = new JSch();
        jsch.setKnownHosts("C:\\Users\\MTJ 03\\.ssh\\known_hosts");
        // jsch.setKnownHosts("C:\\Users\\Administrator\\.ssh\\known_hosts");
        // jsch.setKnownHosts("/.ssh/known_hosts");
        Session jschSession;
        if (httpSession.getAttribute(env.getProperty("ftp.username")) != null) {
            jschSession = (Session) httpSession.getAttribute(env.getProperty("ftp.username"));
            return (ChannelSftp) jschSession.openChannel("sftp");
        }
        jschSession = jsch.getSession(env.getProperty("ftp.username"), env.getProperty("ftp.remoteHost"));
        jschSession.setPassword(env.getProperty("ftp.password"));
        jschSession.connect();
        httpSession.setAttribute(env.getProperty("ftp.username"), jschSession);
        return (ChannelSftp) jschSession.openChannel("sftp");
    }

    public Boolean connected() {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = setupJsch();
            return channelSftp.isConnected();
        } catch (JSchException e) {
            return false;
        }
    }

    public void uploadFile(MultipartFile file, String pathWithFileName) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = setupJsch();
            channelSftp.connect();
            InputStream inputStream = new BufferedInputStream(file.getInputStream());
            channelSftp.put(inputStream, pathWithFileName);
            // channelSftp.exit();
        } catch (JSchException | SftpException | IOException e) {
        }
    }

    public String downloadFile(String file) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = setupJsch();
            channelSftp.connect();
            InputStream is = channelSftp.get(file);
            byte[] bytes = IOUtils.toByteArray(is);
            String imageStr = Base64.encodeBase64String(bytes);
            // channelSftp.exit();
            return imageStr;
        } catch (JSchException | IOException | SftpException e) {
        }
        return "";
    }
}
