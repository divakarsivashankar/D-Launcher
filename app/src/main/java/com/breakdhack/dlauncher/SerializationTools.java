package com.breakdhack.dlauncher;


import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;


/**
 * Created by Divakar
 */

public class SerializationTools {

    public static void serializeData(AppSerializableData obj){
        FileOutputStream fos;
        try{
            fos = MainActivity.act.openFileOutput("data", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(obj);
            os.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static AppSerializableData loadSerializedData(){

        ObjectInputStream inputStram = null;

        try{

            inputStram = new ObjectInputStream(MainActivity.act.openFileInput("data"));
            Object obj = inputStram.readObject();

            if (obj instanceof AppSerializableData){
                return (AppSerializableData)obj;
            }else
                return null;

        }catch (EOFException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            try{
                if (inputStram!=null){
                    inputStram.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return null;
    }

}