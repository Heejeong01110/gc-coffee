package com.example.gccoffee.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    @DisplayName("잘못된 형식의 이메일을 생성하면 에러가 발생한다.")
    public void testInvalidEamil() {
        assertThrows(IllegalArgumentException.class, () ->{
            Email email = new Email("asdfg");
        });
    }

    @Test
    @DisplayName("올바른 형식의 이메일을 생성 가능하다.")
    public void testValidEamil() {
        Email email = new Email("testEmail@gmail.com");
        assertEquals(email.getAddress(),"testEmail@gmail.com");
    }

    @Test
    @DisplayName("이메일 일치 여부를 확인 가능하다.")
    public void testEqualEamil() {
        Email email = new Email("testEmail1@gmail.com");
        Email email2 = new Email("testEmail@gmail.com");
        assertEquals(email,email2);
    }

}