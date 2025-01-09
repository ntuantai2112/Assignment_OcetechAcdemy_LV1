package com.globits.da.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.globits.da.domain.Commune;
import com.globits.da.domain.Employee;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.globits.core.dto.DepartmentDto;

public class ImportExportExcelUtil {
    private static Hashtable<String, Integer> hashStaffColumnConfig = new Hashtable<String, Integer>();
    private static Hashtable<String, Integer> hashDepartmentColumnConfig = new Hashtable<String, Integer>();
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static DecimalFormat numberFormatter = new DecimalFormat("######################");
    private static Hashtable<String, String> hashColumnPropertyConfig = new Hashtable<String, String>();

    private static void scanStaffColumnExcelIndex(Sheet datatypeSheet, int scanRowIndex) {
        Row row = datatypeSheet.getRow(scanRowIndex);
        int numberCell = row.getPhysicalNumberOfCells();

        hashColumnPropertyConfig.put("staffCode".toLowerCase(), "staffCode");
        hashColumnPropertyConfig.put("firstName".toLowerCase(), "firstName");
        hashColumnPropertyConfig.put("lastName".toLowerCase(), "lastName");
        hashColumnPropertyConfig.put("displayName".toLowerCase(), "displayName");
        hashColumnPropertyConfig.put("birthdate".toLowerCase(), "birthdate");
        hashColumnPropertyConfig.put("birthdateMale".toLowerCase(), "birthdateMale");
        hashColumnPropertyConfig.put("birthdateFeMale".toLowerCase(), "birthdateFeMale");
        hashColumnPropertyConfig.put("gender".toLowerCase(), "gender");
        hashColumnPropertyConfig.put("address".toLowerCase(), "address");// Cái này cần xem lại
        hashColumnPropertyConfig.put("userName".toLowerCase(), "userName");
        hashColumnPropertyConfig.put("password".toLowerCase(), "password");
        hashColumnPropertyConfig.put("email".toLowerCase(), "email");
        hashColumnPropertyConfig.put("BirthPlace".toLowerCase(), "BirthPlace");

        hashColumnPropertyConfig.put("departmentCode".toLowerCase(), "departmentCode");
        hashColumnPropertyConfig.put("MaNgach".toLowerCase(), "MaNgach");
        hashColumnPropertyConfig.put("IDCard".toLowerCase(), "IDCard");

        for (int i = 0; i < numberCell; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
                String cellValue = cell.getStringCellValue();
                if (cellValue != null && cellValue.length() > 0) {
                    cellValue = cellValue.toLowerCase().trim();
                    String propertyName = hashColumnPropertyConfig.get(cellValue);
                    if (propertyName != null) {
                        hashStaffColumnConfig.put(propertyName, i);
                    }
                }
            }
        }
    }

    public static List<DepartmentDto> getListDepartmentFromInputStream(InputStream is) {
        try {

            List<DepartmentDto> ret = new ArrayList<DepartmentDto>();
            // FileInputStream excelFile = new FileInputStream(new File(filePath));
            // Workbook workbook = new XSSFWorkbook(excelFile);
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            // Iterator<Row> iterator = datatypeSheet.iterator();
            int rowIndex = 4;

            hashDepartmentColumnConfig.put("code", 0);

            hashDepartmentColumnConfig.put("name", 1);

            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell = null;
                if (currentRow != null) {
                    DepartmentDto department = new DepartmentDto();
                    Integer index = hashDepartmentColumnConfig.get("code");
                    if (index != null) {
                        currentCell = currentRow.getCell(index);// code
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            double value = currentCell.getNumericCellValue();
                            String code = numberFormatter.format(value);
                            department.setCode(code);
                        } else if (currentCell != null && currentCell.getStringCellValue() != null) {
                            String code = currentCell.getStringCellValue();
                            department.setCode(code);
                        }
                    }
                    index = hashDepartmentColumnConfig.get("name");
                    if (index != null) {
                        currentCell = currentRow.getCell(index);// name
                        if (currentCell != null && currentCell.getStringCellValue() != null) {
                            String name = currentCell.getStringCellValue();
                            department.setName(name);
                        }
                    }
                    ret.add(department);
                }
                rowIndex++;
            }
            return ret;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<Employee> getListEmployeeFromInputStream(InputStream is) {
        try {
            List<Employee> ret = new ArrayList<>();
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);

            // Khởi tạo ánh xạ cột trong file Excel với các thuộc tính của Employee
            Hashtable<String, Integer> hashEmployeeColumnConfig = new Hashtable<>();
            hashEmployeeColumnConfig.put("code", 0);
            hashEmployeeColumnConfig.put("name", 1);
            hashEmployeeColumnConfig.put("email", 2);
            hashEmployeeColumnConfig.put("phone", 3);
            hashEmployeeColumnConfig.put("age", 4);
            hashEmployeeColumnConfig.put("communeCode", 5);

            // Đọc dữ liệu từ dòng thứ 1 (giả định dòng 0 là header)
            int rowIndex = 1;
            int num = datatypeSheet.getLastRowNum();

            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                if (currentRow != null) {
                    Employee employee = new Employee();

                    // Lấy giá trị từ cột "code"
                    Integer index = hashEmployeeColumnConfig.get("code");
                    if (index != null) {
                        Cell currentCell = currentRow.getCell(index);
                        if (currentCell != null) {
                            employee.setCode(currentCell.getStringCellValue());
                        }
                    }

                    // Lấy giá trị từ cột "name"
                    index = hashEmployeeColumnConfig.get("name");
                    if (index != null) {
                        Cell currentCell = currentRow.getCell(index);
                        if (currentCell != null) {
                            employee.setName(currentCell.getStringCellValue());
                        }
                    }

                    // Lấy giá trị từ cột "email"
                    index = hashEmployeeColumnConfig.get("email");
                    if (index != null) {
                        Cell currentCell = currentRow.getCell(index);
                        if (currentCell != null) {
                            employee.setEmail(currentCell.getStringCellValue());
                        }
                    }

                    // Lấy giá trị từ cột "phone"
                    index = hashEmployeeColumnConfig.get("phone");
                    if (index != null) {
                        Cell currentCell = currentRow.getCell(index);
                        if (currentCell != null) {
                            employee.setPhone(currentCell.getStringCellValue());
                        }
                    }

                    // Lấy giá trị từ cột "age"
                    index = hashEmployeeColumnConfig.get("age");
                    if (index != null) {
                        Cell currentCell = currentRow.getCell(index);
                        if (currentCell != null) {
                            employee.setAge((int) currentCell.getNumericCellValue());
                        }
                    }

                    // Lấy giá trị từ cột "communeCode"
                    index = hashEmployeeColumnConfig.get("communeCode");
                    if (index != null) {
                        Cell currentCell = currentRow.getCell(index);
                        if (currentCell != null) {
                            Integer communeCode = currentCell.getColumnIndex();
                            Commune commune = findCommuneByCode(communeCode); // Ánh xạ với database
                            if (commune != null) {
                                employee.setCommune(commune);
                            }
                        }
                    }

                    ret.add(employee);
                }
                rowIndex++;
            }
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức ánh xạ "communeCode" với database (giả định bạn có Repository)
    private static Commune findCommuneByCode(Integer communeCode) {
        // Ví dụ ánh xạ với cơ sở dữ liệu (Repository hoặc Service)
        // CommuneRepository communeRepository = ...;
        // return communeRepository.findByCode(communeCode);

        // Nếu không có cơ sở dữ liệu, bạn có thể trả về giá trị giả lập
        Commune commune = new Commune();
        commune.setId(communeCode); // Giả lập ID
        return commune;
    }

}
