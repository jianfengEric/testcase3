package com.tng.portal.email.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dell on 2016/12/29.
 */
public class ReportUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReportUtils.class);

    /**
     *  Incoming type, get output. 
     *
     * @param docType
     * @param jasperPrint
     *@param out @return
     */
    private static JRAbstractExporter getJRExporter(DocType docType, JasperPrint jasperPrint, ByteArrayOutputStream out) {
        JRAbstractExporter exporter = null;
        switch (docType) {
            case PDF:
                exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
                break;
            case HTML:
                exporter = new JRHtmlExporter();
                break;
            case XLS:
                exporter = new JRXlsExporter();
                break;
            case XML:
                exporter = new JRXmlExporter();
                break;
            case RTF:
                exporter = new JRRtfExporter();
                break;
            case CSV:
                exporter = new JRCsvExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleWriterExporterOutput(out));
                break;
            case TXT:
                exporter = new JRTextExporter();
                break;
        }
        return exporter;
    }

    /**
     *  The report output type is defined, and the output type is fixed. 
     */
    public static enum DocType {
        PDF, HTML, XLS, XML, RTF, CSV, TXT
    }



    public static DocType getEnumDocType(String docType){
        DocType type = DocType.HTML;
        docType = docType.toUpperCase();
        if(docType.equals("PDF")){
            type = DocType.PDF;
        }else if(docType.equals("XLS")){
            type = DocType.XLS;
        }else if(docType.equals("XML")){
            type = DocType.XML;
        }else if(docType.equals("RTF")){
            type = DocType.RTF;
        }else if(docType.equals("CSV")){
            type = DocType.CSV;
        }else if(docType.equals("TXT")){
            type = DocType.TXT;
        }
        return type;
    }

    /**
     *  Getting the corresponding type. Content type
     * @param docType
     * @return
     */
    public static String getContentType(DocType docType){
        String contentType="text/html";
        switch(docType){
            case PDF:
                contentType = "application/pdf";
                break;
            case XLS:
                contentType = "application/vnd.ms-excel";
                break;
            case XML:
                contentType = "text/xml";
                break;
            case RTF:
                contentType = "application/rtf";
                break;
            case CSV:
                contentType = "text/plain";
                break;
        }
        return contentType;
    }

    public static byte[] report(DocType docType, InputStream sourceFile, List data ,Map<String, Object> parameters){
        try {
            //  Compile report template file jrxml Generation jasper Binary file 
            JasperReport jasperReport = JasperCompileManager.compileReport(sourceFile);
            if(null == parameters){
                parameters = new HashMap();
            }
            JRDataSource dataSource = new JRBeanCollectionDataSource(data, true);
            if(docType.name().equals(DocType.CSV.name())){
            	parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            JRAbstractExporter exporter = getJRExporter(docType, jasperPrint, out);
            if (exporter == null) {
                return new byte[]{};
            } else {
                exporter.exportReport();
                return out.toByteArray();
            }
        } catch (JRException e) {
            logger.error("parameters:{}",parameters,e);
        }
        return new byte[]{};
    }

    public static String delHTMLTag(String htmlStr){ 
        String regExScript="<script[^>]*?>[\\s\\S]*?<\\/script>"; // Definition script Regular expression
        String regExStyle="<style[^>]*?>[\\s\\S]*?<\\/style>"; // Definiti style Regular expression
        String regExHtml="<[^>]+>"; // Definition HTML Regular expression of labels
         
        Pattern pScript=Pattern.compile(regExScript,Pattern.CASE_INSENSITIVE);
        Matcher mScript=pScript.matcher(htmlStr);
        htmlStr=mScript.replaceAll(""); // filter script Label
         
        Pattern pStyle=Pattern.compile(regExStyle,Pattern.CASE_INSENSITIVE);
        Matcher mStyle=pStyle.matcher(htmlStr);
        htmlStr=mStyle.replaceAll(""); // filter style Label
         
        Pattern pHtml=Pattern.compile(regExHtml,Pattern.CASE_INSENSITIVE);
        Matcher mHtml=pHtml.matcher(htmlStr);
        htmlStr=mHtml.replaceAll(""); // filter html Label

        return htmlStr.trim(); // Return text string  
    } 


}
