package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.core.util.CommandParserUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@JobHandler(value = "curlJobHandler")
@Component
public class CurlJobHandler extends IJobHandler {


    @Override
    public ReturnT<String> execute(String param) throws Exception {

        if (StringUtils.isEmpty(param)) {
            return new ReturnT<String>(IJobHandler.FAIL.getCode(), "参数不能为空，请设置");
        }

        String command = "curl ";
        String m = CommandParserUtil.getOptionValue("-m", param);
        if (StringUtils.isEmpty(m)) {
            command += "-m 60 ";
        }

        String connectTimeout = CommandParserUtil.getOptionValue("--connect-timeout", param);
        if (StringUtils.isEmpty(connectTimeout)) {
            command += "--connect-timeout 60 ";
        }

        command += param;

        int exitValue = -1;
        BufferedReader bufferedReader = null;
        try {
            // command process
            Process process = Runtime.getRuntime().exec(command);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

            // command log
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                XxlJobLogger.log(line);
            }

            // command exit
            process.waitFor();
            exitValue = process.exitValue();
        } catch (Exception e) {
            XxlJobLogger.log(e);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        if (exitValue == 0) {
            return IJobHandler.SUCCESS;
        } else {
            return new ReturnT<String>(IJobHandler.FAIL.getCode(), "curl exit value(" + exitValue + ") is failed");
        }
    }


}
