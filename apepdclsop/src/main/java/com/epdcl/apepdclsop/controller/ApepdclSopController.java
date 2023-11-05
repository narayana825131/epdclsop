package com.epdcl.apepdclsop.controller;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.epdcl.apepdclsop.service.EPSopAnalysisService;
import com.epdcl.apepdclsop.vo.CommonVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Controller
public class ApepdclSopController {

	Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	EPSopAnalysisService epSopAnalysisService;
	
	@RequestMapping(value= {"/","home"})
	public String indexPage(HttpServletRequest req, ModelMap model) throws Exception {
		
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getFuseOffReportAbs(comvo);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "home";
	}
	
	@RequestMapping(value ="fuseOffReportAbsDiv", method = RequestMethod.POST)
	public String fuseOffReportAbsDiv(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		String view="fuseOffReportAbsDiv";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getFuseOffReportAbs(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	/*overHeadLineBreakdownHome reports start*/
	@RequestMapping(value= {"/overHeadLineBreakdownHome"})
	public String  overHeadLineBreakdownHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getOverHeadBreakdown(comvo);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "overHeadHome";
	}
	
	
	@RequestMapping(value ="/overHeadLineBreakdownReport", method = RequestMethod.POST)
	public String overHeadLineBreakdownReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		System.out.println("inputs are:"+selectionType+"="+selectionName+"="+selectionId
				+"="+peri+"+"+perFinYear);
		
		String view="overHeadReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getOverHeadBreakdown(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	/*overHeadLineBreakdownHome reports end*/
	
	@RequestMapping(value= {"/underGroundCableBreakDownHome"})
	public String  underGroundCableBreakDownHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getUGCableBreakDown(comvo);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "underGroundHome";
	}
	
	
	@RequestMapping(value ="/underGroundCableBreakDownReport", method = RequestMethod.POST)
	public String underGroundCableBreakDownReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		System.out.println("inputs are:"+selectionType+"="+selectionName+"="+selectionId
				+"="+peri+"+"+perFinYear);
		
