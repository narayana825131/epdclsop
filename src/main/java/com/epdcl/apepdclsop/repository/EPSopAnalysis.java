package com.epdcl.apepdclsop.repository;

import java.util.List;

import com.epdcl.apepdclsop.vo.CommonVo;

public interface EPSopAnalysis {

	public List<CommonVo> getFuseOffReportAbsData(CommonVo comvo) throws Exception;
}
