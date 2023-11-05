package com.epdcl.apepdclsop.service;

import java.util.List;

import com.epdcl.apepdclsop.vo.CommonVo;

public interface EPSopAnalysisService {

	public List<CommonVo> getFuseOffReportAbs(CommonVo comvo) throws Exception;

	public List<CommonVo> getOverHeadBreakdown(CommonVo comvo) throws Exception;

	public List<CommonVo> getUGCableBreakDown(CommonVo comvo) throws Exception;

	public List<CommonVo> getDTrFailures(CommonVo comvo) throws Exception;

	public List<CommonVo> getvoltageFluctuation(CommonVo comvo) throws Exception;

	public List<CommonVo> getVoltageFluctuationExpansion(CommonVo comvo) throws Exception;

	public List<CommonVo> getReplacementDefectiveMeters(CommonVo comvo) throws Exception;

	public List<CommonVo> getReplacementBurntMetersConsumer(CommonVo comvo) throws Exception;

	public List<CommonVo> getReleaseNewConnectionExisting(CommonVo comvo) throws Exception;

	public List<CommonVo> getreleaseNewConnectionLT(CommonVo comvo) throws Exception;

	public List<CommonVo> gettitleTransfer(CommonVo comvo) throws Exception;

	public List<CommonVo> getCategoryChange(CommonVo comvo) throws Exception;

	public List<CommonVo> getResolutionBillingComplaints(CommonVo comvo) throws Exception;

	public List<CommonVo> getResolutionBillingComplaintsAddl(CommonVo comvo)throws Exception;

	public List<CommonVo> getReplacementBurntMetersLicense(CommonVo comvo) throws Exception;

	public List<CommonVo> getNewConnectionsExistingNetwork(CommonVo comvo) throws Exception;

	public List<CommonVo> getNewConnectionsNetworkExpansionLTHome(CommonVo comvo) throws Exception;

	public List<CommonVo> getSingleToThreePhaseLTHome(CommonVo comvo) throws Exception;

	public List<CommonVo> getReconnectionPaymentBills(CommonVo comvo) throws Exception;

	public List<CommonVo> getWrongfulDisconnectionOfService(CommonVo comvo) throws Exception;

	public List<CommonVo> getScheduledOutagesMaxDuration(CommonVo comvo) throws Exception;

	public List<CommonVo> getScheduledOutagesRestorationsupply(CommonVo comvo) throws Exception;

	public List<CommonVo> newConnectionsNetworkExpansion11Kv(CommonVo comvo) throws Exception;

	public List<CommonVo> getnewConnectionsNetworkExpansion33Kv(CommonVo comvo) throws Exception;

	public List<CommonVo> getnewConnectionsNetworkExpansion132Kv(CommonVo comvo)throws Exception;

	public List<CommonVo> getreleaseNewConnection11Kvdata(CommonVo comvo)throws Exception;

	public List<CommonVo> getreleaseNewConnection33Kvdata(CommonVo comvo) throws Exception;

	public List<CommonVo> getreleaseNewConnection132Kvdata(CommonVo comvo)throws Exception;



	
	
}
