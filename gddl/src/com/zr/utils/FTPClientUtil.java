package com.zr.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * FTP客户端工具
 */
public class FTPClientUtil {

    /**
     * 日志
     */
    private static final Logger LOGGER = LogManager.getLogger(FTPClientUtil.class);

    /**
     * FTP server configuration--IP key,value is type of String
     */
    public static final String SERVER_IP = "SERVER_IP";

    /**
     * FTP server configuration--Port key,value is type of Integer
     */
    public static final String SERVER_PORT = "SERVER_PORT";

    /**
     * FTP server configuration--ANONYMOUS Log in key, value is type of Boolean
     */
    public static final String IS_ANONYMOUS = "IS_ANONYMOUS";

    /**
     * user name of anonymous log in
     */
    public static final String ANONYMOUS_USER_NAME = "anonymous";

    /**
     * password of anonymous log in
     */
    public static final String ANONYMOUS_PASSWORD = "";

    /**
     * FTP server configuration--log in user name, value is type of String
     */
    public static final String USER_NAME = "USER_NAME";

    /**
     * FTP server configuration--log in password, value is type of String
     */
    public static final String PASSWORD = "PASSWORD";

    /**
     * FTP server configuration--PASV key, value is type of Boolean
     */
    public static final String IS_PASV = "IS_PASV";

    /**
     * FTP server configuration--working directory key, value is type of String While logging in, the current directory
     * is the user's home directory, the workingDirectory must be set based on it. Besides, the workingDirectory must
     * exist, it can not be created automatically. If not exist, file will be uploaded in the user's home directory. If
     * not assigned, "/" is used.
     */
    public static final String WORKING_DIRECTORY = "WORKING_DIRECTORY";


    public static Map<String, Object> serverCfg = new HashMap<String, Object>();

    static Properties prop;

    static{
        LOGGER.info("开始加载ftp.properties文件!");
        prop = new Properties();
        try {
            InputStream fps = FTPClientUtil.class.getResourceAsStream("ftp.properties");
            prop.load(fps);
            fps.close();
        } catch (Exception e) {
            LOGGER.error("读取ftp.properties文件异常!",e);
        }
        serverCfg.put(FTPClientUtil.SERVER_IP, values("SERVER_IP"));
        serverCfg.put(FTPClientUtil.SERVER_PORT, Integer.parseInt(values("SERVER_PORT")));
        serverCfg.put(FTPClientUtil.USER_NAME, values("USER_NAME"));
        serverCfg.put(FTPClientUtil.PASSWORD, values("PASSWORD"));
        LOGGER.info(String.valueOf(serverCfg));
    }

    /**
     * Upload a file to FTP server.
     *
     * @param serverCfg : FTP server configuration
     * @param filePathToUpload : path of the file to upload
     * @param fileStoredName : the name to give the remote stored file, null, "" and other blank word will be replaced
     *      by the file name to upload
     * @throws IOException
     * @throws SocketException
     */
    public static  String upload(Map<String, Object> serverCfg, String filePathToUpload, String fileStoredName)
            throws SocketException, IOException {
        upload(serverCfg, new File(filePathToUpload), fileStoredName);
        return "上传成功";
    }

    /**
     * Upload a file to FTP server.
     *
     * @param serverCfg : FTP server configuration
     * @param fileToUpload : file to upload
     * @param fileStoredName : the name to give the remote stored file, null, "" and other blank word will be replaced
     *      by the file name to upload
     * @throws IOException
     * @throws SocketException
     */
    public static  String upload(Map<String, Object> serverCfg, File fileToUpload, String fileStoredName)
            throws SocketException, IOException {
        if (!fileToUpload.exists()) {
            throw new IllegalArgumentException("File to upload does not exists：" + fileToUpload.getAbsolutePath

                    ());
        }
        if (!fileToUpload.isFile()) {
            throw new IllegalArgumentException("File to upload is not a file:" + fileToUpload.getAbsolutePath());
        }
        if (StringUtils.isBlank((String) serverCfg.get(SERVER_IP))) {
            throw new IllegalArgumentException("SERVER_IP must be contained in the FTP server configuration.");
        }
        transferFile(true, serverCfg, fileToUpload, fileStoredName, null, null);
        return "上传成功";
    }

    /**
     * Download a file from FTP server
     *
     * @param serverCfg : FTP server configuration
     * @param fileNameToDownload : file name to be downloaded
     * @param fileStoredPath : stored path of the downloaded file in local
     * @throws SocketException
     * @throws IOException
     */
    public static  void download(Map<String, Object> serverCfg, String fileNameToDownload, String fileStoredPath)
            throws SocketException, IOException {
        if (StringUtils.isBlank(fileNameToDownload)) {
            throw new IllegalArgumentException("File name to be downloaded can not be blank.");
        }
        if (StringUtils.isBlank(fileStoredPath)) {
            throw new IllegalArgumentException("Stored path of the downloaded file in local can not be blank.");
        }
        if (StringUtils.isBlank((String) serverCfg.get(SERVER_IP))) {
            throw new IllegalArgumentException("SERVER_IP must be contained in the FTP server configuration.");
        }
        transferFile(false, serverCfg, null, null, fileNameToDownload, fileStoredPath);
    }

    private static  void transferFile(boolean isUpload, Map<String, Object> serverCfg, File fileToUpload,
                                           String serverFileStoredName, String fileNameToDownload, String localFileStoredPath) throws

