package com.lxkj.dsn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kxn on 2019/1/21 0021.
 */
public class ResultBean extends BaseBean implements Serializable {
    public List<DataListBean> dataList;
    public String totalPage;
    public String totalCount;
    public String pageSize;

    public String isregister;
    public String uid;
    public String phone;
    public String nickname;
    public String icon;
    public String sex;
    public String age;
    public String autograph;
    public String balance;
    public String integral;
    public String province;
    public String city;
    public String area;
    public String invitationcode;
    public String isauthor;
    public String ismember;
    public String gid;
    public String type;
    public String oldprice;
    public String newprice;
    public String skunum;
    public String salenum;
    public String content;
    public String url;
    public String commnum;
    public String iscoll;
    public String aid;
    public String aname;
    public String abirthday;
    public String aimage;
    public String aschool;
    public String datastr;
    public String ordernum;
    public String body;
    public String name;
    public String num;
    public String apkurl;
    public String adtime;
    public String allnum;
    public String daymoney;
    public String allmoney;


    public String logo;

    public Dataobject dataobject;
    public List<String> images;
    public List<String> dataarr;
    public List<String> refundimage;

    public String id;

    public class Dataobject{
        public String uid;
        public String ordernum;
        public String gid;
        public String phone;
        public String nickname;
        public String icon;
        public String sex;
        public String age;
        public String autograph;
        public String balance;
        public String integral;
        public String province;
        public String city;
        public String area;
        public String invitationcode;
        public String isauthor;
        public String ismember;
        public String type;
        public String name;
        public String oldprice;
        public String newprice;
        public String skunum;
        public String salenum;
        public String content;
        public String url;
        public String commnum;
        public String iscoll;
        public String aid;
        public String aname;
        public String abirthday;
        public String aimage;
        public String aschool;
        public String adescs;
        public String num;
        public String apkurl;
        public String adtime;

        public List<String> images;
        public List<String> refundimage;
        public String address;
        public String goodsprice;
        public String backmoney;
        public String paytype;
        public String remarks;
        public String cancelreason;
        public String refundreason;
        public String refunddesc;
        public String logisticsname;
        public String logisticsnum;
        public String state;
        public String paytime;
        public String sendtime;
        public String shtime;
        public String pjtime;
        public String sqtktime;
        public String tkshtime;
        public String qxtime;

        public List<OrdertailListBean> ordertailList;

    }




}
