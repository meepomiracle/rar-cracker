package com.cracker.core;

import com.cracker.common.DictDivider;
import com.cracker.domain.CrackerParam;
import com.cracker.util.DictUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hadoop on 2016/10/5.
 */
public class CrackProcessor {
    private ExecutorService executorService;

    private RarDecompressor rarDecompressor;

    private CrackerParam crackerParam;

    private int maxThreadNum = Runtime.getRuntime().availableProcessors();
    private int maxPWDLen = 6;
    
    private Map<Integer, Map<Integer, Map<Integer, String>>> dictDividerMap;
    private String sample;
    private File srcFile;
    private String destPath;

    private List<String> results = new ArrayList<String>();
    private DictDivider dictDivider;
    public CrackProcessor() {

    }

    private void init(){
       
        executorService = Executors.newFixedThreadPool(maxThreadNum);
        crackerParam = new CrackerParam(maxPWDLen,maxThreadNum,true,false,false,false,false);
        sample = DictUtil.generateSample(crackerParam);
        srcFile = new File("D:/1.rar");
        destPath = "D:/1";
        dictDivider = new DictDivider(crackerParam);

        
    }
    
    public void doProcessing(){
        boolean nextLen = true;

        for (int len = 1; len <=maxPWDLen &&nextLen; len++) {
            System.out.println("开始分配线程");
            for(int threadIndex = 1;threadIndex<=maxThreadNum;threadIndex++){
                char[] startTryPWD,endTryPWD;
                if(threadIndex==1){
                    startTryPWD = new char[len];
                    initStartTryPWD(sample,startTryPWD);
                    endTryPWD = dictDivider.calNthDivider(sample,len,maxThreadNum,threadIndex);
                }else{
                    startTryPWD = dictDivider.calNthDivider(sample,len,maxThreadNum,threadIndex-1);
                    DictUtil.increment(startTryPWD,sample,startTryPWD.length-1);
                    endTryPWD = dictDivider.calNthDivider(sample,len,maxThreadNum,threadIndex);
                }

                rarDecompressor = new RarDecompressor(sample,srcFile,destPath,startTryPWD,endTryPWD);
                CrackTask task = new CrackTask(rarDecompressor);
                executorService.execute(task);
            }
            System.out.println("分配线程结束");
            while(true){
                if(results.size()==maxThreadNum){
                    System.out.println("所有线程执行完毕");
                    for (int i = 0; i <results.size() ; i++) {
                        if(results.get(i)!=null){
                            nextLen = false;
                            break;
                        }
                    }
                    break;
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            results.clear();
        }

    }

    private void initStartTryPWD(String sample,char[] temp){
        for (int i = 0; i <temp.length ; i++) {
            temp[i]=sample.charAt(0);
        }
    }

    class CrackTask implements Runnable{
        private RarDecompressor rarDecompressor;
        public CrackTask(RarDecompressor rarDecompressor) {
            this.rarDecompressor = rarDecompressor;
        }

        public void run() {
            try {
                String result = rarDecompressor.tryUnrar();

                if(result!=null){
                    System.out.println("线程:"+Thread.currentThread().getName()+"找到密码:"+result);
                }else{
                    System.out.println("线程:"+Thread.currentThread().getName()+"没有找到");
                }
                results.add(result);
                System.out.println("结果加入链表,当前链表长度:"+results.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        CrackProcessor crackProcessor = new CrackProcessor();
        crackProcessor.init();
        crackProcessor.doProcessing();
    }

}
