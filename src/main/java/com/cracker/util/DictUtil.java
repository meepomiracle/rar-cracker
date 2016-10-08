package com.cracker.util;

import com.cracker.common.Constants;
import com.cracker.domain.CrackerParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadoop on 2016/10/5.
 */
public class DictUtil {

    /**
     * temp按照sample的顺序自增一位
     * @param temp 自增的对象
     * @param sample 样本
     * @param incrementIndex 当前需要自增的位
     */
    public static void increment(char[] temp, String sample, int incrementIndex) {
        char start = sample.charAt(0);
        char end = sample.charAt(sample.length()-1);
        //当前位需要进位，当前位赋值为start
        if (temp[incrementIndex] == end) {
            temp[incrementIndex] = start;
            //判断上一位是否需要进位
            //当前位大于第一位
            if (incrementIndex - 1 >= 0) {
                if (temp[incrementIndex - 1] == end) {//前一位需要进位
                    increment(temp, sample, incrementIndex - 1);
                }else {
                    int preIndexInSample = sample.lastIndexOf(temp[incrementIndex - 1]);
                    temp[incrementIndex - 1] = sample.charAt(preIndexInSample + 1);
                }
            }
        }else{//当前位不需要进位，则直接赋值为后一个数
            int preIndexInSample = sample.lastIndexOf(temp[incrementIndex]);
            temp[incrementIndex] = sample.charAt(preIndexInSample + 1);
        }


    }

    /**
     * 根据参数生成样本
     * @param crackerParam
     * @return
     */
    public static String generateSample(CrackerParam crackerParam){
        int numBit = crackerParam.isIncluedDigit() ? 1 : 0;
        int lowerCaseBit = crackerParam.isIncluedChar() ? 1 : 0;
        int upperCaseBit = crackerParam.isIncluedUpperCase() ? 1 : 0;
        int specialBit = crackerParam.isInlucedSpecial() ? 1 : 0;

        //将包含数字，包含小写字母，包含大写字母，包含特殊字符的组合转换为4位二进制数，0000到1111
        int category = (numBit << 3) + (lowerCaseBit << 2) + (upperCaseBit << 1) + specialBit;

        List<Character> list = new ArrayList<Character>();
        String sample = "";
        switch (category) {
            case 1://0001
                sample += Constants.SPECIAL;
                break;
            case 2://0010
                sample += Constants.UPPER_CASE;
                break;
            case 3://0011
                sample += Constants.UPPER_CASE;
                sample += Constants.SPECIAL;
                break;
            case 4://0100
                sample += Constants.LOWER_CASE;
                break;
            case 5://0101
                sample += Constants.LOWER_CASE;
                sample += Constants.SPECIAL;
                break;
            case 6://0110
                sample += Constants.LOWER_CASE;
                sample += Constants.UPPER_CASE;
                break;
            case 7://0111
                sample += Constants.LOWER_CASE;
                sample += Constants.UPPER_CASE;
                sample += Constants.SPECIAL;

                break;
            case 8://1000
                sample += Constants.DIGIT;
                break;
            case 9://1001
                sample += Constants.DIGIT;
                sample += Constants.SPECIAL;
                break;
            case 10://1010
                sample += Constants.DIGIT;
                sample += Constants.UPPER_CASE;
                break;
            case 11://1011
                sample += Constants.DIGIT;
                sample += Constants.UPPER_CASE;
                sample += Constants.SPECIAL;
                break;
            case 12://1100
                sample += Constants.DIGIT;
                sample += Constants.LOWER_CASE;
                break;
            case 13://1101
                sample += Constants.DIGIT;
                sample += Constants.LOWER_CASE;
                sample += Constants.SPECIAL;
                break;
            case 14://1110
                sample += Constants.DIGIT;
                sample += Constants.LOWER_CASE;
                sample += Constants.UPPER_CASE;
                break;
            case 15://1111
                sample += Constants.DIGIT;
                sample += Constants.LOWER_CASE;
                sample += Constants.UPPER_CASE;
                sample += Constants.SPECIAL;
                break;
            default://0000或其他
                break;
        }
        return sample;
    }

    /**
     * 计算第index个数
     * sample的长度代表数的进制
     * 总共有Math.pow(sample.lenth,bit)个数，计算其中第index个数
     * @param sample 样本
     * @param index 下标
     * @param bit 数的位数
     * @return 第index个数
     */
    public static char[] calNthDecimal(String sample,long index,int bit){
        int len = sample.length();

        long maxNum = (long) Math.pow(len,bit);
        if(index>maxNum ||index<0){
            throw new RuntimeException("index out of maxNum or 0");
        }

        char[] result = new char[bit];
        for (int i = 0; i <bit ; i++) {
            result[i]=sample.charAt(0);
        }

        int i = result.length-1;
        while(index!=0){
            char temp = sample.charAt((int) (index%len));
            result[i]=temp;
            index /= len;
            i--;
        }
        return result;
    }
}
