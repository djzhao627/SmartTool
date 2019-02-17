package com.djzhao.smarttool.util.morsecode;

import java.util.HashMap;
import java.util.Map;

public class ContrastList {
    public static Map<Character, String> MORSE_CODE_MAP_LIST = new HashMap<>();// 摩尔斯编码表集合

    static {
        MORSE_CODE_MAP_LIST.put('A', ".-");
        MORSE_CODE_MAP_LIST.put('B', "-...");
        MORSE_CODE_MAP_LIST.put('C', "-.-.");
        MORSE_CODE_MAP_LIST.put('D', "-..");
        MORSE_CODE_MAP_LIST.put('E', ".");
        MORSE_CODE_MAP_LIST.put('F', "..-.");
        MORSE_CODE_MAP_LIST.put('G', "--.");
        MORSE_CODE_MAP_LIST.put('H', "....");
        MORSE_CODE_MAP_LIST.put('I', "..");
        MORSE_CODE_MAP_LIST.put('J', ".---");
        MORSE_CODE_MAP_LIST.put('K', "-.-");
        MORSE_CODE_MAP_LIST.put('L', ".-..");
        MORSE_CODE_MAP_LIST.put('M', "--");
        MORSE_CODE_MAP_LIST.put('N', "-.");
        MORSE_CODE_MAP_LIST.put('O', "---");
        MORSE_CODE_MAP_LIST.put('P', ".--.");
        MORSE_CODE_MAP_LIST.put('Q', "--.-");
        MORSE_CODE_MAP_LIST.put('R', ".-.");
        MORSE_CODE_MAP_LIST.put('S', "...");
        MORSE_CODE_MAP_LIST.put('T', "-");
        MORSE_CODE_MAP_LIST.put('U', "..-");
        MORSE_CODE_MAP_LIST.put('V', "...-");
        MORSE_CODE_MAP_LIST.put('W', ".--");
        MORSE_CODE_MAP_LIST.put('X', "-..-");
        MORSE_CODE_MAP_LIST.put('Y', "-.--");
        MORSE_CODE_MAP_LIST.put('Z', "--..");

        /* 数字电码0-9 */
        MORSE_CODE_MAP_LIST.put('0', "-----");
        MORSE_CODE_MAP_LIST.put('1', ".----");
        MORSE_CODE_MAP_LIST.put('2', "..---");
        MORSE_CODE_MAP_LIST.put('3', "...--");
        MORSE_CODE_MAP_LIST.put('4', "....-");
        MORSE_CODE_MAP_LIST.put('5', ".....");
        MORSE_CODE_MAP_LIST.put('6', "-....");
        MORSE_CODE_MAP_LIST.put('7', "--...");
        MORSE_CODE_MAP_LIST.put('8', "---..");
        MORSE_CODE_MAP_LIST.put('9', "----.");

        /* 标点符号，可自增删 */
        MORSE_CODE_MAP_LIST.put(',', "--..--"); // ,逗号  
        MORSE_CODE_MAP_LIST.put('.', ".-.-.-"); // .句号  
        MORSE_CODE_MAP_LIST.put('?', "..--.."); // ?问号  
        MORSE_CODE_MAP_LIST.put('!', "-.-.--"); // !感叹号  
        MORSE_CODE_MAP_LIST.put('\'', ".----.");// '单引号  
        MORSE_CODE_MAP_LIST.put('\"', ".-..-.");// "引号  
        MORSE_CODE_MAP_LIST.put('=', "-...-");  // =等号  
        MORSE_CODE_MAP_LIST.put(':', "---..."); // :冒号  
        MORSE_CODE_MAP_LIST.put(';', "-.-.-."); // ;分号  
        MORSE_CODE_MAP_LIST.put('(', "-.--.");  // (前括号  
        MORSE_CODE_MAP_LIST.put(')', "-.--.-"); // )后括号  
        MORSE_CODE_MAP_LIST.put(' ', "●");      // 留空格，这里的"●"是自定义的
    }

}  