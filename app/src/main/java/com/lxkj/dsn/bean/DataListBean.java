package com.lxkj.dsn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Time:2020/8/21
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class DataListBean implements Serializable {
    public String id;
    public String bid;
    public String gid;
    public String image;
    public String oldprice;
    public String newprice;
    public String name;
    public String sid;
    public String fid;
    public String usericon;
    public String username;
    public String userid;
    public String did;
    public String content;
    public String adtime;
    public String money;
    public String zannum;
    public String commentnum;
    public String iszan;
    public String star;
    public String gcid;
    public String gname;
    public String gimage;
    public String numbers;
    public String state;
    public String mid;
    public boolean isCheck;
    public String title;
    public String url;
    public String type;
    public String objid;
    public String ordernum;
    public String goodsprice;
    public String addressid;
    public String phone;
    public String address;
    public String addressdetail;
    public String isdefault;
    public String usernickname;
    public String integral;
    public String allsalemoney;
    public String allmoney;
    public String dcid;
    public String usertype;
    public String profitmoney;
    public String fname;
    public List<OrdertailList> ordertailList;
    public List<SecondList> secondList;


    public String value;
    public String pm;
    public String code;
    public String userId;
    public List<ChildrenBean> children;
    public List<GoodsListBean> orderGoodsList;
    public List<String> images;

    public class OrdertailList{
        public String gprice;
        public String odid;
        public String gid;
        public String gimage;
        public String gname;
        public String gnum;
    }
    public class SecondList{
        public String dcid;
        public String userid;
        public String usernickname;
        public String usertype;
        public String taid;
        public String tanickname;
        public String tatype;
        public String content;
        public String adtime;
    }
}
