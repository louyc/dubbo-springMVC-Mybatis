package com.semioe.web.util;

import org.springframework.stereotype.Service;

@Service
public class RandomCode {

    public String createRandomByFlag(boolean numberFlag, int codeLength, boolean randFlag) {

        if (randFlag) {
            return RandomCode.createRandom(numberFlag, codeLength);
        } else {
            /**
             * 如果配置为调试固定随机码，则统一返回0000
             */
            return "0000";
        }
    }

    /**
     * 创建指定数量的随机字符串
     * 
     * @param numberFlag
     *            是否是数字
     * @param length
     * @return
     */
    public synchronized static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

}
