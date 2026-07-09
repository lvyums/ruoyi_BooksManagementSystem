package com.ruoyi.system.library;

import com.ruoyi.system.library.domain.Book;
import com.ruoyi.system.library.domain.Borrow;
import com.ruoyi.system.library.domain.Reader;
import com.ruoyi.system.library.domain.Readertype;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * 图书管理核心业务逻辑单元测试
 * 测试 insertReader、borrowBook、returnBook 三个核心方法
 */
public class LibraryBusinessLogicTest {

    private LibraryBusinessLogic logic;

    @Before
    public void setUp() {
        logic = new LibraryBusinessLogic();
    }

    // ==================== insertReader 测试 ====================

    @Test
    public void insertReader_shouldSetDefaultValues() {
        Reader reader = new Reader();
        reader.setRdName("张三");
        reader.setRdType(1L);

        logic.insertReader(reader);

        assertEquals("0", reader.getRdStatus());
        assertEquals(Long.valueOf(0), reader.getRdBorrowQty());
        assertNotNull(reader.getRdPwd());
        assertNotNull(reader.getRdDateReg());
    }

    @Test
    public void insertReader_shouldReturnTrue() {
        Reader reader = new Reader();
        reader.setRdName("李四");

        boolean result = logic.insertReader(reader);

        assertTrue(result);
    }

    @Test
    public void insertReader_shouldGenerateUniqueIdsAfterDeletion() {
        Reader r1 = new Reader();
        r1.setRdName("读者1");
        logic.insertReader(r1);
        Long id1 = r1.getRdID();

        Reader r2 = new Reader();
        r2.setRdName("读者2");
        logic.insertReader(r2);
        Long id2 = r2.getRdID();

        // Simulate deletion by removing from internal map via helper
        logic.removeReader(id1);

        Reader r3 = new Reader();
        r3.setRdName("读者3");
        logic.insertReader(r3);
        Long id3 = r3.getRdID();

        // r3 must NOT get the same ID as r2 (the existing reader)
        assertNotEquals("New reader must not collide with existing reader", id2, id3);
    }

    // ==================== borrowBook 测试 ====================

    @Test(expected = IllegalStateException.class)
    public void borrowBook_shouldRejectInactiveReader() {
        Reader reader = new Reader();
        reader.setRdID(1L);
        reader.setRdStatus("1");
        reader.setRdBorrowQty(0L);
        reader.setRdType(1L);
        logic.addReader(reader);

        Readertype type = new Readertype();
        type.setRdType(1L);
        type.setCanLendQty(5L);
        type.setCanLendDay(30L);
        logic.addReaderType(type);

        Book book = new Book();
        book.setBkID(100L);
        book.setBkStatus("0");
        logic.addBook(book);

        logic.borrowBook(1L, 100L);
    }

    @Test(expected = IllegalStateException.class)
    public void borrowBook_shouldRejectWhenBookNotAvailable() {
        Reader reader = new Reader();
        reader.setRdID(2L);
        reader.setRdStatus("0");
        reader.setRdBorrowQty(0L);
        reader.setRdType(1L);
        logic.addReader(reader);

        Readertype type = new Readertype();
        type.setRdType(1L);
        type.setCanLendQty(5L);
        type.setCanLendDay(30L);
        logic.addReaderType(type);

        Book book = new Book();
        book.setBkID(200L);
        book.setBkStatus("1");
        logic.addBook(book);

        logic.borrowBook(2L, 200L);
    }

    @Test(expected = IllegalStateException.class)
    public void borrowBook_shouldRejectWhenOverLimit() {
        Reader reader = new Reader();
        reader.setRdID(3L);
        reader.setRdStatus("0");
        reader.setRdBorrowQty(5L);
        reader.setRdType(1L);
        logic.addReader(reader);

        Readertype type = new Readertype();
        type.setRdType(1L);
        type.setCanLendQty(5L);
        type.setCanLendDay(30L);
        logic.addReaderType(type);

        Book book = new Book();
        book.setBkID(300L);
        book.setBkStatus("0");
        logic.addBook(book);

        logic.borrowBook(3L, 300L);
    }

    @Test
    public void borrowBook_shouldSucceedForValidReaderAndBook() {
        Reader reader = new Reader();
        reader.setRdID(4L);
        reader.setRdStatus("0");
        reader.setRdBorrowQty(0L);
        reader.setRdType(1L);
        logic.addReader(reader);

        Readertype type = new Readertype();
        type.setRdType(1L);
        type.setCanLendQty(5L);
        type.setCanLendDay(30L);
        logic.addReaderType(type);

        Book book = new Book();
        book.setBkID(400L);
        book.setBkStatus("0");
        logic.addBook(book);

        Borrow result = logic.borrowBook(4L, 400L);

        assertNotNull(result);
        assertEquals(Long.valueOf(4L), result.getRdID());
        assertEquals(Long.valueOf(400L), result.getBkID());
        assertEquals("0", result.getLsHasReturn());
        assertNotNull(result.getLdDateOut());
        assertNotNull(result.getLdDateRetPlan());
        Reader updated = logic.getReader(4L);
        assertEquals(Long.valueOf(1), updated.getRdBorrowQty());
        Book updatedBook = logic.getBook(400L);
        assertEquals("1", updatedBook.getBkStatus());
    }

