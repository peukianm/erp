package erp.util;

import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import erp.bean.LoggerData;
import erp.dao.AttendanceDAO;
import erp.dao.SchedulerDAO;
import erp.dao.StaffDAO;
import erp.dao.UsrDAO;
import erp.entities.Attendance;
import erp.entities.Companytask;
import erp.entities.Staff;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;

//import okhttp3.*;
import java.sql.*;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import okhttp3.*;

import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * @author Michalis Pefkianakis
 *
 */
public class TestFunct {

    static SchedulerDAO schedulerDAO;
    static AttendanceDAO attendanceDAO;
    static StaffDAO staffDAO;

    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static Response outpResponse;

    private static String IP = "";
    private static String department = "";
    private static Date startDate;
    private static Date endDate;
    private static String startMonth = "";
    private static String endMonth = "";

    public static final String DATEPATTERN = "yyyy-MM-dd";
    public static final String TIMEPATTERN = "HH:mm:ss";
    public static final String FULLDATEPATTERN = "yyyy-MM-dd HH:mm:ss";

    private static List<PrintEntry> entries = new ArrayList<PrintEntry>();
    private static String tempIP = "";
    private static String tempUSER = "";
    private static int IPPagesCounter = 0;
    private static Boolean showAll = false;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        try {

            // run3(); //Check printing activity
            // run4(); // Create random attendance files
            //readOro("C:\\tmp\\random5.xlsx");
            //readOro();
            //System.out.println(LocalDate.now() + " das ");
            //Timestamp ter = new Timestamp(13131323);
            //openPDF();
            String sqlSelect = " SELECT amy, first_name as name, last_name as surname, EM.FATHER_NAME as fathername, rolos_id as rankid, "
                    + " section_id as departmentid,  hoursperday as shiftid, EM.MOBILE,"
                    + " branch_id as branchid, adt,specialty_id as specialityid, afm, street as address,  "
                    + " work_phone as phone1,  home_phone as phone2,  "
                    + " birth_date as birthdate,employee_id as cteamid, active as enable,EM.AMKA as amka,"
                    + " studytype_id as studytypeid,familystatus_id as familystatusid,service_id as sectorid,"
                    + " bigsection_id as companyid "
                    + " from SYSPROS.EMP_EMPLOYEES em "
                    + " where em.active=1 AND TODATE >= current_date AND EM.BIGSECTION_ID=1 "
                    + " order by last_name";
           // DBQueryExample(sqlSelect);
            
            readFile("E:\\temp",1);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void readFile(String file, int n) {
        
        String date = new SimpleDateFormat(FormatUtils.yyyyMMdd).format(new Date());
        System.out.println("Date="+date);
        try (BufferedReader br = new BufferedReader(new FileReader(file+"\\"+date+".txt"))) {
            for(int i = 0; i < n-1; ++i){
                br.readLine();
            }
            
            for (String line; (line = br.readLine()) != null;) {
                System.out.println(line);
                String[] parts = line.split("\\t");
                //System.out.println(parts[0] + parts[1]+ parts[2] + parts[3]);
                Date dt = FormatUtils.getDate(parts[1], FormatUtils.LOGGERFULLDATEPATTERN);
                System.out.println(FormatUtils.formatDateToTimestamp(dt, FULLDATEPATTERN));
                
            }
            // line is not visible here.
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestFunct.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestFunct.class.getName()).log(Level.SEVERE, null, ex);
        }

//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            
//            String line;
//            while ((line = br.readLine()) != null) {
//                // process the line.
//            }
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(TestFunct.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(TestFunct.class.getName()).log(Level.SEVERE, null, ex);
//        } 
    }

    private static void openPDF() {
        Document document = new Document();

        try {

            // step 2: creation of the writer-object
            PdfWriter.getInstance(document, new FileOutputStream("E:\\temp\\unicode.pdf"));

            // step 3: we open the document
            document.open();

            // step 4: we add content to the document
            BaseFont bfComic = BaseFont.createFont("c:\\windows\\fonts\\verdana.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.lowagie.text.Font font1 = new com.lowagie.text.Font(bfComic, 12);

            BaseFont bfComic1 = BaseFont.createFont("c:\\windows\\fonts\\candara.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            com.lowagie.text.Font font2 = new com.lowagie.text.Font(bfComic1, 12);

            FontFactory.registerDirectories();
            com.lowagie.text.Font font3 = FontFactory.getFont("verdana.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, 4);

            String text1 = "This is ΠΕΥΚΙΑΝΑΚΗΣ  \u0393\u0394\u03b6 'Comic'.";
            String text2 = "Some greek characters: \u0393\u0394\u03b6 ΠΕΥΚΙΑΝΑΚΗΣ ssaddfsad121213";
            String text3 = "Some cyrillic characters: \u0418\u044f ΠΕΥΚΙΑΝΑΚΗΣ ασδασδ \u0393\u0394\u03b6 ";
            document.add(new com.lowagie.text.Paragraph(text1, font1));
            document.add(new com.lowagie.text.Paragraph(text2, font2));
            document.add(new com.lowagie.text.Paragraph(text3, font3));
        } catch (Exception de) {
            System.err.println(de.getMessage());
        }

        // step 5: we close the document
        document.close();

    }

    private static void dateManipulation() throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String stringData = "26/10/2010";  //df.format(date);
        date = df.parse("DATA TEXT!!!!!!!");

        Timestamp timestamp = new Timestamp(date.getTime());

        Calendar cal = Calendar.getInstance();
        System.out.println(cal.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        Calendar calendar = Calendar.getInstance();
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("Weekday: " + weekday);

        long start = LocalDateTime.of(2020, Month.FEBRUARY, 14, 7, 0, 0).atZone(ZoneId.systemDefault()).toInstant()
                .toEpochMilli();
        long end = System.currentTimeMillis();

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void JPAConn() {
        EntityManager em = getEntityManager();
        UsrDAO dao = new UsrDAO();
    }

    private static void DBQueryExample(String sql) throws SQLException, Exception {
        //java.sql.Connection conn = getConnectionXE();
        try (java.sql.Connection conn = getConnectionXE()) {
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            Array amys = rset.getArray("amy");

            while (rset.next()) {
                System.out.println(rset.getArray(1));
                String sqlUpdate = "update staff  set "
                        + " rankid = " + rset.getString("rankid")
                        + " , departmentid =" + rset.getString("departmentid")
                        + " , shiftid=" + rset.getString("shiftid")
                        + " , branchid=" + rset.getString("branchid")
                        + " , specialityid=" + rset.getString("specialityid")
                        + " , studytypeid=" + rset.getString("studytypeid")
                        + " , familystatusid=" + rset.getString("familystatusid")
                        + " , sectorid=" + rset.getString("sectorid")
                        + " , mobile='" + rset.getString("mobile") + "' "
                        + " , address='" + rset.getString("address") + "' "
                        + " , phone1='" + rset.getString("phone1") + "' "
                        + " , phone2='" + rset.getString("phone2") + "' "
                        + " , amka='" + rset.getString("amka") + "' "
                        + " , adt='" + rset.getString("adt") + "' "
                        + " , afm='" + rset.getString("afm") + "' "
                        + " , fathername='" + rset.getString("fathername") + "' "
                        + " , birthdate=" + rset.getString("birthdate")
                        + " , name='" + rset.getString("name") + "' "
                        + " , surname='" + rset.getString("surname") + "' "
                        + " where amy='" + rset.getString("amy") + "'";
                int code = stmt.executeUpdate(sqlUpdate);
                if (code == 0) {
                    Staff newStaff = new Staff();
                    newStaff.setName(rset.getString("name"));
                    newStaff.setSurname(rset.getString("surname"));
                    newStaff.setFathername(rset.getString("fathername"));
                    newStaff.setBirthdate(rset.getDate("birthdate"));
                    newStaff.setAmka(rset.getString("amka"));
                    newStaff.setAfm(rset.getString("afm"));
                    newStaff.setAdt(rset.getString("adt"));
                    newStaff.setPhone1(rset.getString("phone1"));
                    newStaff.setPhone2(rset.getString("phone2"));
                    newStaff.setMobile(rset.getString("mobile"));
                    newStaff.setAddress(rset.getString("address"));
                    newStaff.setCteamid(rset.getString("cteamid"));
                    newStaff.setAmy(rset.getString("amy"));
                    newStaff.setActive(BigDecimal.ONE);

                    newStaff.setDepartment(staffDAO.getDepartment(Long.parseLong(rset.getString("departmentid"))));
                    newStaff.setSector(staffDAO.getSector(Long.parseLong(rset.getString("sectorid"))));
                    newStaff.setSpeciality(staffDAO.getSpeciality(Long.parseLong(rset.getString("specialityid"))));
                    newStaff.setStudytype(staffDAO.getStudytype(Long.parseLong(rset.getString("studytypeid"))));
                    newStaff.setFamilystatus(staffDAO.getFamilyStatus(Long.parseLong(rset.getString("familystatusid"))));
                    newStaff.setBranch(staffDAO.getBranch(Long.parseLong(rset.getString("branchid"))));
                    newStaff.setCompany(staffDAO.getCompany(1));
                    newStaff.setEmprank(staffDAO.getRank(Long.parseLong(rset.getString("rankid"))));
                    newStaff.setWorkshift(staffDAO.getShift(Long.parseLong(rset.getString("shiftid"))));
                }

            }

            List<Staff> allStaff = staffDAO.getAllStaff(true);

            String[] amysArray = (String[]) amys.getArray();

            boolean contains = true;
            for (int i = 0; i < allStaff.size(); i++) {
                Staff staff = allStaff.get(i);
                contains = Stream.of(amysArray).anyMatch(x -> x.equals(String.valueOf(staff.getStaffid())));
                if (!contains) {
                    staff.setActive(BigDecimal.ZERO);
                    staffDAO.update(staff);
                }

            }

            String sqlInsert = " INSERT INTO staff (name, surname, fathername, birthdate, amka, afm, phone1, "
                    + " phone2, mobile, address, cteamid, rankid, departmentid, shiftid, branchid, specialityid, "
                    + " studytypeid, familystatusid, sectorid, companyid) "
                    + " VALUES ('" + rset.getString("name") + "', '" + rset.getString("surname") + "', '" + rset.getString("fathername") + "'"
                    + " , '" + rset.getString("fathername") + "'          )";

            //System.out.println(rset.getString(1));
            //sql = "Update lab_exams le set le.value=";
            //stmt.executeUpdate(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static java.sql.Connection getConnectionXE() throws Exception {
        try {
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            //java.sql.Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.75.3.252:1521:hosp", "skeleton", "jijikos");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.75.75.5:1521:karddb", "SHADMIN", "cteam");
            return conn;
        } catch (Exception e) {

            throw e;
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("deprecation")
    private static void readOro() {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            System.out.println(dateFormat.format(calendar.getTime()));

            long companyid = 1;//Long.parseLong(SystemParameters.getInstance().getProperty("Companyid"));            
            long taskid = 1;//Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_READ_LOGGERS"));
            Companytask ctask = null;//schedulerDAO.findCtask(companyid, taskid);
            System.out.println("");

            File file1 = new File("e:\\temp\\comp2.xlsx");//new File(SystemParameters.getInstance().getProperty("LOGGER1_PATH"));

            //File file2 = new File(SystemParameters.getInstance().getProperty("LOGGER2_PATH"));
            //File file3 = new File(SystemParameters.getInstance().getProperty("LOGGER3_PATH"));
            //File file4 = new File(SystemParameters.getInstance().getProperty("LOGGER4_PATH"));
            File file = null;
            List<LoggerData> logerDataList = new ArrayList<LoggerData>();
            for (int i = 1; i <= 1; i++) {
                if (i == 1) {
                    file = file1;
                }
                FileInputStream excelFile = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(excelFile);
                Sheet datatypeSheet = workbook.getSheetAt(0);

                int pointer = 0;
//                switch (i) {
//                    case 1:
//                        pointer = ctask.getTaskdata1() == null ? 0 : Integer.parseInt(ctask.getTaskdata1());
//                        break;
//                    case 2:
//                        pointer = ctask.getTaskdata2() == null ? 0 : Integer.parseInt(ctask.getTaskdata2());
//                        break;
//                    case 3:
//                        pointer = ctask.getTaskdata3() == null ? 0 : Integer.parseInt(ctask.getTaskdata3());
//                        break;
//                    default:
//                        pointer = ctask.getTaskdata4() == null ? 0 : Integer.parseInt(ctask.getTaskdata4());
//                        break;
//                }

                int counter = 0;
                Row currentRow = null;
                Iterator<Row> iterator = datatypeSheet.iterator();
                while (iterator.hasNext()) {
                    if (counter < pointer) {
                        counter++;
                        break;
                    }
                    currentRow = iterator.next();
                    
                }
                workbook.close();
                System.out.println(logerDataList.size());
                Collections.sort(logerDataList);
                updateAttendance(logerDataList);
                calendar = Calendar.getInstance();
                System.out.println(dateFormat.format(calendar.getTime()));

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateAttendance(List<LoggerData> loggerDataList) {
        String currentDate = "";
        String previousDate = "";
        for (LoggerData loggerData : loggerDataList) {
            currentDate = FormatUtils.formatDate(loggerData.getDateTime());
            previousDate = FormatUtils.formatDate(FormatUtils.minusOneDay(loggerData.getDateTime()));
            List<Attendance> temp = null;//schedulerDAO.findAttendance(loggerData.getStaff(), currentDate, previousDate);

            if (temp != null) {
                for (Attendance attendance : temp) {
                    if (attendance.getEnded().intValue() == 0) {
                        if (FormatUtils.getDateDiff(attendance.getEntrance(), loggerData.getDateTime(), TimeUnit.HOURS) > 16) {
                            Attendance newAttendance = new Attendance();
                            newAttendance.setCompany(loggerData.getStaff().getCompany());
                            newAttendance.setEntrance(FormatUtils.formatDateToTimestamp(loggerData.getDateTime(), FULLDATEPATTERN));
                            newAttendance.setDepartment(loggerData.getStaff().getDepartment());
                            newAttendance.setEnded(BigDecimal.ZERO);
                            newAttendance.setStaff(loggerData.getStaff());
                            newAttendance.setSector(loggerData.getStaff().getSector());
                            attendanceDAO.saveAttendance(newAttendance);
                        } else {
                            attendance.setEnded(BigDecimal.ONE);
                            attendance.setExit(FormatUtils.formatDateToTimestamp(loggerData.getDateTime(), FULLDATEPATTERN));
                            attendanceDAO.updateAttendance(attendance);
                        }
                    }
                }
            } else {
                Attendance newAttendance = new Attendance();
                newAttendance.setCompany(loggerData.getStaff().getCompany());
                newAttendance.setEntrance(FormatUtils.formatDateToTimestamp(loggerData.getDateTime(), FULLDATEPATTERN));
                newAttendance.setDepartment(loggerData.getStaff().getDepartment());
                newAttendance.setEnded(BigDecimal.ZERO);
                newAttendance.setStaff(loggerData.getStaff());
                newAttendance.setSector(loggerData.getStaff().getSector());
                attendanceDAO.saveAttendance(newAttendance);
            }
        }

    }

    private static Object getCellValue(Cell cell) {
        Object retValue = null;
        switch (cell.getCellType()) {
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue());
                retValue = cell.getBooleanCellValue();
                break;
            case STRING:
                System.out.print(cell.getRichStringCellValue().getString());
                retValue = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    System.out.print(cell.getDateCellValue());
                    retValue = cell.getDateCellValue();
                } else {
                    System.out.print(cell.getNumericCellValue());
                    retValue = cell.getNumericCellValue();
                }
                break;
            case FORMULA:
                System.out.print(cell.getCellFormula());
                retValue = cell.getCellFormula();
                break;
            case BLANK:
                System.out.print("");
                retValue = "";
                break;
            default:
                retValue = "";
                System.out.print("");
        }

        System.out.print("\t");
        return retValue;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void run4() throws IOException {
        for (int k = 1; k < 5; k++) {

            XSSFWorkbook out = new XSSFWorkbook();
            XSSFSheet spreadSheet = out.createSheet("ResultSheet");

            Row headerRow = spreadSheet.createRow(1);

            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("ID");

            headerCell = headerRow.createCell(1);
            headerCell.setCellValue("DATE");

            headerCell = headerRow.createCell(2);
            headerCell.setCellValue("STATUS");

            long start = LocalDateTime.of(2020, Month.FEBRUARY, 14, 7, 0, 0).atZone(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli();
            long end = System.currentTimeMillis();

            int rowCount = 1;
            int randomNum = 0;
            Date randomDate = null;
            long random = 0;
            for (int i = 0; i < 400000; i++) {
                Row row = spreadSheet.createRow(++rowCount);
                Cell cell = row.createCell(0);
                if (i % 2 == 0) {
                    randomNum = ThreadLocalRandom.current().nextInt(18300, 18980 + 1);
                }
                cell.setCellValue(randomNum);

                cell = row.createCell(1);

                if (i % 2 == 0) {
                    random = between(start, end);
                    cell.setCellValue(new Date(random));
                } else {
                    random = random + 60000000;
                    cell.setCellValue(new Date(random));
                }

                cell = row.createCell(2);
                if (i % 2 == 0) {
                    cell.setCellValue("0");
                } else {
                    cell.setCellValue("1");
                }
            }
            System.out.println("END LOOP!!!!=" + k);

            try (FileOutputStream outputStream = new FileOutputStream("c:\\tmp\\random" + k + ".xlsx")) {
                out.write(outputStream);
            }
            out.close();
            System.out.println("ALL OKK=" + k);
        }
    }

    public static Date between(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);
        System.out.println(randomDay);
        // return LocalDate.ofEpochDay(randomDay);
        return new Date(randomDay);
    }

    public static long between(long start, long end) {
        long randomLong = ThreadLocalRandom.current().nextLong(start, end);
        // return LocalDate.ofEpochDay(randomDay);
        return randomLong;

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void run3() throws IOException {
        IP = null;// "10.75.75.62";
        department = null;
        startDate = null; // getDate(2020-02-12)
        endDate = null;
        startMonth = null;
        endMonth = null;
        showAll = false;

        if (startDate != null && endDate == null) {
            endDate = null;
        }

        File folder = new File("\\\\10.75.3.254\\print-log");
        listFilesForFolder(folder);
        writeExcel(entries, "c:\\tmp\\output.xls");
    }

    private static void listFilesForFolder(final File folder) {
        Date fileDate = null;
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                String directoryName = fileEntry.getName();

                // Checking If is a root dir with IP. Seting IP and USERNAME
                if (directoryName.contains("10.75.")) {
                    tempIP = directoryName.split("-")[0];
                    tempUSER = directoryName.split("-")[1];
                    IPPagesCounter = 0;
                }

                // Checking IP/MONTHLY
                if ((IP != null && !fileEntry.getAbsolutePath().contains(IP))
                        || (directoryName.contains("monthly") && startMonth == null))
					; else {
                    listFilesForFolder(fileEntry);
                }
            } else {
                String fileName = fileEntry.getName();

                // NOT USING ALL TIME LOG
                if (fileEntry.getName().equals("papercut-print-log-all-time.csv")
                        || !fileEntry.getName().endsWith("csv")) {
                    break;
                }

                // IF THERE IS DATE ARGUMENTS
                if (startDate != null) {
                    if (fileEntry.getAbsolutePath().contains("daily")) {
                        fileDate = getDate(fileName.substring(19, fileName.length() - 4));
                    }
                    if (fileDate.compareTo(startDate) >= 0 && fileDate.compareTo(endDate) <= 0) {
                        addPrints(fileEntry);
                    }
                } else {
                    addPrints(fileEntry);
                    // if (startDate == null && startMonth == null &&
                    // fileEntry.getName().equals("papercut-print-log-all-time.csv") )
                }

            }
        }
    }

    private static void writeExcel(List<PrintEntry> entries, String excelFilePath) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        Row headerRow = sheet.createRow(1);

        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("IP");

        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("USER");

        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("DATE");

        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("TIME");

        headerCell = headerRow.createCell(4);
        headerCell.setCellValue("PC-USER");

        headerCell = headerRow.createCell(5);
        headerCell.setCellValue("PAGES");

        headerCell = headerRow.createCell(6);
        headerCell.setCellValue("PRINTER");

        headerCell = headerRow.createCell(7);
        headerCell.setCellValue("DOCUMENT NAME");

        headerCell = headerRow.createCell(8);
        headerCell.setCellValue("DUPLEX");

        headerCell = headerRow.createCell(9);
        headerCell.setCellValue("SIZE");

        headerCell = headerRow.createCell(10);
        headerCell.setCellValue("TOTAL PAGES");

        int rowCount = 1;
        String tempIP = "";

        if (!showAll) {
            tempIP = entries.get(0).getIP();
            for (int i = 0; i < entries.size(); i++) {
                PrintEntry entry = entries.get(i);
                if (!tempIP.equals(entry.getIP())) {
                    sheet.createRow(++rowCount);
                    Row row = sheet.createRow(++rowCount);
                    writeEntry(entries.get(i - 1), row);
                    tempIP = entry.getIP();
                }
            }
        } else {
            for (PrintEntry entry : entries) {
                if (!tempIP.equals(entry.getIP())) {
                    sheet.createRow(++rowCount);
                    tempIP = entry.getIP();
                }
                Row row = sheet.createRow(++rowCount);
                writeEntry(entry, row);
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
        }
        workbook.close();
        System.out.println("ALL OK!!!!!!!!!!!11111");
    }

    private static void writeEntry(PrintEntry entry, Row row) {

        Cell cell = row.createCell(0);
        cell.setCellValue(entry.getIP());

        cell = row.createCell(1);
        cell.setCellValue(entry.getName());

        cell = row.createCell(2);
        cell.setCellValue(entry.getDate());

        cell = row.createCell(3);
        cell.setCellValue(entry.getTime());

        cell = row.createCell(4);
        cell.setCellValue(entry.getMsUser());

        cell = row.createCell(5);
        cell.setCellValue(entry.getPages());

        cell = row.createCell(6);
        cell.setCellValue(entry.getPrinter());

        cell = row.createCell(7);
        cell.setCellValue(entry.getDocName());

        cell = row.createCell(8);
        cell.setCellValue(entry.getDuplex());

        cell = row.createCell(9);
        // cell.setCellValue(entry.getSize().substring(0, (entry.getSize().length() -
        // 2)));

        cell = row.createCell(10);
        cell.setCellValue(entry.getTotalPages());
    }

    private static void addPrints(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line = "";
            System.out.println(line);
            br.readLine();
            br.readLine();
            while ((line = br.readLine()) != null) {
                PrintEntry entry = new PrintEntry();

                String[] entris = line.split(",");

                entry.setDate(entris[0].split(" ")[0]);
                entry.setTime(entris[0].split(" ")[1]);
                entry.setMsUser(entris[1]);
                entry.setPages(Integer.parseInt(entris[2]));
                entry.setCopies(Integer.parseInt(entris[3]));
                entry.setPrinter(entris[4]);
                entry.setDocName(entris[5]);
                entry.setDuplex(entris[11]);
                entry.setSize(entris[13]);

                entry.setIP(tempIP);
                entry.setName(tempUSER);

                IPPagesCounter = IPPagesCounter + Integer.parseInt(entris[2]);
                entry.setTotalPages(IPPagesCounter);

                entries.add(entry);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Date getDate(String date) {
        DateFormat df = new SimpleDateFormat(DATEPATTERN);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();

        }
    }

    private static void run2() {
        byte[] encodedBytes = Base64.getEncoder().encode("Test".getBytes());
        System.out.println("encodedBytes " + new String(encodedBytes));
        byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
        System.out.println("decodedBytes " + new String(decodedBytes));

        String encodeBytes = Base64.getEncoder().encodeToString(("99221913_17:@p1User").getBytes());
        System.out.println(encodeBytes);

    }

    private static void run1() {
        String credentials = "{\r\n" + "	\"username\": \"admin\",\r\n" + "	\"password\": \"123456\"\r\n" + "}";
        String optrouteparams = "{\r\n" + "  \"vehicles\" : [\r\n" + "   {\r\n"
                + "     \"vehicle_id\": \"my_vehicle\",\r\n" + "     \"start_address\": {\r\n"
                + "         \"location_id\": \"berlin\",\r\n" + "         \"lon\": 13.406,\r\n"
                + "         \"lat\": 52.537\r\n" + "     },\r\n" + "     \"type_id\": \"my_bike\"\r\n" + "   }\r\n"
                + "],\r\n" + "  \"vehicle_types\" : [\r\n" + "    {\r\n" + "      \"type_id\" : \"my_bike\",\r\n"
                + "      \"profile\" : \"bike\"\r\n" + "    }\r\n" + "  ],\r\n" + "  \"services\" : [\r\n" + "   {\r\n"
                + "     \"id\": \"hamburg\",\r\n" + "     \"name\": \"visit_hamburg\",\r\n" + "     \"address\": {\r\n"
                + "       \"location_id\": \"hamburg\",\r\n" + "       \"lon\": 9.999,\r\n"
                + "       \"lat\": 53.552\r\n" + "     }\r\n" + "   },\r\n" + "   {\r\n"
                + "     \"id\": \"munich\",\r\n" + "     \"name\": \"visit_munich\",\r\n" + "     \"address\": {\r\n"
                + "       \"location_id\": \"munich\",\r\n" + "       \"lon\": 11.570,\r\n"
                + "       \"lat\": 48.145\r\n" + "     }\r\n" + "   },\r\n" + "   {\r\n"
                + "     \"id\": \"cologne\",\r\n" + "     \"name\": \"visit_cologne\",\r\n" + "     \"address\": {\r\n"
                + "       \"location_id\": \"cologne\",\r\n" + "       \"lon\": 6.957,\r\n"
                + "       \"lat\": 50.936\r\n" + "     }\r\n" + "   },\r\n" + "   {\r\n"
                + "     \"id\": \"frankfurt\",\r\n" + "     \"name\": \"visit_frankfurt\",\r\n"
                + "     \"address\": {\r\n" + "       \"location_id\": \"frankfurt\",\r\n"
                + "       \"lon\": 8.670,\r\n" + "       \"lat\": 50.109\r\n" + "     }\r\n" + "   }\r\n" + "]\r\n"
                + "}";

        // RequestBody body = RequestBody.create(JSON, optrouteparams);
        RequestBody body = RequestBody.create(optrouteparams, MediaType.parse("application/json; charset=utf-8"));

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();

        final Request request = new Request.Builder()
                // .url("http://localhost:8080/APIServiceInnov/GH/routing")
                // .url("http://localhost:8080/APIServiceInnov/authenticate")
                .url("http://localhost:8080/APIServiceInnov/GH/optroute")
                // .url("https://graphhopper.com/api/1/vrp/optimize?key=403448b0-df50-4d00-873c-b63a4479c313")
                // .header("Accept", "application/json")
                // .header("Content-Type", "application/json")
                .addHeader("Authorization",
                        "eyJJUCI6IjA6MDowOjA6MDowOjA6MSIsImFsZyI6IkhTMjU2In0.eyJqdGkiOiIxIiwiaWF0IjoxNTc5Njc2MjQzLCJzdWIiOiJBUElJTk5PViIsImlzcyI6ImFkbWluIiwiSVAiOiIwOjA6MDowOjA6MDowOjEiLCJleHAiOjE1ODAwMzYyNDN9.iK8fC9acdcCSGKFhpCxFbOmHi9-isCx65reTpe4LZjY")
                // .addHeader("message","point=51.131,12.414&point=48.224,3.867&vehicle=car&locale=de&calc_points=false")
                .post(body)
                // .get()
                .build();

        try {
            System.out.println("Before Client CalL");

            // Future
            CallbackFuture future = new CallbackFuture();
            client.newCall(request).enqueue(future);
            outpResponse = future.get();
            // System.out.println("!!!!!!!!! response=" + outpResponse.body().string() +
            // outpResponse.code());
            String jsonData = outpResponse.body().string();
            JSONObject Jobject = new JSONObject(jsonData);
            String job_id = (String) Jobject.get("job_id");
            System.out.println("job_id=" + job_id);
//			String URL1 = "https://graphhopper.com/api/1/vrp/solution/"+job_id+"?key=403448b0-df50-4d00-873c-b63a4479c313";
//			System.out.println(URL1);
//			
//			api.innov.client.graphhopper.model.Response rsp;
//			RouteOptimizationApi vrpApi = new RouteOptimizationApi();
//			vrpApi.setApiClient(GHApiUtil.createClient());
//			while (true) {
//	            rsp = vrpApi.getSolution(job_id);
//	            if (rsp.getStatus().equals(api.innov.client.graphhopper.model.Response.StatusEnum.FINISHED)) {
//	                break;
//	            }
//	            Thread.sleep(200);
//	        }
//	        System.out.println(rsp);

            // Asynchronous call
//			client.newCall(request).enqueue(new Callback() {
//				
//				@Override
//				public void onFailure(Request call, IOException e) {
//					e.printStackTrace();
//				}
//
//				@Override
//				public void onResponse(Response response) throws IOException {
//					try (ResponseBody responseBody = response.body()) {
//						if (!response.isSuccessful())
//							throw new IOException("Unexpected code " + response);
//
//						outpResponse = response;
//						System.out.println("inside onResponse="+outpResponse.body().string() );
//						Headers responseHeaders = response.headers();
//						for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//							System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//						}
//
//						System.out.println(responseBody.string());
//					}
//				}
//			});
//			
//			System.out.println("end="+outpResponse.body().string() );
        } // catch (InterruptedException | ExecutionException | IOException e) {
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static EntityManager getEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("erp");
        EntityManager manager = emf.createEntityManager();
        return manager;
    }

}

class CallbackFuture extends CompletableFuture<Response> implements Callback {

    @Override
    public void onResponse(Call call, Response response) {
        System.out.println("ON RESPONSE");
        super.complete(response);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        System.out.println("ON FAILURE");
        super.completeExceptionally(e);
    }

    public void onFailure(Request arg0, IOException arg1) {
        System.out.println("ON FAILURE OVERIDE");
        arg1.printStackTrace();
    }

    public void onResponse(Response response) throws IOException {
        System.out.println("ON RESPONSE OVERRIDE");
        super.complete(response);
    }

}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class PrintEntry {

    String IP;
    String name;
    String Date;
    String Time;
    String msUser;
    int pages;
    int copies;
    String printer;
    String docName;
    String duplex;
    String size;
    int totalPages = 0;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String iP) {
        IP = iP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMsUser() {
        return msUser;
    }

    public void setMsUser(String msUser) {
        this.msUser = msUser;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDuplex() {
        return duplex;
    }

    public void setDuplex(String duplex) {
        this.duplex = duplex;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

class print {

    String IP;
    String department;
    String period;
    List<PrintEntry> printEntries = new ArrayList<PrintEntry>();

    public String getIP() {
        return IP;
    }

    public void setIP(String iP) {
        IP = iP;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<PrintEntry> getPrintEntries() {
        return printEntries;
    }

    public void setPrintEntries(List<PrintEntry> printEntries) {
        this.printEntries = printEntries;
    }

}
