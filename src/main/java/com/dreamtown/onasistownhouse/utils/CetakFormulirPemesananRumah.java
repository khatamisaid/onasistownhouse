package com.dreamtown.onasistownhouse.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dreamtown.onasistownhouse.viewmodel.ViewModelCetakFormulirPemesananRumah;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class CetakFormulirPemesananRumah {
    
    private List<ViewModelCetakFormulirPemesananRumah> data;
    private String path;

    public CetakFormulirPemesananRumah(ViewModelCetakFormulirPemesananRumah vmCetakPemesanan, String path){
        List<ViewModelCetakFormulirPemesananRumah> tempList = new ArrayList<>();
        tempList.add(vmCetakPemesanan);
        this.data = tempList;
        this.path = path;
    }

    public Boolean writePdf(){
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            InputStream employeeReportStream = getClass().getResourceAsStream("/reports/formulirPemesananRumah.jrxml");
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
        Map data = new HashMap();
        InputStream logo = getClass().getResourceAsStream("/static/img/favicon.png");
        InputStream watermark = getClass().getResourceAsStream("/static/img/watermark.png");
        data.put("logo", logo);
        data.put("watermark", watermark);
        return data;
    }

}
