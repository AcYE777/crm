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
        System.out.println("getOwnerService执行");
        List<User> list = userDao.queryUser();
        return list;
    }

    @Override
    public boolean addClue(Clue clue) {
        System.out.println("创建Service开始执行");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        clue.setId(id);
        clue.setCreateTime(createTime);
        int count = clueDao.addClue(clue);
        System.out.println("创建执行结束");
        return count == 1;
    }

    @Override
    public List<Clue> queryClue() {
        System.out.println("展示clueService");
        List<Clue> list = clueDao.queryClue();
        return list;
    }

    @Override
    public Clue detail(String clueId) {
        System.out.println("detailService执行");
        Clue clue = clueDao.detail(clueId);
        return clue;
    }

    @Override
    public List<Activity> showActivity(String clueId) {
        System.out.println("showService开始执行");
        List<Activity> activity = activityDao.showActivity(clueId);
        System.out.println("showService执行结束");
        return activity;
    }

    @Override
    public boolean disAssociate(String tcarId) {
        int count = clueActivityRelationDao.disAssociate(tcarId);
        System.out.println("解除结束");
        return count == 1;
    }

    @Override
    public List<Activity> queryActivity(Map<String, String> map) {
        System.out.println("查询活动Service");
        List<Activity> list = activityDao.queryActivity(map);
        System.out.println("查询活动执行结束");
        return list;
    }

    @Override
    public boolean associateAct(Map<String, Object> map1) {
        System.out.println("关联Service开始");
        boolean flag = true;
        int count = 0;
        //得到活动数组
        String[] s = (String[]) map1.get("activityIds");
        for(String aid : s) {
            count = clueActivityRelationDao.associateAct(UUIDUtil.getUUID(),(String)map1.get("clueId"),aid);
            if(count <= 0) {
                flag = false;
            }
        }
        System.out.println("关联Service结束");
        return flag;
    }

    @Override
    public List<Activity> searchActivityInfo(String activityInfo) {
        System.out.println("查询活动Service开始");
        List<Activity> list = activityDao.searchActivityInfo(activityInfo);
        System.out.println("查询活动Service结束");
        return list;
    }

    @Override
    public boolean convert(String clueId, String createBy, Tran tran) {
        System.out.println("convertService层开始执行");
        boolean flag = true;
        //通过线索的id进行查询线索表
        Clue clue = clueDao.searchClueById(clueId);
        //通过线索里的company和客户中的name判断是否已经存在这个客户
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
            //将这个对象插入表中
            int count = customerDao.insertCustomer(customer);
            if(count != 1) {
                System.out.println("插入客户对象失败");
                flag = false;
            }
        }
        //程序走到这说明客户对象一定存在
        //11.创建一个联系人对象其值利用该线索和已经存在的客户
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
            System.out.println("添加联系人出错");
        }
        //14.根据clueId查询线索备注表
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
                System.out.println("插入客户备注表出错");
            }
            //创建联系人备注信息
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(cr.getNoteContent());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            contactsRemark.setContactsId(contacts.getId());
            int count3 = contactsRemarkDao.insert(contactsRemark);
            if(count3 != 1) {
                flag = false;
                System.out.println("插入联系人备注信息出错");
            }
        }
        //24.查询线索活动关系表clue_activity_relation，根据clueId进行查询
        List<ClueActivityRelation> calist = clueActivityRelationDao.searchByClueId(clueId);
        for(ClueActivityRelation clueActivityRelation : calist) {
            ContactsActivityRelation car = new ContactsActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(clueActivityRelation.getActivityId());
            car.setContactsId(contacts.getId());
            int count4 = contactsActivityRelationDao.insert(car);
            if(count4 != 1) {
                flag = false;
                System.out.println("联系人市场活动关系表插入失败");
            }
        }

        //29.判断传来的交易对象是否为空，若不为空则进行赋值插入
        if (tran != null) {
            //29.判断传来的交易对象是否为空，若不为空则进行赋值插入
            //不需要进行赋值的money，name，expectedDate，stage，source
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
            //type缺欠
            int count5 = tranDao.insert(tran);
            if(count5 != 1) {
                flag = false;
                System.out.println("插入交易信息失败");
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
                System.out.println("插入交易历史信息错误");
            }
        }
        //36.根据clueId删除线索表
        int count7 = clueDao.deleteByClueId(clueId);
        if(count7 != 1) {
            flag = false;
            System.out.println("删除线索失败");
        }
        //37.根据clueId删除线索备注
        clueRemarkDao.deleteByClueId(clueId);
        //38.根据clueId删除线索活动关联表
        clueActivityRelationDao.deleteByClueId(clueId);

        return flag;
    }
}








