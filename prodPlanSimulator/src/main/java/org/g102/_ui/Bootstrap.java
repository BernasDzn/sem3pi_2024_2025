package org.g102._ui;

import org.g102.domain.Component;
import org.g102.domain.Product;
import org.g102.domain.Repository.ProductRepository;
import org.g102.database.JDBC;
import org.g102.tools.ConsoleWriter;
import org.g102._ui._AuthDomain.Password;
import org.g102._ui._AuthDomain.User;
import org.g102._ui._AuthDomain.UserRoles;
import org.g102._ui._AuthDomain.UserRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.List;

public class Bootstrap implements Runnable{

    public void run() {
        System.out.println("Bootstraping...");
        try {
            initializeDB();
            initializeBDValues();
            initializePLSQL();
            addUsers();
            addChairTest();
            addTableTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Bootstrap Complete.");
    }

    public void initializeDB() {
        System.out.println("Initializing DB...");
        boolean connected = JDBC.tryConnect();
        if (connected) {
            String create_tables_file_path = System.getProperty("user.dir") + "/plantFloorManager/sql_sprint3/physical_model.sql";
            File file_with_create_tables = new File(create_tables_file_path);
            if (file_with_create_tables.exists()) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file_with_create_tables));
                    String sql = "";
                    String line;

                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("--") || line.isEmpty()) {
                            continue;
                        }

                        sql += line;

