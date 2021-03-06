package com.ngtesting.platform.service.impl;

import com.ngtesting.platform.config.Constant;
import com.ngtesting.platform.entity.TestProject;
import com.ngtesting.platform.entity.TestUser;
import com.ngtesting.platform.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportServiceImpl extends BaseServiceImpl implements ReportService {
    @Override
    public Map<String, List<Object>> chart_design_progress_by_project(Long projectId, TestProject.ProjectType type, Integer numb) {
        Map<String, List<Object>> map = new LinkedHashMap<>();

        List<Object> xList = new LinkedList<>();
        List<Object> numbList = new LinkedList<>();
        List<Object> totalList = new LinkedList<>();

        List<Object[]> ls = getDao().getListBySQL("{call chart_design_progress_by_project(?,?,?)}",
                projectId, type.toString(), numb);
        Integer sum = null;
        for (Object[] arr : ls) {
            if(sum == null) {
                sum = Integer.valueOf(arr[2].toString());
            }
            xList.add(arr[0].toString());
            numbList.add(arr[1]);

            sum += Integer.valueOf(arr[1].toString());
            totalList.add(sum);
        }
        map.put("xList", xList);
        map.put("numbList", numbList);
        map.put("totalList", totalList);

        return map;
    }

    @Override
    public Map<String, List<Object>> chart_excution_process_by_project(Long projectId, TestProject.ProjectType type, Integer numb) {
        List<Object[]> ls = getDao().getListBySQL("{call chart_execution_process_by_project(?,?,?)}",
                projectId, type.toString(), numb);

        return countByStatus(ls);
    }

    @Override
    public List<Map<Object, Object>> chart_execution_result_by_plan(Long planId) {
        List<Object[]> ls = getDao().getListBySQL("{call chart_execution_result_by_plan(?)}",
                planId);

        Map<String, String> map = new HashMap();
        for (Object[] arr : ls) {
            map.put(arr[0].toString(), arr[1].toString());
        }

        List<Map<Object, Object>> data = orderByStatus(map);
        return data;
    }

    @Override
    public Map<String, List<Object>> chart_execution_process_by_plan(Long planId, Integer numb) {
        List<Object[]> ls = getDao().getListBySQL("{call chart_execution_process_by_plan(?,?)}",
                planId, numb);

        return countByStatus(ls);
    }

    @Override
    public Map<String, Object> chart_execution_process_by_plan_user(Long planId, Integer numb) {
        List<Object[]> ls = getDao().getListBySQL("{call chart_execution_process_by_plan_user(?,?)}",
                planId, numb);

        return countByUser(ls);
    }

    @Override
    public Map<String, Object> chart_execution_progress_by_plan(Long planId, Integer numb) {
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, List<Object>> series = new LinkedHashMap<>();

        List<Object> xList = new LinkedList<>();
        List<Object> numbList = new LinkedList<>();

        List<Object[]> ls = getDao().getListBySQL("{call chart_execution_progress_by_plan(?,?)}",
                planId, numb);
        Integer exeSum = 0; int i = 0;
        for (Object[] arr : ls) {
            xList.add(arr[0].toString());

            Integer totalNumb = Integer.valueOf(arr[2].toString());
            Integer exeNumb = arr[1]==null?0:Integer.valueOf(arr[1].toString());
            exeSum += exeNumb;
            numbList.add(totalNumb - exeSum);
        }
        map.put("xList", xList);

        map.put("series", series);
        series.put("剩余用例", numbList);

        return map;
    }

    @Override
    public Map<String, List<Object>> countByStatus(List<Object[]> ls) {
        Map<String, List<Object>> map = new LinkedHashMap<>();

        List<Object> xList = new LinkedList<>();
        List<Object> passList = new LinkedList<>();
        List<Object> failList = new LinkedList<>();
        List<Object> blockList = new LinkedList<>();

        String day = null;
        Map<String, Object> dayStatus = new HashMap();

        Object a[] = {"last",null,null,null};
        ls.add(a);
        for (Object[] arr : ls) {
            String dayTemp = arr[0].toString();

            if (!dayTemp.equals(day) && day != null) { // 新的一天
                xList.add(day);

                if (!dayStatus.containsKey("pass")) {
                    passList.add(0);
                } else {
                    passList.add(dayStatus.get("pass"));
                }

                if (!dayStatus.containsKey("fail")) {
                    failList.add(0);
                } else {
                    failList.add(dayStatus.get("fail"));
                }

                if (!dayStatus.containsKey("block")) {
                    blockList.add(0);
                } else {
                    blockList.add(dayStatus.get("block"));
                }

                dayStatus = new HashMap();
            }

            // 同一天，对多行内容进行统计
            if (arr[1] != null) {
                String status = arr[1].toString();
                if ("pass".equals(status)) {
                    dayStatus.put("pass", arr[2]);
                } else if("fail".equals(status)) {
                    dayStatus.put("fail", arr[2]);
                } else if("block".equals(status)) {
                    dayStatus.put("block", arr[2]);
                }
            }

            day = dayTemp;
        }

        map.put("xList", xList);
        map.put("passList", passList);
        map.put("failList", failList);
        map.put("blockList", blockList);

        return map;
    }

    @Override
    public Map<String, Object> countByUser(List<Object[]> ls) {
        Map<String, Object> map = new LinkedHashMap<>();

        List<Object> xList = new LinkedList<>();
        Map<String, List<Object>> byUserMap = new TreeMap<>();

        String day = null;

        Object a[] = {"last",null,null,null};
        ls.add(a);

        for (Object[] arr : ls) {
            if (arr[1] != null && !arr[1].equals("null")) {
                String userId = arr[1].toString();
                String userName = getUserName(userId);

                if (!byUserMap.containsKey(userName)) {
                    byUserMap.put(userName, new LinkedList<>());
                }
            }
        }

        Map<String, Object> dayMap = new HashMap();
        for (Object[] arr : ls) {
            String dayTemp = arr[0].toString();
            String userId = arr[1]!=null? arr[1].toString(): null;
            Integer numb = arr[2]!=null?Integer.valueOf(arr[2].toString()): null;
            Integer sum =  arr[3]!=null?Integer.valueOf(arr[3].toString()): null;

            if (!dayTemp.equals(day) && day != null) { // 新的一天
                xList.add(day);

                for (String userName: byUserMap.keySet()) {
                    if (dayMap.containsKey(userName)) {
                        byUserMap.get(userName).add(dayMap.get(userName));
                    } else {
                        byUserMap.get(userName).add(0);
                    }
                }

                dayMap = new HashMap();
            }

            // 同一天，对多行内容进行统计
            if (userId != null) {
                dayMap.put(getUserName(userId), numb);
            }

            day = dayTemp;
        }

        map.put("xList", xList);
        map.put("series", byUserMap);

        return map;
    }

    @Override
    public List<Map<Object, Object>> orderByStatus(Map map) {
        List<Map<Object, Object>> data2 = new LinkedList<>();
        List<String> keys = Arrays.asList("pass","fail","block", "untest");

        for(String key : keys) {
            Map<Object, Object> map2 = new HashMap();
            map2.put("name", Constant.ExeStatus.get(key));
            map2.put("value", map.get(key)!=null?map.get(key): 0);
            data2.add(map2);
        }

        return data2;
    }

    @Override
    public String getUserName(String id) {
        if (id == null) {
           return null;
        }
        TestUser user = (TestUser) get(TestUser.class, Long.valueOf(id));

        return user.getName() + '-' + id;
    }

}

