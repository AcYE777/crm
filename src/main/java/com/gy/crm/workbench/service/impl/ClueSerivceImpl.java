package com.gy.crm.workbench.service.impl;

import com.gy.crm.settings.dao.DicTypeDao;
import com.gy.crm.settings.dao.DicValueDao;
import com.gy.crm.settings.dao.UserDao;
import com.gy.crm.settings.entity.DicType;
import com.gy.crm.settings.entity.DicValue;
import com.gy.crm.settings.entity.User;
import com.gy.crm.utils.DateTimeUtil;
import com.gy.crm.utils.UUIDUtil;
import com.gy.crm.workbench.dao.*;
import com.gy.crm.workbench.entity.*;
import com.gy.crm.workbench.service.ClueSerivce;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("ClueSerivceImpl")
public class ClueSerivceImpl implements ClueSerivce {
    @Resource(name = "dicTypeDao")
    private DicTypeDao dicTypeDao;
    @Resource(name = "dicValueDao")
    private DicValueDao dicValueDao;
    @Resource(name = "userDao")
    private UserDao userDao;
    @Resource(name = "clueDao")
    private ClueDao clueDao;
    @Resource(name = "activityDao")
    private ActivityDao activityDao;
    @Resource(name = "clueActivityRelationDao")
    private ClueActivityRelationDao clueActivityRelationDao;
    @Resource(name = "customerDao")
    private CustomerDao customerDao;
    @Resource(name = "contactsDao")
    private ContactsDao contactsDao;
    @Resource(name = "clueRemarkDao")
    private ClueRemarkDao clueRemarkDao;
    @Resource(name = "customerRemarkDao")
    private CustomerRemarkDao customerRemarkDao;
    @Resource(name = "contactsRemarkDao")
    private ContactsRemarkDao contactsRemarkDao;
    @Resource(name = "contactsActivityRelationDao")
    private ContactsActivityRelationDao contactsActivityRelationDao;
    @Resource(name = "tranDao")
    private TranDao tranDao;
    @Resource(name = "tranHistoryDao")
    private TranHistoryDao tranHistoryDao;
    @Override
    public List<User> getOwner() {
        System.out.println("getOwnerService??????");
        List<User> list = userDao.queryUser();
        return list;
    }

    @Override
    public boolean addClue(Clue clue) {
        System.out.println("??????Service????????????");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        clue.setId(id);
        clue.setCreateTime(createTime);
        int count = clueDao.addClue(clue);
        System.out.println("??????????????????");
        return count == 1;
    }

    @Override
    public List<Clue> queryClue() {
        System.out.println("??????clueService");
        List<Clue> list = clueDao.queryClue();
        return list;
    }

    @Override
    public Clue detail(String clueId) {
        System.out.println("detailService??????");
        Clue clue = clueDao.detail(clueId);
        return clue;
    }

    @Override
    public List<Activity> showActivity(String clueId) {
        System.out.println("showService????????????");
        List<Activity> activity = activityDao.showActivity(clueId);
        System.out.println("showService????????????");
        return activity;
    }

    @Override
    public boolean disAssociate(String tcarId) {
        int count = clueActivityRelationDao.disAssociate(tcarId);
        System.out.println("????????????");
        return count == 1;
    }

    @Override
    public List<Activity> queryActivity(Map<String, String> map) {
        System.out.println("????????????Service");
        List<Activity> list = activityDao.queryActivity(map);
        System.out.println("????????????????????????");
        return list;
    }

    @Override
    public boolean associateAct(Map<String, Object> map1) {
        System.out.println("??????Service??????");
        boolean flag = true;
        int count = 0;
        //??????????????????
        String[] s = (String[]) map1.get("activityIds");
        for(String aid : s) {
            count = clueActivityRelationDao.associateAct(UUIDUtil.getUUID(),(String)map1.get("clueId"),aid);
            if(count <= 0) {
                flag = false;
            }
        }
        System.out.println("??????Service??????");
        return flag;
    }

