package org.skysql.jdbc;

import org.junit.Test;
import org.skysql.jdbc.internal.common.Utils;

import java.sql.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.Assert.assertEquals;

public class DateTest extends BaseTest{
    static { Logger.getLogger("").setLevel(Level.OFF); }

    public DateTest() {

    }

    @Test
    public void dateTest() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("drop table if exists date_test");
        stmt.executeUpdate("create table date_test (id int not null primary key auto_increment, d_test date,dt_test datetime, t_test time)");
        Date date = Date.valueOf("2009-01-17");
        Timestamp timestamp = Timestamp.valueOf("2009-01-17 15:41:01");
        Time time = Time.valueOf("23:59:59");
        PreparedStatement ps = connection.prepareStatement("insert into date_test (d_test, dt_test, t_test) values (?,?,?)");
        ps.setDate(1,date);
        ps.setTimestamp(2,timestamp);
        ps.setTime(3,time);
        ps.executeUpdate();
        ResultSet rs = stmt.executeQuery("select d_test, dt_test, t_test from date_test");
        assertEquals(true,rs.next());
        Date date2 = rs.getDate(1);
        Date date3 = rs.getDate("d_test");
        Time time2=rs.getTime(3);
        Time time3=rs.getTime("t_test");
        Timestamp timestamp2=rs.getTimestamp(2);
        Timestamp timestamp3=rs.getTimestamp("dt_test");
        assertEquals(date.toString(), date2.toString());
        assertEquals(date.toString(), date3.toString());
        assertEquals(time.toString(), time2.toString());
        assertEquals(time.toString(), time3.toString());
        assertEquals(timestamp.toString(), timestamp2.toString());
        assertEquals(timestamp.toString(), timestamp3.toString());

    }
    @Test(expected = SQLException.class)
    public void dateTest2() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select 1");
        rs.next();
        rs.getDate(1);
    }

    @Test(expected = SQLException.class)
    public void dateTest3() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select 1 as a");
        rs.next();
        rs.getDate("a");
    }
    @Test(expected = SQLException.class)
    public void timeTest3() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select 'aaa' as a");
        rs.next();
        rs.getTimestamp("a");
    }


    @Test
    public void timePackTest() {
        for(int hours = 0;hours<24;hours++) {
            for(int minutes=0;minutes<60;minutes++) {
                for(int seconds = 0;seconds<60;seconds++) {
                    long millis = hours*60*60*1000 + minutes*60*1000 + seconds*1000;
                    int packed = Utils.packTime(millis);
                    long unPacked = Utils.unpackTime(packed);
                    assertEquals(millis, unPacked);
                }
            }
        }
    }

    @Test
    public void yearTest() throws SQLException {
        connection.createStatement().execute("drop table if exists yeartest");
        connection.createStatement().execute("create table yeartest (y1 year, y2 year(2))");
        connection.createStatement().execute("insert into yeartest values (null, null), (1901, 70), (0, 0), (2155, 69)");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from yeartest");
        while(rs.next()) {
            System.out.println("--");

            System.out.println(rs.getObject(1));
            System.out.println(rs.getObject(2));

        }
    }
    @Test
    public void timeTest() throws SQLException {
        connection.createStatement().execute("drop table if exists timetest");
        connection.createStatement().execute("create table timetest (t time)");
        connection.createStatement().execute("insert into timetest values (null), ('-838:59:59'), ('00:00:00'), ('838:59:59')");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from timetest");
        while(rs.next()) {
            System.out.println("--");

            System.out.println(rs.getObject(1));
//            System.out.println(rs.getObject(2));

        }
        rs.close();
        Calendar cal = Calendar.getInstance();
        rs = stmt.executeQuery("select '11:11:11'");
        rs.next();
        Time t = rs.getTime(1,cal);
        assertEquals(t.toString(), "11:11:11");
    }

}
