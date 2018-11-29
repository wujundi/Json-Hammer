import com.sun.deploy.util.ArrayUtil;
import hammer.JsonHammer;
import hammer.JsonHammerImpl;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main (String[] args) {
        JsonHammer jh = new JsonHammerImpl();
        Scanner sc = new Scanner(System.in);
        ArrayList<String> paramArr = new ArrayList<String>();
        String input = sc.nextLine();
        while(sc.hasNext()){
            String paramPath = sc.nextLine();
            if(paramPath.equals("EOF")){
                break;
            }
            paramArr.add(paramPath);
        }

        String[] params = new String[paramArr.size()];
        paramArr.toArray(params);

        ArrayList<String> output = jh.hammer(input,params);
        for(String str : output){
            System.out.println(str);
        }

//        ArrayList<String> output = jh.hammer(input);
//        for(String str : output){
//            System.out.println(str);
//        }
    }

}