		String view="underGroundReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getUGCableBreakDown(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	@RequestMapping(value= {"/DTrFailuresHome"})
	public String  DTrFailuresHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getDTrFailures(comvo);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "dTrFailuresHome";
	}
	
	@RequestMapping(value ="/dTrFailuresReport", method = RequestMethod.POST)
	public String dTrFailuresReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		System.out.println("inputs are:"+selectionType+"="+selectionName+"="+selectionId
				+"="+peri+"+"+perFinYear);
		
		String view="dTrFailuresReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getDTrFailures(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	@RequestMapping(value= {"/voltageFluctuationHome"})
	public String  voltageFluctuationHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getvoltageFluctuation(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "voltageFluctuationsHome";
	}
	
	@RequestMapping(value ="/voltageFluctuationReport", method = RequestMethod.POST)
	public String voltageFluctuationReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="voltageFluctuationReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getvoltageFluctuation(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	@RequestMapping(value= {"/voltageFluctuationExpansionHome"})
	public String  voltageFluctuationExpansionHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getVoltageFluctuationExpansion(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "VoltageFluctuationsExpansionHome";
	}
	
	@RequestMapping(value ="/voltageFluctuationExpansionReport", method = RequestMethod.POST)
	public String voltageFluctuationExpansionReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="voltageFluctuationExpansionReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getVoltageFluctuationExpansion(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	@RequestMapping(value= {"/ReplacementDefectiveMetersHome"})
	public String  ReplacementDefectiveMetersHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getReplacementDefectiveMeters(comvo);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "ReplacementDefectiveMetersHome";
	}
	
	@RequestMapping(value= {"/ReplacementDefectiveMetersReport"})
	public String ReplacementDefectiveMetersReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		System.out.println("inputs are:"+selectionType+"="+selectionName+"="+selectionId
				+"="+peri+"+"+perFinYear);
		
		String view="replacementDefectiveMetersReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getReplacementDefectiveMeters(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	
	
	@RequestMapping(value= {"/ReplacementBurntMetersConsumerHome"})
	public String  ReplacementBurntMetersConsumerHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getReplacementBurntMetersConsumer(comvo);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "replacementBurntMetersConsumerHome";
	}
	
	
	@RequestMapping(value= {"/ReplacementBurntMetersConsumerReport"})
	public String ReplacementBurntMetersConsumerReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		System.out.println("inputs are:"+selectionType+"="+selectionName+"="+selectionId
				+"="+peri+"+"+perFinYear);
		
		String view="replacementBurntMetersConsumerReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getReplacementBurntMetersConsumer(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	@RequestMapping(value= {"/releaseNewConnectionExistingHome"})
	public String  releaseNewConnectionExistingHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getReleaseNewConnectionExisting(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "releaseNewConnectionExistingHome";
	}
	
	@RequestMapping(value ="/releaseNewConnectionExistingReport", method = RequestMethod.POST)
	public String releaseNewConnectionExistingReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="releaseNewConnectionExistingReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getReleaseNewConnectionExisting(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	@RequestMapping(value= {"/releaseNewConnectionLTHome"})
	public String  releaseNewConnectionLTHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getreleaseNewConnectionLT(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "releaseNewConnectionLTHome";
	}
	
	@RequestMapping(value ="/releaseNewConnectionLTReport", method = RequestMethod.POST)
	public String releaseNewConnectionLTReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="releaseNewConnectionLTReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getreleaseNewConnectionLT(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	@RequestMapping(value= {"/titleTransferHome"})
	public String  titleTransferHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.gettitleTransfer(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "titleTransferHome";
	}
	
	
	
	@RequestMapping(value ="/titleTransferReport", method = RequestMethod.POST)
	public String titleTransferReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="titleTransferReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.gettitleTransfer(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	@RequestMapping(value= {"/categoryChangeHome"})
	public String  categoryChangeHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getCategoryChange(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "categoryChangeHome";
	}
	
	
	@RequestMapping(value ="/categoryChangeReport", method = RequestMethod.POST)
	public String categoryChangeReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="categoryChangeReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getCategoryChange(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	@RequestMapping(value= {"/resolutionBillingComplaintsHome"})
	public String  resolutionBillingComplaintsHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getResolutionBillingComplaints(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "resolutionBillingComplaintsHome";
	}
	
	

	@RequestMapping(value ="/resolutionBillingComplaintsReport", method = RequestMethod.POST)
	public String resolutionBillingComplaintsReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="resolutionBillingComplaintsReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getResolutionBillingComplaints(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}

	@RequestMapping(value= {"/resolutionBillingComplaintsAddlHome"})
	public String  resolutionBillingComplaintsAddlHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getResolutionBillingComplaintsAddl(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "resolutionBillingComplaintsAddlHome";
	}
	
	
	@RequestMapping(value ="/resolutionBillingComplaintsAddlReport", method = RequestMethod.POST)
	public String resolutionBillingComplaintsAddlReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="resolutionBillingComplaintsAddlReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getResolutionBillingComplaintsAddl(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	/*
	 * @RequestMapping(value ="/error", method = RequestMethod.GET) public String
	 * pageUnderConstrution() { return "error"; }
	 */
	
	@RequestMapping(value= {"/replacementBurntMetersLicense"})
	public String  replacementBurntMetersLicenseHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getReplacementBurntMetersLicense(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "replacementBurntMetersLicenseHome";
	}
	
	@RequestMapping(value ="/replacementBurntMetersLicenseReport", method = RequestMethod.POST)
	public String replacementBurntMetersLicenseReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="replacementBurntMetersLicenseReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getReplacementBurntMetersLicense(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	@RequestMapping(value= {"/newConnectionsExistingNetworkHome"})
	public String  newConnectionsExistingNetworkHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getNewConnectionsExistingNetwork(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "newConnectionsExistingNetworkHome"; 
	}
	
	@RequestMapping(value ="/newConnectionsExistingNetworkReport", method = RequestMethod.POST)
	public String newConnectionsExistingNetworkReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="newConnectionsExistingNetworkReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getNewConnectionsExistingNetwork(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	@RequestMapping(value= {"/newConnectionsNetworkExpansionLT"})
	public String  newConnectionsNetworkExpansionLTHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getNewConnectionsNetworkExpansionLTHome(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "newConnectionsNetworkExpansionLTHome"; 
	}
	
	
	@RequestMapping(value ="/newConnectionsNetworkExpansionLTReport", method = RequestMethod.POST)
	public String newConnectionsNetworkExpansionLTReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="newConnectionsNetworkExpansionLTReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getNewConnectionsNetworkExpansionLTHome(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	@RequestMapping(value= {"/singleToThreePhaseLT"})
	public String  singleToThreePhaseLTHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getSingleToThreePhaseLTHome(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "singleToThreePhaseLTHome"; 
	}
	
	@RequestMapping(value ="/singleToThreePhaseLTReport", method = RequestMethod.POST)
	public String singleToThreePhaseLTReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="singleToThreePhaseLTReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getSingleToThreePhaseLTHome(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	@RequestMapping(value= {"/reconnectionPaymentBills"})
	public String  reconnectionPaymentBillsHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getReconnectionPaymentBills(comvo);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "reconnectionPaymentBillsHome";
	}
	
	@RequestMapping(value ="/reconnectionPaymentBillsReport", method = RequestMethod.POST)
	public String reconnectionPaymentBillsReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		System.out.println("inputs are:"+selectionType+"="+selectionName+"="+selectionId
				+"="+peri+"+"+perFinYear);
		
		String view="reconnectionPaymentBillsReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getReconnectionPaymentBills(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	@RequestMapping(value= {"/wrongfulDisconnectionOfService"})
	public String  wrongfulDisconnectionOfServiceHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getWrongfulDisconnectionOfService(comvo);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "wrongfulDisconnectionOfServiceHome";
	}
	
	@RequestMapping(value ="/wrongfulDisconnectionOfServiceReport", method = RequestMethod.POST)
	public String wrongfulDisconnectionOfServiceReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		System.out.println("inputs are:"+selectionType+"="+selectionName+"="+selectionId
				+"="+peri+"+"+perFinYear);
		
		String view="wrongfulDisconnectionOfServiceReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getWrongfulDisconnectionOfService(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	@RequestMapping(value= {"/scheduledOutagesMaxDuration"})
	public String  scheduledOutagesMaxDurationHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getScheduledOutagesMaxDuration(comvo);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "scheduledOutagesMaxDurationHome";
	}
	
	
	@RequestMapping(value ="/scheduledOutagesMaxDurationReport", method = RequestMethod.POST)
	public String scheduledOutagesMaxDurationReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="scheduledOutagesMaxDurationReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getScheduledOutagesMaxDuration(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	@RequestMapping(value= {"/scheduledOutagesRestorationsupply"})
	public String  scheduledOutagesRestorationsupplyHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getScheduledOutagesRestorationsupply(comvo);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "scheduledOutagesRestorationsupplyHome";
	}
	
	
	@RequestMapping(value ="/scheduledOutagesRestorationsupplyReport", method = RequestMethod.POST)
	public String scheduledOutagesRestorationsupplyReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="scheduledOutagesRestorationsupplyReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getScheduledOutagesRestorationsupply(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	
	
	
	@RequestMapping(value= {"/newConnectionsNetworkExpansion11Kv"})
	public String  newConnectionsNetworkExpansion11Kvhome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.newConnectionsNetworkExpansion11Kv(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "newConnectionsNetworkExpansion11KvHome"; 
	}
	
	
	
	@RequestMapping(value ="/newConnectionsNetworkExpansion11KVReport", method = RequestMethod.POST)
	public String newConnectionsNetworkExpansion11KVReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		String view="newConnectionsNetworkExpansion11KVReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.newConnectionsNetworkExpansion11Kv(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	@RequestMapping(value= {"/newConnectionsNetworkExpansion33Kv"})
	public String  newConnectionsNetworkExpansion33KvHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getnewConnectionsNetworkExpansion33Kv(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "newConnectionsNetworkExpansion33KvHome"; 
	}
	
	@RequestMapping(value ="/newConnectionsNetworkExpansion33KVReport", method = RequestMethod.POST)
	public String newConnectionsNetworkExpansion33KVReport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		String view="newConnectionsNetworkExpansion33Kvreport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getnewConnectionsNetworkExpansion33Kv(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	@RequestMapping(value= {"/newConnectionsNetworkExpansion132Kv"})
	public String  newConnectionsNetworkExpansion132Kvhome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getnewConnectionsNetworkExpansion132Kv(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "newConnectionsNetworkExpansion132KvHome"; 
	}
	
	@RequestMapping(value ="/newConnectionsNetworkExpansion132Kvreport", method = RequestMethod.POST)
	public String newConnectionsNetworkExpansion132Kvreport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		String view="newConnectionsNetworkExpansion132Kvreport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getnewConnectionsNetworkExpansion132Kv(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	@RequestMapping(value= {"/releaseNewConnection11Kv"})
	public String  releaseNewConnection11KvHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getreleaseNewConnection11Kvdata(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "releaseNewConnection11KvHome";
	}
	
	@RequestMapping(value ="/releaseNewConnection11Kvrreport", method = RequestMethod.POST)
	public String releaseNewConnection11Kvreport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="releaseNewConnection11KvReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getreleaseNewConnection11Kvdata(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	@RequestMapping(value= {"/releaseNewConnection33Kv"})
	public String  releaseNewConnection33KvHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getreleaseNewConnection33Kvdata(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "releaseNewConnection33KvHome";
	}
	
	@RequestMapping(value ="/releaseNewConnection33Kvrreport", method = RequestMethod.POST)
	public String releaseNewConnection33Kvreport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="releaseNewConnection33KvReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getreleaseNewConnection33Kvdata(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
	
	
	@RequestMapping(value= {"/releaseNewConnection132Kv"})
	public String  releaseNewConnection132KvHome(ModelMap model) throws Exception {
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType("allcircles");
		comvo.setFld1("1");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>4) {
     	   finYear=c1.get(Calendar.YEAR)+"-"+String.valueOf((c1.get(Calendar.YEAR)+1)).substring(2);
     	  tempFinYear=c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.YEAR)+1);
        }else {
     	   finYear=(c1.get(Calendar.YEAR)-1)+"-"+String.valueOf(c1.get(Calendar.YEAR)).substring(2);
     	  tempFinYear=(c1.get(Calendar.YEAR)-1)+"-"+c1.get(Calendar.YEAR);
        }
        model.put("currMon", currMonth);
        model.put("tempFinYear", tempFinYear);
        
		comvo.setFld2(finYear);
		List<CommonVo> list=epSopAnalysisService.getreleaseNewConnection132Kvdata(comvo);
		System.out.println("data:"+list);
		model.put("thName", "Circle Name");
		model.put("selectionType", "alldivisons");
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return "releaseNewConnection132KvHome";
	}
	
	@RequestMapping(value ="/releaseNewConnection132Kvrreport", method = RequestMethod.POST)
	public String releaseNewConnection132Kvreport(HttpServletRequest req, ModelMap model,
			@RequestParam("selectionType") String selectionType,@RequestParam("selectionName") String selectionName,
			@RequestParam("selectionId") String selectionId,@RequestParam("peri") String peri,@RequestParam("perFinYear") String perFinYear) throws Exception {
		
		
		String view="releaseNewConnection132kvReport";
		CommonVo comvo=new CommonVo();
		comvo.setSelectionType(selectionType);
		comvo.setSelectionId(selectionId);
		comvo.setSelectionName(selectionName);
		comvo.setFld1(peri);
		comvo.setFld2(perFinYear);
		List<CommonVo> list=epSopAnalysisService.getreleaseNewConnection132Kvdata(comvo);
		if(selectionType.equals("allcircles")) {
			model.put("thName", "Circle Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Division Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Division Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Section Name");
			model.put("selectionType", "allDetails");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allsubdivisons");
			model.put("selName", selectionName+" Sub Divison Total");
		}
		
		if(list==null)
		{
			model.put("errCode", "500");
		}else if(list.get(0).getFlag().equals("no"))
		{
			model.put("errCode", "100");
		}else {
			model.put("errCode", "200");
			model.put("list", list);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String responseList = gson.toJson(list);
			model.put("responseList",responseList.replaceAll("[\\t\\n\\r]+",""));
		}
		return view;
	}
}
