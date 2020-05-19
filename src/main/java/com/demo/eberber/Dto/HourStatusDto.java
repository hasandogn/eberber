package com.demo.eberber.Dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HourStatusDto {
    public class WeeklyHours {
        public HashMap<String,List<String>> daysAndHours = new HashMap<String, List<String>>();
    }

    public class MounthlyHours {
        public HashMap<String,HashMap<String,String>> weeksAndDaysAndHours = new HashMap<String, HashMap<String,String>>();
    }

    public class onlyHours {
        public String hour;
    }

    public class updateStaffHours {
        public long staffId;
        public Date startDate;
        public Date endDate;
    }


}
