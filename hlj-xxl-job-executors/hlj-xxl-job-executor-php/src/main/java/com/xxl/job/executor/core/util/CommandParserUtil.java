package com.xxl.job.executor.core.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandParserUtil {


    public static String[] parseCommand(String command) {
        String[] args = Arrays.stream(command.split(" ")).map(e -> e.trim()).collect(Collectors.toList()).toArray(new String[]{});
        return args;
    }


    public static String getOptionValue(String option, String command){
        String[] args = parseCommand(command);
        String optionValue = null;
        for (int i = 0; i < args.length-1; i++) {
            if (Objects.equals(args[i], option)) {
                optionValue = args[i + 1];
                break;
            }
        }
        return optionValue;
    }


    public static void main(String[] args) throws Exception {

        String optionValue1 = getOptionValue("--connect-timeout", "-m 1000 --connect-timeout 3000  www.baidu.com");
        String optionValue2 = getOptionValue("-m", "curl -m 1000 --connect-timeout 3000  www.baidu.com");

        System.out.println(optionValue1);
        System.out.println(optionValue2);

    }


}
