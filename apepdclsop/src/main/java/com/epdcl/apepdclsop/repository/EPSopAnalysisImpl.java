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
				cond=" WHERE  A.sec_name = B.section_NAME     AND  FIN_YEAR =:perFinYear AND PERIOD=:peri";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId  AND PERIOD=:peri";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId AND PERIOD=:peri";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" SECTION_NAME, SECTION_ID ";
				cond=" WHERE A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select URBAN_COMPLAINTS,DECODE(extract( hour from min_urban)||'.'||extract( minute from min_urban ),'.','0.0',extract( hour from min_urban)||'.'||extract( minute from min_urban )) AS min_urban,\r\n"
					+ "nvl(max_urban,0) as max_urban,NVL(avg_urban,0) AS avg_urban,nvl(per_urban,0) as per_urban,RURAL_COMPLAINTS,\r\n"
					+ "DECODE(extract( hour from min_rural)||'.'||extract( minute from min_rural ),'.','0.0',extract( hour from min_rural)||'.'||extract( minute from min_rural )) AS min_rural,\r\n"
					+ "nvl(max_rural,0) as max_rural,NVL(avg_rural,0) AS avg_rural,nvl(per_RURAL,0) as per_RURAL," +grp+ "\r\n"
					+ "from (select COUNT(CASE WHEN AREA_CLASSIFICATION='URBAN' THEN 1 ELSE NULL END ) AS URBAN_COMPLAINTS,\r\n"
					+ "min(CASE WHEN AREA_CLASSIFICATION='URBAN' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_urban,\r\n"
					+ "round(max(CASE WHEN AREA_CLASSIFICATION='URBAN' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end)*24,0)  max_urban,\r\n"
					+ "round(AVG(CASE WHEN AREA_CLASSIFICATION='URBAN' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end)*24,2)  avg_urban,\r\n"
					+ "round((sum(CASE WHEN AREA_CLASSIFICATION='URBAN' AND RECTIFIED_ON<=DISCOM_ETR  THEN 1  else null end) /COUNT(CASE WHEN AREA_CLASSIFICATION='URBAN' THEN 1 ELSE NULL END ) )*100,2)   per_urban,\r\n"
					+ "COUNT(CASE WHEN AREA_CLASSIFICATION='RURAL' THEN 1 ELSE NULL END ) AS RURAL_COMPLAINTS,\r\n"
					+ "min(CASE WHEN AREA_CLASSIFICATION='RURAL' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_rural,\r\n"
					+ "round(max(CASE WHEN AREA_CLASSIFICATION='RURAL' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end)*24,0)  max_rural,\r\n"
					+ "round(AVG(CASE WHEN AREA_CLASSIFICATION='RURAL' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end)*24,2)  avg_rural,\r\n"
					+ "round((sum(CASE WHEN AREA_CLASSIFICATION='RURAL'  AND RECTIFIED_ON<=DISCOM_ETR THEN 1 ELSE NULL END ) /COUNT(CASE WHEN AREA_CLASSIFICATION='RURAL' THEN 1 ELSE NULL END ) )*100,2)  per_RURAL,\r\n"+subgrp
					+ " FROM CSC_NSC.SOP_NORMAL_FUSE_OFF A,COMMON_MASTER.OFFICE_MASTER B \r\n"+ cond
					+ " GROUP BY "+subgrp+"ORDER BY "+subgrp+")";
			System.out.println(sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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
	
	
	
	
	@Override
	public List<CommonVo> getOverHeadBreakdownData(CommonVo comvo) throws Exception {
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
				cond=" WHERE  A.sec_name = B.section_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" SECTION_NAME, SECTION_ID ";
				cond=" WHERE   A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select URBAN_COMPLAINTS,\r\n"
					+ "DECODE(extract( hour from min_urban)||'.'||extract( minute from min_urban ),'.','0.0',extract( hour from min_urban)||'.'||extract( minute from min_urban )) AS min_urban,\r\n"
					+ "NVL(max_urban,0) AS max_urban,NVL(avg_urban,0) AS avg_urban,NVL(per_urban,0) AS per_urban,\r\n"
					+ "RURAL_COMPLAINTS,\r\n"
					+ "DECODE(extract( hour from min_rural)||'.'||extract( minute from min_rural ),'.','0.0',extract( hour from min_rural)||'.'||extract( minute from min_rural )) AS min_rural,\r\n"
					+ "NVL(max_rural,0) AS max_rural,NVL(avg_rural,0) AS avg_rural,NVL(per_RURAL,0) AS per_RURAL,"+grp+"\r\n"
					+ " from (select COUNT(CASE WHEN AREA_CLASSIFICATION='URBAN' THEN 1 ELSE NULL END ) AS URBAN_COMPLAINTS,\r\n"
					+ "min(CASE WHEN AREA_CLASSIFICATION='URBAN' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_urban,\r\n"
					+ "round(max(CASE WHEN AREA_CLASSIFICATION='URBAN' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end)*24,0)  max_urban,\r\n"
					+ "round(AVG(CASE WHEN AREA_CLASSIFICATION='URBAN' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) *24  avg_urban,\r\n"
					+ "round((SUM(CASE WHEN AREA_CLASSIFICATION='URBAN' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end) /COUNT(CASE WHEN AREA_CLASSIFICATION='URBAN' THEN 1 ELSE NULL END ) )*100,2)   per_urban,\r\n"
					+ "COUNT(CASE WHEN AREA_CLASSIFICATION='RURAL' THEN 1 ELSE NULL END ) AS RURAL_COMPLAINTS,\r\n"
					+ "min(CASE WHEN AREA_CLASSIFICATION='RURAL' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_rural,\r\n"
					+ "round(max(CASE WHEN AREA_CLASSIFICATION='RURAL' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end)*24,0)  max_rural,\r\n"
					+ "round(AVG(CASE WHEN AREA_CLASSIFICATION='RURAL' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) *24  avg_rural,\r\n"
					+ "round((SUM(CASE WHEN AREA_CLASSIFICATION='RURAL'  AND RECTIFIED_ON<=DISCOM_ETR THEN 1 ELSE NULL END ) /COUNT(CASE WHEN AREA_CLASSIFICATION='RURAL' THEN 1 ELSE NULL END ) )*100,2)  per_RURAL,"+subgrp+"\r\n"
					+ "FROM CSC_NSC.SOP_OVER_HEAD_BREAKDOWN A,COMMON_MASTER.OFFICE_MASTER B\r\n"
					+ cond
					+ " GROUP BY "+subgrp+"ORDER BY "+subgrp+")"; 
			System.out.println("query executed:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getUGCableBreakDownData(CommonVo comvo) throws Exception {
		
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
				cond=" WHERE  A.sec_name = B.section_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select URBAN_COMPLAINTS,\r\n" + 
					"DECODE(extract( hour from min_urban)||'.'||extract( minute from min_urban ),'.','0.0',extract( hour from min_urban)||'.'||extract( minute from min_urban )) AS min_urban,\r\n" + 
					"DECODE(extract( hour from max_urban)||'.'||extract( minute from max_urban ),'.','0.0',extract( hour from max_urban)||'.'||extract( minute from max_urban )) AS max_urban,\r\n" + 
					"NVL(avg_urban,0) AS avg_urban,\r\n" + 
					"NVL(per_urban,0) AS per_urban,\r\n" + 
					"RURAL_COMPLAINTS,\r\n" + 
					"DECODE(extract( hour from min_rural)||'.'||extract( minute from min_rural ),'.','0.0',extract( hour from min_rural)||'.'||extract( minute from min_rural )) AS min_rural,\r\n" + 
					"DECODE(extract( hour from max_rural)||'.'||extract( minute from max_rural ),'.','0.0',extract( hour from max_rural)||'.'||extract( minute from max_rural )) AS max_rural,\r\n" + 
					"NVL(avg_rural,0) AS avg_rural,\r\n" + 
					"NVL(per_RURAL,0) AS per_RURAL," +grp+ 
					" from (select\r\n" + 
					"COUNT(CASE WHEN AREA_CLASSIFICATION='URBAN' THEN 1 ELSE NULL END ) AS URBAN_COMPLAINTS,\r\n" + 
					"\r\n" + 
					"min(CASE WHEN AREA_CLASSIFICATION='URBAN' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_urban,\r\n" + 
					"max(CASE WHEN AREA_CLASSIFICATION='URBAN' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) max_urban,\r\n" + 
					"round(AVG(CASE WHEN AREA_CLASSIFICATION='URBAN' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) *24  avg_urban,\r\n" + 
					"round(SUM(CASE WHEN AREA_CLASSIFICATION='URBAN' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end)) as urban_rect,\r\n" + 
					"round((SUM(CASE WHEN AREA_CLASSIFICATION='URBAN' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end) /COUNT(CASE WHEN AREA_CLASSIFICATION='URBAN' THEN 1 ELSE NULL END ) )*100,2)   per_urban,"+
					"COUNT(CASE WHEN AREA_CLASSIFICATION='RURAL' THEN 1 ELSE NULL END ) AS RURAL_COMPLAINTS,\r\n" + 
					"min(CASE WHEN AREA_CLASSIFICATION='RURAL' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_rural,\r\n" + 
					"max(CASE WHEN AREA_CLASSIFICATION='RURAL' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) max_rural,\r\n" + 
					"round(AVG(CASE WHEN AREA_CLASSIFICATION='RURAL' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) *24  avg_rural,\r\n" +
					"round(SUM(CASE WHEN AREA_CLASSIFICATION='RURAL' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end)) as rural_rect,"+
					"round((SUM(CASE WHEN AREA_CLASSIFICATION='RURAL'  AND RECTIFIED_ON<=DISCOM_ETR THEN 1 ELSE NULL END ) /COUNT(CASE WHEN AREA_CLASSIFICATION='RURAL' THEN 1 ELSE NULL END ) )*100,2)  per_RURAL,"
					+subgrp+ 
					"FROM CSC_NSC.SOP_UG_CABLE_BREAKDOWN A,\r\n" + 
					"                    COMMON_MASTER.OFFICE_MASTER B" +cond+ 
					" GROUP BY  "+subgrp+ ")"+
					" ORDER BY "+grp;
			System.out.println("query executed:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getDTrFailuresData(CommonVo comvo) throws Exception {
		
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
				cond=" WHERE  A.sec_name = B.section_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" SECTION_NAME, SECTION_ID ";
				cond=" WHERE   A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select URBAN_COMPLAINTS,\r\n"
					+ "DECODE(extract( hour from min_urban)||'.'||extract( minute from min_urban ),'.','0.0',extract( hour from min_urban)||'.'||extract( minute from min_urban )) AS min_urban,\r\n"
					+ "NVL(max_urban,0) AS max_urban,NVL(avg_urban,0) AS avg_urban,nvl(per_urban,0) as per_urban,\r\n"
					+ "RURAL_COMPLAINTS,\r\n"
					+ "DECODE(extract( hour from min_rural)||'.'||extract( minute from min_rural ),'.','0.0',extract( hour from min_rural)||'.'||extract( minute from min_rural )) AS min_rural,\r\n"
					+ "NVL(max_rural,0) AS max_rural,NVL(avg_rural,0) AS avg_rural,\r\n"
					+ "nvl(per_RURAL,0) as per_RURAL,"+grp+"\r\n"
					+ " from (select COUNT(CASE WHEN AREA_CLASSIFICATION='URBAN' THEN 1 ELSE NULL END ) AS URBAN_COMPLAINTS,\r\n"
					+ "min(CASE WHEN AREA_CLASSIFICATION='URBAN' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_urban,\r\n"
					+ "round(max(CASE WHEN AREA_CLASSIFICATION='URBAN' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end)*24,0)  max_urban,\r\n"
					+ "round(AVG(CASE WHEN AREA_CLASSIFICATION='URBAN' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) *24  avg_urban,\r\n"
					+ "round((SUM(CASE WHEN AREA_CLASSIFICATION='URBAN' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end) /COUNT(CASE WHEN AREA_CLASSIFICATION='URBAN' THEN 1 ELSE NULL END ) )*100,2)   per_urban,\r\n"
					+ "COUNT(CASE WHEN AREA_CLASSIFICATION='RURAL' THEN 1 ELSE NULL END ) AS RURAL_COMPLAINTS,\r\n"
					+ "min(CASE WHEN AREA_CLASSIFICATION='RURAL' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_rural,\r\n"
					+ "round(max(CASE WHEN AREA_CLASSIFICATION='RURAL' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end)*24,0)  max_rural,\r\n"
					+ "round(AVG(CASE WHEN AREA_CLASSIFICATION='RURAL' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) *24  avg_rural,\r\n"
					+ "round((SUM(CASE WHEN AREA_CLASSIFICATION='RURAL'  AND RECTIFIED_ON<=DISCOM_ETR THEN 1 ELSE NULL END ) /COUNT(CASE WHEN AREA_CLASSIFICATION='RURAL' THEN 1 ELSE NULL END ) )*100,2)  per_RURAL,"+subgrp+"\r\n"
					+ "FROM CSC_NSC.SOP_DTR_FAILURES A,COMMON_MASTER.OFFICE_MASTER B\r\n"
					+ cond
					+ " GROUP BY "+subgrp+" ORDER BY "+subgrp+")";
			System.out.println("query executed:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getvoltageFluctuationData(CommonVo comvo) throws Exception {
		
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
				cond=" WHERE  A.sec_name = B.section_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" SECTION_NAME, SECTION_ID ";
				cond=" WHERE   A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select \r\n" + 
					"TOT_COMPLAINTS,\r\n" + 
					"NVL(extract( day from min_days),0) min_days,\r\n" + 
					"NVL(extract( day from max_days),0) max_days, \r\n" + 
					"NVL(avg_days,0) AS avg_days,\r\n" + 
					"NVL(per,0) AS per," +grp+ 
					" from ( \r\n" + 
					"select \r\n" + 
					"COUNT(CASE WHEN RECTIFICATION_REQUIRES = 'No Expansion Network' THEN 1 ELSE NULL END ) AS tot_COMPLAINTS,\r\n" + 
					"min(CASE WHEN RECTIFICATION_REQUIRES = 'No Expansion Network' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_days,\r\n" + 
					"max(CASE WHEN RECTIFICATION_REQUIRES = 'No Expansion Network' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) max_days,\r\n" + 
					"round(AVG(CASE WHEN RECTIFICATION_REQUIRES = 'No Expansion Network' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) avg_days,\r\n" + 
					"round((SUM(CASE WHEN RECTIFICATION_REQUIRES = 'No Expansion Network' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end) /COUNT(CASE WHEN RECTIFICATION_REQUIRES = 'No Expansion Network' THEN 1 ELSE NULL END ) )*100,2)   per,"+
					"round(SUM(CASE WHEN RECTIFICATION_REQUIRES = 'No Expansion Network' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end)) as rectified," +subgrp+ 
					"FROM CSC_NSC.SOP_VOLTAGE_FLUCTUATIONS A,COMMON_MASTER.OFFICE_MASTER B\r\n" +cond+
					" GROUP BY "+subgrp+") ORDER BY "+grp;
		System.out.println(sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getvoltageFluctuationExpansionData(CommonVo comvo) throws Exception {
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
				cond=" WHERE  A.sec_name = B.section_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" SECTION_NAME, SECTION_ID ";
				cond=" WHERE   A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select TOT_COMPLAINTS,\r\n" + 
					"NVL(extract( day from min_days),0) min_days,\r\n" + 
					"NVL(extract( day from max_days),0) max_days, \r\n" + 
					"NVL(avg_days,0) AS avg_days,\r\n" + 
					"NVL(per,0) AS per," +grp+ 
					" from (select COUNT(CASE WHEN RECTIFICATION_REQUIRES = 'Upgradation of Distribution System' THEN 1 ELSE NULL END ) AS tot_COMPLAINTS,\r\n" + 
					"min(CASE WHEN RECTIFICATION_REQUIRES = 'Upgradation of Distribution System' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_days,\r\n" + 
					"max(CASE WHEN RECTIFICATION_REQUIRES = 'Upgradation of Distribution System' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) max_days,\r\n" + 
					"round(AVG(CASE WHEN RECTIFICATION_REQUIRES = 'Upgradation of Distribution System' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) avg_days,\r\n" + 
					"round((SUM(CASE WHEN RECTIFICATION_REQUIRES = 'Upgradation of Distribution System'  AND RECTIFIED_ON<=DISCOM_ETR THEN 1 ELSE NULL END ) /COUNT(CASE WHEN RECTIFICATION_REQUIRES = 'Upgradation of Distribution System' THEN 1 ELSE NULL END ) )*100,2)  per,"+
					"round(SUM(CASE WHEN RECTIFICATION_REQUIRES = 'Upgradation of Distribution System' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end)) as rectified,\r\n" +subgrp+
					"FROM CSC_NSC.SOP_VOLTAGE_FLUCTUATIONS A,COMMON_MASTER.OFFICE_MASTER B\r\n" + 
					""+cond+"  GROUP BY  "+subgrp+" ) ORDER BY"+grp;
			System.out.println("query executed:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getReplacementDefectiveMetersData(CommonVo comvo) throws Exception {
		
		List<CommonVo> list=null;
		String sql="";
		String cond="";
		String grp="";
		String subgrp="";
		try {
			if(comvo.getSelectionType().equals("allcircles"))
			{
				grp=" B.CIRCLE_NAME,CIRCLE_ID ";
				subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
				cond=" WHERE  A.section_name = B.section_NAME  AND  FIN_YEAR =:perFinYear and  NATURE_OF_PROBLEM NOT IN ('Meter Burnt') AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" B.DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.section_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId and  NATURE_OF_PROBLEM NOT IN ('Meter Burnt') AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" B.SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.section_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId and  NATURE_OF_PROBLEM NOT IN ('Meter Burnt') AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" B.SECTION_NAME, SECTION_ID ";
				subgrp=" B.SECTION_NAME, SECTION_ID ";
				cond=" WHERE   A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME  = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId and  NATURE_OF_PROBLEM NOT IN ('Meter Burnt') AND PERIOD=:peri ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			
			sql=" select \r\n"
					+ " COUNT(CASE WHEN AREA_TYPE='URBAN' THEN 1 ELSE NULL END ) AS URBAN_COMPLAINTS,\r\n"
					+ " nvl(min(CASE WHEN AREA_TYPE='URBAN' then TRUNC(REPLACEMENT_DT)-TRUNC(DATE_OF_COMPLAINT)  else null end),0) min_urban,\r\n"
					+ "nvl(max(CASE WHEN AREA_TYPE='URBAN' then TRUNC(REPLACEMENT_DT)-TRUNC(DATE_OF_COMPLAINT)  else null end),0) max_urban,\r\n"
					+ "nvl(round(AVG(CASE WHEN AREA_TYPE='URBAN' then TRUNC(REPLACEMENT_DT)-TRUNC(DATE_OF_COMPLAINT) else null end),2),0)   avg_urban,\r\n"
					+ "nvl(round((SUM(CASE WHEN AREA_TYPE='URBAN'  AND NATURE_OF_PROBLEM IN ('Meter Struckup','Meter - Running Slow/ Sluggish','Meter Running Fast/Creeping') \r\n"
					+ "AND trunc(REPLACEMENT_DT)-trunc(DATE_OF_COMPLAINT) <=22 THEN 1 else null end) /COUNT(CASE WHEN AREA_TYPE='URBAN' THEN 1 ELSE NULL END ) )*100,2),0)   per_urban,\r\n"
					+ "COUNT(CASE WHEN AREA_TYPE='RURAL' THEN 1 ELSE NULL END ) AS RURAL_COMPLAINTS,\r\n"
					+ "nvl(min(CASE WHEN AREA_TYPE='RURAL' then TRUNC(REPLACEMENT_DT)-TRUNC(DATE_OF_COMPLAINT)  else null end),0) min_rural,\r\n"
					+ "nvl(max(CASE WHEN AREA_TYPE='RURAL' then TRUNC(REPLACEMENT_DT)-TRUNC(DATE_OF_COMPLAINT)  else null end),0) max_rural,\r\n"
					+ "nvl(round(AVG(CASE WHEN AREA_TYPE='RURAL' then TRUNC(REPLACEMENT_DT)-TRUNC(DATE_OF_COMPLAINT) else null end),2),0)   avg_rural,\r\n"
					+ "nvl(round((SUM(CASE WHEN  AREA_TYPE='RURAL' AND NATURE_OF_PROBLEM IN ('Meter Struckup','Meter - Running Slow/ Sluggish','Meter Running Fast/Creeping') \r\n"
					+ "AND trunc(REPLACEMENT_DT)-trunc(DATE_OF_COMPLAINT) <=30 THEN 1 else null end) /COUNT(CASE WHEN AREA_TYPE='RURAL' THEN 1 ELSE NULL END ) )*100,2),0)   per_RURAL, "+subgrp
					+ "FROM CSC_NSC.SOP_METER_COMPLAINTS A,COMMON_MASTER.OFFICE_MASTER B\r\n"
					+ cond
					+ " GROUP BY "+subgrp+"ORDER BY "+subgrp;
			System.out.println("query executed:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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
				
				int fld2=Integer.parseInt(commVo.getFld2());  
				if(fld2<0) {
				int res=Math.max(0,fld2);
				String setfld2=String.valueOf(res);
				commVo.setFld2(setfld2);}
				
				int fld7=Integer.parseInt(commVo.getFld7());  
				if(fld7<0) {
				int res=Math.max(0,fld7);
				String setfld7=String.valueOf(res);
				commVo.setFld7(setfld7);}
				
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

	
	@Override
	public List<CommonVo> getReplacementBurntMetersConsumerData(CommonVo comvo) throws Exception {
		
		List<CommonVo> list=null;
		String sql="";
		String cond="";
		String grp="";
		String subgrp="";
		try {
			if(comvo.getSelectionType().equals("allcircles"))
			{
				grp=" B.CIRCLE_NAME,CIRCLE_ID ";
				subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
				cond=" WHERE A.CIRCLE_NAME = B.CIRCLE_NAME AND A.SECTION_NAME  = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear and    NATURE_OF_PROBLEM = ('Meter Burnt') AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" B.DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.SECTION_NAME  = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId and  NATURE_OF_PROBLEM = ('Meter Burnt') AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" B.SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SECTION_NAME  = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId and   NATURE_OF_PROBLEM = ('Meter Burnt') AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" B.SECTION_NAME, SECTION_ID ";
				subgrp=" B.SECTION_NAME, SECTION_ID ";
				cond=" WHERE   A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME  = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId and  NATURE_OF_PROBLEM = ('Meter Burnt') AND PERIOD=:peri ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select COUNT(*) AS TOT_COMPLAINTS, nvl(min(TRUNC(REPLACEMENT_DT)-TRUNC(DATE_OF_COMPLAINT)),0) min_burnt_Meters,\r\n"
					+ "nvl(max(TRUNC(REPLACEMENT_DT)-TRUNC(DATE_OF_COMPLAINT)),0) max_burnt_Meters,\r\n"
					+ "nvl(round(AVG(TRUNC(REPLACEMENT_DT)-TRUNC(DATE_OF_COMPLAINT)),2),0) avg_burnt_Meters,\r\n"
					+ "nvl(round(sum(CASE WHEN (trunc(REPLACEMENT_DT)-TRUNC(DATE_OF_COMPLAINT))<=7 THEN 1  else null end)/count(*)*100,2),0) as per,"+grp+" \r\n"
					+ "FROM CSC_NSC.SOP_METER_COMPLAINTS A,COMMON_MASTER.OFFICE_MASTER B\r\n"
					+ cond
					+ " GROUP BY "+subgrp+"ORDER BY "+subgrp;
			System.out.println("query executed:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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
				commVo.setFld2(obj[1]==null  ? "" : obj[1].toString());
				String fld2=commVo.getFld2();
				int fld2no =Integer.parseInt(fld2);
				int res2=Math.max(0, fld2no);
				commVo.setFld2(Integer.toString(res2));
				commVo.setFld3(obj[2]==null ? "" : obj[2].toString());
				commVo.setFld4(obj[3]==null ? "" : obj[3].toString());
				commVo.setFld5(obj[4]==null ? "" : obj[4].toString());
				
				
				commVo.setFld6(obj[5]==null ? "" : obj[5].toString());
				commVo.setFld7(obj[6]==null ? "" : obj[6].toString());			
				
				/*
				 * int fld2=Integer.parseInt(commVo.getFld2()); if(fld2<0) { int
				 * res=Math.max(0,fld2); String setfld2=String.valueOf(res);
				 * commVo.setFld2(setfld2);}
				 */
				
			/*
			 * int fld7=Integer.parseInt(commVo.getFld7()); if(fld7<0) { int
			 * res=Math.max(0,fld7); String setfld7=String.valueOf(res);
			 * commVo.setFld7(setfld7);}
			 */
				
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




	@Override
	public List<CommonVo> getReleaseNewConnectionExistingData(CommonVo comvo) throws Exception {
		
		List<CommonVo> list=null;
		String sql="";
		String cond="";
		String grp="";
		String subgrp="";
		try {
			if(comvo.getSelectionType().equals("allcircles"))
			{
				grp=" CIRCLE_NAME,CIRCLE_ID ";
				subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND  FIN_YEAR =:perFinYear and  FEASIBILITY='From Existing Network' AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId and  FEASIBILITY='From Existing Network' AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId and  FEASIBILITY='From Existing Network' AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" B.SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId and  FEASIBILITY='From Existing Network' AND PERIOD=:peri ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select COUNT(*) AS COMPLAINTS,\r\n"
					+ "nvl(min(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),0) min_days,\r\n"
					+ "nvl(max(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),0) max_days,\r\n"
					+ "nvl(round(AVG(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),2),0)   avg_days,\r\n"
					+ "--nvl(round(SUM(CASE WHEN (trunc(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT))<=30 THEN 1  else null end)),0) as rectified,\r\n "
					+ "nvl(round(sum(CASE WHEN (trunc(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT))<=30 THEN 1  else null end)/count(*)*100,2),0) as per, \r\n"+subgrp
					+ "FROM CSC_NSC.SOP_RELEASE_NSC_ALP A,COMMON_MASTER.OFFICE_MASTER B\r\n"
					+ cond
					+ " GROUP BY "+subgrp+"ORDER BY "+subgrp ;
		System.out.println("executed query:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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
				
				int fld2=Integer.parseInt(commVo.getFld2());  
				if(fld2<0) {
				int res=Math.max(0,fld2);
				String setfld2=String.valueOf(res);
				commVo.setFld2(setfld2);}
				
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




	@Override
	public List<CommonVo> getreleaseNewConnectionLTData(CommonVo comvo) throws Exception {
	
		List<CommonVo> list=null;
		String sql="";
		String cond="";
		String grp="";
		String subgrp="";
		try {
			if(comvo.getSelectionType().equals("allcircles"))
			{
				grp=" CIRCLE_NAME,CIRCLE_ID ";
				subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='LT' ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='LT' ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='LT' ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" B.SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='LT' ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select COUNT(*) AS COMPLAINTS,\r\n"
					+ "nvl(min(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),0) min_days,\r\n"
					+ "nvl(max(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),0) max_days,\r\n"
					+ "nvl(round(AVG(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),2),0)   avg_days,\r\n"
					+ "nvl(round(sum(CASE WHEN (trunc(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT))<=30 THEN 1  else null end)/count(*)*100,2),0) as per,\r\n"
					+ "--nvl(round(SUM(CASE WHEN (trunc(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT))<=30 THEN 1  else null end)),0) as rectified,\r\n"
					+ subgrp +" FROM CSC_NSC.SOP_RELEASE_NSC_ALP A, COMMON_MASTER.OFFICE_MASTER B\r\n"
					+cond
					+ " GROUP BY "+subgrp+"ORDER BY "+subgrp;
		System.out.println("executed query:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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
				
				int fld2=Integer.parseInt(commVo.getFld2());  
				if(fld2<0) {
				int res=Math.max(0,fld2);
				String setfld2=String.valueOf(res);
				commVo.setFld2(setfld2);}
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




	@Override
	public List<CommonVo> gettitleTransferData(CommonVo comvo) throws Exception {
		List<CommonVo> list=null;
		String sql="";
		String cond="";
		String grp="";
		String subgrp="";
		try {
			if(comvo.getSelectionType().equals("allcircles"))
			{
				grp=" CIRCLE_NAME,CIRCLE_ID ";
				subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" B.SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri  ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql=" select COUNT(CASE WHEN NATURE_OF_SERVICE='Title Transfer' THEN 1 ELSE NULL END ) AS COMPLAINTS,\r\n" + 
					"nvl(min(CASE WHEN NATURE_OF_SERVICE='Title Transfer'then TRUNC(DATE_OF_REDRESSAL)-TRUNC(DATE_OF_COMPLAINING)  else null end),0) min_days,\r\n" + 
					"nvl(max(CASE WHEN NATURE_OF_SERVICE='Title Transfer'then TRUNC(DATE_OF_REDRESSAL)-TRUNC(DATE_OF_COMPLAINING)  else null end),0) max_days,\r\n" + 
					"nvl(round(AVG(CASE WHEN NATURE_OF_SERVICE='Title Transfer'then TRUNC(DATE_OF_REDRESSAL)-TRUNC(DATE_OF_COMPLAINING) else null end),2),0)   avg_days,\r\n" + 
					"--nvl(round(SUM(CASE WHEN NATURE_OF_SERVICE='Title Transfer'AND trunc(DATE_OF_REDRESSAL)<=trunc(DATE_OF_COMPLAINING) THEN 1  else null end)),0) as rectified,\r\n" +
					"nvl(round((SUM(CASE WHEN NATURE_OF_SERVICE='Title Transfer' AND (trunc(DATE_OF_REDRESSAL)-trunc(DATE_OF_COMPLAINING))<=7 THEN 1 ELSE NULL END ) /COUNT(CASE WHEN NATURE_OF_SERVICE='Title Transfer'THEN 1 ELSE NULL END ) )*100,2),0)  per,"+subgrp+
					"FROM CSC_NSC.SOP_TITLE_TRANSFER A,COMMON_MASTER.OFFICE_MASTER B\r\n" + cond+
					" GROUP BY "+subgrp+"ORDER BY "+subgrp ;
		System.out.println("executed query:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getCategoryChangeData(CommonVo comvo) throws Exception {
		List<CommonVo> list=null;
		String sql="";
		String cond="";
		String grp="";
		String subgrp="";
		try {
			if(comvo.getSelectionType().equals("allcircles"))
			{
				grp=" CIRCLE_NAME,CIRCLE_ID ";
				subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" B.SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql=" select COUNT(CASE WHEN NATURE_OF_SERVICE='Category Change' THEN 1 ELSE NULL END ) AS COMPLAINTS,\r\n" + 
					"nvl(min(CASE WHEN NATURE_OF_SERVICE='Category Change'then TRUNC(DATE_OF_REDRESSAL)-TRUNC(DATE_OF_COMPLAINING)  else null end),0) min_days,\r\n" + 
					"nvl(max(CASE WHEN NATURE_OF_SERVICE='Category Change'then TRUNC(DATE_OF_REDRESSAL)-TRUNC(DATE_OF_COMPLAINING)  else null end),0) max_days,\r\n" + 
					"nvl(round(AVG(CASE WHEN NATURE_OF_SERVICE='Category Change'then TRUNC(DATE_OF_REDRESSAL)-TRUNC(DATE_OF_COMPLAINING) else null end),2),0)   avg_days,\r\n" +  
					"--nvl(round(SUM(CASE WHEN NATURE_OF_SERVICE='Category Change'AND trunc(DATE_OF_REDRESSAL)<=trunc(DATE_OF_COMPLAINING) THEN 1  else null end)),0) as rectified,\r\n" +
					"nvl(round((SUM(CASE WHEN NATURE_OF_SERVICE='Category Change' AND (trunc(DATE_OF_REDRESSAL)-trunc(DATE_OF_COMPLAINING))<=7 THEN 1 ELSE NULL END ) /COUNT(CASE WHEN NATURE_OF_SERVICE='Category Change'THEN 1 ELSE NULL END ) )*100,2),0)  per,"+subgrp+ 
					"FROM CSC_NSC.SOP_CATEGORY_CHANGE A,COMMON_MASTER.OFFICE_MASTER B\r\n" + cond+
					"GROUP BY "+subgrp+"ORDER BY "+subgrp ;
		System.out.println("executed query:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getResolutionBillingComplaintsData(CommonVo comvo) throws Exception {
		List<CommonVo> list=null;
		String sql="";
		String cond="";
		String grp="";
		String subgrp="";
		try {
			if(comvo.getSelectionType().equals("allcircles"))
			{
				grp=" CIRCLE_NAME,CIRCLE_ID ";
				subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
				cond=" WHERE A.SEC_NAME  = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.SEC_NAME  = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SEC_NAME  = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" B.SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME  = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select TOT_COMPLAINTS,NVL(extract( day from min_days),0) min_days,NVL(extract( day from max_days),0) max_days,\r\n" + 
					"NVL(avg_days,0) AS avg_days,NVL(per,0) AS per," + grp+
					"from ( select COUNT(CASE WHEN ADDL_INFO_REQUIRED ='NO' THEN 1 ELSE NULL END ) AS tot_COMPLAINTS,\r\n" + 
					"min(CASE WHEN ADDL_INFO_REQUIRED ='NO' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_days,\r\n" + 
					"max(CASE WHEN ADDL_INFO_REQUIRED ='NO' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) max_days,\r\n" + 
					"round(AVG(CASE WHEN ADDL_INFO_REQUIRED ='NO' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) avg_days,\r\n" + 
					"--round(SUM(CASE WHEN ADDL_INFO_REQUIRED ='NO' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end)) as rectified,\r\n" +
					"round((SUM(CASE WHEN ADDL_INFO_REQUIRED ='NO' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end) /COUNT(CASE WHEN ADDL_INFO_REQUIRED ='NO' THEN 1 ELSE NULL END ) )*100,2)   per,"+subgrp+ 
					"FROM CSC_NSC.SOP_RESOLUTION_BILL_COMPLAINT A,COMMON_MASTER.OFFICE_MASTER B\r\n" +cond + 
					"GROUP BY "+subgrp+"ORDER BY "+subgrp+")" ;
		System.out.println("executed query:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getResolutionBillingComplaintsAddlData(CommonVo comvo) throws Exception {
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
				//cond=" WHERE  A.CIRCLE = B.CIRCLE_NAME  AND  FIN_YEAR =:perFinYear ";
				cond=" WHERE  A.sec_name = B.section_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME  and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SEC_NAME  = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" SECTION_NAME, SECTION_ID ";
				cond=" WHERE   A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME  = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri  ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select TOT_COMPLAINTS,NVL(extract( day from min_days),0) min_days,NVL(extract( day from max_days),0) max_days,\r\n" + 
					"NVL(avg_days,0) AS avg_days,NVL(per,0) AS per," +grp+ 
					"from ( select COUNT(CASE WHEN ADDL_INFO_REQUIRED ='YES' THEN 1 ELSE NULL END ) AS tot_COMPLAINTS,\r\n" + 
					"min(CASE WHEN ADDL_INFO_REQUIRED ='YES' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_days,\r\n" + 
					"max(CASE WHEN ADDL_INFO_REQUIRED ='YES' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) max_days,\r\n" + 
					"round(AVG(CASE WHEN ADDL_INFO_REQUIRED ='YES' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) avg_days,\r\n" + 
					"--round(SUM(CASE WHEN ADDL_INFO_REQUIRED ='YES' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end)) as rectified,\r\n" +
					"round((SUM(CASE WHEN ADDL_INFO_REQUIRED ='YES' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end) /COUNT(CASE WHEN ADDL_INFO_REQUIRED ='YES' THEN 1 ELSE NULL END ) )*100,2)   per,"+subgrp+ 
					"FROM CSC_NSC.SOP_RESOLUTION_BILL_COMPLAINT A,COMMON_MASTER.OFFICE_MASTER B\r\n" +cond+ 
					"GROUP BY "+subgrp+"ORDER BY "+subgrp+")" ;
		System.out.println("executed query:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getReplacementBurntMetersLicenseData(CommonVo comvo) throws Exception {
		List<CommonVo> list=null;
		String sql="";
		String cond="";
		String grp="";
		String subgrp="";
		try {
			if(comvo.getSelectionType().equals("allcircles"))
			{
				grp=" CIRCLE_NAME,CIRCLE_ID ";
				subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri and   NATURE_OF_PROBLEM  IN ('Defective Meters') ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri  and   NATURE_OF_PROBLEM  IN ('Defective Meters') ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri and   NATURE_OF_PROBLEM  IN ('Defective Meters') ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" B.SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri  and   NATURE_OF_PROBLEM  IN ('Defective Meters') ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select COUNT(*) AS TOT_COMPLAINTS,\r\n"
					+ "nvl(min(TRUNC(REPLACE_MENT_DT)-TRUNC(DATE_OF_COMPLAINT)),0) min_Defective_Meters,\r\n"
					+ "nvl(max(TRUNC(REPLACE_MENT_DT)-TRUNC(DATE_OF_COMPLAINT)),0) max_Defective_Meters,\r\n"
					+ "nvl(round(AVG(TRUNC(REPLACE_MENT_DT)-TRUNC(DATE_OF_COMPLAINT)),2),0)   avg_Defective_Meters,\r\n"
					+ "nvl(round(sum(CASE WHEN (trunc(REPLACE_MENT_DT)-TRUNC(DATE_OF_COMPLAINT))<=7 THEN 1  else null end)/count(*)*100,2),0) as per," +subgrp
					+ "from CSC_NSC.SOP_CAUSE_OF_ATTRIBUTE A,COMMON_MASTER.OFFICE_MASTER B\r\n"
					+ cond
					+ " GROUP BY "+subgrp+"ORDER BY "+subgrp;
		System.out.println("executed query:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getNewConnectionsExistingNetworkData(CommonVo comvo) throws Exception {
		List<CommonVo> list=null;
		String sql="";
		String cond="";
		String grp="";
		String subgrp="";
		try {
			if(comvo.getSelectionType().equals("allcircles"))
			{
				grp=" CIRCLE_NAME,CIRCLE_ID ";
				subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri and   FEASIBILITY='From Existing Network' ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri  and   FEASIBILITY='From Existing Network' ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri and   FEASIBILITY='From Existing Network' ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" B.SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri  and   FEASIBILITY='From Existing Network' ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select COUNT(*) AS COMPLAINTS,\r\n"
					+ "nvl(min(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),0) min_days,\r\n"
					+ "nvl(max(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),0) max_days,\r\n"
					+ "nvl(round(AVG(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),2),0)   avg_days,\r\n"
					+ "nvl(round(sum(CASE WHEN (trunc(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION))<=3 THEN 1  else null end)/count(*)*100,2),0) as per,\r\n"
					+ "--nvl(round(SUM(CASE WHEN (trunc(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION))<=3 THEN 1  else null end)),0) as rectified,\r\n"+subgrp
					+ "FROM CSC_NSC.SOP_APPLICATION_NSC_ALP A,COMMON_MASTER.OFFICE_MASTER B\r\n"
					+ cond
					+ "GROUP BY "+subgrp+"ORDER BY "+subgrp;
		System.out.println("executed query:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getNewConnectionsNetworkExpansionLTHomeData(CommonVo comvo) throws Exception {
		
		List<CommonVo> list=null;
		String sql="";
		String cond="";
		String grp="";
		String subgrp="";
		try {
			if(comvo.getSelectionType().equals("allcircles"))
			{
				grp=" CIRCLE_NAME,CIRCLE_ID ";
				subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri and   VOLTAGE_LEVEL='LT' and FEASIBILITY='Network Expansion Required' ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri  and   VOLTAGE_LEVEL='LT' and FEASIBILITY='Network Expansion Required' ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri and   VOLTAGE_LEVEL='LT' and FEASIBILITY='Network Expansion Required' ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" B.SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri  and   VOLTAGE_LEVEL='LT' and FEASIBILITY='Network Expansion Required' ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			
			sql="select  COUNT(*) AS COMPLAINTS,\r\n"
					+ "nvl(min(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),0) min_days,\r\n"
					+ "nvl(max(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),0) max_days,\r\n"
					+ "nvl(round(AVG(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),2),0)   avg_days,\r\n"
					+ "nvl(round(sum(CASE WHEN (trunc(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION))<=7 THEN 1  else null end)/count(*)*100,2),0) as per,\r\n"
					+ "--nvl(round(SUM(CASE WHEN (trunc(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION))<=7 THEN 1  else null end)),0) as rectified,\r\n"
					+ subgrp + "FROM CSC_NSC.SOP_APPLICATION_NSC_ALP A,COMMON_MASTER.OFFICE_MASTER B\r\n"
					+ cond
					+ " GROUP BY "+subgrp+"ORDER BY "+subgrp;
			
		System.out.println("executed query:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getSingleToThreePhaseLTHomeData(CommonVo comvo) throws Exception {
		List<CommonVo> list=null;
		String sql="";
		String cond="";
		String grp="";
		String subgrp="";
		try {
			if(comvo.getSelectionType().equals("allcircles"))
			{
				grp=" CIRCLE_NAME,CIRCLE_ID ";
				subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME and A.CIRCLE_NAME = B.CIRCLE_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri and   NATURE_OF_SERVICE='LT Single Phase to 3Phase' ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri  and   NATURE_OF_SERVICE='LT Single Phase to 3Phase' ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri and   NATURE_OF_SERVICE='LT Single Phase to 3Phase' ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" B.SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri  and   NATURE_OF_SERVICE='LT Single Phase to 3Phase' ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select COUNT(*) AS COMPLAINTS,\r\n"
					+ "nvl(min(TRUNC(DATE_OF_REDRESSAL)-TRUNC(DATEOF_TIMEOF_COMPLAINING)),0) min_days,\r\n"
					+ "nvl(max(TRUNC(DATE_OF_REDRESSAL)-TRUNC(DATEOF_TIMEOF_COMPLAINING)),0) max_days,\r\n"
					+ "nvl(round(AVG(TRUNC(DATE_OF_REDRESSAL)-TRUNC(DATEOF_TIMEOF_COMPLAINING)),2),0)   avg_days,\r\n"
					+"nvl(round(sum(CASE WHEN (trunc(DATE_OF_REDRESSAL)-TRUNC(DATEOF_TIMEOF_COMPLAINING))<=30 THEN 1  else null end)/count(*)*100,2),0) as per, \r\n"
					+ "--nvl(round(SUM(CASE WHEN (trunc(DATE_OF_REDRESSAL)-TRUNC(DATEOF_TIMEOF_COMPLAINING))<=30 THEN 1  else null end)),0) as rectified, \r\n"+subgrp
					+ "FROM CSC_NSC.SOP_LT_SINGLE_TO_THREE_PHASE A,COMMON_MASTER.OFFICE_MASTER B\r\n"
					+ cond
					+ " GROUP BY "+subgrp+"ORDER BY "+subgrp;
		System.out.println("executed query:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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
				int fld2no =Integer.parseInt(commVo.getFld2());
				int res2=Math.max(0, fld2no);
				commVo.setFld2(Integer.toString(res2));
				commVo.setFld3(obj[2]==null ? "" : obj[2].toString());
				commVo.setFld4(obj[3]==null ? "" : obj[3].toString());
				commVo.setFld5(obj[4]==null ? "" : obj[4].toString());
				commVo.setFld6(obj[5]==null ? "" : obj[5].toString());
				commVo.setFld7(obj[6]==null ? "" : obj[6].toString());
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




	@Override
	public List<CommonVo> getReconnectionPaymentBillsData(CommonVo comvo) throws Exception {
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
				cond=" WHERE  A.sec_name = B.section_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" SECTION_NAME, SECTION_ID ";
				cond=" WHERE   A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select URBAN_COMPLAINTS,\r\n"
					+ "DECODE(extract( hour from min_urban)||'.'||extract( minute from min_urban ),'.','0.0',extract( hour from min_urban)||'.'||extract( minute from min_urban )) AS min_urban,\r\n"
					+ "nvl(max_urban,0) as max_urban,NVL(avg_urban,0) AS avg_urban,nvl(per_urban,0) as per_urban,\r\n"
					+ "RURAL_COMPLAINTS,\r\n"
					+ "DECODE(extract( hour from min_rural)||'.'||extract( minute from min_rural ),'.','0.0',extract( hour from min_rural)||'.'||extract( minute from min_rural )) AS min_rural,\r\n"
					+ "nvl(max_rural,0) as max_rural,NVL(avg_rural,0) AS avg_rural,nvl(per_RURAL,0) as per_RURAL,"+grp+"\r\n"
					+ " from (select COUNT(CASE WHEN AREA_CLASSIFICATION='URBAN' THEN 1 ELSE NULL END ) AS URBAN_COMPLAINTS,\r\n"
					+ "min(CASE WHEN AREA_CLASSIFICATION='URBAN' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_urban,\r\n"
					+ "round(max(CASE WHEN AREA_CLASSIFICATION='URBAN' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end)*24,0)  max_urban,\r\n"
					+ "round(AVG(CASE WHEN AREA_CLASSIFICATION='URBAN' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) *24  avg_urban,\r\n"
					+ "round((SUM(CASE WHEN AREA_CLASSIFICATION='URBAN' AND RECTIFIED_ON<=DISCOM_ETR THEN 1  else null end) /COUNT(CASE WHEN AREA_CLASSIFICATION='URBAN' THEN 1 ELSE NULL END ) )*100,2)   per_urban,\r\n"
					+ "COUNT(CASE WHEN AREA_CLASSIFICATION='RURAL' THEN 1 ELSE NULL END ) AS RURAL_COMPLAINTS,\r\n"
					+ "min(CASE WHEN AREA_CLASSIFICATION='RURAL' then RECTIFIED_ON  - COMPLAINT_REG_DT  else null end) min_rural,\r\n"
					+ "round(max(CASE WHEN AREA_CLASSIFICATION='RURAL' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end)*24,0)  max_rural,\r\n"
					+ "round(AVG(CASE WHEN AREA_CLASSIFICATION='RURAL' then (CAST(RECTIFIED_ON as date)) - (CAST(COMPLAINT_REG_DT as date )) else null end),2) *24  avg_rural,\r\n"
					+ "round((SUM(CASE WHEN AREA_CLASSIFICATION='RURAL'  AND RECTIFIED_ON<=DISCOM_ETR THEN 1 ELSE NULL END ) /COUNT(CASE WHEN AREA_CLASSIFICATION='RURAL' THEN 1 ELSE NULL END ) )*100,2)  per_RURAL,"+subgrp+"\r\n"
					+ "FROM CSC_NSC.SOP_RECONN_DISCONN_NONPAYMENT_BILLS A,COMMON_MASTER.OFFICE_MASTER B\r\n"
					+ cond
					+ "GROUP BY "+subgrp+"ORDER BY "+subgrp+")";
		
		Query query = dbEntityManager1.createNativeQuery(sql);
		System.out.println("query executed:"+sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getWrongfulDisconnectionOfServiceData(CommonVo comvo) throws Exception {
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
				cond=" WHERE  A.sec_name = B.section_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.sec_name = B.section_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" SECTION_NAME, SECTION_ID ";
				cond=" WHERE   A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select NO_OF_COMPLAINTS,"+grp+" from (\r\n"
					+ "select count(*) as NO_OF_COMPLAINTS,"+subgrp+"\r\n"
					+ "FROM CSC_NSC.SOP_WRONGFULL_DISCONNECTION A,COMMON_MASTER.OFFICE_MASTER B\r\n"
					+ cond
					+ " GROUP BY "+subgrp+"ORDER BY "+subgrp+")";
			System.out.println("query executed:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getScheduledOutagesMaxDurationData(CommonVo comvo) throws Exception {
		
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
				cond=" WHERE  A.SEC_NAME = B.SECTION_NAME and A.CIRCLE = B.CIRCLE_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.SEC_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SEC_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri  ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="select count(*) as outages,\r\n"
					+ "round(min(DATE_TIME_RESTORATION-DATE_TIME_OUTAGES)*24) as min,\r\n"
					+ "round(max(DATE_TIME_RESTORATION-DATE_TIME_OUTAGES)*24) as max,\r\n"
					+ "round(avg(DATE_TIME_RESTORATION-DATE_TIME_OUTAGES)*24,2) as avg,\r\n"
					+ "round(count(case when ((DATE_TIME_RESTORATION-DATE_TIME_OUTAGES)*24) <=12  then 1 else null end)/ COUNT(*)*100,2) as per,"+subgrp
					+ "FROM SOP_SCHEDULED_OUTAGES a,OFFICE_MASTER B\r\n"
					+ cond
					+ "GROUP BY "+subgrp+"ORDER BY "+subgrp;
		System.out.println("executed query:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> getScheduledOutagesRestorationsupplyData(CommonVo comvo) throws Exception {
		
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
				cond=" WHERE  A.SEC_NAME = B.SECTION_NAME and A.CIRCLE = B.CIRCLE_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("alldivisons"))
			{
				grp=" DIVISION_NAME, DIVISION_ID ";
				subgrp=" B.DIVISION_NAME, DIVISION_ID ";
				cond=" WHERE  A.SEC_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri  ";
			}else if(comvo.getSelectionType().equals("allsubdivisons"))
			{
				grp=" SUBDIV_NAME, SUBDIV_ID ";
				subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
				cond=" WHERE  A.SEC_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri ";
			}else if(comvo.getSelectionType().equals("allsections"))
			{
				grp=" SECTION_NAME, SECTION_ID ";
				subgrp=" SECTION_NAME, SECTION_ID ";
				cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SEC_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri  ";
			}
			/*
			 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
			 */
			sql="SELECT count(*) as outages,\r\n"
					+ "count(case when to_char(DATE_TIME_RESTORATION,'hh24') > 18 then 1 else null end)  as after_6pm,\r\n"
					+ "count(case when to_char(DATE_TIME_RESTORATION,'hh24') <= 18  then 1 else null end)  as before_6pm,\r\n"
					+ "round(count(case when to_char(DATE_TIME_RESTORATION,'hh24') <= 18  then 1 else null end)/ COUNT(*)*100,2) as per_before_6pm,"+subgrp
					+ "FROM SOP_SCHEDULED_OUTAGES A,OFFICE_MASTER B\r\n"
					+ cond
					+ "GROUP BY "+subgrp+"ORDER BY "+subgrp;
		System.out.println("executed query:"+sql);
		Query query = dbEntityManager1.createNativeQuery(sql);
		 query.setParameter("perFinYear", comvo.getFld2());
		 query.setParameter("peri", comvo.getFld1());
			/*
			 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
			 * comvo.getFld1()); }
			 */
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




	@Override
	public List<CommonVo> newConnectionsNetworkExpansion11Kv(CommonVo comvo) throws Exception {
			List<CommonVo> list=null;
			String sql="";
			String cond="";
			String grp="";
			String subgrp="";
			try {
				if(comvo.getSelectionType().equals("allcircles"))
				{
					grp=" CIRCLE_NAME,CIRCLE_ID ";
					subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
					cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri and   VOLTAGE_LEVEL='11KV' and FEASIBILITY='Network Expansion Required' ";
				}else if(comvo.getSelectionType().equals("alldivisons"))
				{
					grp=" DIVISION_NAME, DIVISION_ID ";
					subgrp=" B.DIVISION_NAME, DIVISION_ID ";
					cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri  and   VOLTAGE_LEVEL='11KV' and FEASIBILITY='Network Expansion Required' ";
				}else if(comvo.getSelectionType().equals("allsubdivisons"))
				{
					grp=" SUBDIV_NAME, SUBDIV_ID ";
					subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
					cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri and   VOLTAGE_LEVEL='11KV' and FEASIBILITY='Network Expansion Required' ";
				}else if(comvo.getSelectionType().equals("allsections"))
				{
					grp=" SECTION_NAME, SECTION_ID ";
					subgrp=" B.SECTION_NAME, SECTION_ID ";
					cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri  and   VOLTAGE_LEVEL='11KV' and FEASIBILITY='Network Expansion Required' ";
				}
				/*
				 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
				 */
				sql="select COUNT(*) AS COMPLAINTS,\r\n"
						+ "nvl(min(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),0) min_days,\r\n"
						+ "nvl(max(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),0) max_days,\r\n"
						+ "nvl(round(AVG(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),2),0)   avg_days,\r\n"
						+ "nvl(round(sum(CASE WHEN (trunc(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION))<=15 THEN 1  else null end)/count(*)*100,2),0) as per,\r\n"
						+ "--nvl(round(SUM(CASE WHEN (trunc(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION))<=15 THEN 1  else null end)),0) as rectified,\r\n"
						+ subgrp + " FROM CSC_NSC.SOP_APPLICATION_NSC_ALP A, COMMON_MASTER.OFFICE_MASTER B\r\n"
						+ cond
						+ " GROUP BY "+subgrp+"ORDER BY "+subgrp;
				
			System.out.println("executed query:"+sql);
			Query query = dbEntityManager1.createNativeQuery(sql);
			 query.setParameter("perFinYear", comvo.getFld2());
			 query.setParameter("peri", comvo.getFld1());
				/*
				 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
				 * comvo.getFld1()); }
				 */
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




	@Override
	public List<CommonVo> getnewConnectionsNetworkExpansion33Kv(CommonVo comvo) throws Exception {
				List<CommonVo> list=null;
				String sql="";
				String cond="";
				String grp="";
				String subgrp="";
				try {
					if(comvo.getSelectionType().equals("allcircles"))
					{
						grp=" CIRCLE_NAME,CIRCLE_ID ";
						subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
						cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri and   VOLTAGE_LEVEL='33KV' and FEASIBILITY='Network Expansion Required' ";
					}else if(comvo.getSelectionType().equals("alldivisons"))
					{
						grp=" DIVISION_NAME, DIVISION_ID ";
						subgrp=" B.DIVISION_NAME, DIVISION_ID ";
						cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri  and   VOLTAGE_LEVEL='33KV' and FEASIBILITY='Network Expansion Required' ";
					}else if(comvo.getSelectionType().equals("allsubdivisons"))
					{
						grp=" SUBDIV_NAME, SUBDIV_ID ";
						subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
						cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri and   VOLTAGE_LEVEL='33KV' and FEASIBILITY='Network Expansion Required' ";
					}else if(comvo.getSelectionType().equals("allsections"))
					{
						grp=" SECTION_NAME, SECTION_ID ";
						subgrp=" B.SECTION_NAME, SECTION_ID ";
						cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri  and   VOLTAGE_LEVEL='33KV' and FEASIBILITY='Network Expansion Required' ";
					}
					/*
					 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
					 */
					sql="select COUNT(*) AS COMPLAINTS,\r\n"
							+ "nvl(min(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),0) min_days,\r\n"
							+ "nvl(max(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),0) max_days,\r\n"
							+ "nvl(round(AVG(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),2),0)   avg_days,\r\n"
							+ "nvl(round(sum(CASE WHEN (trunc(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION))<=30 THEN 1  else null end)/count(*)*100,2),0) as per,\r\n"
							+ "--nvl(round(SUM(CASE WHEN (trunc(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION))<=30 THEN 1  else null end)),0) as rectified,\r\n"
							+ subgrp+ " FROM CSC_NSC.SOP_APPLICATION_NSC_ALP A,COMMON_MASTER.OFFICE_MASTER B\r\n"
							+ cond
							+ " GROUP BY "+subgrp+"ORDER BY "+subgrp;
					
				System.out.println("executed query:"+sql);
				Query query = dbEntityManager1.createNativeQuery(sql);
				 query.setParameter("perFinYear", comvo.getFld2());
				 query.setParameter("peri", comvo.getFld1());
					/*
					 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
					 * comvo.getFld1()); }
					 */
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




	@Override
	public List<CommonVo> getnewConnectionsNetworkExpansion132Kv(CommonVo comvo) throws Exception {
					List<CommonVo> list=null;
					String sql="";
					String cond="";
					String grp="";
					String subgrp="";
					try {
						if(comvo.getSelectionType().equals("allcircles"))
						{
							grp=" CIRCLE_NAME,CIRCLE_ID ";
							subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
							cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear AND PERIOD=:peri and   VOLTAGE_LEVEL='132KV' and FEASIBILITY='Network Expansion Required' ";
						}else if(comvo.getSelectionType().equals("alldivisons"))
						{
							grp=" DIVISION_NAME, DIVISION_ID ";
							subgrp=" B.DIVISION_NAME, DIVISION_ID ";
							cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId AND PERIOD=:peri  and   VOLTAGE_LEVEL='132KV' and FEASIBILITY='Network Expansion Required' ";
						}else if(comvo.getSelectionType().equals("allsubdivisons"))
						{
							grp=" SUBDIV_NAME, SUBDIV_ID ";
							subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
							cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId  AND PERIOD=:peri and   VOLTAGE_LEVEL='132KV' and FEASIBILITY='Network Expansion Required' ";
						}else if(comvo.getSelectionType().equals("allsections"))
						{
							grp=" SECTION_NAME, SECTION_ID ";
							subgrp=" B.SECTION_NAME, SECTION_ID ";
							cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId AND PERIOD=:peri  and   VOLTAGE_LEVEL='132KV' and FEASIBILITY='Network Expansion Required' ";
						}
						/*
						 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
						 */
						sql="select COUNT(*) AS COMPLAINTS,\r\n"
								+ "nvl(min(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),0) min_days,\r\n"
								+ "nvl(max(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),0) max_days,\r\n"
								+ "nvl(round(AVG(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),2),0)   avg_days,\r\n"
								+ "nvl(round(sum(CASE WHEN (trunc(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION))<=30 THEN 1  else null end)/count(*)*100,2),0) as per,\r\n"
								+ "--nvl(round(SUM(CASE WHEN (trunc(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION))<=30 THEN 1  else null end)),0) as rectified,\r\n"
								+ subgrp+ " FROM CSC_NSC.SOP_APPLICATION_NSC_ALP A,COMMON_MASTER.OFFICE_MASTER B\r\n"
								+ cond
								+ " GROUP BY "+subgrp;
						
						sql="select COUNT(*) AS COMPLAINTS,\r\n"
								+ "nvl(min(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),0) min_days,\r\n"
								+ "nvl(max(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),0) max_days,\r\n"
								+ "nvl(round(AVG(TRUNC(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION)),2),0)   avg_days,\r\n"
								+ "nvl(round(sum(CASE WHEN (trunc(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION))<=45 THEN 1  else null end)/count(*)*100,2),0) as per,\r\n"
								+ "--nvl(round(SUM(CASE WHEN (trunc(DATEOF_TIME_ATTENDING)-TRUNC(DATEOF_APPLICATION))<=45 THEN 1  else null end)),0) as rectified,\r\n"
								+ subgrp +" FROM CSC_NSC.SOP_APPLICATION_NSC_ALP A,COMMON_MASTER.OFFICE_MASTER B \r\n"
								+ cond
								+ " GROUP BY "+subgrp+"ORDER BY "+subgrp;
						
					System.out.println("executed query:"+sql);
					Query query = dbEntityManager1.createNativeQuery(sql);
					 query.setParameter("perFinYear", comvo.getFld2());
					 query.setParameter("peri", comvo.getFld1());
						/*
						 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
						 * comvo.getFld1()); }
						 */
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




	@Override
	public List<CommonVo> getreleaseNewConnection11Kvdata(CommonVo comvo) throws Exception {
		
			List<CommonVo> list=null;
			String sql="";
			String cond="";
			String grp="";
			String subgrp="";
			try {
				if(comvo.getSelectionType().equals("allcircles"))
				{
					grp=" CIRCLE_NAME,CIRCLE_ID ";
					subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
					cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='11KV' ";
				}else if(comvo.getSelectionType().equals("alldivisons"))
				{
					grp=" DIVISION_NAME, DIVISION_ID ";
					subgrp=" B.DIVISION_NAME, DIVISION_ID ";
					cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='11KV' ";
				}else if(comvo.getSelectionType().equals("allsubdivisons"))
				{
					grp=" SUBDIV_NAME, SUBDIV_ID ";
					subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
					cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='11KV' ";
				}else if(comvo.getSelectionType().equals("allsections"))
				{
					grp=" SECTION_NAME, SECTION_ID ";
					subgrp=" B.SECTION_NAME, SECTION_ID ";
					cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='11KV' ";
				}
				/*
				 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
				 */
				sql="select COUNT(*) AS COMPLAINTS,\r\n"
						+ "nvl(min(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),0) min_days,\r\n"
						+ "nvl(max(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),0) max_days,\r\n"
						+ "nvl(round(AVG(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),2),0)   avg_days,\r\n"
						+ "nvl(round(sum(CASE WHEN (trunc(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT))<=60 THEN 1  else null end)/count(*)*100,2),0) as per,\r\n"
						+ "--nvl(round(SUM(CASE WHEN (trunc(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT))<=60 THEN 1  else null end)),0) as rectified,\r\n"
						+ subgrp+" FROM CSC_NSC.SOP_RELEASE_NSC_ALP A,COMMON_MASTER.OFFICE_MASTER B\r\n"
						+ cond
						+ " GROUP BY "+subgrp+"ORDER BY "+subgrp;
			System.out.println("executed query:"+sql);
			Query query = dbEntityManager1.createNativeQuery(sql);
			 query.setParameter("perFinYear", comvo.getFld2());
			 query.setParameter("peri", comvo.getFld1());
				/*
				 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
				 * comvo.getFld1()); }
				 */
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
					
					int fld2=Integer.parseInt(commVo.getFld2());  
					if(fld2<0) {
					int res=Math.max(0,fld2);
					String setfld2=String.valueOf(res);
					commVo.setFld2(setfld2);}
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




	@Override
	public List<CommonVo> getreleaseNewConnection33Kvdata(CommonVo comvo) throws Exception {
				List<CommonVo> list=null;
				String sql="";
				String cond="";
				String grp="";
				String subgrp="";
				try {
					if(comvo.getSelectionType().equals("allcircles"))
					{
						grp=" CIRCLE_NAME,CIRCLE_ID ";
						subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
						cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='33KV' ";
					}else if(comvo.getSelectionType().equals("alldivisons"))
					{
						grp=" DIVISION_NAME, DIVISION_ID ";
						subgrp=" B.DIVISION_NAME, DIVISION_ID ";
						cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='33KV' ";
					}else if(comvo.getSelectionType().equals("allsubdivisons"))
					{
						grp=" SUBDIV_NAME, SUBDIV_ID ";
						subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
						cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='33KV' ";
					}else if(comvo.getSelectionType().equals("allsections"))
					{
						grp=" SECTION_NAME, SECTION_ID ";
						subgrp=" B.SECTION_NAME, SECTION_ID ";
						cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='33KV' ";
					}
					/*
					 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
					 */
					
					sql="select COUNT(*) AS COMPLAINTS,\r\n"
							+ "nvl(min(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),0) min_days,\r\n"
							+ "nvl(max(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),0) max_days,\r\n"
							+ "nvl(round(AVG(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),2),0)   avg_days,\r\n"
							+ "nvl(round(sum(CASE WHEN (trunc(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT))<=90 THEN 1  else null end)/count(*)*100,2),0) as per,\r\n"
							+ "--nvl(round(SUM(CASE WHEN (trunc(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT))<=90 THEN 1  else null end)),0) as rectified,\r\n"
							+subgrp+ " FROM CSC_NSC.SOP_RELEASE_NSC_ALP A,COMMON_MASTER.OFFICE_MASTER B\r\n"
							+ cond
							+ " GROUP BY "+subgrp+"ORDER BY "+subgrp;
				System.out.println("executed query:"+sql);
				Query query = dbEntityManager1.createNativeQuery(sql);
				 query.setParameter("perFinYear", comvo.getFld2());
				 query.setParameter("peri", comvo.getFld1());
					/*
					 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
					 * comvo.getFld1()); }
					 */
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
						
						int fld2=Integer.parseInt(commVo.getFld2());  
						if(fld2<0) {
						int res=Math.max(0,fld2);
						String setfld2=String.valueOf(res);
						commVo.setFld2(setfld2);}
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




	@Override
	public List<CommonVo> getreleaseNewConnection132Kvdata(CommonVo comvo) throws Exception {
					List<CommonVo> list=null;
					String sql="";
					String cond="";
					String grp="";
					String subgrp="";
					try {
						if(comvo.getSelectionType().equals("allcircles"))
						{
							grp=" CIRCLE_NAME,CIRCLE_ID ";
							subgrp=" B.CIRCLE_NAME,CIRCLE_ID ";
							cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME  AND  FIN_YEAR =:perFinYear and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='132KV' ";
						}else if(comvo.getSelectionType().equals("alldivisons"))
						{
							grp=" DIVISION_NAME, DIVISION_ID ";
							subgrp=" B.DIVISION_NAME, DIVISION_ID ";
							cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME and   FIN_YEAR =:perFinYear AND CIRCLE_ID=:selectionId and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='132KV' ";
						}else if(comvo.getSelectionType().equals("allsubdivisons"))
						{
							grp=" SUBDIV_NAME, SUBDIV_ID ";
							subgrp=" B.SUBDIV_NAME, SUBDIV_ID ";
							cond=" WHERE  A.SECTION_NAME = B.SECTION_NAME AND A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME and  FIN_YEAR =:perFinYear AND DIVISION_ID=:selectionId and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='132KV' ";
						}else if(comvo.getSelectionType().equals("allsections"))
						{
							grp=" SECTION_NAME, SECTION_ID ";
							subgrp=" B.SECTION_NAME, SECTION_ID ";
							cond=" WHERE  A.DIVISION_NAME = B.DIVISION_NAME AND A.SUBDIV_NAME = B.SUBDIV_NAME AND A.SECTION_NAME = B.SECTION_NAME and FIN_YEAR =:perFinYear AND SUBDIV_ID=:selectionId and  FEASIBILITY='Network Expansion Required' AND PERIOD=:peri AND VOLTAGE_LEVEL='132KV' ";
						}
						/*
						 * if(!comvo.getFld1().equals("ALL")) { cond=cond+" AND PERIOD=:peri  "; }
						 */
						
						sql="select COUNT(*) AS COMPLAINTS,\r\n"
								+ "nvl(min(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),0) min_days,\r\n"
								+ "nvl(max(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),0) max_days,\r\n"
								+ "nvl(round(AVG(TRUNC(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT)),2),0)   avg_days,\r\n"
								+ "nvl(round(sum(CASE WHEN (trunc(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT))<=180 THEN 1  else null end)/count(*)*100,2),0) as per,\r\n"
								+ "--nvl(round(SUM(CASE WHEN (trunc(DATEOF_TIME_RELEASE)-TRUNC(DATE_TIME_OF_PAYMENT))<=180 THEN 1  else null end)),0) as rectified,\r\n"
								+ subgrp+" FROM CSC_NSC.SOP_RELEASE_NSC_ALP A,COMMON_MASTER.OFFICE_MASTER B\r\n"
								+ cond
								+ " GROUP BY "+subgrp+"ORDER BY "+subgrp;
					System.out.println("executed query:"+sql);
					Query query = dbEntityManager1.createNativeQuery(sql);
					 query.setParameter("perFinYear", comvo.getFld2());
					 query.setParameter("peri", comvo.getFld1());
						/*
						 * if(!comvo.getFld1().equals("ALL")) { query.setParameter("peri",
						 * comvo.getFld1()); }
						 */
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
							
							int fld2=Integer.parseInt(commVo.getFld2());  
							if(fld2<0) {
							int res=Math.max(0,fld2);
							String setfld2=String.valueOf(res);
							commVo.setFld2(setfld2);}
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
