package com.dreamtown.onasistownhouse.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dreamtown.onasistownhouse.viewmodel.ViewModelPemesananCicilan;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class CetakFormulirPemesananCicilan {
    private List<ViewModelPemesananCicilan> data;
    private String path;    
    private String namaProperty;  

    public CetakFormulirPemesananCicilan(List<ViewModelPemesananCicilan> vmCetakPemesanan, String path, String namaProperty){
        this.data = vmCetakPemesanan;
        this.path = path;
        this.namaProperty = namaProperty;
    }

    public Boolean writePdf(){
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            InputStream employeeReportStream = getClass().getResourceAsStream("/reports/FormulirPemesananCicilan.jrxml");
            JasperReport jr = JasperCompileManager.compileReport(employeeReportStream);
            JasperPrint jrPrint = JasperFillManager.fillReport(jr, setParameterReport(), dataSource);
            JasperExportManager.exportReportToPdfFile(jrPrint, path);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Map<String, Object> setParameterReport() {
        Map data = new HashMap<>();
        InputStream logo = getClass().getResourceAsStream("/static/img/favicon.png");
        InputStream watermark = getClass().getResourceAsStream("/static/img/watermark.png");
        data.put("logo", logo);
        data.put("watermark", watermark);
        data.put("propertyName", this.namaProperty);
        return data;
    }
}
