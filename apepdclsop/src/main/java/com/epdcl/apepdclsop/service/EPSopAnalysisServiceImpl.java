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

	@Override
	public List<CommonVo> getOverHeadBreakdown(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getOverHeadBreakdownData(comvo);
	}

	@Override
	public List<CommonVo> getUGCableBreakDown(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getUGCableBreakDownData(comvo);
	}

	@Override
	public List<CommonVo> getDTrFailures(CommonVo comvo) throws Exception {
	
		return epSopAnalysis.getDTrFailuresData(comvo);
	}

	@Override
	public List<CommonVo> getvoltageFluctuation(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getvoltageFluctuationData(comvo);
	}

	@Override
	public List<CommonVo> getVoltageFluctuationExpansion(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getvoltageFluctuationExpansionData(comvo);
	}

	@Override
	public List<CommonVo> getReplacementDefectiveMeters(CommonVo comvo) throws Exception {
		
		return  epSopAnalysis.getReplacementDefectiveMetersData(comvo);
	}

	@Override
	public List<CommonVo> getReplacementBurntMetersConsumer(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getReplacementBurntMetersConsumerData(comvo);
	}

	@Override
	public List<CommonVo> getReleaseNewConnectionExisting(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getReleaseNewConnectionExistingData(comvo);
	}

	@Override
	public List<CommonVo> getreleaseNewConnectionLT(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getreleaseNewConnectionLTData(comvo);
	}

	@Override
	public List<CommonVo> gettitleTransfer(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.gettitleTransferData(comvo);
	}

	@Override
	public List<CommonVo> getCategoryChange(CommonVo comvo) throws Exception {
				return epSopAnalysis.getCategoryChangeData(comvo);
	}

	@Override
	public List<CommonVo> getResolutionBillingComplaints(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getResolutionBillingComplaintsData(comvo);
	}

	@Override
	public List<CommonVo> getResolutionBillingComplaintsAddl(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getResolutionBillingComplaintsAddlData(comvo);
	}

	@Override
	public List<CommonVo> getReplacementBurntMetersLicense(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getReplacementBurntMetersLicenseData(comvo);
	}

	@Override
	public List<CommonVo> getNewConnectionsExistingNetwork(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getNewConnectionsExistingNetworkData(comvo);
	}

	@Override
	public List<CommonVo> getNewConnectionsNetworkExpansionLTHome(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getNewConnectionsNetworkExpansionLTHomeData(comvo);
	}

	@Override
	public List<CommonVo> getSingleToThreePhaseLTHome(CommonVo comvo) throws Exception {
	
		return epSopAnalysis.getSingleToThreePhaseLTHomeData(comvo);
	}

	@Override
	public List<CommonVo> getReconnectionPaymentBills(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getReconnectionPaymentBillsData(comvo);
	}

	@Override
	public List<CommonVo> getWrongfulDisconnectionOfService(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getWrongfulDisconnectionOfServiceData(comvo);
	}

	@Override
	public List<CommonVo> getScheduledOutagesMaxDuration(CommonVo comvo) throws Exception {

		return epSopAnalysis.getScheduledOutagesMaxDurationData(comvo);
	}

	@Override
	public List<CommonVo> getScheduledOutagesRestorationsupply(CommonVo comvo) throws Exception {
	
		return epSopAnalysis.getScheduledOutagesRestorationsupplyData(comvo);
	}

	@Override
	public List<CommonVo> newConnectionsNetworkExpansion11Kv(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.newConnectionsNetworkExpansion11Kv(comvo);
	}

	@Override
	public List<CommonVo> getnewConnectionsNetworkExpansion33Kv(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getnewConnectionsNetworkExpansion33Kv(comvo);
	}

	@Override
	public List<CommonVo> getnewConnectionsNetworkExpansion132Kv(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getnewConnectionsNetworkExpansion132Kv(comvo);
	}

	@Override
	public List<CommonVo> getreleaseNewConnection11Kvdata(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getreleaseNewConnection11Kvdata(comvo);
	}

	@Override
	public List<CommonVo> getreleaseNewConnection33Kvdata(CommonVo comvo) throws Exception {
		
		return epSopAnalysis.getreleaseNewConnection33Kvdata(comvo);
	}

	@Override
	public List<CommonVo> getreleaseNewConnection132Kvdata(CommonVo comvo) throws Exception {
		// TODO Auto-generated method stub
		return epSopAnalysis.getreleaseNewConnection132Kvdata(comvo);
	}


	
	
}
