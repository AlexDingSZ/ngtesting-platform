package cn.linkr.testspace.service;

import java.util.List;

import cn.linkr.testspace.entity.EvtGuest;
import cn.linkr.testspace.entity.TestCase;
import cn.linkr.testspace.vo.GuestVo;
import cn.linkr.testspace.vo.Page;
import cn.linkr.testspace.vo.TestCaseTreeVo;
import cn.linkr.testspace.vo.TestCaseVo;

public interface TestCaseService extends BaseService {

	List<TestCase> query(Long projectId, Long moduleId, String keywords);
	TestCaseTreeVo buildTree(List<TestCase> ls);

	List<TestCaseVo> genVos(List<TestCase> pos);

	TestCaseVo genVo(TestCase po);

	
	
}