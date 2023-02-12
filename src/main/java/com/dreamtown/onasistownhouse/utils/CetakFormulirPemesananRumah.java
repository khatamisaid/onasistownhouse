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

import com.dreamtown.onasistownhouse.viewmodel.ViewModelCetakFormulirPemesananRumah;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class CetakFormulirPemesananRumah {

        @Autowired
        private Environment env;

        public Boolean writePdf(List<ViewModelCetakFormulirPemesananRumah> list, String path)
                        throws FileNotFoundException {
                System.out.println("env reports1: " + env.getProperty("storage.reports"));
                String masterReportFileName = env.getProperty("storage.reports")
                                + "SimulasiSistemPembayaran.jrxml";
                String subReportFileName1 = env.getProperty("storage.reports")
                                + "SimulasiSistemPembayaran1.jasper";
                String subReportFileName2 = env.getProperty("storage.reports")
                                + "SimulasiSistemPembayaran2.jasper";
                JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
                try {
                        JasperReport jasperMasterReport = JasperCompileManager
                                        .compileReport(new FileInputStream(new File(masterReportFileName)));
                        Map<String, Object> parameters = new HashMap<>();
                        parameters.put("SUBREPORT_DIR_1",
                                        subReportFileName1);
                        parameters.put("SUBREPORT_DIR_2",
                                        subReportFileName2);
                        JasperPrint jrPrint = JasperFillManager.fillReport(jasperMasterReport,
                                        parameters, beanColDataSource);
                        JasperExportManager.exportReportToPdfFile(jrPrint, path);
                        return true;
                } catch (JRException ex) {
                }
                return false;
        }
}
