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


    public String logo;
    public String name;

    public Dataobject dataobject;
    public List<String> images;
    public List<String> dataarr;

    public String id;

    public class Dataobject{
        public String uid;
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

        public List<String> images;
    }



}
