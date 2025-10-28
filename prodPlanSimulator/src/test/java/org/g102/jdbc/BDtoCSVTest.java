package org.g102.jdbc;

import org.g102.database.BDtoCSV;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BDtoCSVTest {

    @Test
    void generateCSVS() {
        try{
            BDtoCSV.generateCSVS("AS12945S22");
        }catch (Exception e){
            fail("Exception thrown");
        }
    }

}