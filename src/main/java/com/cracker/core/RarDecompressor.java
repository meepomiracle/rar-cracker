package com.cracker.core;

import com.cracker.util.DictUtil;
import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;
import org.apache.tika.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by hadoop on 2016/9/20.
 */
public class RarDecompressor {
    private final String SEPARATOR = File.separator;

    private File srcRarFile;

    private String destPath;

    private char[] startTryPWD;

    private char[] endTryPWD;

    private String sample;

    public RarDecompressor(String sample, File srcRarFile, String destPath, char[] startTryPWD, char[] endTryPWD) {
        this.sample = sample;
        this.srcRarFile = srcRarFile;
        this.destPath = destPath;
        this.startTryPWD = startTryPWD;
        this.endTryPWD = endTryPWD;
    }


    public String tryUnrar() throws IOException {
        if (null == srcRarFile || !srcRarFile.exists()) {
            throw new RuntimeException("指定压缩文件不存在.");
        }
        if (!destPath.endsWith(SEPARATOR)) {
            destPath += SEPARATOR;
        }
        Archive archive = null;
        OutputStream unOut = null;
        boolean end=false;
        for (char[] tryPWD = startTryPWD ;String.valueOf(tryPWD).compareTo(String.valueOf(endTryPWD))<=0&& !end  ; DictUtil
                .increment(tryPWD, sample, tryPWD.length - 1)) {
            if(String.valueOf(tryPWD).equals(String.valueOf(endTryPWD))){
                end=true;
            }
            String password = String.valueOf(tryPWD);
            try {
                archive = new Archive(srcRarFile, password, false);
                FileHeader fileHeader = archive.nextFileHeader();
                while (null != fileHeader) {
                    if (!fileHeader.isDirectory()) {
                        // 1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
                        String destFileName = "";
                        String destDirName = "";
                        if (SEPARATOR.equals("/")) {        // 非windows系统
                            destFileName = (destPath + fileHeader.getFileNameString()).replaceAll("\\\\", "/");
                            destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
                        } else {        // windows系统
                            destFileName = (destPath + Thread.currentThread().getId()+fileHeader.getFileNameString()).replaceAll("/", "\\\\");
                            destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));
                        }

                        File destFile = new File(destFileName);
                        // 2创建文件夹
                        File dir = new File(destDirName);
                        if (!dir.exists() || !dir.isDirectory()) {
                            dir.mkdirs();
                        }
                        // 抽取压缩文件
                        unOut = new FileOutputStream(destFile);
                        archive.extractFile(fileHeader, unOut);
                        System.out.println("线程:"+Thread.currentThread().getName()+"找到密码："+password+"---------------------");

//                        unOut.flush();
                        unOut.close();
                        archive.close();
                        if(destFile!=null&&destFile.exists()){
                            destFile.delete();
                        }
                        if(dir!=null&&dir.exists()){
                            dir.delete();
                        }
                        return password;
                    }
                    fileHeader = archive.nextFileHeader();
                }
                archive.close();
            } catch (RarException e) {
                System.out.println("线程:"+Thread.currentThread().getName()+"---尝试："+password+",失败");
                archive.close();
            } finally {
                IOUtils.closeQuietly(unOut);
            }
        }

        return null;
    }

    /**
     * 解压指定RAR文件到指定的路径
     *
     * @param srcRarFile 需要解压RAR文件
     * @param destPath   指定解压路径
     * @param password   压缩文件时设定的密码
     * @throws IOException
     *//*
    public void unrar(File srcRarFile, String destPath, String password) throws IOException {
        if (null == srcRarFile || !srcRarFile.exists()) {
            throw new IOException("指定压缩文件不存在.");
        }
        if (!destPath.endsWith(SEPARATOR)) {
            destPath += SEPARATOR;
        }
        Archive archive = null;
        OutputStream unOut = null;
        try {
            archive = new Archive(srcRarFile, password, false);
            FileHeader fileHeader = archive.nextFileHeader();
            while (null != fileHeader) {
                if (!fileHeader.isDirectory()) {
                    // 1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
                    String destFileName = "";
                    String destDirName = "";
                    if (SEPARATOR.equals("/")) {        // 非windows系统
                        destFileName = (destPath + fileHeader.getFileNameString()).replaceAll("\\\\", "/");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
                    } else {        // windows系统
                        destFileName = (destPath + fileHeader.getFileNameString()).replaceAll("/", "\\\\");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));
                    }

                    File destFile = new File(destFileName);
                    // 2创建文件夹
                    File dir = new File(destDirName);
                    if (!dir.exists() || !dir.isDirectory()) {
                        dir.mkdirs();
                    }
                    // 抽取压缩文件
                    unOut = new FileOutputStream(destFile);
                    archive.extractFile(fileHeader, unOut);

                    unOut.flush();
                    unOut.close();
                }
                fileHeader = archive.nextFileHeader();
            }
            archive.close();
        } catch (RarException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(unOut);
        }
    }*/

}
