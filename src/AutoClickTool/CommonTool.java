package AutoClickTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by JiaXin on 2017/2/8.
 */
public class CommonTool {

    // 执行命令函数
    public String  execCommand(String cmd) {
        BufferedReader inputStream = null;
        String res = "";
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            // 获取正确的输入流
            inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
            res = readLine(inputStream);
            //System.out.print(res);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    // 读取返回的结果
    private String  readLine(final BufferedReader br) {

        String line = null;
        StringBuffer str = new StringBuffer();
        String res = "" ;
        try {
            // 循环读取输入流
            while ((line = br.readLine()) != null) {
                str.append(line + "\r\n");
                res =  str.toString();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res ;
    }

}