    // ==================== returnBook 测试 ====================

    @Test(expected = IllegalStateException.class)
    public void returnBook_shouldRejectAlreadyReturned() {
        Borrow borrow = new Borrow();
        borrow.setBorrowID(1L);
        borrow.setRdID(5L);
        borrow.setBkID(500L);
        borrow.setLsHasReturn("1");
        logic.addBorrow(borrow);

        logic.returnBook(1L, false);
    }

    @Test
    public void returnBook_shouldCalculateFineForOverdue() {
        Reader reader = new Reader();
        reader.setRdID(6L);
        reader.setRdStatus("0");
        reader.setRdBorrowQty(1L);
        reader.setRdType(1L);
        logic.addReader(reader);

        Readertype type = new Readertype();
        type.setRdType(1L);
        type.setCanLendQty(5L);
        type.setCanLendDay(30L);
        type.setPunishRate(new BigDecimal("0.50"));
        logic.addReaderType(type);

        Book book = new Book();
        book.setBkID(600L);
        book.setBkStatus("1");
        logic.addBook(book);

        Borrow borrow = new Borrow();
        borrow.setBorrowID(2L);
        borrow.setRdID(6L);
        borrow.setBkID(600L);
        borrow.setLsHasReturn("0");
        Date tenDaysAgo = new Date(System.currentTimeMillis() - 10L * 24 * 60 * 60 * 1000);
        borrow.setLdDateRetPlan(tenDaysAgo);
        borrow.setLdDateOut(new Date(System.currentTimeMillis() - 20L * 24 * 60 * 60 * 1000));
        logic.addBorrow(borrow);

        logic.returnBook(2L, false);

        Borrow returned = logic.getBorrow(2L);
        assertEquals("1", returned.getLsHasReturn());
        assertNotNull(returned.getLdDateRetAct());
        assertTrue(returned.getLdOverDay() > 0);
        assertTrue(returned.getLdOverMoney().compareTo(BigDecimal.ZERO) > 0);
        Book updatedBook = logic.getBook(600L);
        assertEquals("0", updatedBook.getBkStatus());
        Reader updated = logic.getReader(6L);
        assertEquals(Long.valueOf(0), updated.getRdBorrowQty());
    }

    @Test
    public void returnBook_shouldHandleLostBook() {
        Reader reader = new Reader();
        reader.setRdID(7L);
        reader.setRdStatus("0");
        reader.setRdBorrowQty(1L);
        reader.setRdType(1L);
        logic.addReader(reader);

        Readertype type = new Readertype();
        type.setRdType(1L);
        type.setCanLendQty(5L);
        type.setCanLendDay(30L);
        logic.addReaderType(type);

        Book book = new Book();
        book.setBkID(700L);
        book.setBkStatus("1");
        book.setBkPrice(new BigDecimal("50.00"));
        logic.addBook(book);

        Borrow borrow = new Borrow();
        borrow.setBorrowID(3L);
        borrow.setRdID(7L);
        borrow.setBkID(700L);
        borrow.setLsHasReturn("0");
        borrow.setLdDateRetPlan(new Date(System.currentTimeMillis() + 10L * 24 * 60 * 60 * 1000));
        borrow.setLdDateOut(new Date());
        logic.addBorrow(borrow);

        logic.returnBook(3L, true);

        Borrow returned = logic.getBorrow(3L);
        assertEquals("1", returned.getLsHasReturn());
        assertEquals(new BigDecimal("150.00"), returned.getLdOverMoney());
        Book updatedBook = logic.getBook(700L);
        assertEquals("2", updatedBook.getBkStatus());
    }

    @Test
    public void returnBook_noOverdueNoFine() {
        Reader reader = new Reader();
        reader.setRdID(8L);
        reader.setRdStatus("0");
        reader.setRdBorrowQty(1L);
        reader.setRdType(1L);
        logic.addReader(reader);

        Readertype type = new Readertype();
        type.setRdType(1L);
        type.setCanLendQty(5L);
        type.setCanLendDay(30L);
        logic.addReaderType(type);

        Book book = new Book();
        book.setBkID(800L);
        book.setBkStatus("1");
        logic.addBook(book);

        Borrow borrow = new Borrow();
        borrow.setBorrowID(4L);
        borrow.setRdID(8L);
        borrow.setBkID(800L);
        borrow.setLsHasReturn("0");
        borrow.setLdDateRetPlan(new Date(System.currentTimeMillis() + 10L * 24 * 60 * 60 * 1000));
        borrow.setLdDateOut(new Date());
        logic.addBorrow(borrow);

        logic.returnBook(4L, false);

        Borrow returned = logic.getBorrow(4L);
        assertEquals("1", returned.getLsHasReturn());
        assertEquals(Long.valueOf(0), returned.getLdOverDay());
        assertEquals(BigDecimal.ZERO, returned.getLdOverMoney());
    }
}