    @Override
    public List<Activity> searchActivityInfo(String activityInfo) {
        System.out.println("????????????Service??????");
        List<Activity> list = activityDao.searchActivityInfo(activityInfo);
        System.out.println("????????????Service??????");
        return list;
    }

    @Override
    public boolean convert(String clueId, String createBy, Tran tran) {
        System.out.println("convertService???????????????");
        boolean flag = true;
        //???????????????id?????????????????????
        Clue clue = clueDao.searchClueById(clueId);
        //??????????????????company???????????????name????????????????????????????????????
        Customer customer = customerDao.searchHasCustomer(clue.getCompany());
        if(customer == null) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setName(clue.getCompany());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateBy(createBy);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());
            //???????????????????????????
            int count = customerDao.insertCustomer(customer);
            if(count != 1) {
                System.out.println("????????????????????????");
                flag = false;
            }
        }
        //?????????????????????????????????????????????
        //11.????????????????????????????????????????????????????????????????????????
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        int count1 = contactsDao.insert(contacts);
        if (count1 != 1){
            flag = false;
            System.out.println("?????????????????????");
        }
        //14.??????clueId?????????????????????
        List<ClueRemark> clist = clueRemarkDao.searchClueRemark(clueId);
        for(ClueRemark cr : clist) {
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(DateTimeUtil.getSysTime());
            customerRemark.setNoteContent(cr.getNoteContent());
            customerRemark.setCustomerId(customer.getId());
            int count2 = customerRemarkDao.insert(customerRemark);
            if (count2 != 1) {
                flag = false;
                System.out.println("???????????????????????????");
            }
            //???????????????????????????
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(cr.getNoteContent());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            contactsRemark.setContactsId(contacts.getId());
            int count3 = contactsRemarkDao.insert(contactsRemark);
            if(count3 != 1) {
                flag = false;
                System.out.println("?????????????????????????????????");
            }
        }
        //24.???????????????????????????clue_activity_relation?????????clueId????????????
        List<ClueActivityRelation> calist = clueActivityRelationDao.searchByClueId(clueId);
        for(ClueActivityRelation clueActivityRelation : calist) {
            ContactsActivityRelation car = new ContactsActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(clueActivityRelation.getActivityId());
            car.setContactsId(contacts.getId());
            int count4 = contactsActivityRelationDao.insert(car);
            if(count4 != 1) {
                flag = false;
                System.out.println("??????????????????????????????????????????");
            }
        }

        //29.???????????????????????????????????????????????????????????????????????????
        if (tran != null) {
            //29.???????????????????????????????????????????????????????????????????????????
            //????????????????????????money???name???expectedDate???stage???source
            tran.setId(UUIDUtil.getUUID());
            tran.setOwner(clue.getOwner());
            tran.setCustomerId(customer.getId());
            tran.setCreateBy(createBy);
            tran.setCreateTime(DateTimeUtil.getSysTime());
            tran.setDescription(clue.getDescription());
            tran.setContactSummary(clue.getContactSummary());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setContactsId(contacts.getId());
            tran.setSource(clue.getSource());
            //type??????
            int count5 = tranDao.insert(tran);
            if(count5 != 1) {
                flag = false;
                System.out.println("????????????????????????");
            }
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setTranId(tran.getId());
            int count6 = tranHistoryDao.insert(tranHistory);
            if(count6 != 1) {
                flag = false;
                System.out.println("??????????????????????????????");
            }
        }
        //36.??????clueId???????????????
        int count7 = clueDao.deleteByClueId(clueId);
        if(count7 != 1) {
            flag = false;
            System.out.println("??????????????????");
        }
        //37.??????clueId??????????????????
        clueRemarkDao.deleteByClueId(clueId);
        //38.??????clueId???????????????????????????
        clueActivityRelationDao.deleteByClueId(clueId);

        return flag;
    }
}








