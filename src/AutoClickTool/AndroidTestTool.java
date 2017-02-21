package AutoClickTool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiaXin on 2017/2/7.
 */
public class AndroidTestTool {

    private  String[] devices = {};
    private  static String [][] pagelist ;

    public static void main(String[] args) {

        pagelist = new AndroidTestTool().initPageList();
        new AndroidTestTool().startListener();

    }

    private String[][] initPageList(){

            InputStream is=this.getClass().getResourceAsStream("/config.ini");
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String lineTxt = null;

            try {
                int row=0;
                List<String> list = new ArrayList();
                while ((lineTxt = bufferedReader.readLine()) != null) {
                   list.add(lineTxt);
                }
                pagelist = new String[list.size()][2];
                for(int i=0;i<list.size();i++){
                    pagelist[i]=list.get(i).split(",");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return pagelist;
    }

    public  void startListener(){

        int threadnum = 1;
        devices=getDevices();
        for(int i=0 ; i<devices.length;i++){

            for(int j=0 ; j<pagelist.length;j++){

                AutoClickTool act = new AutoClickTool();
                act.setWindowname(pagelist[j][0]);
                if(pagelist[j].length==3){
                    act.setKeyboard(1);
                    act.setPassword(pagelist[j][2]);
                }else{
                    act.setKeyboard(0);
                }
                act.setClickname(pagelist[j][1]);
                act.setDevicename(devices[i]);
                act.setThreadname("线程"+threadnum);
                Thread thread = new Thread(act);
                thread.start();
                threadnum++;
            }
        }

    }

    private  String [] getDevices(){

        CommonTool ct = new CommonTool();
        String res = ct.execCommand("adb devices");
        String [] devtemp = res.split("\t");
        devices = new String [devtemp.length-1];
        for(int i=0 ; i<devtemp.length-1;i++){
            int flag = devtemp[i].indexOf("\n");
            devices[i]=devtemp[i].substring(flag);
        }
        return devices;
    }
}