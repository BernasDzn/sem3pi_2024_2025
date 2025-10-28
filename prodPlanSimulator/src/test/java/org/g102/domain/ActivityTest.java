package org.g102.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    @Test
    void getSlack() {
        int LF=10, EF=8;
        Activity ac1= new Activity("A1", "Activity 1", 10, "days", 10, "euros");
        ac1.setLatestFinish(LF);
        ac1.setEarliestFinish(EF);
        int expectedSlack = LF - EF;
        assertEquals(expectedSlack, ac1.getSlack());
    }

    @Test
    void getSlackNoSlack() {
        int LF=0, EF=0;
        Activity ac1= new Activity("A1", "Activity 1", 10, "days", 10, "euros");
        ac1.setLatestFinish(LF);
        ac1.setEarliestFinish(EF);
        int expectedSlack = LF - EF;
        assertEquals(expectedSlack, ac1.getSlack());
    }

}