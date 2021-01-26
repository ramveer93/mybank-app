package com.mybank.app.reporting;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.mybank.app.entity.Transaction;
import com.mybank.app.util.BankException;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportingUtil {
	
	/**
	 * This method will generate pdf report using jasper report library
	 * The method will also update the response to include generated report so the caller will
	 * be forced to download the report
	 * @param transactions is the data to be filled in report
	 * @param reportName will be the 
	 * @param response
	 * @return HttpServletResponse
	 */
	public HttpServletResponse generateReport(List<Transaction> transactions,
			HttpServletResponse response) {
		try {
			File file = ResourceUtils.getFile("classpath:acc_stattement.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(transactions);
			Map<String, Object> params = new HashMap<>();
			params.put("createdBy", "MYBank");
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "inline; filename=jasper.pdf;");
			return response;
		} catch (Exception e) {
			throw new BankException(e.getMessage());
		}
	}
}
