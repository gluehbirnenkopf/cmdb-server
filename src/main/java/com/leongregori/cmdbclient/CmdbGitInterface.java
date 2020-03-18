package com.leongregori.cmdbclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

/**
 * Loads cmdb file (url) from CMDB via GitHub API into Map (Properties).
 * @param gitUrl the file to load.
 * @return a map of loaded cmdb properties.
 * @author Leon Gregori
 */
public class CmdbGitInterface {
    String gitUrl;
    String gitRawUrl;
    String gitToken;
    String branch;
    String cmdbFilePath;

    Logger logger = LoggerFactory.getLogger(CmdbGitInterface.class);

    public CmdbGitInterface(String gitUrl, String gitToken, String branch, String urlTail) {
        if (gitUrl.matches("^http[s]?:\\/\\/")) {
            logger.debug("gitURL is valid: "+gitUrl);
        } else {
            logger.error("gitUrl is invalid as it does start with a valid protocol: "+gitUrl);
        }

        this.gitUrl=gitUrl;
        this.gitRawUrl=gitRawUrl="https://raw."+gitUrl.split("https://")[1];
        this.gitToken=gitToken;
        this.branch=branch;
        this.cmdbFilePath=urlTail;
    }

        HashMap getCmdb() {
            logger.info("Querying File: "+gitRawUrl+branch+"/"+cmdbFilePath);
            String cmdbFile = getGitContent(gitRawUrl+branch+"/"+cmdbFilePath);
            logger.info("Loaded Content From GitHub:\n"+cmdbFile);
            HashMap cmdbMap = loadPropertyFile(cmdbFile);
            return cmdbMap;
        }

        String getGitContent(String url) {
            String properties = "test";
            HttpURLConnection gitURLConnection = null;

            try{
                logger.info("Using"+gitToken+"for github auth");
                URL gitURL = new URL(url);
                gitURLConnection = (HttpURLConnection)gitURL.openConnection();
                gitURLConnection.setRequestProperty("Authorization","token "+gitToken);
                gitURLConnection.setUseCaches(false);
                gitURLConnection.setDoInput(true);
                gitURLConnection.setDoOutput(true);
                gitURLConnection.connect();
                int status = gitURLConnection.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(gitURLConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (MalformedURLException ex) {
                logger.error(String.valueOf(ex));
            } catch (IOException ex) {
                logger.error(String.valueOf(ex));

            } finally {
                if (gitURLConnection != null) {
                    try {
                        gitURLConnection.disconnect();
                    } catch (Exception ex) {
                        logger.error(String.valueOf(ex));
                    }
                }
            }
            return properties;
        }

        HashMap loadPropertyFile(String cmdbFileContent) {
            Properties cmdbProps = new Properties();
            try{
                cmdbProps.load(new StringReader(cmdbFileContent));
            }catch(Exception ex) {
                logger.error(String.valueOf(ex));
            }catch(Throwable t) {
                String errorMessage = "Cmdb file '$cmdbFileContent' could not be loaded because of this error: $t.getMessage()";
                logger.error(errorMessage);
            }
            HashMap cmdb = new HashMap<String,String>();
            cmdb.putAll(cmdbProps);

            logger.info("$cmdbFileContent: ${cmdbProps.inspect()}");
            return cmdb;
        }


    }

