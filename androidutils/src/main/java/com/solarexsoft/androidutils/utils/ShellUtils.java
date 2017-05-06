package com.solarexsoft.androidutils.utils;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 05/05/2017
 *    Desc:
 * </pre>
 */

public final class ShellUtils {
    private ShellUtils() {
        throw new UnsupportedOperationException("Can't instantiate directly");
    }

    public static CommandResult execCmd(String command, boolean isRoot) {
        return execCmd(new String[]{command}, isRoot, true);
    }

    public static CommandResult execCmd(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, isRoot, isNeedResultMsg);
    }

    public static CommandResult execCmd(List<String> commands, boolean isRoot) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}), isRoot, true);
    }

    public static CommandResult execCmd(List<String> commands, boolean isRoot, boolean
            isNeedResultMsg) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}), isRoot,
                isNeedResultMsg);
    }

    public static CommandResult execCmd(String[] commands, boolean isRoot) {
        return execCmd(commands, isRoot, true);
    }


    public static CommandResult execCmd(String[] commands, boolean isRoot, boolean
            isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        DataOutputStream dos = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? "su" : "sh");
            dos = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null)
                    continue;
                dos.write(command.getBytes());
                dos.writeBytes("\n");
                dos.flush();
            }
            dos.writeBytes("exit\n");
            dos.flush();
            result = process.waitFor();
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()
                        , "UTF-8"));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream(),
                        "UTF-8"));
                String tmp;
                while ((tmp = successResult.readLine()) != null) {
                    successMsg.append(tmp);
                }
                while ((tmp = errorResult.readLine()) != null) {
                    errorMsg.append(tmp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Closeutils.closeIO(dos, successResult, errorResult);
            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(
                result,
                successMsg == null ? null : successMsg.toString(),
                errorMsg == null ? null : errorMsg.toString());
    }

    public static class CommandResult {
        public int result;
        public String successMsg;
        public String errorMsg;

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
}
