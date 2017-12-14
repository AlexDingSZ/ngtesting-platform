package com.ngtesting.platform.service.impl;

import com.ngtesting.platform.entity.TestHistory;
import com.ngtesting.platform.service.HistoryService;
import com.ngtesting.platform.util.BeanUtilEx;
import com.ngtesting.platform.vo.TestHistoryVo;
import com.ngtesting.platform.vo.UserVo;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class HistoryServiceImpl extends BaseServiceImpl implements HistoryService {

	@Override
	public List<TestHistory> list(Long projectId) {
		DetachedCriteria dc = DetachedCriteria.forClass(TestHistory.class);

		dc.add(Restrictions.eq("projectId", projectId));
		dc.add(Restrictions.eq("deleted", Boolean.FALSE));
		dc.add(Restrictions.eq("disabled", Boolean.FALSE));

		dc.addOrder(Order.asc("createTime"));

		List<TestHistory> ls = findAllByCriteria(dc);

		return ls;
	}

    @Override
    public TestHistoryVo getById(Long id) {
		TestHistory po = (TestHistory) get(TestHistory.class, id);
		TestHistoryVo vo = genVo(po);

        return vo;
    }
    @Override
    public TestHistory create(Long projectId, UserVo optUser, String action,
                              TestHistory.TargetType entityType, Long entityId, String name) {
        TestHistory history = new TestHistory();

        history.setTitle("用户" + optUser.getName() + action + entityType.name + "\"" + name + "\"");
        history.setProjectId(projectId);
        history.setEntityId(entityId);
        history.setEntityType(entityType);
        history.setOptUserId(optUser.getId());
        saveOrUpdate(history);

        return history;
    }

	@Override
	public List<TestHistoryVo> genVos(List<TestHistory> pos) {
        List<TestHistoryVo> vos = new LinkedList<>();

        for (TestHistory po: pos) {
			TestHistoryVo vo = genVo(po);
        	vos.add(vo);
        }
		return vos;
	}

	@Override
	public TestHistoryVo genVo(TestHistory po) {
		TestHistoryVo vo = new TestHistoryVo();
		BeanUtilEx.copyProperties(vo, po);

		return vo;
	}

}
