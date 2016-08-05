package com.breakdhack.dlauncher;

/**
 * Created by Divakar.
 */
public class AppNameSorting {

    public void exchange_sorting(MyPackage[] myPacs){

        int i, j;
        MyPackage temp;
        for(i = 0; i < myPacs.length - 1; i++){

            for(j = i +1 ; j < myPacs.length - 1; j++){

                if(myPacs[i].label.compareToIgnoreCase(myPacs[j].label) > 0){
                    temp = myPacs[i];
                    myPacs[i] = myPacs[j];
                    myPacs[j] = temp;
                }

            }

        }

    }

}
