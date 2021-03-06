package com.twjitm.receipt.dao;

import com.twjitm.receipt.entity.Receipt;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 文江 on 2017/9/6.
 */
@Repository
public interface IReceiptDao {
    public void insertReceipt(Receipt receipt);

    public void updateReceipt(Receipt receipt);

    public void deleteReceipt(int receiptId);

    public List<Receipt> getReceiptByState(int state);

    public Receipt getReceiptByid(int id);

}
