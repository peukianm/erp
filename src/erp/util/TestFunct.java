package erp.util;

import erp.dao.UsrDAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
import okhttp3.*;

import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * @author Michalis Pefkianakis
 *
 */
public class TestFunct {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
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
            readOro("C:\\tmp\\random5.xlsx");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
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
//    private static void JPAConn() {
//        EntityManager em = getEntityManager();
//        UsrDAO dao = new UsrDAO();
//    }

    private static void DBQueryUpadate(String sql) throws SQLException, Exception {
        java.sql.Connection conn = getConnectionXE();
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery(sql);
        while (rset.next()) {
            System.out.println(rset.getString(1));
        }

         sql = "Update lab_exams le set le.value=";
        stmt.executeUpdate(sql);
    }

    public static java.sql.Connection getConnectionXE() throws Exception {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "skeleton", "jijikos");
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
    private static void readOro(String file) {
        try {

            FileInputStream excelFile = new FileInputStream(new File(file));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);

            datatypeSheet.forEach(row -> {
                row.forEach(cell -> {
                    getCellValue(cell);
                });
                System.out.println();
            });
            workbook.close();

//            Iterator<Row> iterator = datatypeSheet.iterator();
//            DataFormatter dataFormatter = new DataFormatter();
//            while (iterator.hasNext()) {
//                Row currentRow = iterator.next();
//                Iterator<Cell> cellIterator = currentRow.iterator();
//                while (cellIterator.hasNext()) {
//                    Cell currentCell = cellIterator.next();
//                    // getCellTypeEnum shown as deprecated for version 3.15
//                    // getCellTypeEnum ill be renamed to getCellType starting from version 4.0
//                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
//                        System.out.print(currentCell.getStringCellValue() + "ST");
//                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
//                        System.out.print(currentCell.getNumericCellValue() + "NU");
//                    }
//                }
//
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

//    public static EntityManager getEntityManager() {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("erp");
//        EntityManager manager = emf.createEntityManager();
//        return manager;
//    }

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
