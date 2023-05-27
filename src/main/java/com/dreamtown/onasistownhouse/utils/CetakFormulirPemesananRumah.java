package com.dreamtown.onasistownhouse.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.dreamtown.onasistownhouse.viewmodel.ViewModelCetakFormulirPemesananRumah;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class CetakFormulirPemesananRumah {

        public Boolean writePdf(List<ViewModelCetakFormulirPemesananRumah> list, String path,
                        Map<String, Object> paramater)
                        throws FileNotFoundException {
                // String masterReportFileName = env.getProperty("storage.reports")
                // + "SimulasiFormulirPemesanan.jrxml";
                JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
                try {
                        // JasperReport jasperMasterReport = JasperCompileManager
                        // .compileReport(new FileInputStream(new File(masterReportFileName)));
                        InputStream employeeReportStream = getClass()
                                        .getResourceAsStream("/reports/SimulasiFormulirPemesanan.jrxml");
                        JasperReport jasperMasterReport = JasperCompileManager
                                        .compileReport(employeeReportStream);
                        JasperPrint jrPrint = JasperFillManager.fillReport(jasperMasterReport,
                                        paramater, beanColDataSource);
                        JasperExportManager.exportReportToPdfFile(jrPrint, path);
                        return true;
                } catch (JRException ex) {
                        System.out.println(ex.getMessage());
                }
                return false;
        }

        public Boolean writePdfManual(List<ViewModelCetakFormulirPemesananRumah> list, String path,
                        Map<String, Object> paramater)
                        throws FileNotFoundException {
                // String masterReportFileName = env.getProperty("storage.reports")
                // + "SimulasiFormulirPemesanan.jrxml";
                JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
                try {
                        // JasperReport jasperMasterReport = JasperCompileManager
                        // .compileReport(new FileInputStream(new File(masterReportFileName)));
                        InputStream employeeReportStream = getClass()
                                        .getResourceAsStream("/reports/SimulasiFormulirPemesananManual.jrxml");
                        JasperReport jasperMasterReport = JasperCompileManager
                                        .compileReport(employeeReportStream);
                        JasperPrint jrPrint = JasperFillManager.fillReport(jasperMasterReport,
                                        paramater, beanColDataSource);
                        JasperExportManager.exportReportToPdfFile(jrPrint, path);
                        return true;
                } catch (JRException ex) {
                        System.out.println(ex.getMessage());
                }
                return false;
        }
}
