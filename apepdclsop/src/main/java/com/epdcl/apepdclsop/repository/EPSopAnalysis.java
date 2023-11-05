package com.epdcl.apepdclsop.repository;

import java.util.List;

import com.epdcl.apepdclsop.vo.CommonVo;

public interface EPSopAnalysis {

	public List<CommonVo> getFuseOffReportAbsData(CommonVo comvo) throws Exception;

	public List<CommonVo> getOverHeadBreakdownData(CommonVo comvo) throws Exception;
	
	public List<CommonVo> getUGCableBreakDownData(CommonVo comvo) throws Exception;

	public List<CommonVo> getDTrFailuresData(CommonVo comvo) throws Exception;

	public List<CommonVo> getvoltageFluctuationData(CommonVo comvo) throws Exception;

	public List<CommonVo> getvoltageFluctuationExpansionData(CommonVo comvo) throws Exception;

	public List<CommonVo> getReplacementDefectiveMetersData(CommonVo comvo) throws Exception;

	public List<CommonVo> getReplacementBurntMetersConsumerData(CommonVo comvo) throws Exception;

	public List<CommonVo> getReleaseNewConnectionExistingData(CommonVo comvo) throws Exception;

	public List<CommonVo> getreleaseNewConnectionLTData(CommonVo comvo) throws Exception;

	public List<CommonVo> gettitleTransferData(CommonVo comvo) throws Exception;

	public List<CommonVo> getCategoryChangeData(CommonVo comvo) throws Exception;

	public List<CommonVo> getResolutionBillingComplaintsData(CommonVo comvo) throws Exception;

	public List<CommonVo> getResolutionBillingComplaintsAddlData(CommonVo comvo) throws Exception;

	public List<CommonVo> getReplacementBurntMetersLicenseData(CommonVo comvo) throws Exception;

	public List<CommonVo> getNewConnectionsExistingNetworkData(CommonVo comvo) throws Exception;

	public List<CommonVo> getNewConnectionsNetworkExpansionLTHomeData(CommonVo comvo) throws Exception;

	public List<CommonVo> getSingleToThreePhaseLTHomeData(CommonVo comvo) throws Exception;

	public List<CommonVo> getReconnectionPaymentBillsData(CommonVo comvo) throws Exception;

	public List<CommonVo> getWrongfulDisconnectionOfServiceData(CommonVo comvo) throws Exception;

	public List<CommonVo> getScheduledOutagesMaxDurationData(CommonVo comvo) throws Exception;

	public List<CommonVo> getScheduledOutagesRestorationsupplyData(CommonVo comvo) throws Exception;

	public List<CommonVo> newConnectionsNetworkExpansion11Kv(CommonVo comvo)throws Exception;

	public List<CommonVo> getnewConnectionsNetworkExpansion33Kv(CommonVo comvo) throws Exception;

	public List<CommonVo> getnewConnectionsNetworkExpansion132Kv(CommonVo comvo)throws Exception;

	public List<CommonVo> getreleaseNewConnection11Kvdata(CommonVo comvo)throws Exception;

	public List<CommonVo> getreleaseNewConnection33Kvdata(CommonVo comvo)throws Exception;

	public List<CommonVo> getreleaseNewConnection132Kvdata(CommonVo comvo)throws Exception;

	
}
