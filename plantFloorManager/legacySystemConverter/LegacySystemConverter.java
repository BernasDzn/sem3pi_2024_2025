import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.g102.domain.Operation;
import org.g102.domain.Workstation;
import org.g102.domain.*;
import org.g102.tools.ConsoleWriter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class LegacySystemConverter {

    public static File sqlInsertFile = new File("plantFloorManager/sql/insert_file.sql");
    public static FileWriter sqlInsertFileWriter;
    public static List<Customer> customers = new ArrayList<>();
    public static List<Address> addresses = new ArrayList<>();
    public static List<Product> products = new ArrayList<>();
    public static List<CustomerOrder> orders = new ArrayList<>();
    public static List<OrderProductList> orderProductLists = new ArrayList<>();
    public static List<WorkstationType> workstationTypes = new ArrayList<>();
    public static HashMap<Integer, String> families = new HashMap<>();
    public static List<Workstation> workstations = new ArrayList<>();
    public static List<Operation> operations = new ArrayList<>();
    public static List<WorkstationTypeOperation> workstationTypeOperations = new ArrayList<>();

    public static void main(String[] args) {
        File excelFile = new File("resources/Dataset02_v2.xlsx");
        if (!new File("plantFloorManager/sql/").exists()) {
            new File("plantFloorManager/sql/").mkdirs();
        }

        FileInputStream fis = null;
        Workbook workbook = null;
        try {
            sqlInsertFileWriter = new FileWriter(sqlInsertFile);
            fis = new FileInputStream(excelFile);
            workbook = new XSSFWorkbook(fis);
            sqlInsertFileWriter.write("-- CustomerType\n");
            writeCustomerTypeInserts();
            sqlInsertFileWriter.write("-- Address and Customers\n");
            writeClientInserts(workbook.getSheet("Clients"));
            sqlInsertFileWriter.write("-- Products\n");
            writeProductFamilyInserts(workbook.getSheet("ProductFamily"));
            writeProductInserts(workbook.getSheet("Products"));
            sqlInsertFileWriter.write("-- Orders\n");
            writeOrderInserts(workbook.getSheet("Orders"));
            sqlInsertFileWriter.write("-- WorkstationTypes\n");
            writeWorkstationTypeInserts(workbook.getSheet("WorkstationTypes"));
            sqlInsertFileWriter.write("-- Workstations\n");
            writeWorkstationInserts(workbook.getSheet("Workstations"));
            sqlInsertFileWriter.write("-- Operations and WorkstationTypeOperations\n");
            writeOperationInserts(workbook.getSheet("Operations"));
            sqlInsertFileWriter.write("-- Bill of Operations\n");
            writeBillOfOperationsInserts(workbook.getSheet("BOO"));
            sqlInsertFileWriter.write("-- Bill of Materials\n");
            writeBillOfMaterialsInserts(workbook.getSheet("BOM"));
            sqlInsertFileWriter.flush();
            sqlInsertFileWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeProductFamilyInserts(Sheet sheet) {
        for(Row row : sheet) {
            if (row.getRowNum() == 0) {
                ConsoleWriter.displayLog("Skipping header row for ProductFamily");
                continue;
            }
            try {
                sqlInsertFileWriter.write("INSERT INTO ProductFamily (product_family_name) VALUES ('" + row.getCell(1).getStringCellValue() + "');\n");
                families.put((int) row.getCell(0).getNumericCellValue(), row.getCell(1).getStringCellValue());
            } catch (IOException e) {
                ConsoleWriter.displayError("Writing Inserts for Product Family has failed");
            }
        }
    }

    private static void writeBillOfMaterialsInserts(Sheet sheet) throws IOException {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Product product = null;
            for (Product p : products) {
                if (p.getPid().equals(row.getCell(0).getStringCellValue())) {
                    product = p;
                    break;
                }
            }
            Product component = null;
            for (Product p : products) {
                if (p.getPid().equals(row.getCell(1).getStringCellValue())) {
                    component = p;
                    break;
                }
            }
            if(component == null){
                component = new Product(
                        row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue(),
                        row.getCell(2).getStringCellValue()
                );
                products.add(component);
                sqlInsertFileWriter.write(component.getSQLInsert());
            }
            try{
                sqlInsertFileWriter.write("INSERT INTO BillOfMaterials (product_id, component_id, quantity) VALUES ('" +
                        product.getPid() + "', '" + component.getPid() + "', " + (int) row.getCell(3).getNumericCellValue() + ");\n");
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    private static void writeBillOfOperationsInserts(Sheet sheet) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Operation operation = null;
            for (Operation o : operations) {
                if (o.getOperation_id() == (int) row.getCell(1).getNumericCellValue()) {
                    operation = o;
                    break;
                }
            }
            try{
                sqlInsertFileWriter.write("INSERT INTO BillOfOperations (product_family_name, operation_id, sequence_number) VALUES ('" +
                        families.get((int) row.getCell(0).getNumericCellValue()) + "', " + operation.getOperation_id() + ", " + (int) row.getCell(2).getNumericCellValue() + ");\n");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private static void writeWorkstationInserts(Sheet sheet) {
        for (Row row : sheet){
            if(row.getRowNum() == 0){
                continue;
            }
            WorkstationType workstationType = null;
            for(WorkstationType wt : workstationTypes){
                if(wt.getWorkstationType_id().equals(row.getCell(1).getStringCellValue())){
                    workstationType = wt;
                    break;
                }
            }
            if(workstationType == null){
                System.out.println("WorkstationType not found");
                return;
            }
            Workstation workstation = new Workstation(
                    (int) row.getCell(0).getNumericCellValue(),
                    workstationType,
                    row.getCell(2).getStringCellValue(),
                    row.getCell(3).getStringCellValue()
            );
            try {
                sqlInsertFileWriter.write(workstation.getSQLInsert());
                workstations.add(workstation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeWorkstationTypeInserts(Sheet sheet) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            WorkstationType workstationType = new WorkstationType(
                    row.getCell(0).getStringCellValue(),
                    row.getCell(1).getStringCellValue()
            );
            try {
                sqlInsertFileWriter.write(workstationType.getSQLInsert());
                workstationTypes.add(workstationType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeOperationInserts(Sheet sheet) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Operation operation = new Operation(
                    (int) row.getCell(0).getNumericCellValue(),
                    row.getCell(1).getStringCellValue()
            );
            if(!operations.contains(operation)){
                try {
                    sqlInsertFileWriter.write(operation.getSQLInsert());
                    operations.add(operation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for(int i = 2; i < row.getPhysicalNumberOfCells(); i++){
                if(!row.getCell(i).getStringCellValue().isEmpty()){
                    WorkstationType wst = null;
                    for (WorkstationType wt : workstationTypes) {
                        if (wt.getWorkstationType_id().equals(row.getCell(i).getStringCellValue())) {
                            wst = wt;
                            break;
                        }
                    }
                    WorkstationTypeOperation wsto = new WorkstationTypeOperation(wst, operation);
                    try {
                        if(!workstationTypeOperations.contains(wsto)){
                            sqlInsertFileWriter.write(wsto.getSQLInsert());
                            workstationTypeOperations.add(wsto);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void writeOrderInserts(Sheet sheet) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Customer customer = null;
            for (Customer c : customers) {
                if (c.getClient_id() == (int) row.getCell(1).getNumericCellValue()) {
                    customer = c;
                    break;
                }
            }
            if (customer == null) {
                System.out.println("Customer not found");
                return;
            }
            Product product = null;
            for (Product p : products) {
                if (p.getPid().equals(row.getCell(2).getStringCellValue())) {
                    product = p;
                    break;
                }
            }
            if (product == null) {
                System.out.println("Product not found");
                return;
            }
            CustomerOrder order = new CustomerOrder(
                    (int) row.getCell(0).getNumericCellValue(),
                    customer,
                    getCellValueAsString(row.getCell(4)),
                    getCellValueAsString(row.getCell(5))
            );
            if(!orders.contains(order)){
                try {
                    sqlInsertFileWriter.write(order.getSQLInsert());
                    orders.add(order);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            OrderProductList orderProductList = new OrderProductList(order, product, (int) row.getCell(3).getNumericCellValue());
            try {
                sqlInsertFileWriter.write(orderProductList.getSQLInsert());
                orderProductLists.add(orderProductList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getCellValueAsString(Cell cell) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return dateFormat.format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private static void writeProductInserts(Sheet sheet) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Product product = new Product(
                    row.getCell(0).getStringCellValue(),
                    row.getCell(1).getStringCellValue(),
                    row.getCell(2).getStringCellValue()
            );
            try {
                String family = families.get((int) row.getCell(3).getNumericCellValue());
                sqlInsertFileWriter.write(product.getSQLInsert(family));
                products.add(product);
            } catch (IOException e) {
                ConsoleWriter.displayError("Writing Inserts for Product has failed");
            }
        }
    }

    private static void writeCustomerTypeInserts(){
        try {
            for (int i = 0; i < CustomerType.values().length; i++) {
                sqlInsertFileWriter.write("INSERT INTO CustomerType (type) VALUES ('" + CustomerType.values()[i].name() + "');\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeClientInserts(Sheet sheet) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            Address customer_address = new Address(
                    row.getCell(3).getStringCellValue(),
                    row.getCell(4).getStringCellValue(),
                    row.getCell(5).getStringCellValue(),
                    row.getCell(6).getStringCellValue()
            );
            if(addresses.contains(customer_address)){
                customer_address = addresses.get(addresses.indexOf(customer_address));
            }else{
                writeAddressInserts(customer_address);
            }
            CustomerType customerType;
            if(checkCustomerType(row.getCell(2).getStringCellValue()) == 1){
                customerType = CustomerType.Individual;
            }else{
                customerType = CustomerType.Company;
            }
            Customer customer = new Customer(
                    (int) row.getCell(0).getNumericCellValue(),
                    row.getCell(1).getStringCellValue(),
                    row.getCell(2).getStringCellValue(),
                    customer_address,
                    customerType,
                    row.getCell(7).getStringCellValue(),
                    row.getCell(8).getStringCellValue()
            );
            try {
                sqlInsertFileWriter.write(customer.getSQLInsert());
                customers.add(customer);
            } catch (IOException e) {
                ConsoleWriter.displayError("Writing Inserts for Customer has failed");
            }
        }
    }

    private static int checkCustomerType(String c_vatin){
        if(c_vatin.charAt(2) == '1' || c_vatin.charAt(2) == '2'){
            return 1;
        }
        return 0;
    }

    private static void writeAddressInserts(Address address) {
        try {
            sqlInsertFileWriter.write(address.getSQLInsert());
            addresses.add(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}