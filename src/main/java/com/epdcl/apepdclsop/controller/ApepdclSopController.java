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
		comvo.setFld1("ALL");
		Calendar c1 = Calendar.getInstance();
        String finYear="";
        int currMonth=c1.get(Calendar.MONTH)+1;
        String tempFinYear="";
        if(currMonth>=4) {
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
			model.put("thName", "Circles Name");
			model.put("selectionType", "alldivisons");
			model.put("backBtn", "no");
			model.put("back_selectionType", "");
			model.put("selName", "APEPDCL Total");
		}else if(selectionType.equals("alldivisons")) {
			model.put("thName", "Divisions Name");
			model.put("selectionType", "allsubdivisons");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "allcircles");
			model.put("selName", selectionName+" Circle Total");
		}else if(selectionType.equals("allsubdivisons")) {
			model.put("thName", "Sub Divisions Name");
			model.put("selectionType", "allsections");
			model.put("backBtn", "yes");
			model.put("back_selectionType", "alldivisons");
			model.put("selName", selectionName+" Divison Total");
		}else if(selectionType.equals("allsections")) {
			model.put("thName", "Sections Name");
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