            SocketException,
            IOException {
        String host = (String) serverCfg.get(SERVER_IP);
        Integer port = (Integer) serverCfg.get(SERVER_PORT);
        Boolean isAnonymous = (Boolean) serverCfg.get(IS_ANONYMOUS);
        String username = (String) serverCfg.get(USER_NAME);
        String password = (String) serverCfg.get(PASSWORD);
        Boolean isPASV = (Boolean) serverCfg.get(IS_PASV);
        String workingDirectory = (String) serverCfg.get(WORKING_DIRECTORY);
        FTPClient ftpClient = new FTPClient();
        InputStream fileIn = null;
        OutputStream fileOut = null;
        try {
            if (port == null) {
                LOGGER.debug("Connect to FTP server on " + host + ":" + FTP.DEFAULT_PORT);
                ftpClient.connect(host);
            } else {
                LOGGER.debug("Connect to FTP server on " + host + ":" + port);
                ftpClient.connect(host, port);
            }
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                LOGGER.error("FTP server refuses connection");
                return;
            }

            if (isAnonymous != null && isAnonymous) {
                username = ANONYMOUS_USER_NAME;
                password = ANONYMOUS_PASSWORD;
            }
            LOGGER.debug("Log in FTP server with username = " + username + ", password = " + password);
            if (!ftpClient.login(username, password)) {
                LOGGER.error("Fail to log in FTP server with username = " + username + ", password = " +

                        password);
                ftpClient.logout();
                return;
            }

            // Here we will use the BINARY mode as the transfer file type,
            // ASCII mode is not supportted.
            LOGGER.debug("Set type of the file, which is to upload, to BINARY.");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            if (isPASV != null && isPASV) {
                LOGGER.debug("Use the PASV mode to transfer file.");
                ftpClient.enterLocalPassiveMode();
            } else {
                LOGGER.debug("Use the ACTIVE mode to transfer file.");
                ftpClient.enterLocalActiveMode();
            }

            if (StringUtils.isBlank(workingDirectory)) {
                workingDirectory = "/";
            }

            LOGGER.debug("Change current working directory to " + workingDirectory);
            changeWorkingDirectory(ftpClient,workingDirectory);

            if (isUpload) { // upload
                if (StringUtils.isBlank(serverFileStoredName)) {
                    serverFileStoredName = fileToUpload.getName();
                }
                fileIn = new FileInputStream(fileToUpload);
                LOGGER.debug("Upload file : " + fileToUpload.getAbsolutePath() + " to FTP server with name : "
                        + serverFileStoredName);
                if (!ftpClient.storeFile(serverFileStoredName, fileIn)) {
                    LOGGER.error("Fail to upload file, " + ftpClient.getReplyString());
                } else {
                    LOGGER.debug("Success to upload file.");
                }
            } else { // download
                // make sure the file directory exists
                File fileStored = new File(localFileStoredPath);
                if (!fileStored.getParentFile().exists()) {
                    fileStored.getParentFile().mkdirs();
                }
                fileOut = new FileOutputStream(fileStored);
                LOGGER.debug("Download file : " + fileNameToDownload + " from FTP server to local : "
                        + localFileStoredPath);
                if (!ftpClient.retrieveFile(fileNameToDownload, fileOut)) {
                    LOGGER.error("Fail to download file, " + ftpClient.getReplyString());
                } else {
                    LOGGER.debug("Success to download file.");
                }
            }

            ftpClient.noop();

            ftpClient.logout();

        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException f) {
                }
            }
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                }
            }
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static  boolean changeWorkingDirectory(FTPClient ftpClient, String workingDirectory) throws IOException{
        if(!ftpClient.changeWorkingDirectory(workingDirectory)){
            String [] paths = workingDirectory.split("/");
            for(int i=0 ;i<paths.length ;i++){
                if(!"".equals(paths[i])){
                    if(!ftpClient.changeWorkingDirectory(paths[i])){
                        ftpClient.makeDirectory(paths[i]);
                        ftpClient.changeWorkingDirectory(paths[i]);
                    }
                }
            }
        }
        return true;
    }

    public static  String upload(Map<String, Object> serverCfg, String filePathToUpload, String fileStoredPath, String

            fileStoredName)
            throws SocketException, IOException {
        upload(serverCfg, new File(filePathToUpload), fileStoredPath, fileStoredName);
        return "上传成功";
    }

    public static  String upload(Map<String, Object> serverCfg, File fileToUpload, String fileStoredPath, String

            fileStoredName)
            throws SocketException, IOException {
        if(fileStoredPath!=null && !"".equals(fileStoredPath)){
            serverCfg.put(WORKING_DIRECTORY, fileStoredPath);
        }
        upload(serverCfg, fileToUpload, fileStoredName);
        return "上传成功";
    }

    public static  String upload(String filePathToUpload, String fileStoredPath)throws SocketException, IOException {
        upload(serverCfg, filePathToUpload, fileStoredPath, "");
        return "上传成功";
    }

    public static  String upload(File fileToUpload, String fileStoredPath)throws SocketException, IOException {
        upload(serverCfg, fileToUpload, fileStoredPath, "");
        return "上传成功";
    }


    public static String values(String key) {
        String value = prop.getProperty(key);
        if (value != null) {
            return value;
        } else {
            return null;
        }
    }

}