                        if (line.endsWith(";")) {
                            sql = sql.replace(";", "");
                            try{
                                JDBC.tryExecuteSQL(sql);
                                sql = "";
                            } catch (Exception e) {
                                ConsoleWriter.displayCommand("DB was already initialized.");
                            }
                        }

                    }

                } catch (Exception e) {
                    ConsoleWriter.displayError("DB could not be initialized.");
                }
            } else {
                ConsoleWriter.displayError("File with create tables not found.");
            }
        } else {
            ConsoleWriter.displayError("DB could not be initialized.");
        }
    }

    public void initializePLSQL(){
        System.out.println("Initializing PLSQL...");
        String[] plsql_scripts = {"get_material_func.sql", "usbd23_trigger.sql", "usbd24_trigger.sql", "usbd25.sql", "usbd26.sql", "usbd27.sql", "usbd28.sql"};
        boolean isConn = JDBC.tryConnect();
        if(isConn){
            for (String script : plsql_scripts) {
                String script_path = System.getProperty("user.dir") + "/plantFloorManager/sql_sprint3/" + script;
                File file = new File(script_path);
                tryAddPLSQL(JDBC.getConn(), script_path);
            }
        }else {
            ConsoleWriter.displayError("Problem establishing connection with DB...");
        }

    }

    public boolean tryAddPLSQL(Connection conn, String script_path) {
        File file = new File(script_path);
        ConsoleWriter.displayLog("Adding PLSQL: " + script_path);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder sql_builder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("--") || line.isEmpty()) {
                        continue;
                    }

                    sql_builder.append(line + " ");

                    if (line.endsWith("/")) {
                        String sql = sql_builder.toString().replace("/", "");
                        try{
                            JDBC.tryExecuteSQL(sql);
                        } catch (Exception e) {
                            ConsoleWriter.displayError("Error creating procedure." + script_path);
                            return false;
                        }
                        return true;
                    }

                }

            } catch (Exception e) {
                ConsoleWriter.displayError("Error creating procedure." + script_path);
                return false;
            }
        } else {
            ConsoleWriter.displayError("File " + script_path + " not found.");
            return false;
        }
        return false;
    }

    public void initializeBDValues(){
        String insert_file_path = System.getProperty("user.dir") + "/plantFloorManager/sql_sprint3/insert_file.sql";
        File file_with_inserts = new File(insert_file_path);
        if (file_with_inserts.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file_with_inserts));
                String sql = "";
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("--") || line.isEmpty()) {
                        continue;
                    }

                    sql += line;

                    if (line.endsWith(";")) {
                        sql = sql.replace(";", "");
                        try{
                            JDBC.tryExecuteSQL(sql);
                            sql = "";
                        } catch (Exception e) {
                            ConsoleWriter.displayCommand("Value already in BD.");
                        }
                    }

                }

            } catch (Exception e) {
                ConsoleWriter.displayError("DB could not be initialized.");
            }
        } else {
            ConsoleWriter.displayError("File with create tables not found.");
        }

    }

    public void addUsers() throws Exception {
        User user1 = new User("Admin", "admin@this.app", new Password("admin"));
        user1.addRole(UserRoles.ADMINISTRATOR);
        User user2 = new User("Production Manager", "pm@this.app", new Password("manager"));
        user2.addRole(UserRoles.PRODUCTION_MANAGER);
        User user3 = new User("Root", "root", new Password("root"));
        user3.addRole(UserRoles.ADMINISTRATOR);
        UserRepository.getInstance().addUser(user1);
        UserRepository.getInstance().addUser(user2);
        UserRepository.getInstance().addUser(user3);
    }

    public void addChairTest() {
        // Getting the instance of the repository
        ProductRepository repository = ProductRepository.getInstance();

        // Creating the main product
        Product chair = new Product("P001", "Chair", "A chair with components.", true);

        // Creating subcomponents for Chair
        Product seat = new Product("P002", "Seat", "Seat of the chair.");
        Product cushion = new Product("P003", "Cushion", "Cushion for the seat.");
        Product woodenBase = new Product("P004", "Wooden Base", "Wooden base of the seat.");
        Product wood = new Product("P005", "Wood", "Wood material for the base.");
        Product screwsSeat = new Product("P006", "Screws", "Screws (x4) for the seat.");

        // Adding components to the seat
        woodenBase.getComponents().add(new Component(wood, 1));
        seat.getComponents().add(new Component(cushion, 1));
        seat.getComponents().add(new Component(woodenBase, 1));
        seat.getComponents().add(new Component(screwsSeat, 4));

        // Creating other components for Chair
        Product backrest = new Product("P007", "Backrest", "Backrest of the chair.");

        Product legs = new Product("P008", "Legs", "Legs (x4) for the chair.");
        Product rubber = new Product("P009", "Rubber", "Rubber for the legs.");
        Product woodLegs = new Product("P010", "Wood", "Wood material for the legs.");
        Product screwsLegs = new Product("P011", "Screws", "Screws (x2) for the legs.");

        // Adding components to the legs
        legs.getComponents().add(new Component(rubber, 1));
        legs.getComponents().add(new Component(woodLegs, 1));
        legs.getComponents().add(new Component(screwsLegs, 2));

        Product armrests = new Product("P012", "Armrests", "Armrests (x2) for the chair.");
        Product armrestCushion = new Product("P013", "Armrest Cushion", "Cushion for the armrests.");
        Product woodArmrest = new Product("P014", "Wood", "Wood material for the armrests.");

        // Adding components to the armrests
        armrests.getComponents().add(new Component(armrestCushion, 1));
        armrests.getComponents().add(new Component(woodArmrest, 1));

        Product screwsChair = new Product("P015", "Screws", "Screws (x16) for the chair.");

        // Adding all components to the chair
        chair.getComponents().add(new Component(seat, 1));
        chair.getComponents().add(new Component(backrest, 1));
        chair.getComponents().add(new Component(legs, 4));
        chair.getComponents().add(new Component(armrests, 2));
        chair.getComponents().add(new Component(screwsChair, 16));

        // Adding the products to the repository
        repository.addProducts(List.of(chair, seat, cushion, woodenBase, wood, screwsSeat, backrest, legs, rubber, woodLegs, screwsLegs, armrests, armrestCushion, woodArmrest, screwsChair));
    }

    public void addTableTest() {
        // Getting the instance of the repository
        ProductRepository repository = ProductRepository.getInstance();

        // Creating the main product
        Product table = new Product("P016", "Table", "A table with components.", true);

        // Creating subcomponents for Table
        Product tabletop = new Product("P017", "Tabletop", "Top surface of the table.");
        Product woodTabletop = new Product("P018", "Wood", "Wood material for the tabletop.");
        Product screwsTabletop = new Product("P019", "Screws", "Screws (x4) for the tabletop.");

        // Adding components to the tabletop
        tabletop.getComponents().add(new Component(woodTabletop, 1));
        tabletop.getComponents().add(new Component(screwsTabletop, 4));

        // Creating components for Table legs
        Product legs = new Product("P020", "Table Legs", "Legs (x4) for the table.");
        Product woodLegs = new Product("P021", "Wood", "Wood material for the legs.");
        Product screwsLegs = new Product("P022", "Screws", "Screws (x8) for the legs.");

        // Adding components to the legs
        legs.getComponents().add(new Component(woodLegs, 4)); // Four legs
        legs.getComponents().add(new Component(screwsLegs, 8)); // Eight screws for all legs

        // Creating additional components for the table
        Product supportBeam = new Product("P023", "Support Beam", "Support beam for stability.");
        Product woodSupportBeam = new Product("P024", "Wood", "Wood material for the support beam.");
        Product screwsSupportBeam = new Product("P025", "Screws", "Screws (x4) for the support beam.");

        // Adding components to the support beam
        supportBeam.getComponents().add(new Component(woodSupportBeam, 1));
        supportBeam.getComponents().add(new Component(screwsSupportBeam, 4));

        // Adding all components to the table
        table.getComponents().add(new Component(tabletop, 1));
        table.getComponents().add(new Component(legs, 1));
        table.getComponents().add(new Component(supportBeam, 1));

        // Adding the products to the repository
        repository.addProducts(List.of(table, tabletop, woodTabletop, screwsTabletop, legs, woodLegs, screwsLegs, supportBeam, woodSupportBeam, screwsSupportBeam));
    }


}
