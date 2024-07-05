package com.example.studentguidance.ModelClasses;

import java.util.ArrayList;
import java.util.regex.*;
public class StringExtract {

    public static ArrayList<String> getSourceAndDestination(String line){
        line=line.toLowerCase();
        String[] locations = {"academicblock","boyshostel1","boyshostelone","boyshostelon","block1","blockone","blockon",
                "boyshostel2","boyshostelto","boyshosteltoo","boyshosteltwo","block2","blockto","blocktwo","blocktoo",
                "boyshostel3","boyshostelthree","block3","blockthree","boyshostel4","boyshostelfour","block4","blockfour",
                "boyshostel5","boyshostelfive","block5","blockfive", "boyshostel6","boyshostelsix","block6","blocksix",
                "girlshostel","underbelly","visamart","labcomplex","auditorium","multipurposehall","postoffice","parkingarea",
                "ab","ub","mph","lc"};

        String[] res1 =null;
        if(line.contains("and")) res1=line.split("and");
        String[] res2 =null;
        if(line.contains("to"))res2=line.split("to");
        String[] res3 =null;
        if(line.contains("two"))res3=line.split("two");
        String[] res4 =null;
        if(line.contains("too"))res4=line.split("too");
        ArrayList<String> arrLst = new ArrayList<>();
        if(res1!=null) {
            for(String ele:res1){
                ele=ele.replaceAll("\\s+","");
                for (String e:locations){
                    if(ele.equals(e)) arrLst.add(e);
                    if(arrLst.size()==2) return  arrLst;
                }
            }
        }
        arrLst.clear();
        if(res2!=null) {
            for(String ele:res2){
                ele=ele.replaceAll("\\s+","");
                for (String e:locations){
                    if(ele.equals(e)) arrLst.add(e);
                    if(arrLst.size()==2) return  arrLst;
                }
            }
        }
        arrLst.clear();
        if(res3!=null) {
            for(String ele:res3){
                ele=ele.replaceAll("\\s+","");
                for (String e:locations){
                    if(ele.equals(e)) arrLst.add(e);
                    if(arrLst.size()==2) return  arrLst;
                }
            }
        }
        arrLst.clear();
        if(res4!=null) {
            for(String ele:res4){
                ele=ele.replaceAll("\\s+","");
                for (String e:locations){
                    if(ele.equals(e)) arrLst.add(e);
                    if(arrLst.size()==2) return  arrLst;
                }
            }
        }
        return arrLst;
    }
}
