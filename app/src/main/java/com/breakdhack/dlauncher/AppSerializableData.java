package com.breakdhack.dlauncher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divakar
 */
class AppSerializableData implements Serializable {
    private static final long serialVersionUID = -7084935533941944723L;

    List<MyPackage> apps = new ArrayList<MyPackage>();

}
