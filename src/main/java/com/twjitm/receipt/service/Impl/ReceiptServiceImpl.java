package com.twjitm.receipt.service.Impl;

import com.twjitm.receipt.dao.IReceiptDao;
import com.twjitm.receipt.entity.Equzlize;
import com.twjitm.receipt.entity.Receipt;
import com.twjitm.receipt.enums.ReceiptStateType;
import com.twjitm.receipt.service.IReceiptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 文江 on 2017/9/6.
 */
@Service
public class ReceiptServiceImpl implements IReceiptService {
    @Resource
    IReceiptDao receiptDao;

    public void addReceipt(Receipt receipt) {
        receiptDao.insertReceipt(receipt);
    }

    public void updateReceipt(Receipt receipt) {
        receiptDao.updateReceipt(receipt);
    }

    public List<Receipt> getReceiptByState(ReceiptStateType type) {
        return receiptDao.getReceiptByState(type);
    }

    public List<Equzlize> getEquzlizeList(List<Receipt> reports, List<Long> uIds) {
        List<Equzlize> list = new ArrayList<Equzlize>();
        float allNum = 0;
        Map<Long, Double> anyOne = new HashMap<Long, Double>();
        for (Receipt report : reports) {
            allNum += report.getMoney();
            if (anyOne.containsKey(report.getUid())) {
                anyOne.put(report.getUid(), anyOne.get(report) + report.getMoney());
            } else {
                anyOne.put(report.getUid(), report.getMoney());
            }
        }
        float avg = allNum / uIds.size();
        Map<Long, Double> havgMap = new HashMap<Long, Double>();//insert
        Map<Long, Double> davgMap = new ConcurrentHashMap<Long, Double>();//out
        for (int i = 0; i < uIds.size(); i++) {
            double value = anyOne.get(uIds.get(i)) - avg;
            if (value > 0) {
                havgMap.put(uIds.get(i), value);
            } else {
                davgMap.put(uIds.get(i), value);
            }
        }
        for (Map.Entry<Long, Double> entry : havgMap.entrySet()) {
            double inmoney = entry.getValue();
            double chamoney = entry.getValue();
            for (Map.Entry<Long, Double> dentry : davgMap.entrySet()) {
                inmoney = inmoney - Math.abs(dentry.getValue());
                if (inmoney < 0) {
                    Equzlize equzlize = new Equzlize();
                    equzlize.setEquxUid(dentry.getKey());
                    equzlize.setBeqUzUid(entry.getKey());
                    //dentry->entry all
                    equzlize.setMuchMoney(Math.abs(chamoney));
                    davgMap.put(dentry.getKey(), davgMap.get(dentry.getKey()) + Math.abs(chamoney));
                    list.add(equzlize);
                    break;
                } else {
                    Equzlize equzlize = new Equzlize();
                    equzlize.setEquxUid(dentry.getKey());
                    equzlize.setBeqUzUid(entry.getKey());
                    //dentry->entry little
                    chamoney = inmoney;
                    equzlize.setMuchMoney(Math.abs(dentry.getValue()));
                    list.add(equzlize);
                    davgMap.remove(dentry.getKey());
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getEquxUid() + "->give-->" + list.get(i).getBeqUzUid() + ":---->" + list.get(i).getMuchMoney());
        }
        return list;
    }
}