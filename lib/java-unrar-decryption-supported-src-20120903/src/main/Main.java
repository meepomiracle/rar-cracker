package main;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;
import org.apache.tika.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by hadoop on 2016/9/25.
 */
public class Main {
    private final String SEPARATOR = File.separator;
    /**
     * 解压指定RAR文件到指定的路径
     * @param srcRarFile 需要解压RAR文件
     * @param destPath 指定解压路径
     * @param password 压缩文件时设定的密码
     * @throws IOException
     */
    public void unrar(File srcRarFile, String destPath, String password) throws IOException {
        if (null == srcRarFile || !srcRarFile.exists()) {
            throw new IOException("指定压缩文件不存在.");
        }
        if (!destPath.endsWith(SEPARATOR)) {
            destPath += SEPARATOR;
        }

        long begin = 0;
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
                    begin = System.currentTimeMillis();
                    archive.extractFile(fileHeader, unOut);
                    unOut.flush();
                    unOut.close();
                }
                fileHeader = archive.nextFileHeader();
            }
            archive.close();
        } catch (RarException e) {
            e.printStackTrace();
            System.out.println("archive.extractFile:"+(System.currentTimeMillis()-begin));
        } finally {
            IOUtils.closeQuietly(unOut);
        }
    }

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        Main m = new Main();
        try {
            m.unrar(new File("D:/file/1.rar"),"D:/file/1","12345");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("costtime:"+(System.currentTimeMillis()-begin));
    }
}
