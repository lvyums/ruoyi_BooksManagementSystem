package com.ruoyi.system.library;

import com.ruoyi.system.library.domain.Book;
import com.ruoyi.system.library.domain.Borrow;
import com.ruoyi.system.library.domain.Reader;
import com.ruoyi.system.library.domain.Readertype;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 图书管理核心业务逻辑（可独立单元测试，无Spring依赖）
 */
public class LibraryBusinessLogic {

    private final Map<Long, Reader> readers = new HashMap<>();
    private final Map<Long, Readertype> readerTypes = new HashMap<>();
    private final Map<Long, Book> books = new HashMap<>();
    private final Map<Long, Borrow> borrows = new HashMap<>();
    private Long nextBorrowId = 1L;

    // ==================== insertReader ====================

    public boolean insertReader(Reader reader) {
        reader.setRdStatus("0");
        reader.setRdBorrowQty(Long.valueOf(0));
        reader.setRdPwd("123456");
        if (reader.getRdDateReg() == null) {
            reader.setRdDateReg(new Date());
        }
        if (reader.getRdID() == null) {
            reader.setRdID((long) (readers.size() + 1));
        }
        readers.put(reader.getRdID(), reader);
        return true;
    }

    // ==================== borrowBook ====================

    public Borrow borrowBook(Long rdID, Long bkID) {
        Reader reader = readers.get(rdID);
        if (reader == null) {
            throw new IllegalStateException("读者ID不存在");
        }
        if (!"0".equals(reader.getRdStatus())) {
            throw new IllegalStateException("该借书证处于挂失或注销状态，无法借阅");
        }

        Readertype type = readerTypes.get(reader.getRdType());
        if (type == null) {
            throw new IllegalStateException("读者类别异常");
        }
        if (reader.getRdBorrowQty() >= type.getCanLendQty()) {
            throw new IllegalStateException("已达到最大借书数量限制");
        }

        Book book = books.get(bkID);
        if (book == null) {
            throw new IllegalStateException("图书ID不存在");
        }
        if (!"0".equals(book.getBkStatus())) {
            throw new IllegalStateException("图书当前不是在馆状态，无法借阅");
        }

        Borrow borrow = new Borrow();
        borrow.setBorrowID(nextBorrowId++);
        borrow.setRdID(rdID);
        borrow.setBkID(bkID);
        borrow.setLdDateOut(new Date());
        borrow.setLdContinueTimes(0L);
        borrow.setLsHasReturn("0");
        borrow.setLdDateRetPlan(new Date(System.currentTimeMillis() + type.getCanLendDay() * 24L * 60 * 60 * 1000));
        borrows.put(borrow.getBorrowID(), borrow);

        book.setBkStatus("1");
        reader.setRdBorrowQty(reader.getRdBorrowQty() + 1);
        return borrow;
    }

    // ==================== returnBook ====================

    public void returnBook(Long borrowID, Boolean isLost) {
        Borrow borrow = borrows.get(borrowID);
        if (borrow == null) {
            throw new IllegalStateException("借阅记录不存在");
        }
        if ("1".equals(borrow.getLsHasReturn())) {
            throw new IllegalStateException("该图书已经归还过了");
        }

        Date now = new Date();
        long overDays = 0;
        BigDecimal fine = BigDecimal.ZERO;

        if (isLost != null && isLost) {
            Book book = books.get(borrow.getBkID());
            fine = book.getBkPrice().multiply(new BigDecimal(3));
            book.setBkStatus("2");
        } else {
            if (now.after(borrow.getLdDateRetPlan())) {
                long diff = now.getTime() - borrow.getLdDateRetPlan().getTime();
                overDays = diff / (1000 * 3600 * 24);
            }
            Book book = books.get(borrow.getBkID());
            book.setBkStatus("0");
            if (overDays > 0) {
                Readertype type = readerTypes.get(readers.get(borrow.getRdID()).getRdType());
                fine = type.getPunishRate().multiply(new BigDecimal(overDays));
            }
        }

        borrow.setLdDateRetAct(now);
        borrow.setLdOverDay(overDays);
        borrow.setLdOverMoney(fine);
        borrow.setLdPunishMoney(fine);
        borrow.setLsHasReturn("1");

        Reader reader = readers.get(borrow.getRdID());
        reader.setRdBorrowQty(reader.getRdBorrowQty() - 1);
    }

    // ==================== 辅助方法（测试用） ====================

    public void addReader(Reader reader) {
        readers.put(reader.getRdID(), reader);
    }

    public void addReaderType(Readertype type) {
        readerTypes.put(type.getRdType(), type);
    }

    public void addBook(Book book) {
        books.put(book.getBkID(), book);
    }

    public void addBorrow(Borrow borrow) {
        borrows.put(borrow.getBorrowID(), borrow);
    }

    public Reader getReader(Long rdID) {
        return readers.get(rdID);
    }

    public Book getBook(Long bkID) {
        return books.get(bkID);
    }

    public Borrow getBorrow(Long borrowID) {
        return borrows.get(borrowID);
    }
}
