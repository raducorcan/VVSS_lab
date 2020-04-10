package tasks.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private SimpleDateFormat formatter;

    @BeforeEach
    void setUp() {
        formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Test
    void TC_01() throws ParseException {
        Task t = new Task("title", formatter.parse("09/04/2020 07:00:00"));
        Date current = formatter.parse("09/04/2020 08:00:00");
        Date result = t.nextTimeAfter(current);
        assertNull(result);
    }

    @Test
    void TC_02() throws ParseException {
        Task t = new Task("title", formatter.parse("09/04/2020 08:00:00"));
        Date current = formatter.parse("09/04/2020 07:00:00");
        Date result = t.nextTimeAfter(current);
        assert (result.equals(formatter.parse("09/04/2020 08:00:00")));
    }

    @Test
    void TC_03() throws ParseException {
        Task t = new Task("title", formatter.parse("09/04/2020 08:00:00"), formatter.parse("09/04/2020 10:00:00"), 1800);
        Date current = formatter.parse("09/04/2020 07:00:00");
        Date result = t.nextTimeAfter(current);
        assert (result.equals(formatter.parse("09/04/2020 08:00:00")));
    }

    @Test
    void TC_04() throws ParseException {
        Task t = new Task("title", formatter.parse("09/04/2020 08:00:00"), formatter.parse("09/04/2020 10:00:00"), 1800);
        Date current = formatter.parse("09/04/2020 08:00:00");
        Date result = t.nextTimeAfter(current);
        assert (result.equals(formatter.parse("09/04/2020 08:00:00")));
    }

    @Test
    void TC_05() throws ParseException {
        Task t = new Task("title", formatter.parse("09/04/2020 08:00:00"), formatter.parse("09/04/2020 07:00:00"), 1800);
        Date current = formatter.parse("09/04/2020 12:00:00");
        Date result = t.nextTimeAfter(current);
        assertNull(result);
    }

    @Test
    void TC_06() throws ParseException {
        Task t = new Task("title", formatter.parse("09/04/2020 08:00:00"), formatter.parse("09/04/2020 10:00:00"), 1800);
        Date current = formatter.parse("09/04/2020 08:15:00");
        Date result = t.nextTimeAfter(current);
        assert (result.equals(formatter.parse("09/04/2020 08:30:00")));
    }

    @Test
    void TC_07() throws ParseException {
        Task t = new Task("title", formatter.parse("09/04/2020 08:00:00"), formatter.parse("09/04/2020 10:00:00"), 1800);
        Date current = formatter.parse("09/04/2020 09:00:00");
        Date result = t.nextTimeAfter(current);
        assert (result.equals(formatter.parse("09/04/2020 09:00:00")));
    }

    @Test
    void TC_08() throws ParseException {
        Task t = new Task("title", formatter.parse("09/04/2020 08:00:00"), formatter.parse("09/04/2020 10:00:00"), 1800);
        Date current = formatter.parse("09/04/2020 10:00:00");
        Date result = t.nextTimeAfter(current);
        assert (result.equals(formatter.parse("09/04/2020 10:00:00")));
    }

    @Test
    void TC_09() throws ParseException {
        Task t = new Task("title", formatter.parse("09/04/2020 08:00:00"), formatter.parse("09/04/2020 10:00:00"), 1800);
        Date current = formatter.parse("09/04/2020 09:30:00");
        Date result = t.nextTimeAfter(current);
        assert (result.equals(formatter.parse("09/04/2020 09:30:00")));
    }
}