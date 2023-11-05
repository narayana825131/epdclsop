package com.epdcl.apepdclsop.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epdcl.apepdclsop.repository.EPSopAnalysis;
import com.epdcl.apepdclsop.vo.CommonVo;


@Service
public class EPSopAnalysisServiceImpl implements EPSopAnalysisService {

	Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private EPSopAnalysis epSopAnalysis;

	@Override
	public List<CommonVo> getFuseOffReportAbs(CommonVo comvo) throws Exception {
		return epSopAnalysis.getFuseOffReportAbsData(comvo);
	}
	
	
}
