package com.epdcl.apepdclsop.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epdcl.apepdclsop.vo.CommonVo;

@Repository
public class EPSopAnalysisImpl implements EPSopAnalysis{
	Logger logger = LogManager.getLogger(this.toString());
	
	@Autowired
	private EntityManager dbEntityManager1;

	@Override
	public List<CommonVo> getFuseOffReportAbsData(CommonVo comvo) throws Exception {
		List<CommonVo> list=null;
		String sql="";
		String cond="";
		String grp="";
		String subgrp="";
		try {
			if(comvo.getSelectionType().equals("allcircles"))
			{
				grp=" CIRCLE_NAME,CIRCLE_ID ";
				subgrp=" CIRCLE_NAME,CIRCLE_ID ";
				cond=" WHERE  A.CIRCLE = B.CIRCLE_NAME  AND  FIN_YEAR =:perFinYear ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.CIRCLE = B.CIRCLE_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.CIRCLE = B.CIRCLE_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.CIRCLE = B.CIRCLE_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId ";
			}
			if(!comvo.getFld1().equals("ALL")) {
				cond=cond+" AND PERIOD=:peri  ";
			}
			sql=" select URBAN_COMPLAINTS,  " + 
					"DECODE(extract( hour from min_urban)||'.'||extract( minute from min_urban ),'.',0,extract( hour from min_urban)||'.'||extract( minute from min_urban )) AS min_urban,  " + 
					"DECODE(extract( hour from max_urban)||'.'||extract( minute from max_urban ),'.',0,extract( hour from max_urban)||'.'||extract( minute from max_urban )) AS max_urban,  " + 
					"NVL(avg_urban,0) AS avg_urban,NVL(per_urban,0) AS per_urban,RURAL_COMPLAINTS,  " + 
					"DECODE(extract( hour from min_rural)||'.'||extract( minute from min_rural ),'.',0,extract( hour from min_rural)||'.'||extract( minute from min_rural )) AS min_rural,  " + 
					"DECODE(extract( hour from max_rural)||'.'||extract( minute from max_rural ),'.',0,extract( hour from max_rural)||'.'||extract( minute from max_rural )) AS max_rural,  " + 
					"NVL(avg_rural,0) AS avg_rural,NVL(per_rural,0) AS per_rural,  " +grp+ 
					" from (  " + 
					"select COUNT(CASE WHEN AREA_CLASSIFICATION='URBAN' THEN 1 ELSE NULL END ) AS URBAN_COMPLAINTS,  " + 
					"COUNT(CASE WHEN AREA_CLASSIFICATION='RURAL' THEN 1 ELSE NULL END ) AS RURAL_COMPLAINTS,  " + 
					"min(CASE WHEN AREA_CLASSIFICATION='RURAL' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_rural,  " + 
					"min(CASE WHEN AREA_CLASSIFICATION='URBAN' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_urban,  " + 
					"max(CASE WHEN AREA_CLASSIFICATION='RURAL' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) max_rural,  " + 
					"max(CASE WHEN AREA_CLASSIFICATION='URBAN' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) max_urban,  " + 
					"round(AVG(CASE WHEN AREA_CLASSIFICATION='URBAN' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) *24  avg_urban,  " + 
					"round(AVG(CASE WHEN AREA_CLASSIFICATION='RURAL' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) *24  avg_rural,  " + 
					"round((SUM(CASE WHEN AREA_CLASSIFICATION='URBAN' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end) /COUNT(CASE WHEN AREA_CLASSIFICATION='URBAN' THEN 1 ELSE NULL END ) )*100,2)   per_urban,  " + 
					"round((SUM(CASE WHEN AREA_CLASSIFICATION='RURAL'  AND RECTIFIED_ON<=DISCOM_ETR THEN 1 ELSE NULL END ) /COUNT(CASE WHEN AREA_CLASSIFICATION='RURAL' THEN 1 ELSE NULL END ) )*100,2)  per_RURAL,  " + 
					"round(SUM(CASE WHEN AREA_CLASSIFICATION='URBAN' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end)) as urban_rect,  " + 
					"round(SUM(CASE WHEN AREA_CLASSIFICATION='RURAL' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end)) as rural_rect,  " + subgrp +
					"FROM CSC_NSC.SOP_NORMAL_FUSE_OFF A,COMMON_MASTER.OFFICE_MASTER B  " + cond+
					" GROUP BY "+subgrp+") "+
					" ORDER BY "+grp;
		
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 if(!comvo.getFld1().equals("ALL")) {
		 query.setParameter("peri", comvo.getFld1());
		 }
		 if(comvo.getSelectionType().equals("alldivisons")||comvo.getSelectionType().equals("allsubdivisons")||comvo.getSelectionType().equals("allsections"))
			 query.setParameter("selectionId", comvo.getSelectionId());
		 
		 @SuppressWarnings("unchecked")
		List<Object[]> resList=query.getResultList();
		list=new ArrayList<CommonVo>(); 
		 if(resList.size()>0) {
			for (Object[] obj : resList) {
				CommonVo commVo=new CommonVo();
				commVo.setFlag("yes");
				commVo.setFld1(obj[0]==null ? "" : obj[0].toString());
				commVo.setFld2(obj[1]==null ? "" : obj[1].toString());
				commVo.setFld3(obj[2]==null ? "" : obj[2].toString());
				commVo.setFld4(obj[3]==null ? "" : obj[3].toString());
				commVo.setFld5(obj[4]==null ? "" : obj[4].toString());
				commVo.setFld6(obj[5]==null ? "" : obj[5].toString());
				commVo.setFld7(obj[6]==null ? "" : obj[6].toString());
				commVo.setFld8(obj[7]==null ? "" : obj[7].toString());
				commVo.setFld9(obj[8]==null ? "" : obj[8].toString());
				commVo.setFld10(obj[9]==null ? "" : obj[9].toString());
				
				commVo.setSelectionName(obj[10]==null ? "" : obj[10].toString());
				commVo.setSelectionId(obj[11]==null ? "" : obj[11].toString());
				list.add(commVo);
			}
		 }else {
			 CommonVo commVo=new CommonVo();
			 commVo.setFlag("no");
			 list.add(commVo);
		 }
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception ::",e);
		}finally {
			if(dbEntityManager1!=null)
				dbEntityManager1.close();
		}
		return list;
	}
	
}
