package com.cracker.common;

import com.cracker.domain.CrackerParam;
import com.cracker.util.DictUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2016/9/25.
 */
public class DictDivider {
    private CrackerParam crackerParam;

    /**
     * Map<最大密码长度，Map<线程数量，Map<index，分割串>>>
     */
    private Map<Integer, Map<Integer, Map<Integer, String>>> dictDividerMap;

    public DictDivider(CrackerParam crackerParam) {
        this.crackerParam = crackerParam;
        dictDividerMap = new HashMap<Integer, Map<Integer, Map<Integer, String>>>();
//        init();
    }

    private void init() {

        String sample = DictUtil.generateSample(crackerParam);

        for (int i = 1; i <= crackerParam.getLen(); i++) {
            Map<Integer, Map<Integer, String>> tempMap = new HashMap<Integer, Map<Integer, String>>();
            for (int j = 1; j <= crackerParam.getThreadNum(); j++) {
                tempMap.put(j, calDivider(sample, i, j));
            }
            dictDividerMap.put(i, tempMap);
        }

    }

    private Map<Integer, String> calDivider(String sample, int maxDigit, int threadNum) {
        Map<Integer, String> dividerMap = new HashMap<Integer, String>();
        int sampleLen = sample.length();
        long total = (long) Math.pow(sampleLen, maxDigit);

        if(total <threadNum) {
            threadNum = (int)total;
        }
        char[] temp = new char[maxDigit];

        for (int i = 0; i < maxDigit; i++) {
            temp[i] = sample.charAt(0);
        }
        int j=1;
        for (long i= 1; i < total; i++) {

            int divider = (int) (total*j/threadNum-1);
            if(divider>0) {
                //
                DictUtil.increment(temp, sample, maxDigit - 1);
            }
            //根据线程数量对样本进行分段
            if (i == total * j / threadNum -1 ) {
                dividerMap.put(j, String.valueOf(temp));
                j++;
            }
        }

        return dividerMap;
    }

    /**
     * 计算第maxNum/pieces*n - 1个数
     * @param sample 样本
     * @param digit 数的位数
     * @param pieces 均分份数
     * @param n 第n份
     * @return
     */
    public char[] calNthDivider(String sample,int digit,int pieces,int n){
        int len = sample.length();
        long maxNum = (long) Math.pow(len,digit);

        if(pieces>maxNum){
            pieces=(int)maxNum;
        }

        long index = maxNum*n/pieces - 1;
        return DictUtil.calNthDecimal(sample,index,digit);

    }
}
