package com.lxkj.dsn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Time:2021/2/4
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class OrdertailListBean implements Serializable {
    public String odid;
    public String gid;
    public String gname;
    public String gimage;
    public String gprice;
    public String gnum;
    public String state;
    public String type;
    public boolean check;

    public List<String> mBannerSelectPath = new ArrayList<>();
}
